package br.com.musiclink.service;

import br.com.serviceframework.domain.DTO.PrestadorDTO;
import br.com.serviceframework.domain.entity.Prestador;
import br.com.serviceframework.domain.entity.User;
import br.com.serviceframework.repository.PrestadorRepository;
import br.com.serviceframework.service.AbstractPrestadorService;
import br.com.musiclink.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrestadorServiceImpl extends AbstractPrestadorService {

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validarCriacao(User user) {
        if (user == null) {
            throw new RuntimeException("Usuário é obrigatório.");
        }
    }

    @Override
    public Prestador instanciarPrestador(User user) {
        Prestador prestador = new Prestador();
        prestador.setUser(user);
        return prestador;
    }

    @Override
    public Prestador salvar(Prestador prestador) {
        return prestadorRepository.save(prestador);
    }

    @Override
    public List<Prestador> buscarTodos() {
        return prestadorRepository.findAll();
    }

    @Override
    public Prestador buscarOuFalhar(Long id) {
        return prestadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prestador não encontrado com o ID: " + id));
    }

    @Override
    public Prestador buscarPorUserId(Long userId) {
        return prestadorRepository.findByUserId(userId);
    }

    @Override
    public void desativarUsuario(Prestador prestador) {
        User user = prestador.getUser();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void salvarAlteracoes(Prestador prestador) {
        prestadorRepository.save(prestador);
    }

    @Override
    public List<PrestadorDTO> mapearParaDTO(List<Prestador> prestadores) {
        return prestadores.stream().map(this::mapearParaDTO).toList();
    }

    @Override
    public PrestadorDTO mapearParaDTO(Prestador prestador) {
        return new PrestadorDTO(
                prestador.getId(),
                prestador.getUser().isActive(),
                prestador.getUser().getId(),
                prestador.getUser().getUsername(),
                prestador.getUser().getEmail(),
                prestador.getPerfilPrestador().getDescricao()
        );
    }
}
