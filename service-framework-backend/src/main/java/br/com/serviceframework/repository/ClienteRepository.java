package br.com.serviceframework.repository;



import br.com.serviceframework.domain.entity.Cliente;
import br.com.serviceframework.domain.entity.PerfilUsuario;
import br.com.serviceframework.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByUserId(Long id);

    boolean existsByUser(User user);

    @Query("SELECT c.perfilUsuario FROM Cliente c WHERE c.id = :clienteId")
    Optional<PerfilUsuario> findPerfilByClienteId(@Param("clienteId") Long clienteId);
}
