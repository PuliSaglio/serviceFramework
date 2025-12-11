package br.com.mindlink.domain.entity;

import br.com.serviceframework.domain.entity.PerfilUsuario;
import jakarta.persistence.Entity;

@Entity
public class PrestadorPerfil extends PerfilUsuario {
    Integer idade;
}