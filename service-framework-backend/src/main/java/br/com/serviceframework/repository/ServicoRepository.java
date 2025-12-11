package br.com.serviceframework.repository;

import br.com.serviceframework.domain.entity.Servico;
import br.com.serviceframework.domain.interfaces.ICategoriaServicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByNomeContainingIgnoreCase(String nome);
    @Query("SELECT s FROM Servico s WHERE s.categoria = :categoria")
    List<Servico> findByCategoria(@Param("categoria")ICategoriaServicos categoria);

    @Query("SELECT s FROM Servico s " +
            "WHERE s.categoria = :categoria AND LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
            "ORDER BY s.precoBase DESC " +
            "LIMIT 1")
    Optional<Servico> findTop1ByOrderByPrecoBaseDesc(
            @Param("categoria") ICategoriaServicos categoria,
            @Param("nome") String nome
    );

    @Query("SELECT s FROM Servico s " +
            "WHERE s.categoria = :categoria AND LOWER(s.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
            "ORDER BY s.precoBase ASC " +
            "LIMIT 1")
    Optional<Servico> findTop1ByOrderByPrecoBaseAsc(
            @Param("categoria") ICategoriaServicos categoria,
            @Param("nome") String nome
    );

    List<Servico> findByPrestadorId(Long prestadorId);

    @Query("""
    SELECT s FROM Servico s
    WHERE (:id IS NULL OR s.id != :id)
      AND (:categoria IS NULL OR s.categoria = :categoria)
""")
    List<Servico> findByCategoriaAndIdNot(
            @Param("categoria") ICategoriaServicos categoria,
            @Param("id") Long id
    );

    @Query("""
    SELECT s FROM Servico s
    WHERE (:id IS NULL OR s.id = :id)
      AND (:nome IS NULL OR s.nome LIKE %:nome%)
      AND (:descricao IS NULL OR s.descricao LIKE %:descricao%)
      AND (:precoMin IS NULL OR s.precoBase >= :precoMin)
      AND (:precoMax IS NULL OR s.precoBase <= :precoMax)
      AND (:categoria IS NULL OR s.categoria = :categoria)
""")
    List<Servico> buscarServicos(
            @Param("id") Long id,
            @Param("nome") String nome,
            @Param("descricao") String descricao,
            @Param("precoMin") BigDecimal precoMin,
            @Param("precoMax") BigDecimal precoMax,
            @Param("categoria")ICategoriaServicos categoria
    );
}
