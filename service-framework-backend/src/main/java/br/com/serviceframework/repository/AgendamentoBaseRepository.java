package br.com.serviceframework.repository;

import br.com.serviceframework.domain.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;


import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface AgendamentoBaseRepository<T extends Agendamento>
        extends JpaRepository <T, Long>{
    T save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void deleteById(Long id);
}
