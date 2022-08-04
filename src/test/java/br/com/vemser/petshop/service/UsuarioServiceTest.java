package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.login.LoginCreateDTO;
import br.com.vemser.petshop.dto.login.LoginDTO;
import br.com.vemser.petshop.dto.login.LoginStatusDTO;
import br.com.vemser.petshop.dto.login.LoginUpdateDTO;
import br.com.vemser.petshop.entity.CargoEntity;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.EnumDesativar;
import br.com.vemser.petshop.enums.TipoCargo;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.CargoRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CargoRepository cargoRepository;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarFindByIdComSucesso() throws EntidadeNaoEncontradaException {
        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(getUsuarioEntity());

        when(usuarioRepository.findById(anyInt())).thenReturn(usuarioEntityOptional);

        UsuarioEntity usuarioEntity = usuarioService.findById(anyInt());

        assertNotNull(usuarioEntity);
        assertEquals(usuarioEntityOptional.get().getIdUsuario(), usuarioEntity.getIdUsuario());
        assertEquals(usuarioEntityOptional.get().getCliente(), usuarioEntity.getCliente());
        assertEquals(usuarioEntityOptional.get().getCargos(), usuarioEntity.getCargos());
        assertEquals(usuarioEntityOptional.get().getAtivo(), usuarioEntity.getAtivo());
        assertEquals(usuarioEntityOptional.get().getSenha(), usuarioEntity.getSenha());
        assertEquals(usuarioEntityOptional.get().getUsername(), usuarioEntity.getUsername());

    }

    @Test
    public void deveTestarFindByUsername() {
        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(getUsuarioEntity());

        when(usuarioRepository.findByUsername(anyString())).thenReturn(usuarioEntityOptional);

        Optional<UsuarioEntity> usuarioEntityRecuperado = usuarioService.findByUsername(anyString());

        assertNotNull(usuarioEntityRecuperado);
        assertEquals(usuarioEntityOptional.get().getIdUsuario(), usuarioEntityRecuperado.get().getIdUsuario());
        assertEquals(usuarioEntityOptional.get().getCliente(), usuarioEntityRecuperado.get().getCliente());
        assertEquals(usuarioEntityOptional.get().getCargos(), usuarioEntityRecuperado.get().getCargos());
        assertEquals(usuarioEntityOptional.get().getAtivo(), usuarioEntityRecuperado.get().getAtivo());
        assertEquals(usuarioEntityOptional.get().getSenha(), usuarioEntityRecuperado.get().getSenha());
        assertEquals(usuarioEntityOptional.get().getUsername(), usuarioEntityRecuperado.get().getUsername());
    }

    @Test
    public void deveTestarCadastroComSucesso() throws RegraDeNegocioException {
        LoginCreateDTO loginCreateDTO = getLoginCreateDTO();
        CargoEntity cargoEntity = getCargoEntity();

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(cargoRepository.findById(anyInt())).thenReturn(Optional.of(cargoEntity));

        LoginDTO loginDTO = usuarioService.cadastro(loginCreateDTO);

        assertNotNull(loginDTO);
        assertEquals(loginCreateDTO.getUsername(), loginDTO.getUsername());
        assertEquals(loginDTO.getCargos().get(0), cargoEntity.getNome());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCadastro() throws RegraDeNegocioException {
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(getUsuarioEntity()));

        usuarioService.cadastro(getLoginCreateDTO());
    }

    @Test
    public void deveTestarUpdateCargos() throws EntidadeNaoEncontradaException {
        CargoEntity cargoEntity = getCargoEntity();
        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(getUsuarioEntity());
        usuarioEntityOptional.get().setCargos(Set.of(getCargoEntity()));

        when(usuarioRepository.findById(anyInt())).thenReturn(usuarioEntityOptional);
        when(cargoRepository.findById(anyInt())).thenReturn(Optional.of(cargoEntity));

        LoginDTO loginDTO = usuarioService.updateCargos(1, Set.of(TipoCargo.ADMIN));

        assertNotNull(loginDTO);
        assertEquals(usuarioEntityOptional.get().getIdUsuario(), loginDTO.getIdUsuario());
        assertEquals(loginDTO.getCargos().get(0), cargoEntity.getNome());
        assertEquals(usuarioEntityOptional.get().getUsername(), loginDTO.getUsername());
    }

    @Test
    public void deveTestarDesativarContaComSucesso() throws EntidadeNaoEncontradaException {
        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(getUsuarioEntity());

        when(usuarioRepository.findById(anyInt())).thenReturn(usuarioEntityOptional);

        LoginStatusDTO loginStatusDTO = usuarioService.desativarConta(1, EnumDesativar.DESATIVAR);
        assertNotNull(loginStatusDTO);
        assertEquals(usuarioEntityOptional.get().getIdUsuario(), loginStatusDTO.getIdUsuario());
        assertEquals(usuarioEntityOptional.get().getUsername(), loginStatusDTO.getUsername());
    }

    @Test
    public void deveTestarUpdateLoggedUsername() throws RegraDeNegocioException {
        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(getUsuarioEntity());
        LoginUpdateDTO loginUpdateDTO = new LoginUpdateDTO();
        loginUpdateDTO.setUsername("Teste");
        loginUpdateDTO.setTipoCargo(TipoCargo.ADMIN);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(123, "senha");
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        when(usuarioRepository.findById(anyInt())).thenReturn(usuarioEntityOptional);
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        LoginDTO loginDTO = usuarioService.updateLoggedUsername(loginUpdateDTO);

        assertNotNull(loginDTO);
        assertEquals(loginUpdateDTO.getUsername(), loginDTO.getUsername());
    }

    @Test
    public void deveTestarGetLoggedUser() throws EntidadeNaoEncontradaException {
        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(getUsuarioEntity());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(123, "senha");
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        when(usuarioRepository.findById(anyInt())).thenReturn(usuarioEntityOptional);

        LoginDTO loginDTO = usuarioService.getLoggedUser();

        assertNotNull(loginDTO);
        assertEquals(usuarioEntityOptional.get().getUsername(), loginDTO.getUsername());
        assertEquals(usuarioEntityOptional.get().getIdUsuario(), loginDTO.getIdUsuario());
    }

    @Test
    public void deveTestarUpdateLoggedPassWord() throws RegraDeNegocioException {
        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(getUsuarioEntity());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(123, "senha");
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        when(usuarioRepository.findById(anyInt())).thenReturn(usuarioEntityOptional);

        String retorno = usuarioService.updateLoggedPassword("123");

        assertEquals("Senha alterada com sucesso!", retorno);
    }

    @Test
    public void deveTestarDeleteUser() {
        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(getUsuarioEntity());

        when(usuarioRepository.findById(anyInt())).thenReturn(usuarioEntityOptional);

        usuarioService.deleteUser(anyInt());

        verify(usuarioRepository, times(1)).delete(any(UsuarioEntity.class));
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setUsername("Jean");
        usuarioEntity.setSenha(new Argon2PasswordEncoder().encode("123"));
        usuarioEntity.setAtivo(true);
        usuarioEntity.setCliente(getClienteEntity());
        usuarioEntity.setCargos(Set.of(getCargoEntity()));

        return usuarioEntity;
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

    private static CargoEntity getCargoEntity() {
        CargoEntity cargoEntity = new CargoEntity();
        cargoEntity.setIdCargo(1);
        cargoEntity.setNome("ROLE_ADMIN");

        return cargoEntity;
    }

    private static LoginCreateDTO getLoginCreateDTO() {
        LoginCreateDTO loginCreateDTO = new LoginCreateDTO();
        loginCreateDTO.setUsername("Jean");
        loginCreateDTO.setSenha("123");
        return loginCreateDTO;
    }
}
