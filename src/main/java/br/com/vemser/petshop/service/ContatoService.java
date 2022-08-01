package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.contato.ContatoCreateDTO;
import br.com.vemser.petshop.dto.contato.ContatoDTO;
import br.com.vemser.petshop.dto.pet.PetDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.ContatoEntity;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ContatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContatoService {

    private final ObjectMapper objectMapper;
    private final ContatoRepository contatoRepository;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final static String NOT_FOUND_MESSAGE = "{idContato} não encontrado";



    public List<ContatoDTO> getByLoggedUser() throws EntidadeNaoEncontradaException{
        ClienteEntity clienteLogado = clienteService.returnLoggedClient();

        return clienteLogado.getContatos().stream()
                .map(this::returnDtoWithId).toList();
    }

    public ContatoDTO createByLoggedUser(ContatoCreateDTO contatoDTO) throws EntidadeNaoEncontradaException{
        ClienteEntity clienteLogado = clienteService.returnLoggedClient();
        ContatoEntity contatoEntity = returnEntity(contatoDTO);
        contatoEntity.setCliente(clienteLogado);
        return returnDtoWithId(contatoRepository.save(contatoEntity));
    }

    public ContatoDTO updateByLoggedUser(Integer idContato, ContatoCreateDTO contatoDto) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        verificarContatoDoUserLogado(idContato);
        return update(idContato, contatoDto);
    }


    public ContatoDTO create(Integer idCliente, ContatoCreateDTO contatoDTO) throws EntidadeNaoEncontradaException {
        ClienteEntity clienteRecuperado = clienteService.retornarPorIdVerificado(idCliente);
        ContatoEntity contato = returnEntity(contatoDTO);

        contato.setCliente(clienteRecuperado);

        return returnDtoWithId(contatoRepository.save(contato));
    }

    public List<ContatoDTO> listByIdCliente(Integer idCliente) throws EntidadeNaoEncontradaException {
        clienteService.verificarId(idCliente);
        return contatoRepository.findAll().stream()
                .filter(contato -> contato.getCliente().getIdCliente().equals(idCliente))
                .map(this::returnDtoWithId)
                .collect(Collectors.toList());
    }

    public ContatoDTO update(Integer idContato, ContatoCreateDTO contatoDto) throws EntidadeNaoEncontradaException {
        ContatoEntity contatoRecuperado = getContatoEntityById(idContato);
        contatoRecuperado.setTelefone(contatoDto.getTelefone());
        contatoRecuperado.setDescricao(contatoDto.getDescricao());
        return returnDtoWithId(contatoRepository.save(contatoRecuperado));
    }

    public void delete(Integer idContato) throws EntidadeNaoEncontradaException {
        ContatoEntity contatoRecuperado = getContatoEntityById(idContato);
        contatoRepository.delete(contatoRecuperado);
    }



    private void verificarContatoDoUserLogado(Integer idContato) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        UsuarioEntity loggedUser = usuarioService.findById(usuarioService.getIdLoggedUser());
        List<Integer> idContatos = loggedUser.getCliente().getContatos().stream()
                .map(ContatoEntity::getIdContato).toList();
        if(!idContatos.contains(idContato)){
            throw new RegraDeNegocioException("Este contato não é seu!");
        }
    }

    public ContatoEntity getContatoEntityById(Integer idContato) throws EntidadeNaoEncontradaException {
        return contatoRepository.findById(idContato).stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    private ContatoEntity returnEntity(ContatoCreateDTO dto) {
        return objectMapper.convertValue(dto, ContatoEntity.class);
    }
    private ContatoDTO returnDTO(ContatoEntity entity){
        return objectMapper.convertValue(entity, ContatoDTO.class);
    }

    private ContatoDTO returnDtoWithId(ContatoEntity entity) {
        ContatoDTO dto = returnDTO(entity);
        dto.setIdCliente(entity.getCliente().getIdCliente());
        return dto;
    }
}
