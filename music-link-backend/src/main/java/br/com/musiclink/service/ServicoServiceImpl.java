package br.com.musiclink.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.musiclink.domain.entity.ServicoMusicInfo;
import br.com.musiclink.repository.ServicoMusicInfoRepository;
import br.com.serviceframework.domain.DTO.BuscaServicosDTO;
import br.com.serviceframework.domain.interfaces.ICategoriaServicos;
import br.com.musiclink.domain.DTO.ServicoDTO;
import br.com.serviceframework.domain.entity.Prestador;
import br.com.serviceframework.repository.PrestadorRepository;
import br.com.musiclink.enumerations.CategoriaMusica;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.serviceframework.domain.entity.Servico;
import br.com.serviceframework.repository.ServicoRepository;
import br.com.serviceframework.service.ServicoService;

@Service
public class ServicoServiceImpl extends ServicoService {

    private final ServicoRepository servicoRepository;
    private final ServicoMusicInfoRepository servicoMusicInfoRepository;

    @Autowired
    public ServicoServiceImpl(ServicoRepository servicoRepository,
                              ServicoMusicInfoRepository servicoMusicInfoRepository) {
        super(servicoRepository);
        this.servicoRepository = servicoRepository;
        this.servicoMusicInfoRepository = servicoMusicInfoRepository;
    }

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Transactional
    public List<Servico> adicionarServicos(Long prestadorId, List<ServicoDTO> servicosDTO) throws BadRequestException {

        validarListaServicos(servicosDTO);
        validarPrestadorId(prestadorId);

        Prestador prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new EntityNotFoundException("Prestador não encontrado com o ID: " + prestadorId));

        for (ServicoDTO dto : servicosDTO) {
            validarServicoDTO(dto);
        }

        List<Servico> novosServicos = servicosDTO.stream()
                .map(dto -> criarServicoFromDTO(dto, prestador))
                .collect(Collectors.toList());

        return servicoRepository.saveAll(novosServicos);

    }

    public Servico editarServico(Long servicoId, ServicoDTO newServicoDTO) throws BadRequestException {
        ICategoriaServicos categoriaDoDominio = CategoriaMusica.ofId(newServicoDTO.categoriaId());

        if (categoriaDoDominio == null) {
            throw new IllegalArgumentException("ID de Categoria " + newServicoDTO.categoriaId() + " é inválido para este domínio.");
        }

        validarServicoId(servicoId);

        validarServicoDTO(newServicoDTO);

        Servico servicoAtual = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + servicoId));

        servicoAtual.setNome(newServicoDTO.nome().trim());
        servicoAtual.setDescricao(newServicoDTO.descricao().trim());
        servicoAtual.setPrecoBase(newServicoDTO.precoBase());
        servicoAtual.setCategoria(categoriaDoDominio);
        servicoAtual.setImagemUrl(newServicoDTO.imagemUrl());

        return servicoRepository.save(servicoAtual);
    }

    private void validarListaServicos(List<ServicoDTO> servicosDTO) throws BadRequestException {
        if (servicosDTO == null || servicosDTO.isEmpty()) {
            throw new BadRequestException("A lista de serviços não pode estar vazia.");
        }
    }

    private void validarPrestadorId(Long prestadorId) throws BadRequestException {
        if (prestadorId == null || prestadorId <= 0) {
            throw new BadRequestException("ID do prestador inválido.");
        }
    }

    private void validarServicoDTO(ServicoDTO dto) throws BadRequestException {

        if (dto == null) {
            throw new BadRequestException("Serviço não pode ser nulo.");
        }

        if (dto.nome() == null || dto.nome().trim().isEmpty()) {
            throw new BadRequestException("Nome do serviço é obrigatório.");
        }

        if (dto.descricao() == null || dto.descricao().trim().isEmpty()) {
            throw new BadRequestException("Descrição do serviço é obrigatória.");
        }

        if (dto.descricao().length() > 300) {
            throw new BadRequestException("Descrição do serviço não pode exceder 300 caracteres.");
        }

        if (dto.precoBase() == null) {
            throw new BadRequestException("Preço base do serviço é obrigatório.");
        }

        if (dto.precoBase().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Preço base deve ser maior que zero.");
        }
    }

    private void validarBuscaServicoDTO(BuscaServicosDTO dto) throws BadRequestException {
        if (dto == null) {
            throw new BadRequestException("Serviço não pode ser nulo.");
        }

        if(dto.precoMax() != null && dto.precoMin() != null){
            if (dto.precoMax().compareTo(dto.precoMin()) < 1) {
                throw new BadRequestException("Preço max deve ser maior que preço min");
            }
        }
        if(dto.precoMax() !=null){
            if(dto.precoMax().compareTo(java.math.BigDecimal.ZERO) <= 0){
                throw new BadRequestException("Preco max deve ser maior que zero");
            }
        }
        if(dto.precoMin() !=null){
            if(dto.precoMin().compareTo(java.math.BigDecimal.ZERO) <= 0){
                throw new BadRequestException("Preco min deve ser maior que zero");
            }
        }
    }

    private Servico criarServicoFromDTO(ServicoDTO dto, Prestador prestador) {
        ICategoriaServicos categoriaDoDominio = CategoriaMusica.ofId(dto.categoriaId());



        if (categoriaDoDominio == null) {
            throw new IllegalArgumentException("ID de Categoria " + dto.categoriaId() + " é inválido para este domínio.");
        }
        Servico servico = new Servico();
        servico.setNome(dto.nome().trim());
        servico.setDescricao(dto.descricao().trim());
        servico.setPrecoBase(dto.precoBase());

        servico.setCategoria(categoriaDoDominio);

        servico.setPrestador(prestador);

        servicoRepository.save(servico);
        ServicoMusicInfo musicInfo = new ServicoMusicInfo();
        musicInfo.setServicoId(servico.getId());
        musicInfo.setQuantidadeAulas(dto.duracaoEmDias());
        CategoriaMusica instrumento = CategoriaMusica.ofId(dto.categoriaId());
        musicInfo.setInstrumento(instrumento);
        servicoMusicInfoRepository.save(musicInfo);
        return servico;
    }

    @Override
    public List<Servico> listarServicos() {
        return servicoRepository.findAll();
    }


    @Override
    public void deletarServico(Long id) {
        servicoRepository.deleteById(id);
    }


    public List<Servico> buscarServicosPorPrecoBase(String categoria, String nome) {

        CategoriaMusica categoriaDomestica = CategoriaMusica.fromString(categoria);

        Optional<Servico> servicoMaisCaro = servicoRepository.findTop1ByOrderByPrecoBaseDesc(categoriaDomestica, nome);
        Optional<Servico> servicoMaisBarato = servicoRepository.findTop1ByOrderByPrecoBaseAsc(categoriaDomestica, nome);

        List<Servico> servicosEncontrados = new ArrayList<>();
        servicoMaisCaro.ifPresent(servicosEncontrados::add);
        servicoMaisBarato.ifPresent(servicosEncontrados::add);

        return servicosEncontrados;
    }


    public List<Servico> buscarServicosPorPrestadorId(Long prestadorId) {
        List<Servico> servicos = servicoRepository.findByPrestadorId(prestadorId);
        return servicos;
    }

    public List<Servico> buscarServico(BuscaServicosDTO servicoDTO) throws BadRequestException {

        validarBuscaServicoDTO(servicoDTO);

        ICategoriaServicos categoriaDoDominio = CategoriaMusica.ofId(servicoDTO.id());

        if (categoriaDoDominio == null) {
            throw new IllegalArgumentException("ID de Categoria " + servicoDTO.id() + " é inválido para este domínio.");
        }

        List<Servico> servicos = servicoRepository.buscarServicos(
                servicoDTO.id(),
                servicoDTO.nome(),
                servicoDTO.descricao(),
                servicoDTO.precoMin(),
                servicoDTO.precoMax(),
                categoriaDoDominio
        );

        return servicos;
    }

    private void validarServicoId(Long servicoId) throws BadRequestException {
        if (servicoId == null || servicoId <= 0) {
            throw new BadRequestException("Id do Serviço inválido");
        }
    }
}
