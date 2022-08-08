package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.cliente.ClienteCreateDTO;
import br.com.vemser.petshop.dto.cliente.ClienteDTO;
import br.com.vemser.petshop.dto.cliente.ClienteDadosRelatorioDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.TipoRequisicao;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import br.com.vemser.petshop.repository.UsuarioRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailService emailService;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(clienteService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarCreateComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        ClienteCreateDTO clienteCreateDTO = getClienteCreateDTO();
        UsuarioEntity loggedUser = getUsuarioEntity();
        Optional<ClienteEntity> cliente = Optional.of(getClienteEntity());

        when(usuarioService.findById(any(Integer.class))).thenReturn(loggedUser);
        when(clienteRepository.findById(any(Integer.class))).thenReturn(cliente);
        when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(cliente.get());
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(null);
        doNothing().when(emailService).sendEmail(any(String.class), any(Integer.class), any(String.class), any(TipoRequisicao.class));

        ClienteDTO clienteDTO = clienteService.create(clienteCreateDTO);

        assertNotNull(clienteDTO);
        assertEquals(cliente.get().getIdCliente(), clienteDTO.getIdCliente());
        assertEquals(cliente.get().getNome(), clienteDTO.getNome());
        assertEquals(cliente.get().getEmail(), clienteDTO.getEmail());
        assertEquals(Integer.valueOf(0) , clienteDTO.getValorPagamento());
        assertEquals(Integer.valueOf(0), clienteDTO.getQuantidadeDePedidos());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCreateComCliente() throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        ClienteCreateDTO clienteCreateDTO = getClienteCreateDTO();
        UsuarioEntity loggedUser = getUsuarioEntity();
        loggedUser.setCliente(getClienteEntity());

        when(usuarioService.findById(any(Integer.class))).thenReturn(loggedUser);

        clienteService.create(clienteCreateDTO);
    }

    @Test
    public void deveTestarDeleteComSucesso() throws EntidadeNaoEncontradaException {
        Optional<ClienteEntity> cliente = Optional.of(getClienteEntity());

        when(clienteRepository.findById(any(Integer.class))).thenReturn(cliente);
        doNothing().when(clienteRepository).delete(any(ClienteEntity.class));

        clienteService.delete(1);

        verify(clienteRepository, times(1)).delete(any(ClienteEntity.class));
    }

    @Test
    public void deveTestarUpdateComSucesso() throws EntidadeNaoEncontradaException {
        ClienteCreateDTO clienteCreateDTO = getClienteCreateDTO();
        Optional<ClienteEntity> cliente = Optional.of(getClienteEntity());

        when(clienteRepository.findById(any(Integer.class))).thenReturn(cliente);
        when(clienteRepository.save(any(ClienteEntity.class))).thenReturn(cliente.get());
        doNothing().when(emailService).sendEmail(any(String.class), any(Integer.class), any(String.class), any(TipoRequisicao.class));

        ClienteDTO clienteDTOAtulizado = clienteService.update(1, clienteCreateDTO);

        assertNotNull(clienteDTOAtulizado);
        assertEquals(clienteCreateDTO.getNome(), clienteDTOAtulizado.getNome());
        assertEquals(clienteCreateDTO.getEmail(), clienteDTOAtulizado.getEmail());
    }

    @Test
    public void deveTestarListarDadosClienteComSucesso(){
        ClienteDadosRelatorioDTO clienteDadosRelatorioDTO = new ClienteDadosRelatorioDTO();

        when(clienteRepository.relatorioCliente(1)).thenReturn(List.of(clienteDadosRelatorioDTO));

        List<ClienteDadosRelatorioDTO> clienteDadosRelatorioDTOList = clienteService.listarDadosCliente(1);

        assertNotNull(clienteDadosRelatorioDTOList);
        assertTrue(!clienteDadosRelatorioDTOList.isEmpty());
    }

    @Test
    public void deveTestarGetLoggedComSucesso() throws EntidadeNaoEncontradaException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ClienteEntity clienteEntity = getClienteEntity();
        usuarioEntity.setCliente(clienteEntity);

        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);

        ClienteDTO clienteDTO = clienteService.getLogged();

        assertNotNull(clienteDTO);
        assertEquals(clienteEntity.getIdCliente(), clienteDTO.getIdCliente());
        assertEquals(clienteEntity.getNome(), clienteDTO.getNome());
        assertEquals(clienteEntity.getEmail(), clienteDTO.getEmail());
        assertEquals(Integer.valueOf(clienteEntity.getValorPagamento().intValue()), clienteDTO.getValorPagamento());
        assertEquals(clienteEntity.getQuantidadeDePedidos(), clienteDTO.getQuantidadeDePedidos());
    }

    @Test
    public void deveTestarGetByIdComSucesso() {
        Optional<ClienteEntity> clienteEntityOptional = Optional.of(getClienteEntity());

        when(clienteRepository.findById(anyInt())).thenReturn(clienteEntityOptional);

        ClienteDTO clienteDTO = clienteService.getById(1);

        assertNotNull(clienteDTO);
        assertEquals(clienteEntityOptional.get().getIdCliente(), clienteDTO.getIdCliente());
        assertEquals(clienteEntityOptional.get().getNome(), clienteDTO.getNome());
        assertEquals(clienteEntityOptional.get().getEmail(), clienteDTO.getEmail());
        assertEquals(Integer.valueOf(clienteEntityOptional.get().getValorPagamento().intValue()), clienteDTO.getValorPagamento());
        assertEquals(clienteEntityOptional.get().getQuantidadeDePedidos(), clienteDTO.getQuantidadeDePedidos());
    }

    @Test
    public void deveTestarVerificarIdComSucesso() throws EntidadeNaoEncontradaException {
        Optional<ClienteEntity> clienteEntityOptional = Optional.of(getClienteEntity());

        when(clienteRepository.findById(anyInt())).thenReturn(clienteEntityOptional);

        clienteService.verificarId(1);

        verify(clienteRepository, times(1)).findById(anyInt());
    }

    @Test
    public void deveTestarReturnLoggedClient() throws EntidadeNaoEncontradaException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ClienteEntity clienteEntity = getClienteEntity();
        usuarioEntity.setCliente(clienteEntity);

        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);

        ClienteEntity clienteEntityRecuperado = clienteService.returnLoggedClient();

        assertNotNull(clienteEntityRecuperado);
        assertEquals(clienteEntity.getIdCliente(), clienteEntityRecuperado.getIdCliente());
        assertEquals(clienteEntity.getNome(), clienteEntityRecuperado.getNome());
        assertEquals(clienteEntity.getEmail(), clienteEntityRecuperado.getEmail());
        assertEquals(clienteEntity.getValorPagamento(), clienteEntityRecuperado.getValorPagamento());
        assertEquals(clienteEntity.getQuantidadeDePedidos(), clienteEntityRecuperado.getQuantidadeDePedidos());
    }

    private static ClienteCreateDTO getClienteCreateDTO(){
        ClienteCreateDTO clienteCreateDTO = new ClienteCreateDTO();
        clienteCreateDTO.setNome("Jean Silva");
        clienteCreateDTO.setEmail("jean@teste.com");

        return clienteCreateDTO;
    }

    private static ClienteEntity getClienteEntity(){
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNome("Jean Silva");
        clienteEntity.setEmail("jean@teste.com");
        clienteEntity.setValorPagamento(0.0);
        clienteEntity.setQuantidadeDePedidos(0);
        clienteEntity.setIdCliente(1);

        return clienteEntity;
    }

    private static UsuarioEntity getUsuarioEntity(){
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setAtivo(true);
        usuarioEntity.setSenha("123");
        usuarioEntity.setUsername("JeanSilva");
        return usuarioEntity;
    }
}
