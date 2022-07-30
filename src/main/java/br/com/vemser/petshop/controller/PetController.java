package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.documentation.PetDocumentation;
import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.pet.PetCreateDTO;
import br.com.vemser.petshop.dto.pet.PetDTO;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pet")
@Validated
public class PetController implements PetDocumentation {
    @Autowired
    private PetService petService;

    @PostMapping("/{idCliente}")
    public ResponseEntity<PetDTO> post(@PathVariable("idCliente") Integer id, @Valid @RequestBody PetCreateDTO pet) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(petService.createByClientId(id, pet));
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<List<PetDTO>> get(@PathVariable("idCliente") Integer id) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(petService.getByClientId(id));
    }

    @GetMapping("/{idPet}/pet")
    public ResponseEntity<PetDTO> getByPetId(@PathVariable("idPet") Integer idPet) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(petService.getByPetId(idPet));
    }

    @GetMapping("/page-pets")
    public PageDTO<PetDTO> paginarPetsCliente(@RequestParam(value = "idCliente", required = false) Integer idCliente,
                                              Integer pagina, Integer quantidadeRegistro) {
        return petService.paginarPets(idCliente, pagina, quantidadeRegistro);
    }

    @PutMapping("/{idPet}")
    public ResponseEntity<PetDTO> put(@PathVariable("idPet") Integer id, @Valid @RequestBody PetCreateDTO petAtualizado) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(petService.update(id, petAtualizado));
    }

    @DeleteMapping("/{idPet}")
    public void delete(@PathVariable("idPet") Integer id) throws EntidadeNaoEncontradaException {
        petService.delete(id);
    }
}
