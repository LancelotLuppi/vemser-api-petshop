package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.documentation.ContatoDocumentation;
import br.com.vemser.petshop.dto.ContatoCreateDTO;
import br.com.vemser.petshop.dto.ContatoDTO;

import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.exception.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contato")
@Validated
public class ContatoController implements ContatoDocumentation {


    @Autowired
    private ContatoService contatoService;

    @GetMapping("/{idCliente}")
    public ResponseEntity<List<ContatoDTO>> get(@PathVariable("idCliente") Integer id) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(contatoService.listarContatoPorId(id));
    }

    @PostMapping("/{idCliente}")
    public ResponseEntity<ContatoDTO> post(@PathVariable("idCliente") Integer id, @Valid @RequestBody ContatoCreateDTO contato) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        return ResponseEntity.ok(contatoService.create(id, contato));
    }

    @PutMapping("/{idContato}")
    public ResponseEntity<ContatoDTO> put(@PathVariable("idContato") Integer id,
                                                       @Valid @RequestBody ContatoCreateDTO contatoAtualizado) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        return ResponseEntity.ok(contatoService.update(id, contatoAtualizado));
    }

    @DeleteMapping("/{idContato}")
    public void delete(@PathVariable("idContato") Integer id) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        contatoService.delete(id);
    }
}
