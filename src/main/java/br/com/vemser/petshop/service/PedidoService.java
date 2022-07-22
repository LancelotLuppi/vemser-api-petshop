package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.PedidoCreateDTO;
import br.com.vemser.petshop.dto.PedidoDTO;
import br.com.vemser.petshop.dto.PedidoStatusRelatorioDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.PedidoRepository;
import br.com.vemser.petshop.repository.PetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
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
    private CalculadoraService calculadoraService;
    @Autowired
    private ObjectMapper objectMapper;

    private final static String NOT_FOUND_MESSAGE = "{idPedido} não encontrado";

    public PedidoDTO create(Integer idPet, PedidoCreateDTO pedidoDto) throws EntidadeNaoEncontradaException {
        PetEntity petRecuperado = petService.getPetByIdEntity(idPet);
        PedidoEntity pedidoEntity = returnEntity(pedidoDto);
        ClienteEntity clienteRecuperado = clienteRepository.findById(petRecuperado.getCliente().getIdCliente()).get();

        pedidoEntity.setCliente(clienteRecuperado);
        pedidoEntity.setPet(petRecuperado);
        pedidoEntity.setValor(calculadoraService.calcularValorDoPedido(pedidoEntity, petRecuperado));
        pedidoEntity.setStatus(StatusPedido.ABERTO);
        pedidoEntity.setDataEHora(LocalDate.now());
        PedidoDTO pedidoCriado = returnDTO(pedidoRepository.save(pedidoEntity));

        clienteRecuperado.setQuantidadeDePedidos(clienteRecuperado.getQuantidadeDePedidos() + 1);
        clienteRecuperado.setValorPagamento(clienteRecuperado.getValorPagamento() + pedidoEntity.getValor());
        clienteRepository.save(clienteRecuperado);

        return pedidoCriado;
    }

    public List<PedidoDTO> list(Integer idCliente) throws EntidadeNaoEncontradaException {
        clienteService.verificarId(idCliente);
        return pedidoRepository.findAll().stream()
                .filter(pedido -> pedido.getCliente().getIdCliente().equals(idCliente))
                .map(this::returnDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listByPetId(Integer idPet) throws EntidadeNaoEncontradaException {
        petService.verificarIdPet(idPet);
        return pedidoRepository.findAll().stream()
                .filter(pedido -> pedido.getPet().getIdPet().equals(idPet))
                .map(this::returnDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO update(Integer idPedido, PedidoCreateDTO pedidoDto) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoRecuperado = returnByIdPedidoEntity(idPedido);
        verificarStatusPedido(pedidoRecuperado);

        Double valorAnterior = pedidoRecuperado.getValor();

        pedidoRecuperado.setServico(pedidoDto.getServico());
        pedidoRecuperado.setDescricao(pedidoRecuperado.getDescricao());
        pedidoRecuperado.setValor(0.0);

        PedidoDTO pedidoAtualizado = returnDTO(pedidoRepository.save(pedidoRecuperado));
        pedidoAtualizado.setIdCliente(pedidoRecuperado.getCliente().getIdCliente());
        pedidoAtualizado.setIdPet(pedidoRecuperado.getPet().getIdPet());
        ClienteEntity clienteRecuperado = clienteService.retornarPorIdVerificado(pedidoAtualizado.getIdCliente());
        clienteRecuperado.setValorPagamento(clienteRecuperado.getValorPagamento() - valorAnterior + pedidoRecuperado.getValor());
        clienteRepository.save(clienteRecuperado);

        return pedidoAtualizado;
    }

    public void delete(Integer idPedido) throws EntidadeNaoEncontradaException {
        PedidoEntity pedidoEntity = returnByIdPedidoEntity(idPedido);
        ClienteEntity clienteEntityRecuperado = clienteService.retornarPorIdVerificado(pedidoEntity.getCliente().getIdCliente());
        pedidoRepository.delete(pedidoEntity);
        clienteEntityRecuperado.setQuantidadeDePedidos(clienteEntityRecuperado.getQuantidadeDePedidos() - 1);
    }

    public PageDTO<PedidoStatusRelatorioDTO> gerarRelatorioStatus(StatusPedido status, Integer pagina, Integer registro) {
        PageRequest pageRequest = PageRequest.of(pagina, registro);
        Page<PedidoStatusRelatorioDTO> page = pedidoRepository.listStatusPedido(status, pageRequest);
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, registro, page.getContent());
    }


    public PedidoEntity returnByIdPedidoEntity(Integer id) throws EntidadeNaoEncontradaException {
        return pedidoRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    public void verificarStatusPedido(PedidoEntity pedidoEntity) throws RegraDeNegocioException {
        if (pedidoEntity.getStatus().equals(StatusPedido.CANCELADO)) {
            throw new RegraDeNegocioException("Pedidos CANCELADOS não podem ser atualizados");
        } else if (pedidoEntity.getStatus().equals(StatusPedido.CONCLUIDO)) {
            throw new RegraDeNegocioException("Pedidos CONCLUIDOS não podem ser atualizados");
        }
    }

    private PedidoEntity returnEntity(PedidoCreateDTO dto) {
        return objectMapper.convertValue(dto, PedidoEntity.class);
    }
    private PedidoDTO returnDTO(PedidoEntity entity) {
        return objectMapper.convertValue(entity, PedidoDTO.class);
    }
}
