package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.pedido.PedidoCreateDTO;
import br.com.vemser.petshop.dto.pedido.PedidoDTO;
import br.com.vemser.petshop.dto.pedido.PedidoStatusRelatorioDTO;
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

import java.time.LocalDateTime;
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
    private RegraStatusPedidoService regraStatusPedidoService;
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
        pedidoEntity.setDataEHora(LocalDateTime.now());
        PedidoDTO pedidoCriado = returnDtoWithId(pedidoRepository.save(pedidoEntity));

        clienteRecuperado.setQuantidadeDePedidos(clienteRecuperado.getQuantidadeDePedidos() + 1);
        clienteRecuperado.setValorPagamento(clienteRecuperado.getValorPagamento() + pedidoEntity.getValor());
        clienteRepository.save(clienteRecuperado);

        return pedidoCriado;
    }

    public List<PedidoDTO> list(Integer idCliente) throws EntidadeNaoEncontradaException {
        clienteService.verificarId(idCliente);
        return pedidoRepository.findAll().stream()
                .filter(pedido -> pedido.getCliente().getIdCliente().equals(idCliente))
                .map(this::returnDtoWithId)
                .collect(Collectors.toList());
    }

    public List<PedidoDTO> listByPetId(Integer idPet) throws EntidadeNaoEncontradaException {
        petService.verificarIdPet(idPet);
        return pedidoRepository.findAll().stream()
                .filter(pedido -> pedido.getPet().getIdPet().equals(idPet))
                .map(this::returnDtoWithId)
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

    public PedidoDTO updateStatus(Integer idPedido, StatusPedido statusPedido) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedido = returnByIdPedidoEntity(idPedido);
        regraStatusPedidoService.updateStatus(pedido, statusPedido);
        pedido.setStatus(statusPedido);
        return returnDtoWithId(pedidoRepository.save(pedido));
    }

    public void delete(Integer idPedido) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = returnByIdPedidoEntity(idPedido);
        if(pedidoEntity.getStatus().equals(StatusPedido.ABERTO) || pedidoEntity.getStatus().equals(StatusPedido.EM_ANDAMENTO)) {
            throw new RegraDeNegocioException("Pedidos com status ABERTO e EM_ANDAMENTO não podem ser deletados!");
        }
        pedidoRepository.delete(pedidoEntity);
    }

    public PageDTO<PedidoStatusRelatorioDTO> gerarRelatorioStatus(StatusPedido status, Integer pagina, Integer registro) {
        PageRequest pageRequest = PageRequest.of(pagina, registro);
        Page<PedidoStatusRelatorioDTO> page = pedidoRepository.listStatusPedido(status, pageRequest);
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, registro, page.getContent());
    }

    public PageDTO<PedidoDTO> listarPedidosPaginado(Integer idCliente, Integer idPet, Integer pagina, Integer registro) {
        PageRequest pageRequest = PageRequest.of(pagina, registro);

        Page<PedidoEntity> page = resolverPaginacao(idCliente, idPet, pageRequest);

        List<PedidoDTO> pedidosDTO = page.getContent().stream()
                .map(this::returnDtoWithId)
                .collect(Collectors.toList());
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, registro, pedidosDTO);
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

    public Page<PedidoEntity> resolverPaginacao(Integer idCliente, Integer idPet, PageRequest pageRequest) {
        if(idCliente != null && idPet == null) {
            return pedidoRepository.listarPedidosPorClientePaginado(idCliente, pageRequest);
        }
        else if(idPet != null && idCliente == null) {
            return pedidoRepository.listarPedidosPorPetPaginado(idPet, pageRequest);
        }
        else if(idPet != null && idCliente != null) {
            return pedidoRepository.listarPedidosPorClienteAndPetPaginado(idCliente ,idPet, pageRequest);
        }
        return pedidoRepository.findAll(pageRequest);
    }

    private PedidoEntity returnEntity(PedidoCreateDTO dto) {
        return objectMapper.convertValue(dto, PedidoEntity.class);
    }

    private PedidoDTO returnDtoWithId(PedidoEntity entity) {
        PedidoDTO dto = returnDTO(entity);
        dto.setIdCliente(entity.getCliente().getIdCliente());
        dto.setIdPet(entity.getPet().getIdPet());
        dto.setData(entity.getDataEHora());
        return dto;
    }

    private PedidoDTO returnDTO(PedidoEntity entity) {
        return objectMapper.convertValue(entity, PedidoDTO.class);
    }
}
