package br.com.musiclink.domain.DTO;

import java.math.BigDecimal;

public record ServicoDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoBase,
        Long categoriaId,
        String imagemUrl
) {
}

