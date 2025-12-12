package br.com.mindlink.controller;

import br.com.serviceframework.domain.DTO.AvaliacaoDTO;
import br.com.mindlink.domain.DTO.AgendamentoDTO;
import br.com.mindlink.domain.DTO.AgendamentoListagemDTO;
import br.com.mindlink.service.AgendamentoServiceMindLink;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import br.com.mindlink.enumerations.AgendamentoStatusMind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamento")
public class AgendamentoController {
    @Autowired
    private AgendamentoServiceMindLink agendamentoService;

    /**
     * Lista todos os agendamentos cadastrados.
     *
     * @return Lista de agendamentos
     */
    @GetMapping
    public List<AgendamentoListagemDTO> findAll(){
        return agendamentoService.listarAgendamentos();
    }

    /**
     * Busca um agendamento específico por ID.
     *
     * @param id ID do agendamento
     * @return Dados do agendamento
     */
    @GetMapping(value = "/{id}")
    public AgendamentoListagemDTO findById(@PathVariable Long id){
        return agendamentoService.buscarAgendamentosPorId(id);
    }

    /**
     * Cria um novo agendamento.
     *
     * @param agendamentoDTO Dados do agendamento a ser criado
     * @return Agendamento criado
     */
    @PostMapping
    public AgendamentoDTO save(@RequestBody AgendamentoDTO agendamentoDTO){
        AgendamentoDTO newAgendamento = agendamentoService.salvarAgendamento(agendamentoDTO);
        return newAgendamento;
    }

    /**
     * Atualiza um agendamento existente.
     *
     * @param agendamentoDTO Dados do agendamento a ser atualizado
     * @return Agendamento atualizado
     */
    @PutMapping("/{agendamentoId}")
    public AgendamentoDTO update(@RequestBody AgendamentoDTO agendamentoDTO, @PathVariable Long agendamentoId) throws BadRequestException {
        AgendamentoDTO updatedAgendamento = agendamentoService.editarAgendamento(agendamentoDTO, agendamentoId);
        return updatedAgendamento;
    }

    /**
     * Remove um agendamento.
     *
     * @param id ID do agendamento a ser removido
     */
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        agendamentoService.deletarAgendamento(id);
    }

    /**
     * Lista todos os agendamentos de um prestador específico.
     *
     * @param prestadorId ID do prestador
     * @return Lista de agendamentos do prestador
     */
    @GetMapping("/{prestadorId}/agendamentos")
    public List<AgendamentoListagemDTO> listarAgendamentosDoPrestador(@PathVariable Long prestadorId) {
        return agendamentoService.listarAgendamentosPorPrestador(prestadorId);
    }

    /**
     * Busca a avaliação de um agendamento específico.
     *
     * @param id ID do agendamento
     * @return Dados da avaliação
     */
    @GetMapping("/{id}/avaliacao")
    public AvaliacaoDTO avaliacaoPorAgendamento(@PathVariable Long id) {
        try {
            AvaliacaoDTO avaliacao = agendamentoService.avaliacaoPorAgendamentoId(id);
            return avaliacao;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada para o agendamento ID: " + id, e);
        }
    }

    /**
     * Adiciona uma avaliação a um agendamento.
     *
     * @param id ID do agendamento
     * @param avaliacaoDTO Dados da avaliação
     */
    @PostMapping("/{id}/avaliacao")
    public void avaliacao(@PathVariable Long id, @RequestBody @Valid AvaliacaoDTO avaliacaoDTO) {
        agendamentoService.adicionarAvaliacaoAoAgendamento(id, avaliacaoDTO);
    }

    /**
     * Lista os agendamentos do dia atual de um prestador.
     *
     * @param prestadorId ID do prestador
     * @return Lista de agendamentos de hoje
     */
    @GetMapping("/{prestadorId}/agendamentos-hoje")
    public List<AgendamentoListagemDTO> listarAgendamentosDeHoje(@PathVariable Long prestadorId) {
        return agendamentoService.listarAgendamentosDoDia(prestadorId);
    }

    /**
     * Lista os próximos 5 agendamentos futuros de um prestador.
     *
     * @param prestadorId ID do prestador
     * @return Lista dos próximos 5 agendamentos
     */
    @GetMapping("/{prestadorId}/prox-agendamentos")
    public List<AgendamentoListagemDTO> listarAgendamentosFuturos(@PathVariable Long prestadorId) {
        return agendamentoService.listarProximos5Agendamentos(prestadorId);
    }

    /**
     * Calcula o faturamento mensal de um prestador.
     *
     * @param prestadorId ID do prestador
     * @param ano Ano de referência
     * @param mes Mês de referência
     * @return Valor do faturamento mensal
     */
    @GetMapping("/{prestadorId}/faturamento")
    public BigDecimal listarFaturamentoMensal(
            @PathVariable Long prestadorId,
            @RequestParam Integer ano,
            @RequestParam Integer mes
    ) {
        return agendamentoService.calcularFaturamentoMensal(prestadorId, ano, mes);
    }

    /**
     * Lista os agendamentos de um prestador agrupados por dia do mês.
     *
     * @param prestadorId ID do prestador
     * @param ano Ano de referência
     * @param mes Mês de referência
     * @return Mapa com agendamentos agrupados por dia
     */
    @GetMapping("/{prestadorId}/agendamentos/mensal")
    public Map<Integer, List<AgendamentoListagemDTO>> listarAgendamentosPorMes(
            @PathVariable Long prestadorId,
            @RequestParam Integer ano,
            @RequestParam Integer mes) {
        return agendamentoService.buscarAgendamentosPorMes(prestadorId, ano, mes);
    }

    /**
     * Lista todos os agendamentos de um cliente específico.
     *
     * @param clienteId ID do cliente
     * @return Lista de agendamentos do cliente
     */
    @GetMapping("/cliente/{clienteId}/agendamentos")
    public List<AgendamentoListagemDTO> listarAgendamentosDoCliente(@PathVariable Long clienteId) {
        return agendamentoService.listarAgendamentosPorCliente(clienteId);
    }

    /**
     * Atualiza o status de um agendamento.
     *
     * @param agendamentoId ID do agendamento
     * @param status Novo status do agendamento
     * @return Resposta sem conteúdo
     */
    @PutMapping("/{agendamentoId}/status/{status}")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long agendamentoId,
            @PathVariable AgendamentoStatusMind status
    ) {
        try{
            agendamentoService.editarStatusAgendamento(agendamentoId, status);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }

}

