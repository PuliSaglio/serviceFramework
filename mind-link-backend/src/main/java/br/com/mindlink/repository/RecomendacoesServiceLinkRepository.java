package br.com.mindlink.repository;

import br.com.serviceframework.domain.entity.RecomendacoesCliente;
import br.com.mindlink.domain.entity.AgendamentoMindLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecomendacoesServiceLinkRepository extends JpaRepository<RecomendacoesCliente, Long> {

}
