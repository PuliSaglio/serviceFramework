package br.com.musiclink.domain.DTO;

import java.util.List;

/**
 * DTO de resposta para listagem de recomendações de serviços para um cliente.
 */
public record RecomendacoesClienteResponseDTO(
        Integer id,
        Long clienteId, // Apenas o ID do cliente para evitar serialização de entidades completas
        List<ServicoDTO> servicos
) {}