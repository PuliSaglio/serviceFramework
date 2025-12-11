package br.com.serviceframework.service;

import br.com.serviceframework.domain.entity.Servico;
import br.com.serviceframework.domain.interfaces.ICategoriaServicos;
import br.com.serviceframework.repository.ServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }


    @Transactional
    public List<Servico> adicionarServicos(List<Servico> novosServicos) {
        novosServicos.forEach(this::validarIntegridadeBase);
        return servicoRepository.saveAll(novosServicos);
    }

    @Transactional
    public Servico editarServico(Long servicoId, Servico servicoAtualizado) {
        validarServicoId(servicoId);
        validarIntegridadeBase(servicoAtualizado);

        Servico servicoExistente = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + servicoId));

        servicoAtualizado.setId(servicoExistente.getId());
        servicoAtualizado.setPrestador(servicoExistente.getPrestador());

        return servicoRepository.save(servicoAtualizado);
    }


    public List<Servico> listarServicos() {
        return servicoRepository.findAll();
    }

    public void deletarServico(Long id) {
        servicoRepository.deleteById(id);
    }

    public final List<Servico> buscarServicosPorPrecoBase(ICategoriaServicos categoria, String nome) {
        // A categoria é uma interface, o JPA lida com ela via AttributeConverter.
        Optional<Servico> servicoMaisCaro = servicoRepository.findTop1ByOrderByPrecoBaseDesc(categoria, nome);
        Optional<Servico> servicoMaisBarato = servicoRepository.findTop1ByOrderByPrecoBaseAsc(categoria, nome);

        List<Servico> servicosEncontrados = new ArrayList<>();
        servicoMaisCaro.ifPresent(servicosEncontrados::add);
        servicoMaisBarato.ifPresent(servicosEncontrados::add);

        return servicosEncontrados;
    }

    // Validações (FIXAS)

    private void validarIntegridadeBase(Servico servico) {
        if (servico.getPrestador() == null || servico.getPrestador().getId() == null) {
            throw new IllegalArgumentException("O Prestador é obrigatório (Framework Rule).");
        }
        if (servico.getNome() == null || servico.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do serviço é obrigatório.");
        }
    }

    private void validarServicoId(Long servicoId) {
        if (servicoId == null || servicoId <= 0) {
            throw new IllegalArgumentException("Id do Serviço inválido");
        }
    }
}