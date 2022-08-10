package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.pedido.PedidoCreateDTO;
import br.com.vemser.petshop.dto.pedido.PedidoDTO;
import br.com.vemser.petshop.dto.pedido.PedidoStatusRelatorioDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.PedidoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final PetService petService;
    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final CalculadoraService calculadoraService;
    private final RegraStatusPedidoService regraStatusPedidoService;
    private final ObjectMapper objectMapper;
    private final LogService logService;
    private final KafkaProducer kafkaProducer;


    private final static String NOT_FOUND_MESSAGE = "{idPedido} não encontrado";

    public PedidoDTO create(Integer idPet, PedidoCreateDTO pedidoDto) throws EntidadeNaoEncontradaException {
        PetEntity petRecuperado = petService.getPetByIdEntity(idPet);
        PedidoEntity pedidoEntity = returnEntity(pedidoDto);
        ClienteEntity clienteRecuperado = clienteRepository.findById(petRecuperado.getCliente().getIdCliente()).get();
        log.info(logService.info("Criando pedido para o cliente " + clienteRecuperado.getIdCliente() + ", pet " + petRecuperado.getIdPet()));

        pedidoEntity.setCliente(clienteRecuperado);
        pedidoEntity.setPet(petRecuperado);
        pedidoEntity.setValor(calculadoraService.calcularValorDoPedido(pedidoEntity, petRecuperado));
        pedidoEntity.setStatus(StatusPedido.ABERTO);
        pedidoEntity.setDataEHora(LocalDateTime.now());
        PedidoDTO pedidoCriado = returnDtoWithId(pedidoRepository.save(pedidoEntity));

        clienteRecuperado.setQuantidadeDePedidos(clienteRecuperado.getQuantidadeDePedidos() + 1);
        clienteRecuperado.setValorPagamento(clienteRecuperado.getValorPagamento() + pedidoEntity.getValor());
        clienteRepository.save(clienteRecuperado);
        log.info(logService.info("Novo pedido criado, Id: " + pedidoCriado.getIdPedido()));

        return pedidoCriado;
    }

    public PedidoDTO createByLoggedUser(Integer idPet, PedidoCreateDTO pedidoDto) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        petService.verificaPetDoUserLogado(idPet);
        return create(idPet, pedidoDto);
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
        PetEntity petRecuperado = petService.getPetByIdEntity(pedidoRecuperado.getPet().getIdPet());

        Double valorAnterior = pedidoRecuperado.getValor();

        pedidoRecuperado.setServico(pedidoDto.getServico());
        pedidoRecuperado.setDescricao(pedidoRecuperado.getDescricao());
        pedidoRecuperado.setValor(calculadoraService.calcularValorDoPedido(pedidoRecuperado, petRecuperado));

        PedidoDTO pedidoAtualizado = returnDTO(pedidoRepository.save(pedidoRecuperado));
        pedidoAtualizado.setIdCliente(pedidoRecuperado.getCliente().getIdCliente());
        pedidoAtualizado.setIdPet(pedidoRecuperado.getPet().getIdPet());
        pedidoAtualizado.setData(pedidoRecuperado.getDataEHora());
        ClienteEntity clienteRecuperado = clienteService.retornarPorIdVerificado(pedidoAtualizado.getIdCliente());
        clienteRecuperado.setValorPagamento(clienteRecuperado.getValorPagamento() - valorAnterior + pedidoRecuperado.getValor());
        clienteRepository.save(clienteRecuperado);

        return pedidoAtualizado;
    }

    public PedidoDTO updateByLoggedUser(Integer idPedido, PedidoCreateDTO pedidoDto) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        verificarPedidoDoUserLogado(idPedido);
        return update(idPedido, pedidoDto);
    }

    public PedidoDTO updateStatus(Integer idPedido, StatusPedido statusPedido) throws EntidadeNaoEncontradaException, RegraDeNegocioException, JsonProcessingException {
        log.info(logService.info("Atualizando status do pedido " + idPedido));
        PedidoEntity pedido = returnByIdPedidoEntity(idPedido);
        regraStatusPedidoService.updateStatus(pedido, statusPedido);
        pedido.setStatus(statusPedido);
        if (pedido.getStatus().equals(StatusPedido.CONCLUIDO)) {
            kafkaProducer.sendPedido(pedido);
        }
        log.info(logService.info("Status do pedido atualizado para " + statusPedido.name()));
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
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    public Page<PedidoEntity> resolverPaginacao(Integer idCliente, Integer idPet, PageRequest pageRequest) {
        if(idCliente != null && idPet == null) {
            return pedidoRepository.listarPedidosPorClientePaginado(idCliente, pageRequest);
        }
        else if(idPet != null && idCliente == null) {
            return pedidoRepository.listarPedidosPorPetPaginado(idPet, pageRequest);
        }
        else if(idPet != null) {
            return pedidoRepository.listarPedidosPorClienteAndPetPaginado(idCliente ,idPet, pageRequest);
        }
        return pedidoRepository.findAll(pageRequest);
    }

    public List<PedidoDTO> getByLoggedUser() throws EntidadeNaoEncontradaException{
        ClienteEntity clienteLogado = clienteService.returnLoggedClient();

        return clienteLogado.getPedidos().stream()
                .map(this::returnDtoWithId).toList();
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

    private void verificarPedidoDoUserLogado(Integer idPedido) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        UsuarioEntity loggedUser = usuarioService.findById(usuarioService.getIdLoggedUser());
        List<Integer> idPedidos = loggedUser.getCliente().getPedidos().stream()
                .map(PedidoEntity::getIdPedido).toList();
        if(!idPedidos.contains(idPedido)){
            throw new RegraDeNegocioException("Este pedido não é seu!");
        }
    }

    public void verificarStatusPedido(PedidoEntity pedidoEntity) throws RegraDeNegocioException {
        if (pedidoEntity.getStatus().equals(StatusPedido.CANCELADO)) {
            throw new RegraDeNegocioException("Pedidos CANCELADOS não podem ser atualizados");
        } else if (pedidoEntity.getStatus().equals(StatusPedido.CONCLUIDO)) {
            throw new RegraDeNegocioException("Pedidos CONCLUIDOS não podem ser atualizados");
        }
    }
}
