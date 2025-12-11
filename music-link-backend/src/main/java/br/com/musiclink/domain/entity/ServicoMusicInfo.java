package br.com.musiclink.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "servico_music_info")
public class ServicoMusicInfo {

    @Id
    @Column(name = "servico_id")
    private Long servicoId;

    @Column(name = "quantidade_aulas")
    private Integer quantidadeAulas;

    public ServicoMusicInfo() {}

    public ServicoMusicInfo(Long servicoId, Integer quantidadeAulas) {
        this.servicoId = servicoId;
        this.quantidadeAulas = quantidadeAulas;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public Integer getQuantidadeAulas() {
        return quantidadeAulas;
    }

    public void setQuantidadeAulas(Integer quantidadeAulas) {
        this.quantidadeAulas = quantidadeAulas;
    }
}
