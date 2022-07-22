package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.ClienteCreateDTO;
import br.com.vemser.petshop.dto.ClienteDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.enums.TipoRequisicao;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.ContatoRepository;

import br.com.vemser.petshop.repository.PedidoRepository;
import br.com.vemser.petshop.repository.PetRepository;
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
    private ContatoRepository contatoRepository;
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String NOT_FOUND_MESSAGE = "{idCliente} não encontrado";



//    public ClienteDTO create(ClienteCreateDTO clienteDto) throws  RegraDeNegocioException {
//        ClienteEntity clienteEntityTempParaRetorno = null;
//        try {
//            ClienteEntity clienteEntity = returnEntity(clienteDto);
//            clienteEntity.setQuantidadeDePedidos(0);
//            clienteEntityTempParaRetorno = clienteRepository.adicionar(clienteEntity);
//            emailService.sendEmail(clienteEntity.getNome(), clienteEntityTempParaRetorno.getIdCliente(), clienteEntity.getEmail(), TipoRequisicao.POST);
//            return returnDto(clienteEntityTempParaRetorno);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public ClienteDTO create(ClienteCreateDTO clienteDto) {
        ClienteEntity cliente = returnEntity(clienteDto);
        cliente.setQuantidadeDePedidos(0);
        cliente.setValorPagamento(0);
        return returnDto(clienteRepository.save(cliente));
    }

    public List<ClienteDTO> list() {
        return clienteRepository.findAll().stream()
                .map(this::returnDto)
                .toList();
    }

    public ClienteDTO getById(Integer id) throws EntidadeNaoEncontradaException {
        ClienteEntity cliente = clienteRepository.findById(id).get();
        return returnDto(cliente);
    }

    public ClienteDTO update(Integer id, ClienteCreateDTO clienteDto) throws EntidadeNaoEncontradaException {
        ClienteEntity clienteRecuperado = retornarPorIdVerificado(id);
        clienteRecuperado.setNome(clienteDto.getNome());
        clienteRecuperado.setEmail(clienteDto.getEmail());
        return returnDto(clienteRepository.save(clienteRecuperado));
    }

    public void delete(Integer id) throws EntidadeNaoEncontradaException {
        ClienteEntity clienteRecuperado = retornarPorIdVerificado(id);
        clienteRepository.delete(clienteRecuperado);
    }

    public ClienteEntity retornarPorIdVerificado(Integer id) throws EntidadeNaoEncontradaException {
        return clienteRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException("{idPessoa} não encontrado"));
    }

    public void verificarId(Integer id) throws EntidadeNaoEncontradaException {
        clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("{idPessoa} não encontrado"));
    }

    private ClienteEntity returnEntity(ClienteCreateDTO dto) {
        return objectMapper.convertValue(dto, ClienteEntity.class);
    }

    private ClienteDTO returnDto(ClienteEntity entity) {
        return objectMapper.convertValue(entity, ClienteDTO.class);
    }
}
