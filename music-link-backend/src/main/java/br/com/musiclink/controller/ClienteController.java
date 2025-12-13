package br.com.musiclink.controller;

import br.com.musiclink.domain.DTO.ClienteInstrumentosDTO;
import br.com.serviceframework.domain.DTO.ClienteDTO;

import br.com.serviceframework.domain.entity.Cliente;
import br.com.musiclink.service.ClienteServiceImpl;
import br.com.serviceframework.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
    @Autowired
    private ClienteServiceImpl clienteService;

    /**
     * Lista todos os clientes cadastrados.
     *
     * @return Lista de clientes
     */
    @GetMapping
    public List<ClienteDTO> findAll(){
        List<Cliente> listaClientes = clienteService.buscarTodos();
        return clienteService.mapearParaDTO(listaClientes);
    }

    /**
     * Busca um cliente específico por ID.
     *
     * @param id ID do cliente
     * @return Dados do cliente
     */
    @GetMapping(value = "/{id}")
    public ClienteDTO findById(@PathVariable Long id){
        Cliente cliente =  clienteService.buscarPorUserId(id);
        return  clienteService.mapearParaDTO(cliente);
    }

    /**
     * Desativa específico um cliente.
     *
     * @param id ID do cliente a ser desativado
     */
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id){
        Cliente cliente =  clienteService.buscarPorUserId(id);
        clienteService.desativarUsuario(cliente);
    }

    @PutMapping("/me/instrumentos")
    public ResponseEntity<Void> atualizarInstrumentos(
            @AuthenticationPrincipal User userLogado, // Pega o usuário do Token JWT
            @RequestBody ClienteInstrumentosDTO dto
    ) {
        clienteService.atualizarInstrumentos(userLogado.getId(), dto);
        return ResponseEntity.noContent().build();
    }
}
