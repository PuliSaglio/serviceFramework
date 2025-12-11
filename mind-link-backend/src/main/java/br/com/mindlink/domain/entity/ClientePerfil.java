package br.com.mindlink.domain.entity;

import br.com.serviceframework.domain.entity.PerfilUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ClientePerfil extends PerfilUsuario {
    @Column
    private String metodologia;

    public String getMetodologia() {
        return metodologia;
    }

    public void setMetodologia(String metodologia) {
        this.metodologia = metodologia;
    }
}