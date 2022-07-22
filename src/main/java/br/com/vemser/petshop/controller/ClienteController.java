package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.documentation.ClienteDocumentation;
import br.com.vemser.petshop.dto.ClienteCreateDTO;
import br.com.vemser.petshop.dto.ClienteDTO;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.service.ClienteService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cliente")
@Validated
public class ClienteController implements ClienteDocumentation {
    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> post(@Valid @RequestBody ClienteCreateDTO cliente) throws RegraDeNegocioException {
        return ResponseEntity.ok(clienteService.create(cliente));
    }

    @Hidden // Criado para testar conexão com a DB e facilitar no desenvolvimento da api
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> get() throws RegraDeNegocioException {
        return ResponseEntity.ok(clienteService.list());
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> getById(@PathVariable("idCliente") Integer id) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(clienteService.getById(id));
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> put(@PathVariable("idCliente") Integer id, @Valid @RequestBody ClienteCreateDTO clienteAtualizado) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        return ResponseEntity.ok(clienteService.update(id, clienteAtualizado));
    }

    @DeleteMapping("/{idCliente}")
    public void delete(@PathVariable("idCliente") Integer id) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        clienteService.delete(id);
    }
}
