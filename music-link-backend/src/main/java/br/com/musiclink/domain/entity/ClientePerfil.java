package br.com.musiclink.domain.entity;

import br.com.musiclink.enumerations.CategoriaMusica;
import br.com.musiclink.enumerations.NivelExperiencia;
import br.com.serviceframework.domain.entity.PerfilUsuario;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ClientePerfil extends PerfilUsuario {

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "cliente_instrumentos_preferencias",
            joinColumns = @JoinColumn(name = "perfil_id")
    )
    private List<InstrumentoPreferencia> instrumentos = new ArrayList<>();

    public void adicionarInstrumento(CategoriaMusica instrumento, NivelExperiencia nivel) {
        this.instrumentos.add(new InstrumentoPreferencia(instrumento, nivel));
    }

    public List<InstrumentoPreferencia> getInstrumentos() {
        return instrumentos;
    }
}