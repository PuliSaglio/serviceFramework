package br.com.mindlink.controller;

import br.com.serviceframework.domain.entity.Cliente;
import br.com.serviceframework.domain.entity.RecomendacoesCliente;
import br.com.serviceframework.repository.ClienteRepository;
import br.com.serviceframework.service.AbstractRecomendacoesClienteService;
import br.com.mindlink.domain.DTO.RecomendacoesClienteResponseDTO;
import br.com.mindlink.domain.mapper.RecomendacoesClienteMapper;
import br.com.mindlink.service.RecomendacoesClienteServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/recomendacoes")
public class RecomendacoesClienteController {

    private final RecomendacoesClienteServiceImpl recomendacoesService;
    private final ClienteRepository clienteRepository;

    public RecomendacoesClienteController(
            RecomendacoesClienteServiceImpl recomendacoesService,
            ClienteRepository clienteRepository
    ) {
        this.recomendacoesService = recomendacoesService;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Gera e salva uma nova recomendação para o cliente
     */
    @PostMapping("/cliente/{clienteId}")
    public RecomendacoesClienteResponseDTO gerar(@PathVariable Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        RecomendacoesCliente entidade;
        try {
            entidade = recomendacoesService.criarRecomendacao(cliente);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Cliente não encontrado") || e.getMessage().contains("não possui agendamentos")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            }
        }

        return RecomendacoesClienteMapper.toResponseDTO(entidade);
    }

    /**
     * Busca uma recomendação já salva
     */
    @GetMapping("/{id}")
    public RecomendacoesCliente buscar(@PathVariable Integer id) {
        return recomendacoesService.getRecomendacao(id);
    }
}
