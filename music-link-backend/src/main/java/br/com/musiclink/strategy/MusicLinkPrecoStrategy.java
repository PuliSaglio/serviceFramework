package br.com.musiclink.strategy;

import br.com.musiclink.domain.entity.ServicoMusicInfo;
import br.com.musiclink.repository.ServicoMusicInfoRepository;
import br.com.serviceframework.strategy.PrecoStrategy;
import br.com.musiclink.domain.entity.AgendamentoMusicLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MusicLinkPrecoStrategy implements PrecoStrategy<AgendamentoMusicLink> {

    @Autowired
    private ServicoMusicInfoRepository servicoMusicInfoRepository;

    @Override
    public BigDecimal calcularPreco(AgendamentoMusicLink agendamento) {
        if (agendamento.getServico() == null || agendamento.getServico().getPrecoBase() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal valorTotalPacote = agendamento.getServico().getPrecoBase();
        Long servicoId = agendamento.getServico().getId();

        Integer quantidadeAulas = servicoMusicInfoRepository.findById(servicoId)
                .map(ServicoMusicInfo::getQuantidadeAulas)
                .orElse(1);

        if (quantidadeAulas <= 0) quantidadeAulas = 1;

        return valorTotalPacote.divide(new BigDecimal(quantidadeAulas));
    }

    public BigDecimal calcularCustoMateriais(AgendamentoMusicLink agendamento) {
        return BigDecimal.ZERO; // logica de materiais
    }
}
