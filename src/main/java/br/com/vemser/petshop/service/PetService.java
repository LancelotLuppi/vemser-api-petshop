package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PetCreateDTO;
import br.com.vemser.petshop.dto.PetDTO;
import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.entity.Pet;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.PetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public PetDTO create(Integer idCliente, PetCreateDTO petDto) {
        Pet pet = returnEntity(petDto);
        return returnDto(petRepository.adicionar(idCliente, pet));
    }

    public List<PetDTO> list() {
        return petRepository.listar().stream()
                .map(this::returnDto)
                .collect(Collectors.toList());
    }

    private Pet returnEntity(PetCreateDTO dto) {
        return objectMapper.convertValue(dto, Pet.class);
    }

    private PetDTO returnDto(Pet entity) {
        return objectMapper.convertValue(entity, PetDTO.class);
    }
}
