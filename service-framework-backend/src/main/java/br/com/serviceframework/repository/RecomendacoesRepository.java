package br.com.serviceframework.repository;

import br.com.serviceframework.domain.entity.RecomendacoesCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RecomendacoesRepository <T extends RecomendacoesCliente>
        extends JpaRepository<T,Long> {

}
