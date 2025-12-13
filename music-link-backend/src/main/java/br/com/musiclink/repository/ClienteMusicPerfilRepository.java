package br.com.musiclink.repository;

import br.com.musiclink.domain.entity.ClientePerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteMusicPerfilRepository extends JpaRepository<ClientePerfil, Integer> {

}
