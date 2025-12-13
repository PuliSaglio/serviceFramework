package br.com.musiclink.domain.DTO;

import br.com.serviceframework.domain.DTO.AvaliacaoDTO;

import java.time.LocalDateTime;
import java.util.List;

public record AgendamentoCadastroDTO(
        Long id,
        List<LocalDateTime> datasHora,
        String observacao,
        String status,
        Long clienteId,
        Long servicoId
) {
}
