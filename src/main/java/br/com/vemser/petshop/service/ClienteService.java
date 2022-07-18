package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.ClienteCreateDTO;
import br.com.vemser.petshop.dto.ClienteDTO;
import br.com.vemser.petshop.entity.Cliente;
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



    public ClienteDTO create(ClienteCreateDTO clienteDto) throws  RegraDeNegocioException {
        Cliente clienteTempParaRetorno = null;
        try {
            Cliente cliente = returnEntity(clienteDto);
            cliente.setQuantidadeDePedidos(0);
            clienteTempParaRetorno = clienteRepository.adicionar(cliente);
            emailService.sendEmail(cliente.getNome(), clienteTempParaRetorno.getIdCliente(), cliente.getEmail(), TipoRequisicao.POST);
            return returnDto(clienteTempParaRetorno);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            Cliente cliente = clienteRepository.returnByIdUtil(id);
            return returnDto(cliente);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ClienteDTO update(Integer id, ClienteCreateDTO clienteDto) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        try {
            verificarId(id);
            Cliente cliente = returnEntity(clienteDto);
            Cliente clienteRecuperado = clienteRepository.returnByIdUtil(id);
            cliente.setQuantidadeDePedidos(clienteRecuperado.getQuantidadeDePedidos());
            cliente.setIdCliente(clienteRecuperado.getIdCliente());
            clienteRepository.update(id, cliente);
            emailService.sendEmail(cliente.getNome(), id, cliente.getEmail(), TipoRequisicao.POST);
            return returnDto(cliente);
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
            Cliente clienteRecuperado = clienteRepository.returnByIdUtil(id);
            emailService.sendEmail(clienteRecuperado.getNome(), clienteRecuperado.getIdCliente(), clienteRecuperado.getEmail(), TipoRequisicao.DELETE);
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

    private Cliente returnEntity(ClienteCreateDTO dto) {
        return objectMapper.convertValue(dto, Cliente.class);
    }

    private ClienteDTO returnDto(Cliente entity) {
        return objectMapper.convertValue(entity, ClienteDTO.class);
    }
}
