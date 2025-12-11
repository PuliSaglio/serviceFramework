package br.com.serviceframework.service;

import br.com.serviceframework.domain.DTO.ClienteDTO;
import br.com.serviceframework.domain.entity.Cliente;
import br.com.serviceframework.domain.entity.PerfilUsuario;
import br.com.serviceframework.domain.entity.User;

import java.util.List;

public abstract class AbstractClienteService {

    public final Cliente criarCliente(User user, PerfilUsuario perfilUsuario) {
        validarCriacao(user);
        Cliente cliente = instanciarCliente(user);
        cliente.setPerfilUsuario(perfilUsuario);
        return salvar(cliente);
    }

    public final List<ClienteDTO> listar() {
        List<Cliente> clientes = buscarTodos();
        return mapearParaDTO(clientes);
    }

    public final ClienteDTO buscarPorId(Long id) {
        Cliente cliente = buscarOuFalhar(id);
        return mapearParaDTO(cliente);
    }

    public final void deletar(Long id) {
        Cliente cliente = buscarOuFalhar(id);
        desativarUsuario(cliente);
        salvarAlteracoes(cliente);
    }

    public final Long buscarIdPorUsuario(Long userId) {
        Cliente cliente = buscarPorUserId(userId);
        return cliente.getId();
    }

    protected abstract void validarCriacao(User user);
    protected abstract Cliente instanciarCliente(User user);
    protected abstract Cliente salvar(Cliente cliente);

    protected abstract List<Cliente> buscarTodos();
    protected abstract Cliente buscarOuFalhar(Long id);
    protected abstract Cliente buscarPorUserId(Long userId);
    protected abstract void desativarUsuario(Cliente cliente);
    protected abstract void salvarAlteracoes(Cliente cliente);
    protected abstract List<ClienteDTO> mapearParaDTO(List<Cliente> clientes);
    protected abstract ClienteDTO mapearParaDTO(Cliente cliente);
}

