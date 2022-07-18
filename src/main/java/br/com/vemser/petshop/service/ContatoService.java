package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.ContatoCreateDTO;
import br.com.vemser.petshop.dto.ContatoDTO;
import br.com.vemser.petshop.entity.Contato;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
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
    ClienteRepository clienteRepository;

    public List<ContatoDTO> listarContatoPorId(Integer idCliente) throws SQLException {
        log.info("listando contatos");
        return contatoRepository.listarContatosPorCliente(idCliente).stream()
                .map(this::returnDTO)
                .collect(Collectors.toList());

    }

    public ContatoDTO create (Integer idCliente, ContatoCreateDTO contatoDTO) throws SQLException, RegraDeNegocioException {
        log.info("Criando contato");
        Contato contato = returnEntity(contatoDTO);
        contato.setIdCliente(idCliente);
        return returnDTO(contatoRepository.adicionar(idCliente, contato));
    }

    public ContatoDTO update(Integer idContato, ContatoCreateDTO contatoAtualizado) throws SQLException {
        log.info("atualizando contato");
        Contato contato = returnEntity(contatoAtualizado);
        Contato contato1 = contatoRepository.returnByIdUtil(idContato);
        contato.setIdCliente(contato1.getIdCliente());
        return returnDTO(contatoRepository.atualizar(idContato, contato));
    }

    public void delete(Integer id) throws SQLException, RegraDeNegocioException {
        log.info("chamou deletar");
        contatoRepository.remover(id);
    }

    private Contato returnEntity(ContatoCreateDTO dto) {
        return objectMapper.convertValue(dto, Contato.class);
    }
    private ContatoDTO returnDTO(Contato entity){
        return objectMapper.convertValue(entity, ContatoDTO.class);
    }
}
