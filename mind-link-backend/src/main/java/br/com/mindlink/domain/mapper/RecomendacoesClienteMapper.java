package br.com.mindlink.domain.mapper;

import br.com.serviceframework.domain.entity.RecomendacoesCliente;
import br.com.serviceframework.domain.entity.Servico;
import br.com.mindlink.domain.DTO.RecomendacoesClienteResponseDTO;
import br.com.mindlink.domain.DTO.ServicoDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class RecomendacoesClienteMapper {

    // Método principal de conversão
    public static RecomendacoesClienteResponseDTO toResponseDTO(RecomendacoesCliente entidade) {
        if (entidade == null) {
            return null;
        }

        List<ServicoDTO> servicosDTO = entidade.getServicos().stream()
                .map(RecomendacoesClienteMapper::toServicoDTO)
                .collect(Collectors.toList());

        return new RecomendacoesClienteResponseDTO(
                entidade.getId(),
                entidade.getCliente().getId(),
                servicosDTO
        );
    }

    // Método auxiliar para converter a Entidade Servico para ServicoBasicDTO
    private static ServicoDTO toServicoDTO(Servico servico) {
        if (servico == null) {
            return null;
        }
        return new ServicoDTO(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPrecoBase(),
                servico.getCategoria().getIdCategoria(),
                servico.getImagemUrl()
        );
    }
}