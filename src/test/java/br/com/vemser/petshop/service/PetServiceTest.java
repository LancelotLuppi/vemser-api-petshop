package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.pet.PetCreateDTO;
import br.com.vemser.petshop.dto.pet.PetDTO;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.PelagemPet;
import br.com.vemser.petshop.enums.PortePet;
import br.com.vemser.petshop.enums.TipoPet;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.PetRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PetServiceTest {

    @InjectMocks
    private PetService petService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PetRepository petRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private UsuarioService usuarioService;



    @Before
    public void init(){
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(petService, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(clienteService, "objectMapper", objectMapper);
    }
    @Test
    public void deveTestarCreateByClientIdComSucesso() throws EntidadeNaoEncontradaException {
        ClienteEntity clienteEntity = getClienteEntityGeneric();
        PetEntity petEntity = getPetGeneric();
        PetCreateDTO petCreateDTO = getPetGenericDTO();

        when(clienteService.retornarPorIdVerificado(any())).thenReturn(clienteEntity);
        when(petRepository.save(any())).thenReturn(petEntity);
        PetDTO responsePetDTO = petService.createByClientId(10, petCreateDTO);

        Assertions.assertNotNull(responsePetDTO);
    }

    @Test
    public void deveTestarCreateByLoggedUserComSucesso() throws EntidadeNaoEncontradaException {
        PetEntity petEntity = getPetGeneric();
        PetCreateDTO petCreateDTO = getPetGenericDTO();
        UsuarioEntity loggedUser = getUsuarioEntityGeneric();

        when(petRepository.save(any())).thenReturn(petEntity);
        PetDTO responsePetDTO = petService.createByLoggedUser(petCreateDTO);

        Assertions.assertNotNull(responsePetDTO);
    }


    @Test
    public void deveTestarListarPetsPorClienteComSucesso() throws EntidadeNaoEncontradaException {
        List<PetEntity> petEntityList = List.of(getPetGeneric());

        when(petRepository.findAll()).thenReturn(petEntityList);

        List<PetDTO> petDTOS = petService.getByClientId(10);

        Assertions.assertNotNull(petDTOS);
        Assertions.assertTrue(!petDTOS.isEmpty());

    }


    @Test
    public void deveTestarGetByLoggedUserComSucesso() throws EntidadeNaoEncontradaException {
        UsuarioEntity usuarioEntity = getUsuarioEntityGeneric();
        ClienteEntity clienteEntity = getClienteEntityGeneric();
        List<PetEntity> petEntityList = List.of(getPetGenericComCliente(clienteEntity));


        when(clienteService.returnLoggedClient()).thenReturn(clienteEntity);
        List<PetDTO> petDTOS = petService.getByLoggedUser();

        Assertions.assertNotNull(petDTOS);
        Assertions.assertTrue(!petDTOS.isEmpty());
    }

    @Test
    public void deveTestarGetByPetIdComSucesso() throws EntidadeNaoEncontradaException {
        PetEntity petEntity = (getPetGeneric());

        when(petRepository.findById(any())).thenReturn(Optional.of(getPetGeneric()));

        PetDTO petDTO = petService.getByPetId(76);


        Assertions.assertNotNull(petDTO);

    }


    @Test
    public void deveTestarLoggedUpdateComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {

        PetCreateDTO petCreateDTO = getPetGenericDTO();
        PetEntity petEntity = getPetGeneric();
        UsuarioEntity loggedUser = getUsuarioEntityGeneric();

        when(usuarioService.findById(anyInt())).thenReturn(loggedUser);
        when(petRepository.findById(any())).thenReturn(Optional.of(petEntity));
        when(petRepository.save(any(PetEntity.class))).thenReturn(petEntity);

        PetDTO petDTO = petService.loggedUpdate(76, petCreateDTO);

        Assertions.assertNotNull(petDTO);
        Assertions.assertEquals("Smith", petDTO.getNome());
        Assertions.assertEquals(TipoPet.CACHORRO, petDTO.getTipoPet());
        Assertions.assertEquals("PITBULL", petDTO.getRaca());
        Assertions.assertEquals(PelagemPet.CURTO, petDTO.getPelagem());
        Assertions.assertEquals(PortePet.MEDIO, petDTO.getPorte());
        Assertions.assertEquals(5, petDTO.getIdade());
    }

    @Test
    public void deveTestarDeleteDoPetComSucesso() throws EntidadeNaoEncontradaException {

        Integer idParaDelete = 76;
        PetEntity petEntity = getPetGeneric();

        when(petRepository.findById(anyInt())).thenReturn(Optional.of(petEntity));
        doNothing().when(petRepository).delete(any(PetEntity.class));

        petService.delete(idParaDelete);

        verify(petRepository, times(1)).delete(any(PetEntity.class));
    }

    @Test
    public void deveTestarPagincaoPetsComSucesso(){

        List<PetEntity> petEntityList = List.of(getPetGeneric());
        Page<PetEntity> pagePets = new PageImpl<>(petEntityList);
        PageRequest pageRequest = PageRequest.of(0, 1);


        when(petRepository.findById(10, pageRequest)).thenReturn(pagePets);
        PageDTO<PetDTO> paginaDePets = petService.paginarPets(10, 0, 1);

        Assertions.assertNotNull(paginaDePets);
        Assertions.assertEquals(1, paginaDePets.getTotalElements());
        Assertions.assertEquals(1, paginaDePets.getContent().size());
    }

    @Test
    public void deveTestarVerificarIdPetComSucesso() throws EntidadeNaoEncontradaException {
        PetEntity petEntity = getPetGeneric();
        List<PetEntity> petEntityList = List.of(petEntity);
        Integer idPet = 76;

        when(petRepository.findAll()).thenReturn(petEntityList);

        petService.verificarIdPet(idPet);

        Assertions.assertNotNull(petEntity);
        Assertions.assertEquals(76, petEntity.getIdPet());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarVerificarPetDeOutroUser() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PetEntity petEntity = getPetGeneric();
        UsuarioEntity loggedUser = getUsuarioEntityGeneric();
        loggedUser.setCliente(getClienteEntityGeneric());
        loggedUser.getCliente().setPets(Set.of(petEntity));


        when(usuarioService.findById(anyInt())).thenReturn(loggedUser);


        petService.verificaPetDoUserLogado(10);
    }


    private static ClienteEntity getClienteEntityGeneric() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setIdCliente(10);
        clienteEntity.setNome("Mateus");
        clienteEntity.setEmail("mateus.test@gmail.com");
        clienteEntity.setPets(Set.of(PetEntity.builder().idPet(76).cliente(ClienteEntity.builder().idCliente(1).build()).build()));
        return clienteEntity;
    }


    private static PetCreateDTO getPetGenericDTO() {
        PetCreateDTO petCreateDTO = new PetCreateDTO();
        petCreateDTO.setNome("Smith");
        petCreateDTO.setTipoPet(TipoPet.CACHORRO);
        petCreateDTO.setRaca("PITBULL");
        petCreateDTO.setPelagem(PelagemPet.CURTO);
        petCreateDTO.setPorte(PortePet.MEDIO);
        petCreateDTO.setIdade(5);
        return petCreateDTO;
    }



    private static PetEntity getPetGeneric() {
        PetEntity petEntity = new PetEntity();
        petEntity.setNome("Smith");
        petEntity.setCliente(getClienteEntityGeneric());
        petEntity.setIdPet(76);
        petEntity.setTipoPet(TipoPet.CACHORRO);
        petEntity.setRaca("PITBULL");
        petEntity.setPelagem(PelagemPet.CURTO);
        petEntity.setPorte(PortePet.MEDIO);
        petEntity.setIdade(5);
        return petEntity;
    }
    private static PetEntity getPetGenericComCliente(ClienteEntity cliente) {
        PetEntity petEntity = new PetEntity();
        petEntity.setNome("Smith");
        petEntity.setCliente(cliente);
        petEntity.setIdPet(76);
        petEntity.setTipoPet(TipoPet.CACHORRO);
        petEntity.setRaca("PITBULL");
        petEntity.setPelagem(PelagemPet.CURTO);
        petEntity.setPorte(PortePet.MEDIO);
        petEntity.setIdade(5);

        return petEntity;
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


}
