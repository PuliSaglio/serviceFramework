package br.com.serviceframework.service;

import br.com.serviceframework.domain.DTO.PrestadorDTO;
import br.com.serviceframework.domain.entity.PerfilUsuario;
import br.com.serviceframework.domain.entity.Prestador;
import br.com.serviceframework.domain.entity.User;

import java.util.List;

public abstract class AbstractPrestadorService {

    public final Prestador criarPrestador(User user, PerfilUsuario perfilUsuario) {
        validarCriacao(user);
        Prestador prestador = instanciarPrestador(user);
        prestador.setPerfilPrestador(perfilUsuario);
        return salvar(prestador);
    }

    public final List<PrestadorDTO> listar() {
        return mapearParaDTO(buscarTodos());
    }

    public final PrestadorDTO buscarPorId(Long id) {
        return mapearParaDTO(buscarOuFalhar(id));
    }

    public final void deletar(Long id) {
        Prestador prestador = buscarOuFalhar(id);
        desativarUsuario(prestador);
        salvarAlteracoes(prestador);
    }

    public final Long buscarIdPorUsuario(Long userId) {
        return buscarPorUserId(userId).getId();
    }

    protected abstract void validarCriacao(User user);
    protected abstract Prestador instanciarPrestador(User user);
    protected abstract Prestador salvar(Prestador prestador);
    protected abstract List<Prestador> buscarTodos();
    protected abstract Prestador buscarOuFalhar(Long id);
    protected abstract Prestador buscarPorUserId(Long userId);
    protected abstract void desativarUsuario(Prestador prestador);
    protected abstract void salvarAlteracoes(Prestador prestador);
    protected abstract List<PrestadorDTO> mapearParaDTO(List<Prestador> prestadores);
    protected abstract PrestadorDTO mapearParaDTO(Prestador prestador);
}
