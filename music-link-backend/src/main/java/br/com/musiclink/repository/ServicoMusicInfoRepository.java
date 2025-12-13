package br.com.musiclink.repository;

import br.com.musiclink.domain.entity.ServicoMusicInfo;
import br.com.musiclink.enumerations.CategoriaMusica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicoMusicInfoRepository extends JpaRepository<ServicoMusicInfo, Long> {
    Optional<ServicoMusicInfo> findByServicoId(Long servicoId);

    List<ServicoMusicInfo> findByInstrumentoIn(List<CategoriaMusica> instrumentosPreferidos);
}
