package br.com.musiclink.domain.entity;

import br.com.musiclink.domain.converter.CategoriaMusicaConverter;
import br.com.musiclink.enumerations.CategoriaMusica;
import jakarta.persistence.*;

@Entity
@Table(name = "servico_music_info")
public class ServicoMusicInfo {

    @Id
    @Column(name = "servico_id")
    private Long servicoId;

    @Column(name = "quantidade_aulas")
    private Integer quantidadeAulas;


    @Convert(converter = CategoriaMusicaConverter.class)
    @Column(name = "instrumento_id")
    private CategoriaMusica instrumento;

    public ServicoMusicInfo() {}

    public ServicoMusicInfo(Long servicoId, Integer quantidadeAulas, CategoriaMusica instrumento) {
        this.servicoId = servicoId;
        this.quantidadeAulas = quantidadeAulas;
        this.instrumento = instrumento;
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

    public CategoriaMusica getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(CategoriaMusica instrumento) {
        this.instrumento = instrumento;
    }
}