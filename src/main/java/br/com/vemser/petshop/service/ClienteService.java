package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.ClienteCreateDTO;
import br.com.vemser.petshop.dto.ClienteDTO;
import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.enums.TipoRequisicao;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.ContatoRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.vemser.petshop.service.EmailService.*;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContatoRepository contatoRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;



    public ClienteDTO create(ClienteCreateDTO clienteDto) throws SQLException, RegraDeNegocioException {
        Cliente cliente = returnEntity(clienteDto);
        Cliente clienteTempParaRetorno = clienteRepository.adicionar(cliente);
        emailService.sendEmail(cliente.getNome(), clienteTempParaRetorno.getIdCliente(), cliente.getEmail(), TipoRequisicao.POST);
        return returnDto(clienteTempParaRetorno);
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
        Cliente cliente = returnEntity(clienteDto);
        emailService.sendEmail(cliente.getNome(), id, cliente.getEmail(), TipoRequisicao.POST);
        return returnDto(cliente);
    }

    public void delete(Integer id) throws SQLException, RegraDeNegocioException {
        Cliente clienteRecuperado = clienteRepository.returnByIdUtil(id);
        emailService.sendEmail(clienteRecuperado.getNome(), clienteRecuperado.getIdCliente(), clienteRecuperado.getEmail(), TipoRequisicao.DELETE);
        clienteRepository.remover(id);
    }



    private Cliente returnEntity(ClienteCreateDTO dto) {
        return objectMapper.convertValue(dto, Cliente.class);
    }

    private ClienteDTO returnDto(Cliente entity) {
        return objectMapper.convertValue(entity, ClienteDTO.class);
    }
}
