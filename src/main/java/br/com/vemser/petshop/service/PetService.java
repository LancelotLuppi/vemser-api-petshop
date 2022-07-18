package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PetCreateDTO;
import br.com.vemser.petshop.dto.PetDTO;
import br.com.vemser.petshop.entity.Pedido;
import br.com.vemser.petshop.entity.Pet;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.PedidoRepository;
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
    private PedidoRepository pedidoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public PetDTO create(Integer idCliente, PetCreateDTO petDto) throws SQLException, RegraDeNegocioException {
        Pet pet = returnEntity(petDto);
        pet.setIdCliente(idCliente);
        return returnDto(petRepository.adicionar(idCliente, pet));
    }

    public List<PetDTO> list(Integer idCliente) throws SQLException, RegraDeNegocioException {
        return petRepository.listarAnimalPorCliente(idCliente).stream()
                .map(this::returnDto)
                .collect(Collectors.toList());
    }

    public PetDTO update(Integer idPet, PetCreateDTO petDto) throws SQLException, RegraDeNegocioException {
        Pet petRecuperado = petRepository.returnByIdUtil(idPet);
        Pet petAtualizado = returnEntity(petDto);
        petAtualizado.setIdCliente(petRecuperado.getIdCliente());
        return returnDto(petRepository.update(idPet, petAtualizado));
    }

    public void delete(Integer id) throws SQLException, RegraDeNegocioException {
        pedidoRepository.removerPedidosPorIDAnimal(id);
        petRepository.remover(id);
    }

    private Pet returnEntity(PetCreateDTO dto) {
        return objectMapper.convertValue(dto, Pet.class);
    }

    private PetDTO returnDto(Pet entity) {
        return objectMapper.convertValue(entity, PetDTO.class);
    }
}
