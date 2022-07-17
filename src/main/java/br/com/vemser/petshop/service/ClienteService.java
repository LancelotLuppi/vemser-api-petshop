package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.ClienteCreateDTO;
import br.com.vemser.petshop.dto.ClienteDTO;
import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ObjectMapper objectMapper;



    public ClienteDTO create(ClienteCreateDTO clienteDto) throws SQLException, RegraDeNegocioException {
        Cliente cliente = returnEntity(clienteDto);
        return returnDto(clienteRepository.adicionar(cliente));
    }

    public List<ClienteDTO>  list() throws SQLException, RegraDeNegocioException {
        return clienteRepository.listar().stream()
                .map(this::returnDto)
                .collect(Collectors.toList());
    }

    public ClienteDTO getById(Integer id) throws SQLException, RegraDeNegocioException {
        return returnDto(clienteRepository.getById(id));
    }

    public ClienteDTO update(Integer id, ClienteCreateDTO clienteDto) throws SQLException, RegraDeNegocioException {
        Cliente clienteAtualizado = returnEntity(clienteDto);
        return returnDto(clienteRepository.update(id, clienteAtualizado));
    }

    public void delete(Integer id) throws SQLException, RegraDeNegocioException {
        clienteRepository.remover(id);
    }



    private Cliente returnEntity(ClienteCreateDTO dto) {
        return objectMapper.convertValue(dto, Cliente.class);
    }

    private ClienteDTO returnDto(Cliente entity) {
        return objectMapper.convertValue(entity, ClienteDTO.class);
    }
}
