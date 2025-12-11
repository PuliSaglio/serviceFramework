package br.com.serviceframework.repository;

import br.com.serviceframework.domain.entity.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Long> {
    @Query("SELECT p FROM Prestador p WHERE p.user.id = :userId")
    Prestador findByUserId(@Param("userId") Long userId);
}
