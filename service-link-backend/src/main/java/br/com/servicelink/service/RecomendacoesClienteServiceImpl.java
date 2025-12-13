package br.com.servicelink.service;

import br.com.serviceframework.domain.entity.Agendamento;
import br.com.serviceframework.domain.entity.Cliente;
import br.com.serviceframework.domain.entity.RecomendacoesCliente;
import br.com.serviceframework.domain.entity.Servico;
import br.com.serviceframework.repository.ServicoRepository;
import br.com.serviceframework.service.AbstractRecomendacoesClienteService;
import br.com.servicelink.repository.AgendamentoServiceLinkRepository;
import br.com.servicelink.repository.RecomendacoesServiceLinkRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecomendacoesClienteServiceImpl extends AbstractRecomendacoesClienteService {

    private final AgendamentoServiceLinkRepository agendamentoRepository;
    private final RecomendacoesServiceLinkRepository recomendacoesRepository;
    private final ServicoRepository servicoRepository;

    public RecomendacoesClienteServiceImpl(
            AgendamentoServiceLinkRepository agendamentoRepository,
            RecomendacoesServiceLinkRepository recomendacoesRepository,
            ServicoRepository servicoRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.recomendacoesRepository = recomendacoesRepository;
        this.servicoRepository = servicoRepository;
    }

    @Override
    protected RecomendacoesCliente montarEstruturaBase(Cliente cliente) {
        RecomendacoesCliente rec = new RecomendacoesCliente();
        rec.setCliente(cliente);
        return rec;
    }

    @Override
    protected void aplicarLogicaDeRecomendacao(
            RecomendacoesCliente recomendacao,
            Cliente cliente
    ) {

        Agendamento ultimoAgendamento = agendamentoRepository
                .findTop1ByClienteIdOrderByDataHoraDesc(cliente.getId())
                .orElseThrow(() -> new RuntimeException("Cliente não possui agendamentos"));

        Servico servicoBase = ultimoAgendamento.getServico();

        List<Servico> servicosSemelhantes = servicoRepository
                .findByCategoriaAndIdNot(
                        servicoBase.getCategoria(),
                        servicoBase.getId()
                );

        if (servicosSemelhantes.isEmpty()) {
            throw new RuntimeException("Nenhum serviço semelhante encontrado");
        }

        recomendacao.setServicos(servicosSemelhantes);
    }

    @Override
    protected RecomendacoesCliente salvar(RecomendacoesCliente recomendacao) {
        return recomendacoesRepository.save(recomendacao);
    }

    @Override
    protected RecomendacoesCliente buscarPorId(Integer id) {
        return recomendacoesRepository.findById(id.longValue())
                .orElseThrow(() -> new RuntimeException("Recomendação não encontrada"));
    }
}
