package br.com.mindlink.domain.entity;

import br.com.serviceframework.domain.entity.Agendamento;
import br.com.serviceframework.domain.interfaces.AgendamentoStatus;
import br.com.mindlink.enumerations.AgendamentoStatusMind;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "agendamento")
public class AgendamentoMindLink extends Agendamento {
    @Column
    private Integer minutosDuracao;

    @Override
    @Transient
    public AgendamentoStatus getStatus() {
        Integer codigo = this.getStatusId();

        return AgendamentoStatusMind.toEnum(codigo);
    }

    public void setStatus(AgendamentoStatus status) {
        super.setStatus(status);
    }

    public Integer getMinutosDuracao() {
        return minutosDuracao;
    }

    public void setMinutosDuracao(Integer minutosDuracao) {
        this.minutosDuracao = minutosDuracao;
    }

}
