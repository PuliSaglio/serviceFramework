package br.com.musiclink.controller;

import br.com.serviceframework.domain.DTO.PrestadorDTO;
import br.com.serviceframework.domain.entity.Prestador;
import br.com.musiclink.service.PrestadorServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/prestador")
public class PrestadorController {
    @Autowired
    private PrestadorServiceImpl prestadorService;

    /**
     * Lista todos os prestadores cadastrados.
     *
     * @return Lista de prestadores
     */
    @GetMapping
    public List<PrestadorDTO> findAll(){
        List<Prestador> listaPrestadores = prestadorService.buscarTodos();
        return prestadorService.mapearParaDTO(listaPrestadores);
    }

    /**
     * Busca um prestador específico por ID.
     *
     * @param id ID do prestador
     * @return Dados do prestador
     */
    @GetMapping(value = "/{id}")
    public PrestadorDTO findById(@PathVariable Long id){
        try {
            return prestadorService.buscarPorId(id);
        } catch (Exception e) {
            if (e instanceof EntityNotFoundException) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
        }
    }

    /**
     * Desativa um prestador.
     *
     * @param id ID do prestador a ser desativado
     */
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        Prestador prestador = prestadorService.buscarOuFalhar(id);
        prestadorService.desativarUsuario(prestador);
    }
}
