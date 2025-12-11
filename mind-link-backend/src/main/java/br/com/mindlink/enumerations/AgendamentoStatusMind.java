package br.com.mindlink.enumerations;

import br.com.serviceframework.domain.interfaces.AgendamentoStatus;

public enum AgendamentoStatusMind implements AgendamentoStatus {
    PENDENTE(1, "Pendente"),
    CONFIRMADO(2, "Confirmado"),
    CANCELADO(3, "Cancelado"),
    EM_ANDAMENTO(4, "Aguardando Pagamento"),
    CONCLUIDO(5, "Concluído");

    private final int codigo;
    private final String nome;

    AgendamentoStatusMind(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    @Override
    public String getNomeStatus() {
        return nome;
    }

    @Override
    public Integer getCodigoStatus() {
        return codigo;
    }

    public static AgendamentoStatusMind toEnum(Integer cod) {
        if (cod == null) return null;

        for (AgendamentoStatusMind item : AgendamentoStatusMind.values()) {
            if (cod.equals(item.getCodigoStatus())) return item;
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
