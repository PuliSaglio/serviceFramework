package br.com.serviceframework.domain.entity;

import br.com.serviceframework.domain.enumerations.Perfis;
import br.com.serviceframework.exceptions.BusinessException;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


@Entity(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean active;

    @Column(unique = true)
    private String email;

    @Column
    private String senha;

    @Column
    private Perfis perfil;

    public User(String email, String senha, Perfis perfil) {
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.active = true;
    }

    public User() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        switch (this.perfil) {
            case ADMIN -> authorities.add(new SimpleGrantedAuthority("ADMIN"));
            case CLIENTE -> authorities.add(new SimpleGrantedAuthority("CLIENTE"));
            case PRESTADOR -> authorities.add(new SimpleGrantedAuthority("PRESTADOR"));
            default -> throw new BusinessException("Unexpected value: " + this.perfil);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

    // getters e setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfis getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfis perfil) {
        this.perfil = perfil;
    }
}
