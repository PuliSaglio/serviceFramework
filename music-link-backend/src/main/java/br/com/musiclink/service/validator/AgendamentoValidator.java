package br.com.musiclink.service.validator;

import br.com.musiclink.domain.DTO.AgendamentoCadastroDTO;
import br.com.serviceframework.domain.entity.Servico;
import br.com.serviceframework.exceptions.BusinessException;
import br.com.musiclink.domain.entity.AgendamentoMusicLink;
import br.com.musiclink.repository.AgendamentoMusicLinkRepository;
import br.com.musiclink.domain.DTO.AgendamentoDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AgendamentoValidator {

    private final AgendamentoMusicLinkRepository agendamentoRepository;

    public AgendamentoValidator(AgendamentoMusicLinkRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    /**
     * Valida os dados de um novo agendamento antes de salvá-lo.
     *
     * @param agendamentoDTO os dados do agendamento a ser validado
     * @param servico o serviço relacionado ao agendamento
     * @throws BusinessException se alguma validação falhar
     */
    public void validarNovoAgendamento(AgendamentoCadastroDTO agendamentoDTO, Servico servico) {
        agendamentoDTO.datasHora().forEach(agendamentoDataHora -> {
            validarDataHora(agendamentoDataHora);
            validarDisponibilidadePrestador(servico.getPrestador().getId(), agendamentoDataHora);
        });

        //validarDadosObrigatorios(agendamentoDTO);
    }

    /**
     * Valida se a data/hora do agendamento é válida (não está no passado).
     */
    private void validarDataHora(LocalDateTime dataHora) {
        if (dataHora == null) {
            throw new BusinessException("A data e hora do agendamento são obrigatórias.");
        }

        if (dataHora.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é possível agendar um serviço para uma data no passado.");
        }
    }

    /**
     * Valida se o prestador já tem um agendamento no intervalo de 30 minutos.
     */
    private void validarDisponibilidadePrestador(Long prestadorId, LocalDateTime dataHora) {
        LocalDateTime inicioJanela = dataHora.minusMinutes(30);
        LocalDateTime fimJanela = dataHora.plusMinutes(30);

        List<AgendamentoMusicLink> agendamentosConflitantes = agendamentoRepository
                .findByServicoPrestadorIdAndDataHoraBetween(prestadorId, inicioJanela, fimJanela);

        if (!agendamentosConflitantes.isEmpty()) {
            throw new BusinessException(
                    "O prestador já possui um agendamento próximo a esse horário. " +
                    "Por favor, escolha outro horário."
            );
        }
    }

    /**
     * Valida se todos os dados obrigatórios estão presentes.
     */
    private void validarDadosObrigatorios(AgendamentoDTO agendamentoDTO) {
        if (agendamentoDTO.clienteId() == null) {
            throw new BusinessException("O ID do cliente é obrigatório.");
        }

        if (agendamentoDTO.servicoId() == null) {
            throw new BusinessException("O ID do serviço é obrigatório.");
        }
    }
}

