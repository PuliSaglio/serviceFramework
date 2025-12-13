package br.com.musiclink.domain.DTO;

import br.com.musiclink.enumerations.CategoriaMusica;
import br.com.musiclink.enumerations.NivelExperiencia;

import java.math.BigDecimal;

public record ServicoDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoBase,
        Long categoriaId,
        String imagemUrl,
        Integer duracaoEmDias,
        NivelExperiencia nivel
) {
}

