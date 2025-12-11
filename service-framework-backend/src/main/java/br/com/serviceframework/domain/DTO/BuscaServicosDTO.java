package br.com.serviceframework.domain.DTO;

import java.math.BigDecimal;

public record BuscaServicosDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoMin,
        BigDecimal precoMax,
        Long categoria
){

}
