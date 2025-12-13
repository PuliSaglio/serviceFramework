package br.com.musiclink.service;

import java.util.List;

import br.com.musiclink.domain.DTO.ClienteInstrumentosDTO;
import br.com.musiclink.domain.DTO.InstrumentoPreferenciaDTO;
import br.com.musiclink.enumerations.CategoriaMusica;
import br.com.serviceframework.domain.DTO.ClienteDTO;
import br.com.serviceframework.domain.entity.PerfilUsuario;
import br.com.serviceframework.domain.entity.User;
import br.com.serviceframework.service.AbstractClienteService;
import br.com.musiclink.repository.UserRepository;
import br.com.musiclink.domain.entity.ClientePerfil;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serviceframework.repository.ClienteRepository;
import br.com.serviceframework.domain.entity.Cliente;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl extends AbstractClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void validarCriacao(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Usuário é obrigatório para criar um cliente.");
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
        if (user != null) {
            user.setActive(false);
            userRepository.save(user);
        }
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
        String nome = "Sem Nome";
        if (cliente.getPerfilUsuario() != null) {
            nome = cliente.getPerfilUsuario().getNome();
        }

        return new ClienteDTO(
                cliente.getId(),
                cliente.getUser().getId(),
                cliente.getUser().getUsername(),
                cliente.getUser().getEmail()
        );
    }

    /**
     * Método ESPECÍFICO desta aplicação. Não existe no AbstractClienteService.
     * Atualiza a lista de instrumentos do cliente logado.
     */
    @Transactional
    public void atualizarInstrumentos(Long userId, ClienteInstrumentosDTO dto) {

        Cliente cliente = buscarPorUserId(userId);

        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado para este usuário.");
        }

        PerfilUsuario perfilGenerico = cliente.getPerfilUsuario();
        PerfilUsuario perfilReal = (PerfilUsuario) Hibernate.unproxy(perfilGenerico);
        if (perfilReal instanceof ClientePerfil perfilMusical) {

            perfilMusical.getInstrumentos().clear();

            if (dto.instrumentos() != null) {
                for (InstrumentoPreferenciaDTO item : dto.instrumentos()) {
                    CategoriaMusica instrumento = CategoriaMusica.ofId(item.categoriaId());

                    perfilMusical.adicionarInstrumento(instrumento, item.nivel());
                }
            }

            salvarAlteracoes(cliente);

        } else {
            throw new IllegalStateException("O perfil deste usuário não é um perfil de músico.");
        }
    }
}