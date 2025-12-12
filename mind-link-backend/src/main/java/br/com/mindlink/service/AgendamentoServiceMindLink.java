package br.com.mindlink.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.com.mindlink.domain.entity.AgendamentoMindLink;
import br.com.serviceframework.domain.DTO.AvaliacaoDTO;
import br.com.serviceframework.domain.entity.Avaliacao;
import br.com.serviceframework.domain.entity.Cliente;
import br.com.serviceframework.domain.entity.Servico;
import br.com.serviceframework.domain.interfaces.AgendamentoStatus;
import br.com.serviceframework.strategy.PrecoStrategy;
import br.com.mindlink.enumerations.AgendamentoStatusMind;
import br.com.mindlink.repository.AgendamentoServiceLinkRepository;
import br.com.serviceframework.repository.AvaliacaoRepository;
import br.com.serviceframework.repository.ClienteRepository;
import br.com.serviceframework.repository.ServicoRepository;
import br.com.serviceframework.service.AbstractAgendamentoService;
import br.com.mindlink.security.AuthService;
import br.com.mindlink.service.validator.AgendamentoValidator;
import br.com.mindlink.domain.DTO.AgendamentoDTO;
import br.com.mindlink.domain.DTO.AgendamentoListagemDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoServiceMindLink extends AbstractAgendamentoService<AgendamentoMindLink> {

    private final AgendamentoServiceLinkRepository agendamentoRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoRepository servicoRepository;
    private final AgendamentoValidator agendamentoValidator;
    private final AuthService authService;
    private final PrecoStrategy<AgendamentoMindLink> precoStrategy;

    @Autowired
    public AgendamentoServiceMindLink(
            AgendamentoServiceLinkRepository agendamentoRepository,
            AvaliacaoRepository avaliacaoRepository,
            ClienteRepository clienteRepository,
            ServicoRepository servicoRepository,
            AgendamentoValidator agendamentoValidator,
            AuthService authService,
            PrecoStrategy<AgendamentoMindLink> precoStrategy) {
        this.agendamentoRepository = agendamentoRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.clienteRepository = clienteRepository;
        this.servicoRepository = servicoRepository;
        this.agendamentoValidator = agendamentoValidator;
        this.authService = authService;
        this.precoStrategy = precoStrategy;
    }

    // --- MÉTODOS DO FRAMEWORK ---

    @Override
    protected void validarRegrasDeNegocio(AgendamentoMindLink agendamento) {
        // Validações extras da entidade podem vir aqui
    }

    @Override
    protected AgendamentoStatus getStatusInicial() {
        return AgendamentoStatusMind.PENDENTE;
    }

    @Override
    protected void calcularPreco(AgendamentoMindLink agendamento) {
        BigDecimal precoFinal = precoStrategy.calcularPreco(agendamento);

        agendamento.setPrecoTotal(precoFinal);
    }

    @Override
    protected AgendamentoMindLink salvarNoRepositorio(AgendamentoMindLink agendamento) {
        return agendamentoRepository.save(agendamento);
    }

    // --- MÉTODOS PÚBLICOS DA APLICAÇÃO ---

    @Transactional
    public AgendamentoDTO salvarAgendamento(AgendamentoDTO agendamentoDTO) {
        Long usuarioId = agendamentoDTO.clienteId();
        Cliente cliente = clienteRepository.findByUserId(usuarioId);

        if (cliente == null) {
            throw new EntityNotFoundException("Cliente não encontrado para o Usuário com ID: " + usuarioId);
        }

        Servico servico = servicoRepository.findById(agendamentoDTO.servicoId())
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + agendamentoDTO.servicoId()));

        agendamentoValidator.validarNovoAgendamento(agendamentoDTO, servico);

        AgendamentoMindLink agendamento = new AgendamentoMindLink();
        agendamento.setDataHora(agendamentoDTO.dataHora());
        agendamento.setObservacao(agendamentoDTO.observacao());
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setMinutosDuracao(agendamentoDTO.minutosDuracao());

        AgendamentoMindLink agendamentoSalvo = super.criarAgendamento(agendamento);

        return converterParaDTO(agendamentoSalvo);
    }

    public AgendamentoDTO editarAgendamento(AgendamentoDTO agendamentoDTO, Long id) throws BadRequestException {
        AgendamentoMindLink agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));

        agendamento.setStatus(AgendamentoStatusMind.valueOf(agendamentoDTO.status()));
        agendamento.setDataHora(agendamentoDTO.dataHora());
        agendamento.setObservacao(agendamentoDTO.observacao());

        AgendamentoMindLink agendamentoSalvo = agendamentoRepository.save(agendamento);
        return converterParaDTO(agendamentoSalvo);
    }

    @Transactional
    public List<AgendamentoListagemDTO> listarAgendamentos() {
        return agendamentoRepository.findAll().stream()
                .map(this::converterParaListagemDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AgendamentoListagemDTO> listarAgendamentosPorPrestador(Long prestadorId) {
        List<AgendamentoMindLink> agendamentosDoPrestador = agendamentoRepository.findByServico_Prestador_Id(prestadorId);

        return agendamentosDoPrestador.stream()
                .map(this::converterParaListagemDTO)
                .collect(Collectors.toList());
    }

    public AgendamentoListagemDTO buscarAgendamentosPorId(Long id) {
        var agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado: " + id));
        return converterParaListagemDTO(agendamento);
    }

    public void deletarAgendamento(Long id) {
        agendamentoRepository.deleteById(id);
    }

    public AvaliacaoDTO avaliacaoPorAgendamentoId(Long id) {
        AgendamentoMindLink agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));

        try {
            Avaliacao avaliacao = agendamento.getAvaliacao();
            return new AvaliacaoDTO(
                    avaliacao.getId(),
                    avaliacao.getEstrelas(),
                    avaliacao.getComentario());
        } catch (Exception e) {
            throw new EntityNotFoundException("Avaliação não encontrada para o Agendamento com o ID: " + id);
        }
    }

    public AgendamentoDTO adicionarAvaliacaoAoAgendamento(Long agendamentoId, AvaliacaoDTO avaliacaoDTO) {
        AgendamentoMindLink agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + agendamentoId));

        if (agendamento.getAvaliacao() != null) {
            throw new RuntimeException("Este agendamento já foi avaliado.");
        }

        if (agendamento.getStatus() != AgendamentoStatusMind.CONCLUIDO) {
            throw new RuntimeException("Só é possível avaliar agendamentos concluídos.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setEstrelas(avaliacaoDTO.estrelas());
        avaliacao.setComentario(avaliacaoDTO.comentario());

        avaliacao = avaliacaoRepository.save(avaliacao);

        agendamento.setAvaliacao(avaliacao);
        agendamentoRepository.save(agendamento);

        return converterParaDTO(agendamento);
    }

    @Transactional
    public List<AgendamentoListagemDTO> listarAgendamentosDoDia(Long prestadorId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        List<AgendamentoMindLink> agendamentos = agendamentoRepository.findByServicoPrestadorIdAndDataHoraBetween(
                prestadorId, startOfDay, endOfDay
        );

        return agendamentos.stream().map(this::converterParaListagemDTO).collect(Collectors.toList());
    }

    @Transactional
    public List<AgendamentoListagemDTO> listarProximos5Agendamentos(Long prestadorId) {
        LocalDateTime now = LocalDateTime.now();
        List<AgendamentoMindLink> agendamentos = agendamentoRepository.findTop5ByServicoPrestadorIdAndDataHoraAfterOrderByDataHoraAsc(
                prestadorId, now
        );
        return agendamentos.stream().map(this::converterParaListagemDTO).collect(Collectors.toList());
    }

    public BigDecimal calcularFaturamentoMensal(Long prestadorId, int ano, int mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDateTime dataInicio = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime dataFim = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        BigDecimal faturamento = agendamentoRepository.calcularFaturamentoPorPeriodo(
                prestadorId, dataInicio, dataFim, AgendamentoStatusMind.CONCLUIDO.getCodigoStatus()
        );
        return faturamento != null ? faturamento : BigDecimal.ZERO;
    }

    @Transactional
    public Map<Integer, List<AgendamentoListagemDTO>> buscarAgendamentosPorMes(Long prestadorId, int ano, int mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDateTime dataInicio = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime dataFim = yearMonth.atEndOfMonth().atTime(23, 59, 59, 999999999);

        List<AgendamentoMindLink> agendamentos = agendamentoRepository.findByServicoPrestadorIdAndDataHoraBetween(
                prestadorId, dataInicio, dataFim
        );

        return agendamentos.stream()
                .map(this::converterParaListagemDTO)
                .collect(Collectors.groupingBy(
                        dto -> dto.getDataHora().getDayOfMonth(),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    public List<AgendamentoListagemDTO> listarAgendamentosPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::converterParaListagemDTO)
                .toList();
    }

    @Transactional
    public AgendamentoDTO editarStatusAgendamento(Long id, AgendamentoStatusMind status) throws BadRequestException {
        AgendamentoMindLink agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado com o ID: " + id));

        validarTransicaoStatus(status, agendamento);
        agendamento.setStatus(status);

        AgendamentoMindLink agendamentoSalvo = agendamentoRepository.save(agendamento);
        return converterParaDTO(agendamentoSalvo);
    }

    // --- MÉTODOS AUXILIARES ---

    private AgendamentoDTO converterParaDTO(AgendamentoMindLink agendamento) {
        AvaliacaoDTO avaliacaoDTO = null;
        if (agendamento.getAvaliacao() != null) {
            avaliacaoDTO = new AvaliacaoDTO(
                    agendamento.getAvaliacao().getId(),
                    agendamento.getAvaliacao().getEstrelas(),
                    agendamento.getAvaliacao().getComentario()
            );
        }
        return new AgendamentoDTO(
                agendamento.getId(),
                agendamento.getDataHora(),
                agendamento.getObservacao(),
                agendamento.getStatus().toString(),
                agendamento.getCliente().getUser().getId(),
                agendamento.getServico().getId(),
                avaliacaoDTO,
                agendamento.getMinutosDuracao()
        );
    }

    private AgendamentoListagemDTO converterParaListagemDTO(AgendamentoMindLink agendamento) {
        return new AgendamentoListagemDTO(
                agendamento.getId(),
                agendamento.getDataHora(),
                agendamento.getStatus().toString(),
                agendamento.getObservacao(),
                agendamento.getCliente().getId(),
                agendamento.getCliente().getUser().getUsername(),
                agendamento.getServico().getId(),
                agendamento.getServico().getNome(),
                agendamento.getServico().getPrestador().getUser().getUsername()
        );
    }

    // CORREÇÃO DE TIPO NO PARÂMETRO
    private static void validarTransicaoStatus(AgendamentoStatusMind status, AgendamentoMindLink agendamento) throws BadRequestException {
        AgendamentoStatus atual = agendamento.getStatus();

        if (atual.equals(AgendamentoStatusMind.CONCLUIDO) || atual.equals(AgendamentoStatusMind.CANCELADO)) {
            throw new BadRequestException("Não é possível alterar o status de um agendamento que já está '" + atual + "' (finalizado).");
        }
        if (atual.equals(status)) {
            throw new BadRequestException("O status da requisição ('" + status + "') é o mesmo do status atual.");
        }

        if (atual.equals(AgendamentoStatusMind.PENDENTE) && status.equals(AgendamentoStatusMind.CONFIRMADO)) {
            // ok
        }
        else if (atual.equals(AgendamentoStatusMind.PENDENTE) && status.equals(AgendamentoStatusMind.CANCELADO)) {
            // ok
        }
        else if (atual.equals(AgendamentoStatusMind.CONFIRMADO) && status.equals(AgendamentoStatusMind.CONCLUIDO)) {
            if(agendamento.getDataHora().isBefore(LocalDateTime.now())) {
                // ok
            } else throw new BadRequestException("Não é possivel concluir um agendamento que ainda não aconteceu");
        }
        else {
            throw new BadRequestException("Transição de status inválida: De '" + atual + "' para '" + status + "' não é permitida.");
        }
    }
}