package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.pet.PetCreateDTO;
import br.com.vemser.petshop.dto.pet.PetDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.repository.PedidoRepository;
import br.com.vemser.petshop.repository.PetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PetDTO create(Integer idCliente, PetCreateDTO petDto) throws EntidadeNaoEncontradaException {
        ClienteEntity clienteEntity = clienteService.retornarPorIdVerificado(idCliente);
        PetEntity petEntity = returnEntity(petDto);

        petEntity.setCliente(clienteEntity);

        return returnDtoWithId(petRepository.save(petEntity));
    }

    public List<PetDTO> list(Integer idCliente) throws EntidadeNaoEncontradaException {
        clienteService.verificarId(idCliente);
        return petRepository.findAll().stream()
                .filter(pet -> pet.getCliente().getIdCliente().equals(idCliente))
                .map(this::returnDtoWithId)
                .toList();
    }

    public PetDTO getByPetId(Integer idPet) throws EntidadeNaoEncontradaException {
        return returnDtoWithId(getPetByIdEntity(idPet));
    }



    public PetDTO update(Integer idPet, PetCreateDTO petDto) throws EntidadeNaoEncontradaException {
        PetEntity petRecuperado = getPetByIdEntity(idPet);

        petRecuperado.setNome(petDto.getNome());
        petRecuperado.setTipoPet(petDto.getTipoPet());
        petRecuperado.setRaca(petDto.getRaca());
        petRecuperado.setPelagem(petDto.getPelagem());
        petRecuperado.setPorte(petDto.getPorte());
        petRecuperado.setIdade(petDto.getIdade());

        return returnDtoWithId(petRepository.save(petRecuperado));
    }

    public void delete(Integer id) throws EntidadeNaoEncontradaException {
        PetEntity petRecuperado = getPetByIdEntity(id);
        petRepository.delete(petRecuperado);
    }

    public PageDTO<PetDTO> paginarPets(Integer idCliente, Integer pagina, Integer registro) {
        PageRequest pageRequest = PageRequest.of(pagina, registro);
        Page<PetEntity> page = petRepository.findById(idCliente, pageRequest);
        List<PetDTO> petDTOS = page.getContent().stream()
                .map(this::returnDtoWithId)
                .toList();
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, registro, petDTOS);
    }

    public void verificarIdPet(Integer id) throws EntidadeNaoEncontradaException {
        petRepository.findAll().stream()
                .filter(petEntity -> petEntity.getIdPet().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    public PetEntity getPetByIdEntity(Integer idPet) throws EntidadeNaoEncontradaException {
        return petRepository.findById(idPet).stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    private PetEntity returnEntity(PetCreateDTO dto) {
        return objectMapper.convertValue(dto, PetEntity.class);
    }

    private PetDTO returnDto(PetEntity entity) {
        return objectMapper.convertValue(entity, PetDTO.class);
    }

    private PetDTO returnDtoWithId(PetEntity entity) {
        PetDTO dto = returnDto(entity);
        dto.setIdCliente(entity.getCliente().getIdCliente());
        return dto;
    }
}
