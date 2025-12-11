package br.com.musiclink.repository;

import br.com.musiclink.domain.entity.ServicoMusicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoMusicInfoRepository extends JpaRepository<ServicoMusicInfo, Long> {
}
