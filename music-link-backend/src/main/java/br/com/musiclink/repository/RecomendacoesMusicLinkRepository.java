package br.com.musiclink.repository;

import br.com.serviceframework.domain.entity.RecomendacoesCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecomendacoesMusicLinkRepository extends JpaRepository<RecomendacoesCliente, Long> {

}
