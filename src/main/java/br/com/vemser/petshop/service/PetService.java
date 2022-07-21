package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PetCreateDTO;
import br.com.vemser.petshop.dto.PetDTO;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
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
    private ClienteService clienteService;

    private final static String NOT_FOUND_MESSAGE = "{idPet} não encontrado";

    public PetDTO create(Integer idCliente, PetCreateDTO petDto) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        clienteService.verificarId(idCliente);
        PetEntity petEntity = returnEntity(petDto);
        petEntity.setIdCliente(idCliente);
        return returnDto(petRepository.adicionar(idCliente, petEntity));
    }

    public List<PetDTO> list(Integer idCliente) throws SQLException, EntidadeNaoEncontradaException {
        clienteService.verificarId(idCliente);
        return petRepository.listarAnimalPorCliente(idCliente).stream()
                .map(this::returnDto)
                .collect(Collectors.toList());
    }

    public PetDTO getByPetId(Integer idPet) throws SQLException, EntidadeNaoEncontradaException {
        verificarIdPet(idPet);
        return returnDto(petRepository.getPetPorId(idPet));
    }

    public PetDTO update(Integer idPet, PetCreateDTO petDto) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        verificarIdPet(idPet);
        PetEntity petEntityRecuperado = petRepository.returnByIdUtil(idPet);
        PetEntity petEntityAtualizado = returnEntity(petDto);
        petEntityAtualizado.setIdCliente(petEntityRecuperado.getIdCliente());
        return returnDto(petRepository.update(idPet, petEntityAtualizado));
    }

    public void delete(Integer id) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        verificarIdPet(id);
        pedidoRepository.removerPedidosPorIDAnimal(id);
        petRepository.remover(id);
    }

    public void verificarIdPet(Integer id) throws SQLException, EntidadeNaoEncontradaException {
        petRepository.listar().stream()
                .filter(petEntity -> petEntity.getIdPet().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    private PetEntity returnEntity(PetCreateDTO dto) {
        return objectMapper.convertValue(dto, PetEntity.class);
    }

    private PetDTO returnDto(PetEntity entity) {
        return objectMapper.convertValue(entity, PetDTO.class);
    }
}
