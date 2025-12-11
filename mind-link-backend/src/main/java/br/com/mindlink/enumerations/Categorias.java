package br.com.mindlink.enumerations;

import br.com.serviceframework.domain.interfaces.ICategoriaServicos;

import java.util.Arrays;

public enum Categorias implements ICategoriaServicos {
    COMPORTAMENTAL(1L, "Terapia cognitivo-comportamental"),
    PSICANALISE(2L, "Psicanálise"),
    HUMANISTA(3L, "Terapias humanista"),
    COMPORTAMENTAL_CLASSICA(4L, "Terapias comportamentais clássicas"),
    SISTEMICA(5L, "Terapias sistêmicas"),
    EMDR(6L, "EMDR"),
    INTERPESSOAL(7L, "Terapia Interpessoal"),
    GERAL(999L, "Geral");

    private final Long id;
    private final String nomeCategoria;

    Categorias(Long id, String nomeCategoria) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
    }
    public static Categorias fromString(String texto){
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
        return nomeCategoria;
    }

    @Override
    public Long getIdCategoria() {
        return id;
    }

    public static Categorias ofId(Long id) {
        if (id == null) {
            return GERAL;
        }
        return Arrays.stream(values())
                .filter(servico -> servico.getIdCategoria().equals(id))
                .findFirst()
                .orElse(GERAL);
    }
}
