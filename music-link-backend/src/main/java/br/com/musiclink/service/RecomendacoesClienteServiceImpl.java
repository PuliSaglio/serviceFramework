package br.com.musiclink.service;

import br.com.musiclink.domain.entity.ClientePerfil;
import br.com.musiclink.domain.entity.InstrumentoPreferencia;
import br.com.musiclink.domain.entity.ServicoMusicInfo;
import br.com.musiclink.enumerations.CategoriaMusica;
import br.com.musiclink.repository.ClienteMusicPerfilRepository;
import br.com.musiclink.repository.ServicoMusicInfoRepository;
import br.com.serviceframework.domain.entity.*;
import br.com.serviceframework.repository.ClienteRepository;
import br.com.serviceframework.repository.ServicoRepository;
import br.com.serviceframework.service.AbstractRecomendacoesClienteService;
import br.com.musiclink.repository.AgendamentoMusicLinkRepository;
import br.com.musiclink.repository.RecomendacoesMusicLinkRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecomendacoesClienteServiceImpl extends AbstractRecomendacoesClienteService {

    private final AgendamentoMusicLinkRepository agendamentoRepository;
    private final RecomendacoesMusicLinkRepository recomendacoesRepository;
    private final ServicoRepository servicoRepository;
    private final ClienteServiceImpl clienteService;
    private final ServicoMusicInfoRepository servicoMusicInfoRepository;

    public RecomendacoesClienteServiceImpl(
            AgendamentoMusicLinkRepository agendamentoRepository,
            RecomendacoesMusicLinkRepository recomendacoesRepository,
            ServicoRepository servicoRepository,
            ClienteServiceImpl clienteService,
            ServicoMusicInfoRepository servicoMusicInfoRepository
    ) {
        this.agendamentoRepository = agendamentoRepository;
        this.recomendacoesRepository = recomendacoesRepository;
        this.servicoRepository = servicoRepository;
        this.clienteService = clienteService;
        this.servicoMusicInfoRepository = servicoMusicInfoRepository;
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
        PerfilUsuario perfilGenerico = cliente.getPerfilUsuario();
        PerfilUsuario perfilReal = (PerfilUsuario) Hibernate.unproxy(perfilGenerico);
        if (!(perfilReal instanceof ClientePerfil)) {
            return;
        }

        ClientePerfil perfilMusical = (ClientePerfil) perfilReal;

        List<CategoriaMusica> instrumentosPreferencia = perfilMusical.getInstrumentos().stream()
                .map(InstrumentoPreferencia::getInstrumento)
                .toList();

        if (instrumentosPreferencia.isEmpty()) {
            return;
        }

        // 2. Buscar na Tabela da Aplicação (ServicoMusicInfo)
        // Isso retorna apenas as infos (IDs e Instrumentos), não o serviço completo
        List<ServicoMusicInfo> infosCompativeis = servicoMusicInfoRepository.findByInstrumentoIn(instrumentosPreferencia);

        if (infosCompativeis.isEmpty()) {
            return;
        }

        // 3. Extrair os IDs dos serviços encontrados
        List<Long> idsDosServicos = infosCompativeis.stream()
                .map(ServicoMusicInfo::getServicoId)
                .toList();

        List<Servico> servicosRecomendados = new ArrayList<>();

        idsDosServicos.forEach(id -> {
            servicosRecomendados.add(servicoRepository.findById(id).orElse(null));
        });
        recomendacao.setServicos(servicosRecomendados);

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
