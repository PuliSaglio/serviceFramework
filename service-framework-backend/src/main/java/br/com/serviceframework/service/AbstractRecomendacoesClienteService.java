package br.com.serviceframework.service;

import br.com.serviceframework.domain.entity.Cliente;
import br.com.serviceframework.domain.entity.RecomendacoesCliente;

public abstract class AbstractRecomendacoesClienteService {

    public final RecomendacoesCliente criarRecomendacao(Cliente cliente) {
        validarCliente(cliente);

        var recomendacao = montarEstruturaBase(cliente);

        aplicarLogicaDeRecomendacao(recomendacao, cliente);

        return salvar(recomendacao);
    }

    public final RecomendacoesCliente getRecomendacao(Integer id) {
        validarId(id);
        return buscarPorId(id);
    }

    protected void validarCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("Cliente inválido para gerar recomendação");
        }
    }

    protected void validarId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }
    }

    protected abstract RecomendacoesCliente montarEstruturaBase(Cliente cliente);

    protected abstract void aplicarLogicaDeRecomendacao(
            RecomendacoesCliente recomendacao,
            Cliente cliente
    );

    protected abstract RecomendacoesCliente salvar(RecomendacoesCliente recomendacao);

    protected abstract RecomendacoesCliente buscarPorId(Integer id);
}
