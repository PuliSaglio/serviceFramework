package br.com.mindlink.controller;

import br.com.serviceframework.domain.DTO.BuscaServicosDTO;
import br.com.mindlink.domain.DTO.ServicoDTO;
import br.com.serviceframework.domain.entity.Servico;
import br.com.serviceframework.service.ServicoService;
import br.com.mindlink.service.ServicoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/servico")
public class ServicoController {
    @Autowired
    private ServicoServiceImpl servicoService;

    /**
     * Busca serviços com filtros opcionais. Se nenhum filtro for fornecido, retorna todos os serviços.
     *
     * @param id ID do serviço
     * @param nome Nome do serviço
     * @param descricao Descrição do serviço
     * @param precoMin Preço mínimo
     * @param precoMax Preço máximo
     * @param categoria Categoria do serviço
     * @return Lista de serviços que atendem aos filtros
     */
    @GetMapping
    public List<Servico> findAll(
            @RequestParam(required = false, name = "id") Long id,
            @RequestParam(required = false, name = "nome") String nome,
            @RequestParam(required = false, name = "descricao") String descricao,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            //colocar converson de string para id?
            @RequestParam(required = false) Long categoria
            ){
        boolean semFiltro = id == null && nome == null && descricao == null && precoMin == null && precoMax == null && categoria == null;

        if (semFiltro) {
            return servicoService.listarServicos();
        }

        BuscaServicosDTO filtro = new BuscaServicosDTO(id, nome, descricao, precoMin, precoMax, categoria);
        try{
            return servicoService.buscarServico(filtro);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Remove um serviço.
     *
     * @param id ID do serviço a ser removido
     */
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        servicoService.deletarServico(id);
    }

    /**
     * Adiciona uma lista de serviços a um prestador.
     *
     * @param prestadorId ID do prestador
     * @param servicosDTO Lista de serviços a serem adicionados
     * @return Lista de serviços criados
     */
    @PostMapping("/prestador/{prestadorId}")
    public List<Servico> addServicos(@PathVariable("prestadorId") Long prestadorId, @RequestBody @Valid List<ServicoDTO> servicosDTO) {
        try {
            return servicoService.adicionarServicos(prestadorId, servicosDTO);
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            } else if (e instanceof EntityNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            }
        }
    }

    /**
     * Atualiza um serviço existente.
     *
     * @param servicoDTO Dados do serviço a ser atualizado
     * @param servicoId ID do serviço
     * @return Serviço atualizado
     */
    @PutMapping("/{servicoId}")
    public Servico atualizarServicos(@RequestBody ServicoDTO servicoDTO, @PathVariable Long servicoId) {
        try {
            return servicoService.editarServico(servicoId, servicoDTO);
        } catch (Exception e) {
            if (e instanceof BadRequestException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            } else if (e instanceof EntityNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
            }
        }
    }

    /**
     * Lista todos os serviços oferecidos por um prestador específico.
     *
     * @param prestadorId ID do prestador
     * @return Lista de serviços do prestador
     */
    @GetMapping("/prestador/{prestadorId}")
    public List<Servico> getServicosPrestador(@PathVariable Long prestadorId) {
        return servicoService.buscarServicosPorPrestadorId(prestadorId);
    }
}

