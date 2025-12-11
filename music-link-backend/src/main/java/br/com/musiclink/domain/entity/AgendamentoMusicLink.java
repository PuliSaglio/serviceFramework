package br.com.musiclink.domain.entity;

import br.com.serviceframework.domain.entity.Agendamento;
import br.com.serviceframework.domain.interfaces.AgendamentoStatus;
import br.com.musiclink.enumerations.AgendamentoStatusMusicLink;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "agendamento")
public class AgendamentoMusicLink extends Agendamento {

    @Override
    @Transient
    public AgendamentoStatus getStatus() {
        Integer codigo = this.getStatusId();

        return AgendamentoStatusMusicLink.toEnum(codigo);
    }

    public void setStatus(AgendamentoStatus status) {
        super.setStatus(status);
    }
}
