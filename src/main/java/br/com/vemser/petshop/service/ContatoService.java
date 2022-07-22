package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.ContatoCreateDTO;
import br.com.vemser.petshop.dto.ContatoDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.ContatoEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ContatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContatoService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ContatoRepository contatoRepository;
    @Autowired
    private ClienteService clienteService;

    private final static String NOT_FOUND_MESSAGE = "{idContato} não encontrado";



    public ContatoDTO create(Integer idCliente, ContatoCreateDTO contatoDTO) throws EntidadeNaoEncontradaException {
        ClienteEntity clienteRecuperado = clienteService.retornarPorIdVerificado(idCliente);
        ContatoEntity contato = returnEntity(contatoDTO);

        contato.setCliente(clienteRecuperado);
        ContatoDTO contatoSalvo = returnDTO(contatoRepository.save(contato));
        contatoSalvo.setIdCliente(idCliente);

        return contatoSalvo;
    }

    public List<ContatoDTO> listByIdCliente(Integer idCliente) throws EntidadeNaoEncontradaException {
        clienteService.verificarId(idCliente);
        return contatoRepository.findAll().stream()
                .filter(contato -> contato.getCliente().getIdCliente().equals(idCliente))
                .map(this::returnDTO)
                .collect(Collectors.toList());
    }

    public ContatoDTO update(Integer idContato, ContatoCreateDTO contatoDto) throws EntidadeNaoEncontradaException {
        ContatoEntity contatoRecuperado = getContatoEntityById(idContato);
        contatoRecuperado.setTelefone(contatoDto.getTelefone());
        contatoRecuperado.setDescricao(contatoDto.getDescricao());
        ContatoDTO contatoAtualizado = returnDTO(contatoRepository.save(contatoRecuperado));
        contatoAtualizado.setIdCliente(contatoRecuperado.getCliente().getIdCliente());
        return contatoAtualizado;
    }

    public void delete(Integer idContato) throws EntidadeNaoEncontradaException {
        ContatoEntity contatoRecuperado = getContatoEntityById(idContato);
        contatoRepository.delete(contatoRecuperado);
    }

    public void verificarIdContato(Integer idContato) throws EntidadeNaoEncontradaException {
        contatoRepository.findById(idContato).stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
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
}
