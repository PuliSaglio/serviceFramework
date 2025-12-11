package br.com.serviceframework.strategy;

import br.com.serviceframework.domain.entity.Agendamento;

import java.math.BigDecimal;

public interface PrecoStrategy <T extends Agendamento> {
    BigDecimal calcularPreco(T agendamento);
}
