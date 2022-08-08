package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.pedido.PedidoCreateDTO;
import br.com.vemser.petshop.dto.pedido.PedidoDTO;
import br.com.vemser.petshop.dto.pedido.PedidoStatusRelatorioDTO;
import br.com.vemser.petshop.entity.*;
import br.com.vemser.petshop.enums.*;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.PedidoRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private CalculadoraService calculadoraService;
    @Mock
    private PetService petService;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ClienteService clienteService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private RegraStatusPedidoService regraStatusPedidoService;
    @Mock
    private BalancoMensalService balancoMensalService;
    @Mock
    private PedidosMensalService pedidosMensalService;
    @Mock
    private LogService logService;

    private static final Integer ID_PEDIDO = 12;
    private static final Integer ID_PET = 42;
    private static final Integer ID_CLIENTE = 10;


    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(pedidoService, "objectMapper", objectMapper);
    }


    @Test
    public void deveTestarCreateByLoggedUserComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoCreateDTO pedidoCreateDTO = getPedidoCreateDTO();
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoEntity(clienteEntity, petEntity);

        doNothing().when(petService).verificaPetDoUserLogado(anyInt());
        when(petService.getPetByIdEntity(anyInt())).thenReturn(petEntity);
        when(clienteRepository.findById(anyInt())).thenReturn(Optional.of(clienteEntity));
        when(calculadoraService.calcularValorDoPedido(any(PedidoEntity.class), any(PetEntity.class))).thenReturn(90.0);
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedidoEntity);

        // act
        PedidoDTO pedidoDTO = pedidoService.createByLoggedUser(42, pedidoCreateDTO);

        // asserts
        assertNotNull(pedidoDTO);
        assertEquals(12, pedidoDTO.getIdPedido().intValue());
        assertEquals(10, pedidoDTO.getIdCliente().intValue());
        assertEquals(42, pedidoDTO.getIdPet().intValue());
        assertEquals(90, pedidoDTO.getValor().intValue());
        assertEquals(StatusPedido.ABERTO, pedidoDTO.getStatus());
        assertEquals(LocalDateTime.of(LocalDate.of(2022, 8, 2), LocalTime.of(21, 20)), pedidoDTO.getData());
        assertEquals(TipoServico.BANHO, pedidoDTO.getServico());
        assertEquals("SD", pedidoDTO.getDescricao());
    }

    @Test(expected = EntidadeNaoEncontradaException.class)
    public void deveTestarCreateSemId() throws EntidadeNaoEncontradaException {
        PedidoCreateDTO pedidoCreateDTO = getPedidoCreateDTO();
        doThrow(new EntidadeNaoEncontradaException("Pet não encontrado"))
                .when(petService).getPetByIdEntity(anyInt());
        int idInvalido = 13;


        pedidoService.create(idInvalido, pedidoCreateDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCreateByLoggedUserComPetDeOutroUsuario() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoCreateDTO pedidoCreateDTO = getPedidoCreateDTO();

        doThrow(new RegraDeNegocioException("Esse pet não é seu!"))
                .when(petService).verificaPetDoUserLogado(anyInt());

        pedidoService.createByLoggedUser(4, pedidoCreateDTO);
    }

    @Test
    public void deveTestarListComSucesso() throws EntidadeNaoEncontradaException {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        List<PedidoEntity> pedidoEntityList = List.of(getPedidoEntity(clienteEntity, petEntity));

        doNothing().when(clienteService).verificarId(anyInt());
        when(pedidoRepository.findAll()).thenReturn(pedidoEntityList);

        List<PedidoDTO> pedidoDTOList = pedidoService.list(ID_CLIENTE);

        assertNotNull(pedidoDTOList);
        assertFalse(pedidoDTOList.isEmpty());
    }

    @Test(expected = EntidadeNaoEncontradaException.class)
    public void deveTestarListSemIdDoCliente() throws EntidadeNaoEncontradaException {

        doThrow(new EntidadeNaoEncontradaException("Cliente não encontrado!")).when(clienteService).verificarId(anyInt());

        Integer idCliente = 10;
        pedidoService.list(idCliente);
    }

    @Test
    public void deveTestarListByPetIdComSucesso() throws EntidadeNaoEncontradaException {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        List<PedidoEntity> pedidoEntityList = List.of(getPedidoEntity(clienteEntity, petEntity));

        doNothing().when(petService).verificarIdPet(anyInt());
        when(pedidoRepository.findAll()).thenReturn(pedidoEntityList);

        List<PedidoDTO> pedidoDTOList = pedidoService.listByPetId(ID_PET);

        assertNotNull(pedidoDTOList);
        assertFalse(pedidoDTOList.isEmpty());
    }

    @Test(expected = EntidadeNaoEncontradaException.class)
    public void deveTestarListByPetIdSemIdDoPet() throws EntidadeNaoEncontradaException {
        doThrow(new EntidadeNaoEncontradaException("Cliente não encontrado!")).when(petService).verificarIdPet(anyInt());
        int idInvalido = 8;

        pedidoService.listByPetId(idInvalido);
    }

    @Test
    public void deveTestarUpdateByLoggedUserComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        PetEntity petEntity = getPetEntity(usuarioEntity.getCliente());

        PedidoCreateDTO pedidoCreateDTO = getPedidoCreateDTO();
        PedidoEntity pedidoEntity = getPedidoEntity(usuarioEntity.getCliente(), petEntity);

        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);
        when(petService.getPetByIdEntity(anyInt())).thenReturn(petEntity);
        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedidoEntity));
        when(calculadoraService.calcularValorDoPedido(any(PedidoEntity.class), any(PetEntity.class))).thenReturn(90.0);
        when(pedidoRepository.save(any())).thenReturn(pedidoEntity);
        when(clienteService.retornarPorIdVerificado(anyInt())).thenReturn(usuarioEntity.getCliente());
        when(clienteRepository.save(any())).thenReturn(usuarioEntity.getCliente());

        PedidoDTO pedidoDTO = pedidoService.updateByLoggedUser(ID_PEDIDO, pedidoCreateDTO);

        assertNotNull(pedidoDTO);
        assertEquals(12, pedidoDTO.getIdPedido().intValue());
        assertEquals(10, pedidoDTO.getIdCliente().intValue());
        assertEquals(42, pedidoDTO.getIdPet().intValue());
        assertEquals(90, pedidoDTO.getValor().intValue());
        assertEquals(StatusPedido.ABERTO, pedidoDTO.getStatus());
        assertEquals(LocalDateTime.of(LocalDate.of(2022, 8, 2), LocalTime.of(21, 20)), pedidoDTO.getData());
        assertEquals(TipoServico.BANHO, pedidoDTO.getServico());
        assertEquals("SD", pedidoDTO.getDescricao());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateByLoggedUserComPedidoDeOutroUsuario() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        PedidoCreateDTO pedidoCreateDTO = new PedidoCreateDTO();
        int idPedidoInvalido = 15;

        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);

        pedidoService.updateByLoggedUser(idPedidoInvalido, pedidoCreateDTO);
    }

    @Test(expected = EntidadeNaoEncontradaException.class)
    public void deveTestarUpdateSemIdDoPedido() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoCreateDTO pedidoCreateDTO = getPedidoCreateDTO();

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.empty());

        pedidoService.update(3, pedidoCreateDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateComStatusConcluido() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoCreateDTO pedidoCreateDTO = getPedidoCreateDTO();
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoEntity(clienteEntity, petEntity);
        pedidoEntity.setStatus(StatusPedido.CONCLUIDO);

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedidoEntity));

        pedidoService.update(ID_PEDIDO, pedidoCreateDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateComStatusCancelado() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoCreateDTO pedidoCreateDTO = getPedidoCreateDTO();
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoEntity(clienteEntity, petEntity);
        pedidoEntity.setStatus(StatusPedido.CANCELADO);

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedidoEntity));

        pedidoService.update(ID_PEDIDO, pedidoCreateDTO);
    }

    @Test
    public void deveTestarUpdateStatusComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoEntity(clienteEntity, petEntity);
        StatusPedido statusPedido = StatusPedido.CONCLUIDO;

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedidoEntity));
        doNothing().when(regraStatusPedidoService).updateStatus(any(), any());
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedidoEntity);
        doNothing().when(balancoMensalService).atualizarBalanco(any());
        doNothing().when(pedidosMensalService).atualizarPedidos(any());

        PedidoDTO pedidoDTO = pedidoService.updateStatus(12, statusPedido);

        assertNotNull(pedidoDTO);
        assertEquals(12, pedidoDTO.getIdPedido().intValue());
        assertEquals(10, pedidoDTO.getIdCliente().intValue());
        assertEquals(42, pedidoDTO.getIdPet().intValue());
        assertEquals(90, pedidoDTO.getValor().intValue());
        assertEquals(StatusPedido.CONCLUIDO, pedidoDTO.getStatus());
        assertEquals(LocalDateTime.of(LocalDate.of(2022, 8, 2), LocalTime.of(21, 20)), pedidoDTO.getData());
        assertEquals(TipoServico.BANHO, pedidoDTO.getServico());
        assertEquals("SD", pedidoDTO.getDescricao());
    }

    @Test
    public void deveTestarUpdateStatusComSucessoDiferenteDeConcluido() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoEntity(clienteEntity, petEntity);
        StatusPedido statusPedido = StatusPedido.EM_ANDAMENTO;

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedidoEntity));
        doNothing().when(regraStatusPedidoService).updateStatus(any(), any());
        when(pedidoRepository.save(any(PedidoEntity.class))).thenReturn(pedidoEntity);
        when(logService.info(anyString())).thenReturn(anyString());

        PedidoDTO pedidoDTO = pedidoService.updateStatus(12, statusPedido);

        assertNotNull(pedidoDTO);
        assertEquals(12, pedidoDTO.getIdPedido().intValue());
        assertEquals(10, pedidoDTO.getIdCliente().intValue());
        assertEquals(42, pedidoDTO.getIdPet().intValue());
        assertEquals(90, pedidoDTO.getValor().intValue());
        assertEquals(StatusPedido.EM_ANDAMENTO, pedidoDTO.getStatus());
        assertEquals(LocalDateTime.of(LocalDate.of(2022, 8, 2), LocalTime.of(21, 20)), pedidoDTO.getData());
        assertEquals(TipoServico.BANHO, pedidoDTO.getServico());
        assertEquals("SD", pedidoDTO.getDescricao());
    }

    @Test(expected = EntidadeNaoEncontradaException.class)
    public void deveTestarUpdateStatusSemId() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        StatusPedido statusPedido = StatusPedido.EM_ANDAMENTO;

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.empty());

        pedidoService.updateStatus(ID_PEDIDO, statusPedido);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateStatusInvalido() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoEntity(clienteEntity, petEntity);
        StatusPedido statusPedido = StatusPedido.CONCLUIDO;

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedidoEntity));
        doThrow(new RegraDeNegocioException("Pedido em ABERTO não pode ser CONCLUIDO"))
                .when(regraStatusPedidoService).updateStatus(any(), any());

        pedidoService.updateStatus(ID_PEDIDO, statusPedido);
    }

    @Test
    public void deveTestarDeleteComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoEntity(clienteEntity, petEntity);
        pedidoEntity.setStatus(StatusPedido.CONCLUIDO);

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedidoEntity));
        doNothing().when(pedidoRepository).delete(any(PedidoEntity.class));

        pedidoService.delete(ID_PEDIDO);

        verify(pedidoRepository, times(1)).delete(any(PedidoEntity.class));
    }

    @Test(expected = EntidadeNaoEncontradaException.class)
    public void deveTestarDeleteSemId() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.empty());

        pedidoService.delete(ID_PEDIDO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarDeleteInvalido() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoEntity(clienteEntity, petEntity);

        when(pedidoRepository.findById(anyInt())).thenReturn(Optional.of(pedidoEntity));

        pedidoService.delete(3);
    }

    @Test
    public void deveTestarGerarRelatorioStatusComSucesso() {
        PedidoStatusRelatorioDTO pedidoStatusRelatorioDTO = new PedidoStatusRelatorioDTO();
        pedidoStatusRelatorioDTO.setIdCliente(ID_CLIENTE);
        pedidoStatusRelatorioDTO.setNomeCliente("Anakin Skywalker");
        pedidoStatusRelatorioDTO.setEmail("ImNotDarthVader@sith.com.br");
        pedidoStatusRelatorioDTO.setNomePet("Leona");
        pedidoStatusRelatorioDTO.setTipoPet(TipoPet.CACHORRO);
        pedidoStatusRelatorioDTO.setStatus(StatusPedido.ABERTO);
        pedidoStatusRelatorioDTO.setServico(TipoServico.BANHO);
        pedidoStatusRelatorioDTO.setValor(90.0);
        StatusPedido status = StatusPedido.ABERTO;

        List<PedidoStatusRelatorioDTO> pedidoStatusRelatorioDTOList = List.of(pedidoStatusRelatorioDTO);
        Page<PedidoStatusRelatorioDTO> pageRelatorioPedidos = new PageImpl<>(pedidoStatusRelatorioDTOList);

        when(pedidoRepository.listStatusPedido(any(StatusPedido.class), any())).thenReturn(pageRelatorioPedidos);

        pedidoService.gerarRelatorioStatus(status, 0, 3);

        assertNotNull(pageRelatorioPedidos);
        assertEquals(1, pageRelatorioPedidos.getTotalElements());
        assertEquals(1, pageRelatorioPedidos.getContent().size());
    }

    @Test
    public void deveTestarListarPedidosPaginadoComIdClienteAndIdPet() {
        Page<PedidoEntity> paginaPedidos = getPagePedidos();

        when(pedidoRepository.listarPedidosPorClienteAndPetPaginado(
                anyInt(),
                anyInt(),
                any(PageRequest.class)))
                .thenReturn(paginaPedidos);

        PageDTO<PedidoDTO> paginacaoDePedidos = pedidoService.listarPedidosPaginado(ID_CLIENTE, ID_PET, 0, 5);

        assertNotNull(paginacaoDePedidos);
        assertEquals(1, paginacaoDePedidos.getTotalElements().intValue());
        assertEquals(1, paginacaoDePedidos.getContent().size());
    }



    @Test
    public void deveTestarListarPedidosPaginadoComIdCliente() {
        Page<PedidoEntity> paginaPedidos = getPagePedidos();

        when(pedidoRepository.listarPedidosPorClientePaginado(anyInt(), any(PageRequest.class))).thenReturn(paginaPedidos);

        PageDTO<PedidoDTO> paginacaoDePedidos = pedidoService.listarPedidosPaginado(ID_CLIENTE, null, 0, 5);

        assertNotNull(paginacaoDePedidos);
        assertEquals(1, paginacaoDePedidos.getTotalElements().intValue());
        assertEquals(1, paginacaoDePedidos.getContent().size());
    }

    @Test
    public void deveTestarListarPedidosPaginadoComIdPet() {
        Page<PedidoEntity> paginaPedidos = getPagePedidos();

        when(pedidoRepository.listarPedidosPorPetPaginado(anyInt(), any(PageRequest.class))).thenReturn(paginaPedidos);

        PageDTO<PedidoDTO> paginacaoDePedidos = pedidoService.listarPedidosPaginado(null, ID_PET, 0, 5);

        assertNotNull(paginacaoDePedidos);
        assertEquals(1, paginacaoDePedidos.getTotalElements().intValue());
        assertEquals(1, paginacaoDePedidos.getContent().size());
    }

    @Test
    public void deveTestarListarPedidosPaginadoSemId() {
        Page<PedidoEntity> paginaPedidos = getPagePedidos();

        when(pedidoRepository.findAll(any(PageRequest.class))).thenReturn(paginaPedidos);

        PageDTO<PedidoDTO> paginacaoDePedidos = pedidoService.listarPedidosPaginado(null, null, 0, 5);

        assertNotNull(paginacaoDePedidos);
        assertEquals(1, paginacaoDePedidos.getTotalElements().intValue());
        assertEquals(1, paginacaoDePedidos.getContent().size());
    }

    @Test
    public void deveTestarGetByLoggedUserComSucesso() throws EntidadeNaoEncontradaException {
        ClienteEntity clienteEntity = getClienteComPetEPedidoEntity();

        when(clienteService.returnLoggedClient()).thenReturn(clienteEntity);

        List<PedidoDTO> pedidosDoUserLogado = pedidoService.getByLoggedUser();

        assertNotNull(pedidosDoUserLogado);
        assertFalse(pedidosDoUserLogado.isEmpty());
    }




    private UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(10);
        usuarioEntity.setUsername("Lancelot");
        usuarioEntity.setSenha("123");
        usuarioEntity.setCargos(Set.of(new CargoEntity()));
        usuarioEntity.setAtivo(true);
        usuarioEntity.setCliente(getClienteComPetEPedidoEntity());
        return usuarioEntity;
    }

    private PedidoEntity getPedidoEntity(ClienteEntity clienteEntity, PetEntity petEntity) {
        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setIdPedido(12);
        pedidoEntity.setServico(TipoServico.BANHO);
        pedidoEntity.setStatus(StatusPedido.ABERTO);
        pedidoEntity.setValor(90.0);
        pedidoEntity.setDescricao("SD");
        pedidoEntity.setCliente(clienteEntity);
        pedidoEntity.setPet(petEntity);
        pedidoEntity.setDataEHora(LocalDateTime.of(LocalDate.of(2022, 8, 2), LocalTime.of(21, 20)));
        return pedidoEntity;
    }

    private PetEntity getPetEntity(ClienteEntity clienteEntity) {
        PetEntity petEntity = new PetEntity();
        petEntity.setIdPet(42);
        petEntity.setNome("Leona");
        petEntity.setTipoPet(TipoPet.CACHORRO);
        petEntity.setRaca("Border Collie");
        petEntity.setPelagem(PelagemPet.LONGO);
        petEntity.setPorte(PortePet.GRANDE);
        petEntity.setIdade(4);
        petEntity.setCliente(clienteEntity);
        return petEntity;
    }

    private PedidoCreateDTO getPedidoCreateDTO() {
        PedidoCreateDTO pedidoCreateDTO = new PedidoCreateDTO();
        pedidoCreateDTO.setDescricao("SD");
        pedidoCreateDTO.setServico(TipoServico.BANHO);
        return pedidoCreateDTO;
    }

    private ClienteEntity getClienteEntity() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setIdCliente(10);
        clienteEntity.setNome("Anakin Skywalker");
        clienteEntity.setEmail("ImNotDarthVader@sith.com.br");
        clienteEntity.setQuantidadeDePedidos(0);
        clienteEntity.setValorPagamento(0.0);
        return clienteEntity;
    }

    private ClienteEntity getClienteComPetEPedidoEntity() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setIdCliente(10);
        clienteEntity.setNome("Anakin Skywalker");
        clienteEntity.setEmail("ImNotDarthVader@sith.com.br");
        clienteEntity.setQuantidadeDePedidos(1);

        PetEntity petEntity = new PetEntity();
        petEntity.setIdPet(42);
        petEntity.setNome("Leona");
        petEntity.setTipoPet(TipoPet.CACHORRO);
        petEntity.setRaca("Border Collie");
        petEntity.setPelagem(PelagemPet.LONGO);
        petEntity.setPorte(PortePet.GRANDE);
        petEntity.setIdade(4);
        petEntity.setCliente(clienteEntity);

        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setIdPedido(12);
        pedidoEntity.setServico(TipoServico.BANHO);
        pedidoEntity.setStatus(StatusPedido.ABERTO);
        pedidoEntity.setValor(90.0);
        pedidoEntity.setDescricao("SD");
        pedidoEntity.setCliente(clienteEntity);
        pedidoEntity.setPet(petEntity);
        pedidoEntity.setDataEHora(LocalDateTime.of(LocalDate.of(2022, 8, 2), LocalTime.of(21, 20)));


        clienteEntity.setValorPagamento(pedidoEntity.getValor());
        clienteEntity.setPedidos(Set.of(pedidoEntity));
        return clienteEntity;
    }

    private Page<PedidoEntity> getPagePedidos() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petEntity = getPetEntity(clienteEntity);
        List<PedidoEntity> pedidoEntityList = List.of(getPedidoEntity(clienteEntity, petEntity));
        return new PageImpl<>(pedidoEntityList);
    }
}
