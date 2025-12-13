package br.com.musiclink.domain.entity;

import br.com.musiclink.domain.converter.CategoriaMusicaConverter;
import br.com.musiclink.enumerations.CategoriaMusica;
import br.com.musiclink.enumerations.NivelExperiencia;
import jakarta.persistence.*;

import java.util.Objects;

@Embeddable
public class InstrumentoPreferencia {


    @Convert(converter = CategoriaMusicaConverter.class)
    @Column(name = "instrumento_id", nullable = false)
    private CategoriaMusica instrumento;


    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_experiencia", nullable = false)
    private NivelExperiencia nivel;


    public InstrumentoPreferencia() {}

    public InstrumentoPreferencia(CategoriaMusica instrumento, NivelExperiencia nivel) {
        this.instrumento = instrumento;
        this.nivel = nivel;
    }


    public CategoriaMusica getInstrumento() { return instrumento; }
    public void setInstrumento(CategoriaMusica instrumento) { this.instrumento = instrumento; }

    public NivelExperiencia getNivel() { return nivel; }
    public void setNivel(NivelExperiencia nivel) { this.nivel = nivel; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstrumentoPreferencia that = (InstrumentoPreferencia) o;
        return instrumento == that.instrumento && nivel == that.nivel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(instrumento, nivel);
    }
}
