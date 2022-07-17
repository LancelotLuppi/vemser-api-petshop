package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PedidoCreateDTO;
import br.com.vemser.petshop.dto.PedidoDTO;
import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.entity.Pedido;
import br.com.vemser.petshop.entity.Pet;
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
    private ClienteRepository clienteRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public PedidoDTO create(Integer idPet, PedidoCreateDTO pedidoDto) throws SQLException {
        Pedido pedido = returnEntity(pedidoDto);
        Pet petRecuperado = petRepository.returnByIdUtil(idPet);
        Cliente clienteRecuperado = clienteRepository.returnByIdUtil(petRecuperado.getIdCliente());

        pedido.setIdCliente(clienteRecuperado.getIdCliente());
        pedido.setIdPet(idPet);
        clienteRecuperado.setQuantidadeDePedidos(clienteRecuperado.getQuantidadeDePedidos() + 1);
        clienteRepository.update(clienteRecuperado.getIdCliente(), clienteRecuperado);

        return returnDTO(pedidoRepository.adicionar(idPet, pedido));
    }

    public List<PedidoDTO> list(Integer idCliente) throws SQLException {
        return pedidoRepository.listar(idCliente).stream()
                .map(this::returnDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listByPetId(Integer idPet) throws SQLException {
        return pedidoRepository.listarPedidosPorPet(idPet).stream()
                .map(this::returnDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO update(Integer idPedido, PedidoCreateDTO pedidoDto) throws SQLException {
        Pedido pedidoAtualizado = returnEntity(pedidoDto);

        return returnDTO(pedidoRepository.update(idPedido, pedidoAtualizado));
    }

    public void delete(Integer idPedido) throws SQLException {
        Pedido pedidoRecuperado = pedidoRepository.returnByIdUtil(idPedido);
        Cliente clienteRecuperado = clienteRepository.returnByIdUtil(pedidoRecuperado.getIdCliente());
        pedidoRepository.remover(idPedido);
        clienteRecuperado.setQuantidadeDePedidos(clienteRecuperado.getQuantidadeDePedidos() - 1);
    }

    private Pedido returnEntity(PedidoCreateDTO dto) {
        return objectMapper.convertValue(dto, Pedido.class);
    }
    private PedidoDTO returnDTO(Pedido entity) {
        return objectMapper.convertValue(entity, PedidoDTO.class);
    }
}
