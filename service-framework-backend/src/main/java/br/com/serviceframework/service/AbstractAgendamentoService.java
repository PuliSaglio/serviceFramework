package br.com.serviceframework.service;

import br.com.serviceframework.domain.entity.Agendamento;
import br.com.serviceframework.domain.interfaces.AgendamentoStatus;

public abstract class AbstractAgendamentoService<T extends Agendamento> {
    public final T criarAgendamento(T agendamento) {
        validarRegrasDeNegocio(agendamento);
        agendamento.setStatus(getStatusInicial());
        calcularPreco(agendamento);
        T agendamentoSalvo = salvarNoRepositorio(agendamento);
        gerenciarCicloDeAgendamento(agendamentoSalvo);
        return agendamentoSalvo;
    }

    protected abstract void validarRegrasDeNegocio(T agendamento);
    protected abstract AgendamentoStatus getStatusInicial();
    protected abstract void calcularPreco(T agendamento);
    protected abstract T salvarNoRepositorio(T agendamento);
    protected void gerenciarCicloDeAgendamento(T agendamento) {}
}
