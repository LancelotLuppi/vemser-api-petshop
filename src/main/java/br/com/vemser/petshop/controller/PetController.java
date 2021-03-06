package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.documentation.PetDocumentation;
import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.PetCreateDTO;
import br.com.vemser.petshop.dto.PetDTO;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/pet")
@Validated
public class PetController implements PetDocumentation {
    @Autowired
    private PetService petService;

    @PostMapping("/{idCliente}")
    public ResponseEntity<PetDTO> post(@PathVariable("idCliente") Integer id, @Valid @RequestBody PetCreateDTO pet) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        return ResponseEntity.ok(petService.create(id, pet));
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<List<PetDTO>> get(@PathVariable("idCliente") Integer id) throws SQLException, EntidadeNaoEncontradaException {
        return ResponseEntity.ok(petService.list(id));
    }

    @GetMapping("/{idPet}/pet")
    public ResponseEntity<PetDTO> getByPetId(@PathVariable("idPet") Integer idPet) throws SQLException, EntidadeNaoEncontradaException {
        return ResponseEntity.ok(petService.getByPetId(idPet));
    }

    @GetMapping("/page-pets")
    public PageDTO<PetDTO> paginarPetsCliente(@RequestParam(value = "idCliente", required = false) Integer idCliente,
                                              Integer pagina, Integer quantidadeRegistro) throws Exception{
        return petService.paginarPets(idCliente, pagina, quantidadeRegistro);
    }

    @PutMapping("/{idPet}")
    public ResponseEntity<PetDTO> put(@PathVariable("idPet") Integer id, @Valid @RequestBody PetCreateDTO petAtualizado) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        return ResponseEntity.ok(petService.update(id, petAtualizado));
    }

    @DeleteMapping("/{idPet}")
    public void delete(@PathVariable("idPet") Integer id) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        petService.delete(id);
    }
}
