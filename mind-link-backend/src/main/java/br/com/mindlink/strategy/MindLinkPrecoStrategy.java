package br.com.mindlink.strategy;

import br.com.serviceframework.strategy.PrecoStrategy;
import br.com.mindlink.domain.entity.AgendamentoMindLink;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MindLinkPrecoStrategy implements PrecoStrategy<AgendamentoMindLink> {

    @Override
    public BigDecimal calcularPreco(AgendamentoMindLink agendamento) {
        BigDecimal precoPorHora = agendamento.getServico().getPrecoBase();

        BigDecimal duracaoHoras = agendamento.getHorasDuracao();

        return duracaoHoras.multiply(precoPorHora);
    }
}
