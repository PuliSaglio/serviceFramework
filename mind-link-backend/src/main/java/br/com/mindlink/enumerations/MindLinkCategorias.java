package br.com.mindlink.enumerations;

import br.com.serviceframework.domain.interfaces.ICategoriaServicos;

import java.util.Arrays;

public enum MindLinkCategorias implements ICategoriaServicos {
    COMPORTAMENTAL(1L, "Cognitivo-comportamental"),
    PSICANALISE(2L, "Psicanálise"),
    HUMANISTA(3L, "Humanista"),
    COMPORTAMENTAL_CLASSICA(4L, "Comportamentais clássicas"),
    SISTEMICA(5L, "Sistêmica"),
    EMDR(6L, "EMDR"),
    INTERPESSOAL(7L, "Interpessoal"),
    GERAL(999L, "Geral");

    private final Long id;
    private final String nomeCategoria;

    MindLinkCategorias(Long id, String nomeCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
    }

    public static MindLinkCategorias fromString(String texto){
        if (texto == null) {
            return GERAL;
        }

        String textoNormalizado = texto.toUpperCase();

        return Arrays.stream(values())
                .filter(servico -> servico.name().equals(textoNormalizado))
                .findFirst()
                .orElse(GERAL);
    }

    @Override
    public String getNomeCategoria() {
        return this.nomeCategoria;
    }

    @Override
    public Long getIdCategoria() {
        return id;
    }

    public static MindLinkCategorias ofId(Long id) {
        if (id == null) {
            return GERAL;
        }
        return Arrays.stream(values())
                .filter(servico -> servico.getIdCategoria().equals(id))
                .findFirst()
                .orElse(GERAL);
    }
}
