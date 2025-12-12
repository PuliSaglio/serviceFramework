package br.com.servicelink.domain.DTO;

import java.math.BigDecimal;

public record ServicoDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoPorHora,
        Long categoriaId,
        String imagemUrl
) {
}

