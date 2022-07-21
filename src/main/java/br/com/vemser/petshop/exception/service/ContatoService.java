package br.com.vemser.petshop.exception.service;

import br.com.vemser.petshop.dto.ContatoCreateDTO;
import br.com.vemser.petshop.dto.ContatoDTO;
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
    ClienteService clienteService;

    private final static String NOT_FOUND_MESSAGE = "{idContato} não encontrado";

    public List<ContatoDTO> listarContatoPorId(Integer idCliente) throws EntidadeNaoEncontradaException {
        try {
            clienteService.verificarId(idCliente);
            log.info("listando contatos");
            return contatoRepository.listarContatosPorCliente(idCliente).stream()
                    .map(this::returnDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ContatoDTO create (Integer idCliente, ContatoCreateDTO contatoDTO) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        try {
            log.info("Criando contato");
            clienteService.verificarId(idCliente);
            ContatoEntity contatoEntity = returnEntity(contatoDTO);
            contatoEntity.setIdCliente(idCliente);
            return returnDTO(contatoRepository.adicionar(idCliente, contatoEntity));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ContatoDTO update(Integer idContato, ContatoCreateDTO contatoAtualizado) throws EntidadeNaoEncontradaException {
        try {
            log.info("atualizando contato");
            verificarIdContato(idContato);
            ContatoEntity contatoEntity = returnEntity(contatoAtualizado);
            ContatoEntity contatoEntityRecuperado = contatoRepository.returnByIdUtil(idContato);
            contatoEntity.setIdCliente(contatoEntityRecuperado.getIdCliente());
            return returnDTO(contatoRepository.atualizar(idContato, contatoEntity));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Integer id) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        try {
            log.info("chamou deletar");
            verificarIdContato(id);
            contatoRepository.remover(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void verificarIdContato(Integer id) throws EntidadeNaoEncontradaException {
        try {
            contatoRepository.listar().stream()
                    .filter(contatoEntity -> contatoEntity.getIdContato().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ContatoEntity returnEntity(ContatoCreateDTO dto) {
        return objectMapper.convertValue(dto, ContatoEntity.class);
    }
    private ContatoDTO returnDTO(ContatoEntity entity){
        return objectMapper.convertValue(entity, ContatoDTO.class);
    }
}
