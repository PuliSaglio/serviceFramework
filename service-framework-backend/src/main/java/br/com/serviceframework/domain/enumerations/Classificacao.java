package br.com.serviceframework.domain.enumerations;

import java.util.Arrays;

public enum Classificacao {
    RECOMENDACAO_SERVICO,
    ORCAMENTO,
    DUVIDA_COMUM,
    ELOGIO,
    RECLAMACAO,
    AGRADECIMENTO,
    SAUDACAO,
    GERAL; // Categoria para o que não se encaixar em nenhum dos outros

    public static Classificacao fromString(String texto){
        if (texto == null) {
            return GERAL;
        }

        String textoNormalizado = texto.toUpperCase();

        return Arrays.stream(values())
                .filter(classificacao -> classificacao.name().equals(textoNormalizado))
                .findFirst()
                .orElse(GERAL);
    }
}
