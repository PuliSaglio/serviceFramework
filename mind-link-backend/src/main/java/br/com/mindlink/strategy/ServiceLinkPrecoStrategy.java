package br.com.mindlink.strategy;

import br.com.serviceframework.strategy.PrecoStrategy;
import br.com.mindlink.domain.entity.AgendamentoMindLink;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ServiceLinkPrecoStrategy implements PrecoStrategy<AgendamentoMindLink> {

    @Override
    public BigDecimal calcularPreco(AgendamentoMindLink agendamento) {
        BigDecimal precoBase = agendamento.getServico().getPrecoBase();

        if (precoBase == null) {
            precoBase = BigDecimal.ZERO;
        }

        BigDecimal custoMateriais = calcularCustoMateriais(agendamento);

        return precoBase.add(custoMateriais);
    }

    public BigDecimal calcularCustoMateriais(AgendamentoMindLink agendamento) {
        return BigDecimal.ZERO; // logica de materiais
    }
}
