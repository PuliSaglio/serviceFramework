package br.com.serviceframework.service;

import br.com.serviceframework.domain.entity.Agendamento;
import br.com.serviceframework.domain.enumerations.Classificacao;

public interface LLMService {
    Classificacao classificarIntencao(String mensagemCliente);

    String gerarMensagemConfirmacao(Agendamento agendamento);

    String responderDuvidaComum(String mensagemCliente);
}
