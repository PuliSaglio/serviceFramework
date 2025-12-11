package br.com.mindlink.service;

import br.com.serviceframework.domain.entity.Agendamento;
import br.com.serviceframework.domain.enumerations.Classificacao;
import br.com.serviceframework.service.LLMService;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;


@Service
public class LLMServiceImpl implements LLMService {
    private static final Logger logger = LoggerFactory.getLogger(LLMServiceImpl.class);
    private final GenerativeModel generativeModel;

    public LLMServiceImpl(GenerativeModel generativeModel) {
        VertexAI vertexAi = new VertexAI("gen-lang-client-0859440912", "us-central1");

        this.generativeModel = new GenerativeModel("gemini-2.5-pro", vertexAi);
    }

    @Override
    public Classificacao classificarIntencao(String mensagemCliente) {
        String prompt = String.format(
                "Você é um classificador de intenções para um chatbot de serviços domésticos. " +
                "Sua única tarefa é analisar a mensagem do usuário e responder APENAS com uma das seguintes categorias: " +
                "RECOMENDACAO_SERVICO, ORCAMENTO, DUVIDA_COMUM, ELOGIO, RECLAMACAO, AGRADECIMENTO, SAUDACAO, GERAL. " +
                "Mensagem do usuário: \"%s\"", mensagemCliente
        );
        System.out.print(prompt);
        String categoriaRetornada = chamarAPI(prompt).trim();
        String categoriaNormalizada = categoriaRetornada.toUpperCase();
        return Classificacao.valueOf(categoriaNormalizada);
    }

    @Override
    public String gerarMensagemConfirmacao(Agendamento agendamento) {
        String prompt = String.format(
                "Gere uma mensagem de confirmação de agendamento amigável e concisa com os seguintes dados: " +
                "Cliente: %s, Serviço: %s, Data: %s, Horário: %s, O valor será de R$%s. " +
                "Não inclua saudações nem despedidas.",
                agendamento.getCliente().getUser().getUsername(),
                agendamento.getServico().getNome(),
                agendamento.getDataHora().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                agendamento.getDataHora().toLocalTime().toString(),
                agendamento.getServico().getPrecoBase().toString()
        );
        return chamarAPI(prompt);
    }

    @Override
    public String responderDuvidaComum(String mensagemCliente) {
        String prompt = String.format(
                "Você é um assistente virtual de uma plataforma de serviços domésticos chamada ServiceLink. " +
                "Sua função é responder a perguntas comuns de clientes de forma concisa e útil, agindo como um FAQ inteligente. Siga estritamente as regras abaixo:\n" +
                "1. Se a pergunta for sobre **cancelamento**, a política é: 'Cancelamentos podem ser feitos com até 24 horas de antecedência sem custo. Após esse período, pode haver uma taxa.'\n" +
                "2. Se a pergunta for sobre **alterar o horário**, oriente o cliente a entrar em contato direto com o profissional pelo chat da plataforma para verificar a disponibilidade.\n" +
                "3. Se a pergunta for sobre **preço ou pagamento**, informe que o valor final é definido pelo profissional e que o pagamento é feito pela plataforma após a conclusão do serviço.\n" +
                "4. Se a pergunta não se encaixar nessas categorias, responda de forma educada que você não tem informações sobre esse tópico específico.\n\n" +
                "--- \n" +
                "Pergunta do cliente: \"%s\"",
                mensagemCliente
        );
        String resposta = chamarAPI(prompt);

        if (resposta.isEmpty()) {
            return "Desculpe, não consegui processar sua dúvida no momento. Tente novamente.";
        }

        return resposta;
    }

    private String chamarAPI(String prompt) {
        try {
            GenerateContentResponse response = this.generativeModel.generateContent(
                    ContentMaker.fromString(prompt)
            );
            return ResponseHandler.getText(response).trim();
        } catch (Exception e) {
            logger.error("Erro ao chamar API do Gemini: ", e);
            return "";
        }
    }

    public String classificarServico(String mensagemCliente) {
        String prompt = String.format(
                "Você é um extrator de informações de serviços domésticos. Sua única tarefa é analisar a mensagem do usuário e retornar APENAS a categoria do serviço e o nome do serviço, separados por um ponto e vírgula (;). " +
                        "Se a categoria não for encontrada, retorne 'OUTRA'.\n" +
                        "Categorias de serviço: [LIMPEZA,\n" +
                        "HIDRAULICA,\n" +
                        "ELETRICA,\n" +
                        "PINTURA,\n" +
                        "JARDINAGEM,\n" +
                        "COZINHA,\n" +
                        "OUTRAS]." +
                        "Exemplo de Retorno: 'HIDRAULICA;conserto de torneira'.\n\n" +
                        "Mensagem: \"%s\"",
                mensagemCliente
        );

        String respostaDaLLM = chamarAPI(prompt);

        return respostaDaLLM.trim();
    }

    public String recomendarServico(String mensagemCliente) {
        String prompt = String.format(
                "Você é um consultor especializado em serviços domésticos. Sua tarefa é analisar a descrição do problema de um cliente e recomendar o serviço mais adequado, retornando APENAS a categoria do serviço, o nome do serviço, e uma breve justificativa para a sua recomendação. Os três campos devem ser separados por um ponto e vírgula (;).\n" +
                        "Se a descrição não se encaixar em nenhuma, use 'OUTRA'.\n" +
                        "Siga estritamente as categorias de serviço: [HIDRÁULICA, ELÉTRICA, JARDINAGEM, LIMPEZA, OUTRA].\n" +
                        "\n" +
                        "Exemplo:\n" +
                        "Descrição: 'A pia da cozinha está vazando e eu já tentei apertar a rosca, mas não adiantou.'\n" +
                        "Retorno esperado: HIDRÁULICA;conserto de vazamento;A pia vazando é um problema hidráulico comum que exige reparo no encanamento.\n\n" +
                        "Mensagem do cliente: \"%s\"",
                mensagemCliente
        );

        String respostaDaLLM = chamarAPI(prompt);
        return respostaDaLLM.trim();
    }

}
