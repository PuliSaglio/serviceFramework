package br.com.servicelink.domain.entity;

import br.com.serviceframework.domain.entity.Agendamento;
import br.com.serviceframework.domain.interfaces.AgendamentoStatus;
import br.com.servicelink.enumerations.AgendamentoStatusServiceLink;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "agendamento")
public class AgendamentoServiceLink extends Agendamento {

    @Override
    @Transient
    public AgendamentoStatus getStatus() {
        Integer codigo = this.getStatusId();

        return AgendamentoStatusServiceLink.toEnum(codigo);
    }

    public void setStatus(AgendamentoStatus status) {
        super.setStatus(status);
    }

}
