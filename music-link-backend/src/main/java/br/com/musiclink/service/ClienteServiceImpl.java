package br.com.musiclink.service;

import java.util.List;

import br.com.serviceframework.domain.DTO.ClienteDTO;
import br.com.serviceframework.domain.entity.User;
import br.com.serviceframework.service.AbstractClienteService;
import br.com.musiclink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serviceframework.repository.ClienteRepository;
import br.com.serviceframework.domain.entity.Cliente;


@Service
public class ClienteServiceImpl extends AbstractClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void validarCriacao(User user) {
        if (user == null) {
            throw new RuntimeException("Usuário é obrigatório.");
        }
    }

    @Override
    protected Cliente instanciarCliente(User user) {
        Cliente cliente = new Cliente();
        cliente.setUser(user);
        return cliente;
    }

    @Override
    protected Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    protected Cliente buscarOuFalhar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + id));
    }

    @Override
    public Cliente buscarPorUserId(Long userId) {
        return clienteRepository.findByUserId(userId);
    }

    @Override
    public void desativarUsuario(Cliente cliente) {
        User user = cliente.getUser();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    protected void salvarAlteracoes(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    @Override
    public List<ClienteDTO> mapearParaDTO(List<Cliente> clientes) {
        return clientes.stream().map(this::mapearParaDTO).toList();
    }

    @Override
    public ClienteDTO mapearParaDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getUser().getId(),
                cliente.getUser().getUsername(),
                cliente.getUser().getEmail()
        );
    }
}

