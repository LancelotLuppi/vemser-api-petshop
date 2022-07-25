package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.documentation.ClienteDocumentation;
import br.com.vemser.petshop.dto.ClienteCreateDTO;
import br.com.vemser.petshop.dto.ClienteDTO;
import br.com.vemser.petshop.dto.ClienteDadosRelatorioDTO;
import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cliente")
@Validated
public class ClienteController implements ClienteDocumentation {
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<ClienteDTO> post(@Valid @RequestBody ClienteCreateDTO cliente) throws RegraDeNegocioException {
        return ResponseEntity.ok(clienteService.create(cliente));
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> getById(@PathVariable("idCliente") Integer id) {
        return ResponseEntity.ok(clienteService.getById(id));
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<ClienteDTO> put(@PathVariable("idCliente") Integer id, @Valid @RequestBody ClienteCreateDTO clienteAtualizado) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(clienteService.update(id, clienteAtualizado));
    }

    @DeleteMapping("/{idCliente}")
    public void delete(@PathVariable("idCliente") Integer id) throws EntidadeNaoEncontradaException {
        clienteService.delete(id);
    }

    @GetMapping("/relatorio-dados")
    public ResponseEntity<List<ClienteDadosRelatorioDTO>> listClienteDados(@RequestParam(value = "idCliente" , required = false) Integer idCliente) {
        return ResponseEntity.ok(clienteService.listarDadosCliente(idCliente));
    }
}
