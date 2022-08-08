package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.contato.ContatoCreateDTO;
import br.com.vemser.petshop.dto.contato.ContatoDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.ContatoEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ContatoRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ContatoServiceTest {

    @InjectMocks
    private ContatoService contatoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ClienteService clienteService;

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Before
    public void init(){
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(contatoService, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(clienteService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarGetByLoggedUserComSucesso() throws EntidadeNaoEncontradaException {
        UsuarioEntity usuarioEntity = getUsuarioEntityGeneric();
        ClienteEntity clienteEntity = getClienteEntityGeneric();
        List<ContatoEntity> contatoEntityList = List.of(getContatoGenericComCliente(clienteEntity));


        when(clienteService.returnLoggedClient()).thenReturn(clienteEntity);
        List<ContatoDTO> contatoDTOS = contatoService.getByLoggedUser();

        Assertions.assertNotNull(contatoDTOS);
        Assertions.assertTrue(!contatoDTOS.isEmpty());
    }

    @Test
    public void deveTestarCreateByLoggedUserComSucesso() throws EntidadeNaoEncontradaException {
        ContatoEntity contatoEntity = getContatoGeneric();
        ContatoCreateDTO contatoCreateDTO = getContatoGenericDTO();
        UsuarioEntity loggedUser = getUsuarioEntityGeneric();

        when(contatoRepository.save(any())).thenReturn(contatoEntity);
        ContatoDTO responseContatoDTO = contatoService.createByLoggedUser(contatoCreateDTO);

        Assertions.assertNotNull(responseContatoDTO);
    }

    @Test
    public void deveTestarLoggedUpdateComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {

        ContatoCreateDTO contatoCreateDTO = getContatoGenericDTO();
        ContatoEntity contatoEntity = getContatoGeneric();
        UsuarioEntity loggedUser = getUsuarioEntityGeneric();

        when(usuarioService.findById(anyInt())).thenReturn(loggedUser);
        when(contatoRepository.findById(any())).thenReturn(Optional.of(contatoEntity));
        when(contatoRepository.save(any(ContatoEntity.class))).thenReturn(contatoEntity);

        ContatoDTO contatoDTO = contatoService.updateByLoggedUser(1, contatoCreateDTO);

        Assertions.assertNotNull(contatoDTO);
        Assertions.assertEquals(1, contatoDTO.getIdContato());
        Assertions.assertEquals("51998877333", contatoDTO.getTelefone());
        Assertions.assertEquals("celular pessoal", contatoDTO.getDescricao());
        Assertions.assertEquals(10, contatoDTO.getIdCliente());
    }

    @Test
    public void deveTestarCreateByClientIdComSucesso() throws EntidadeNaoEncontradaException {
        ClienteEntity clienteEntity = getClienteEntityGeneric();
        ContatoEntity contatoEntity = getContatoGeneric();
        ContatoCreateDTO contatoCreateDTO = getContatoGenericDTO();

        when(clienteService.retornarPorIdVerificado(any())).thenReturn(clienteEntity);
        when(contatoRepository.save(any())).thenReturn(contatoEntity);
        ContatoDTO responseContatoDTO = contatoService.create(10, contatoCreateDTO);

        Assertions.assertNotNull(responseContatoDTO);
    }

    @Test
    public void deveTestarListarContatosPorClienteComSucesso() throws EntidadeNaoEncontradaException {
        List<ContatoEntity> contatoEntityList = List.of(getContatoGeneric());

        when(contatoRepository.findAll()).thenReturn(contatoEntityList);

        List<ContatoDTO> contatoDTOS = contatoService.listByIdCliente(10);

        Assertions.assertNotNull(contatoDTOS);
        Assertions.assertTrue(!contatoDTOS.isEmpty());

    }

    @Test
    public void deveTestarDeleteDeContatoComSucesso() throws EntidadeNaoEncontradaException {

        Integer idParaDelete = 1;
        ContatoEntity contatoEntity = getContatoGeneric();

        when(contatoRepository.findById(anyInt())).thenReturn(Optional.of(contatoEntity));
        doNothing().when(contatoRepository).delete(any(ContatoEntity.class));

        contatoService.delete(idParaDelete);

        verify(contatoRepository, times(1)).delete(any(ContatoEntity.class));
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarVerificarContatoDeOutroUser() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        ContatoEntity contatoEntity = getContatoGeneric();
        UsuarioEntity loggedUser = getUsuarioEntityGeneric();
        loggedUser.setCliente(getClienteEntityGeneric());
        loggedUser.getCliente().setContatos(Set.of(contatoEntity));


        when(usuarioService.findById(anyInt())).thenReturn(loggedUser);


        contatoService.verificarContatoDoUserLogado(10);
    }

    private static UsuarioEntity getUsuarioEntityGeneric() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(7);
        usuarioEntity.setUsername("tetz");
        usuarioEntity.setSenha("123");
        usuarioEntity.setAtivo(true);
        usuarioEntity.setCliente(getClienteEntityGeneric());
        return usuarioEntity;
    }

    private static ClienteEntity getClienteEntityGeneric() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setIdCliente(10);
        clienteEntity.setNome("Mateus");
        clienteEntity.setEmail("mateus.test@gmail.com");
        clienteEntity.setContatos(Set.of(ContatoEntity.builder().idContato(1).cliente(ClienteEntity.builder().idCliente(1).build()).build()));
        return clienteEntity;
    }

    private static ContatoEntity getContatoGeneric() {
        ContatoEntity contatoEntity = new ContatoEntity();
        contatoEntity.setIdContato(1);
        contatoEntity.setTelefone("51998877333");
        contatoEntity.setDescricao("celular pessoal");
        contatoEntity.setCliente(getClienteEntityGeneric());
        return contatoEntity;
    }

    private static ContatoEntity getContatoGenericComCliente(ClienteEntity cliente) {
        ContatoEntity contatoEntity = new ContatoEntity();
        contatoEntity.setIdContato(1);
        contatoEntity.setTelefone("51998877333");
        contatoEntity.setDescricao("celular pessoal");
        contatoEntity.setCliente(getClienteEntityGeneric());
        return contatoEntity;
    }

    private static ContatoCreateDTO getContatoGenericDTO(){
        ContatoCreateDTO contatoCreateDTO = new ContatoCreateDTO();
        contatoCreateDTO.setTelefone("51998877333");
        contatoCreateDTO.setDescricao("celular pessoal");
        return contatoCreateDTO;
    }
}
