package br.com.musiclink.domain.DTO;

import java.util.List;

public record ClienteInstrumentosDTO(
        List<InstrumentoPreferenciaDTO> instrumentos
) {}