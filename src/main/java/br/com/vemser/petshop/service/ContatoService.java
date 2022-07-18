package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.ContatoCreateDTO;
import br.com.vemser.petshop.dto.ContatoDTO;
import br.com.vemser.petshop.entity.Contato;
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

    public List<ContatoDTO> listarContatoPorId(Integer idCliente) throws SQLException {
        log.info("listando contatos");
        return contatoRepository.listarContatosPorCliente(idCliente).stream()
                .map(this::returnDTO)
                .collect(Collectors.toList());

    }

    public ContatoDTO create (Integer idCliente, ContatoCreateDTO contatoDTO) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        log.info("Criando contato");
        clienteService.verificarId(idCliente);
        Contato contato = returnEntity(contatoDTO);
        contato.setIdCliente(idCliente);
        return returnDTO(contatoRepository.adicionar(idCliente, contato));
    }

    public ContatoDTO update(Integer idContato, ContatoCreateDTO contatoAtualizado) throws SQLException, EntidadeNaoEncontradaException {
        log.info("atualizando contato");
        verificarIdContato(idContato);
        Contato contato = returnEntity(contatoAtualizado);
        Contato contatoRecuperado = contatoRepository.returnByIdUtil(idContato);
        contato.setIdCliente(contatoRecuperado.getIdCliente());
        return returnDTO(contatoRepository.atualizar(idContato, contato));
    }

    public void delete(Integer id) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        log.info("chamou deletar");
        verificarIdContato(id);
        contatoRepository.remover(id);
    }

    public void verificarIdContato(Integer id) throws SQLException, EntidadeNaoEncontradaException {
        contatoRepository.listar().stream()
                .filter(contato -> contato.getIdContato().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    private Contato returnEntity(ContatoCreateDTO dto) {
        return objectMapper.convertValue(dto, Contato.class);
    }
    private ContatoDTO returnDTO(Contato entity){
        return objectMapper.convertValue(entity, ContatoDTO.class);
    }
}
