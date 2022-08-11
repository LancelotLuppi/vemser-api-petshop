package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.documentation.ClienteDocumentation;
import br.com.vemser.petshop.dto.cliente.ClienteCreateDTO;
import br.com.vemser.petshop.dto.cliente.ClienteDTO;
import br.com.vemser.petshop.dto.cliente.ClienteDadosRelatorioDTO;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.service.ClienteService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public ResponseEntity<ClienteDTO> post(@Valid @RequestBody ClienteCreateDTO cliente) throws EntidadeNaoEncontradaException, RegraDeNegocioException, JsonProcessingException {
        return ResponseEntity.ok(clienteService.create(cliente));
    }

    @GetMapping("/logged-user")
    public ResponseEntity<ClienteDTO> getByLoggedUser() throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(clienteService.getLogged());
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> getById(@PathVariable("idCliente") Integer id) {
        return ResponseEntity.ok(clienteService.getById(id));
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> put(@PathVariable("idCliente") Integer id, @Valid @RequestBody ClienteCreateDTO clienteAtualizado) throws EntidadeNaoEncontradaException, JsonProcessingException {
        return ResponseEntity.ok(clienteService.update(id, clienteAtualizado));
    }

    @DeleteMapping("/{idCliente}")
    public void delete(@PathVariable("idCliente") Integer id) throws EntidadeNaoEncontradaException, JsonProcessingException {
        clienteService.delete(id);
    }

    @GetMapping("/relatorio-dados")
    public ResponseEntity<List<ClienteDadosRelatorioDTO>> listClienteDados(@RequestParam(value = "idCliente" , required = false) Integer idCliente) {
        return ResponseEntity.ok(clienteService.listarDadosCliente(idCliente));
    }
}
