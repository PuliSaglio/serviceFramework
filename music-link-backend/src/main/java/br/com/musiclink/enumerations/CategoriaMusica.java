package br.com.musiclink.enumerations;

import br.com.serviceframework.domain.interfaces.ICategoriaServicos;

import java.util.Arrays;

public enum CategoriaMusica implements ICategoriaServicos {
    VIOLAO(1L, "Violão"),
    PIANO(2L, "Piano"),
    FLAUTA(3L, "Flauta"),
    BATERIA(4L, "Bateria"),
    CANTO(5L, "Canto"),
    VIOLINO(6L, "Violino"),

    OUTRO(999L, "Outro");

    private final Long id;
    private final String nomeCategoria;

    CategoriaMusica(Long id, String nomeCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
    }
    public static CategoriaMusica fromString(String texto){
        if (texto == null) {
            return OUTRO;
        }

        String textoNormalizado = texto.toUpperCase();

        return Arrays.stream(values())
                .filter(servico -> servico.name().equals(textoNormalizado))
                .findFirst()
                .orElse(OUTRO);
    }


    @Override
    public String getNomeCategoria() {
        return nomeCategoria;
    }

    @Override
    public Long getIdCategoria() {
        return id;
    }

    public static CategoriaMusica ofId(Long id) {
        if (id == null) {
            return OUTRO;
        }
        return Arrays.stream(values())
                .filter(servico -> servico.getIdCategoria().equals(id))
                .findFirst()
                .orElse(OUTRO);
    }
}
