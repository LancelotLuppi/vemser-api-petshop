package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PedidoCreateDTO;
import br.com.vemser.petshop.dto.PedidoDTO;
import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.entity.Pedido;
import br.com.vemser.petshop.entity.Pet;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.PedidoRepository;
import br.com.vemser.petshop.repository.PetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private PetService petService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ObjectMapper objectMapper;

    private final static String NOT_FOUND_MESSAGE = "{idPedido} não encontrado";

    public PedidoDTO create(Integer idPet, PedidoCreateDTO pedidoDto) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        petService.verificarIdPet(idPet);
        Pedido pedido = returnEntity(pedidoDto);
        Pet petRecuperado = petRepository.returnByIdUtil(idPet);
        Cliente clienteRecuperado = clienteRepository.returnByIdUtil(petRecuperado.getIdCliente());

        pedido.setIdCliente(clienteRecuperado.getIdCliente());
        pedido.setIdPet(idPet);
        clienteRecuperado.setQuantidadeDePedidos(clienteRecuperado.getQuantidadeDePedidos() + 1);
        clienteRepository.update(clienteRecuperado.getIdCliente(), clienteRecuperado);

        return returnDTO(pedidoRepository.adicionar(idPet, pedido));
    }

    public List<PedidoDTO> list(Integer idCliente) throws SQLException, EntidadeNaoEncontradaException {
        clienteService.verificarId(idCliente);
        return pedidoRepository.listarPorIdCliente(idCliente).stream()
                .map(this::returnDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listByPetId(Integer idPet) throws SQLException, EntidadeNaoEncontradaException {
        petService.verificarIdPet(idPet);
        return pedidoRepository.listarPorIdPet(idPet).stream()
                .map(this::returnDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO update(Integer idPedido, PedidoCreateDTO pedidoDto) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        verificarIdPedido(idPedido);
        Pedido pedidoAtualizado = returnEntity(pedidoDto);
        return returnDTO(pedidoRepository.update(idPedido, pedidoAtualizado));
    }

    public void delete(Integer idPedido) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        verificarIdPedido(idPedido);
        Pedido pedidoRecuperado = pedidoRepository.returnByIdUtil(idPedido);
        Cliente clienteRecuperado = clienteRepository.returnByIdUtil(pedidoRecuperado.getIdCliente());
        pedidoRepository.remover(idPedido);
        clienteRecuperado.setQuantidadeDePedidos(clienteRecuperado.getQuantidadeDePedidos() - 1);
    }

    public void verificarIdPedido(Integer id) throws SQLException, EntidadeNaoEncontradaException {
        pedidoRepository.listar().stream()
                .filter(pedido -> pedido.getIdPedido().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    private Pedido returnEntity(PedidoCreateDTO dto) {
        return objectMapper.convertValue(dto, Pedido.class);
    }
    private PedidoDTO returnDTO(Pedido entity) {
        return objectMapper.convertValue(entity, PedidoDTO.class);
    }
}
