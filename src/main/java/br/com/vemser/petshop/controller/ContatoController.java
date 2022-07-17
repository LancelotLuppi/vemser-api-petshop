package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.dto.ContatoCreateDTO;
import br.com.vemser.petshop.dto.ContatoDTO;

import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/contato")
@Validated
public class ContatoController {


    @Autowired
    private ContatoService contatoService;

    @GetMapping("/{idCliente}")
    public ResponseEntity<List<ContatoDTO>> buscarPorIdCliente(@PathVariable("idCliente") Integer id) throws SQLException, RegraDeNegocioException {
        return ResponseEntity.ok(contatoService.listarContatoPorId(id));
    }

    @PostMapping("/{idCliente}")
    public ResponseEntity<ContatoDTO> criarContatoPorCliente(@PathVariable("idCliente") Integer id, @Valid @RequestBody ContatoCreateDTO contato) throws SQLException, RegraDeNegocioException {
        return ResponseEntity.ok(contatoService.create(id, contato));
    }

    @PutMapping("/{idContato}")
    public ResponseEntity<ContatoDTO> atualizarContato(@PathVariable("idContato") Integer id,
                                                       @Valid @RequestBody ContatoCreateDTO contatoAtualizado) throws SQLException, RegraDeNegocioException {
        return ResponseEntity.ok(contatoService.update(id, contatoAtualizado));
    }

    @DeleteMapping("/{idContato}")
    public void delete(@PathVariable("idContato") Integer id) throws SQLException, RegraDeNegocioException {
        contatoService.delete(id);
    }
}
