package br.com.serviceframework.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id")
    @JsonIgnore
    private PerfilUsuario perfilUsuario;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PerfilUsuario getPerfilUsuario() {
        return perfilUsuario;
    }

    public void setPerfilUsuario(PerfilUsuario perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }
}
