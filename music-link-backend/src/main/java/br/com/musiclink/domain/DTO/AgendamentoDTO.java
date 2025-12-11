package br.com.musiclink.domain.DTO;

import java.time.LocalDateTime;
import br.com.serviceframework.domain.DTO.AvaliacaoDTO;

public record AgendamentoDTO(
        Long id,
        LocalDateTime dataHora,
        String observacao,
        String status,
        Long clienteId,
        Long servicoId,
        AvaliacaoDTO avaliacaoDTO
) {
}
