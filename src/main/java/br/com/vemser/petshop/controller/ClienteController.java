package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.dto.ClienteCreateDTO;
import br.com.vemser.petshop.dto.ClienteDTO;
import br.com.vemser.petshop.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/cliente")
@Validated
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> post(@Valid @RequestBody ClienteCreateDTO cliente) throws SQLException {
        return ResponseEntity.ok(clienteService.create(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> get() throws SQLException {
        return ResponseEntity.ok(clienteService.list());
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> getById(@PathVariable("idCliente") Integer id) throws SQLException {
        return ResponseEntity.ok(clienteService.getById(id));
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> put(@PathVariable("idCliente") Integer id, @RequestBody ClienteCreateDTO clienteAtualizado) throws SQLException {
        return ResponseEntity.ok(clienteService.update(id, clienteAtualizado));
    }

    @DeleteMapping("/{idCliente}")
    public void delete(@PathVariable("idCliente") Integer id) throws SQLException {
        clienteService.delete(id);
    }
}
