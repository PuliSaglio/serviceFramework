package br.com.musiclink.domain.DTO;

import br.com.musiclink.enumerations.NivelExperiencia;
import jakarta.validation.constraints.NotNull;

public record InstrumentoPreferenciaDTO(

        @NotNull(message = "O ID do instrumento é obrigatório")
        Long categoriaId,

        @NotNull(message = "O nível de experiência é obrigatório")
        NivelExperiencia nivel
) {}
