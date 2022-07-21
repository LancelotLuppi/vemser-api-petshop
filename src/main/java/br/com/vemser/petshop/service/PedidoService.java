package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PedidoCreateDTO;
import br.com.vemser.petshop.dto.PedidoDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.entity.PetEntity;
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
        PedidoEntity pedidoEntity = returnEntity(pedidoDto);
        PetEntity petEntityRecuperado = petRepository.returnByIdUtil(idPet);
        ClienteEntity clienteEntityRecuperado = clienteRepository.returnByIdUtil(petEntityRecuperado.getIdCliente());

        pedidoEntity.setIdCliente(clienteEntityRecuperado.getIdCliente());
        pedidoEntity.setIdPet(idPet);
        clienteEntityRecuperado.setQuantidadeDePedidos(clienteEntityRecuperado.getQuantidadeDePedidos() + 1);
        clienteRepository.update(clienteEntityRecuperado.getIdCliente(), clienteEntityRecuperado);

        return returnDTO(pedidoRepository.adicionar(idPet, pedidoEntity));
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
        PedidoEntity pedidoEntityAtualizado = returnEntity(pedidoDto);
        return returnDTO(pedidoRepository.update(idPedido, pedidoEntityAtualizado));
    }

    public void delete(Integer idPedido) throws SQLException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        verificarIdPedido(idPedido);
        PedidoEntity pedidoEntityRecuperado = pedidoRepository.returnByIdUtil(idPedido);
        ClienteEntity clienteEntityRecuperado = clienteRepository.returnByIdUtil(pedidoEntityRecuperado.getIdCliente());
        pedidoRepository.remover(idPedido);
        clienteEntityRecuperado.setQuantidadeDePedidos(clienteEntityRecuperado.getQuantidadeDePedidos() - 1);
    }

    public void verificarIdPedido(Integer id) throws SQLException, EntidadeNaoEncontradaException {
        pedidoRepository.listar().stream()
                .filter(pedidoEntity -> pedidoEntity.getIdPedido().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    private PedidoEntity returnEntity(PedidoCreateDTO dto) {
        return objectMapper.convertValue(dto, PedidoEntity.class);
    }
    private PedidoDTO returnDTO(PedidoEntity entity) {
        return objectMapper.convertValue(entity, PedidoDTO.class);
    }
}
