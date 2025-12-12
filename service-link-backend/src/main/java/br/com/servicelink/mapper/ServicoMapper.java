package br.com.servicelink.mapper;


import br.com.serviceframework.domain.entity.Servico;
import br.com.servicelink.domain.DTO.ServicoDTO;

import java.util.List;

public class ServicoMapper {

    public static ServicoDTO toServicoDTO(Servico servico) {
        if  (servico == null) return null;

        return new ServicoDTO(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getPrecoBase(),
                servico.getCategoria() != null ? servico.getCategoria().getIdCategoria() : null,
                servico.getImagemUrl()
        );
    }

    public static List<ServicoDTO> toServicoDTOList(List<Servico> servicos) {
        return servicos.stream()
                .map(ServicoMapper::toServicoDTO)
                .toList();
    }
}
