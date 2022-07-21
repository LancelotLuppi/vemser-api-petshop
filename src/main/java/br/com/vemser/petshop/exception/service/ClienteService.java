package br.com.vemser.petshop.exception.service;

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
        return returnDto(clienteRepository.save(cliente));
    }

    public List<ClienteDTO>  list() throws RegraDeNegocioException {
        try {
            return clienteRepository.listar().stream()
                    .map(this::returnDto)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ClienteDTO getById(Integer id) throws EntidadeNaoEncontradaException {
        try {
            verificarId(id);
            ClienteEntity clienteEntity = clienteRepository.returnByIdUtil(id);
            return returnDto(clienteEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ClienteDTO update(Integer id, ClienteCreateDTO clienteDto) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        try {
            verificarId(id);
            ClienteEntity clienteEntity = returnEntity(clienteDto);
            ClienteEntity clienteEntityRecuperado = clienteRepository.returnByIdUtil(id);
            clienteEntity.setQuantidadeDePedidos(clienteEntityRecuperado.getQuantidadeDePedidos());
            clienteEntity.setIdCliente(clienteEntityRecuperado.getIdCliente());
            clienteRepository.update(id, clienteEntity);
            emailService.sendEmail(clienteEntity.getNome(), id, clienteEntity.getEmail(), TipoRequisicao.POST);
            return returnDto(clienteEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Integer id) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        try {
            verificarId(id);
            pedidoRepository.removerPedidosPorIDCliente(id);
            petRepository.removerPetPorIDCliente(id);
            contatoRepository.removerContatosPorIDCliente(id);
            ClienteEntity clienteEntityRecuperado = clienteRepository.returnByIdUtil(id);
            emailService.sendEmail(clienteEntityRecuperado.getNome(), clienteEntityRecuperado.getIdCliente(), clienteEntityRecuperado.getEmail(), TipoRequisicao.DELETE);
            clienteRepository.remover(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void verificarId(Integer id) throws EntidadeNaoEncontradaException {
        try {
            clienteRepository.listar().stream()
                    .filter(contato -> contato.getIdCliente().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ClienteEntity returnEntity(ClienteCreateDTO dto) {
        return objectMapper.convertValue(dto, ClienteEntity.class);
    }

    private ClienteDTO returnDto(ClienteEntity entity) {
        return objectMapper.convertValue(entity, ClienteDTO.class);
    }
}
