package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.BalancoMensalEntity;
import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.enums.TipoServico;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.repository.BalancoMensalRepository;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BalancoMensalServiceTest {
    @InjectMocks
    private BalancoMensalService balancoMensalService;
    @Mock
    private BalancoMensalRepository balancoMensalRepository;
    @Mock
    private SequencesMongoService sequencesMongoService;
    @Mock
    private MongoTemplate mongoTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(balancoMensalService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarGetBalancoMesAtual() throws EntidadeNaoEncontradaException {
        BalancoMensalEntity balancoMensalEntity = getBalancoMensal();

        when(balancoMensalRepository.findBalancoMensalEntitiesByMesAndAno(anyInt(), anyInt()))
                .thenReturn(Optional.of(balancoMensalEntity));

        balancoMensalService.getBalancoMesAtual();

        assertNotNull(balancoMensalEntity);
        assertEquals(3, balancoMensalEntity.getIdBalancoMensal().intValue());
        assertEquals(8, balancoMensalEntity.getMes().intValue());
        assertEquals(2022, balancoMensalEntity.getAno().intValue());
        assertEquals(15600.0, balancoMensalEntity.getLucroBruto(), 0);
    }

    @Test
    public void deveTestarGetBalancoByMesAndAno() throws EntidadeNaoEncontradaException {
        BalancoMensalEntity balancoMensalEntity = getBalancoMensal();

        when(balancoMensalRepository.findBalancoMensalEntitiesByMesAndAno(anyInt(), anyInt()))
                .thenReturn(Optional.of(balancoMensalEntity));

        balancoMensalService.getBalancoByMesAndAno(1, 2022);

        assertNotNull(balancoMensalEntity);
        assertEquals(3, balancoMensalEntity.getIdBalancoMensal().intValue());
        assertEquals(8, balancoMensalEntity.getMes().intValue());
        assertEquals(2022, balancoMensalEntity.getAno().intValue());
        assertEquals(15600.0, balancoMensalEntity.getLucroBruto(), 0);
    }

    @Test(expected = EntidadeNaoEncontradaException.class)
    public void deveTestarFindBalancoMesAtualComEntidadeNaoEncontrada() throws EntidadeNaoEncontradaException {

        when(balancoMensalRepository.findBalancoMensalEntitiesByMesAndAno(anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        balancoMensalService.getBalancoMesAtual();
    }

    @Test
    public void deveTestarAtualizarBalancoComEntidade() {
        BalancoMensalEntity balancoMensalEntity = getBalancoMensal();
        PedidoEntity pedidoEntity = getPedidoEntity();
        BalancoMensalEntity balancoMensalAtualizado = getBalancoMensalAtualizado();

        when(balancoMensalRepository.findBalancoMensalEntitiesByMesAndAno(anyInt(), anyInt()))
                .thenReturn(Optional.of(balancoMensalEntity));

        balancoMensalService.atualizarBalanco(pedidoEntity);

        assertNotNull(balancoMensalEntity);
        assertEquals(3, balancoMensalAtualizado.getIdBalancoMensal().intValue());
        assertEquals(8, balancoMensalAtualizado.getMes().intValue());
        assertEquals(2022, balancoMensalAtualizado.getAno().intValue());
        assertEquals(15690.0, balancoMensalAtualizado.getLucroBruto(), 0);
    }

    @Test
    public void deveTestarAtualizarBalancoSemEntidade() {
        BalancoMensalEntity balancoMensalEntity = getBalancoMensal();
        PedidoEntity pedidoEntity = getPedidoEntity();
        BalancoMensalEntity balancoMensalUnico = getBalancoMensalUnico();

        when(balancoMensalRepository.findBalancoMensalEntitiesByMesAndAno(anyInt(), anyInt()))
                .thenReturn(Optional.empty());
        when(balancoMensalRepository.save(any())).thenReturn(balancoMensalUnico);

        balancoMensalService.atualizarBalanco(pedidoEntity);

        assertNotNull(balancoMensalEntity);
        assertEquals(0, balancoMensalUnico.getIdBalancoMensal().intValue());
        assertEquals(8, balancoMensalUnico.getMes().intValue());
        assertEquals(2022, balancoMensalUnico.getAno().intValue());
        assertEquals(90.0, balancoMensalUnico.getLucroBruto(), 0);
    }



    public BalancoMensalEntity getBalancoMensal() {
        BalancoMensalEntity balancoMensalEntity = new BalancoMensalEntity();
        balancoMensalEntity.setIdBalancoMensal(3);
        balancoMensalEntity.setMes(8);
        balancoMensalEntity.setAno(2022);
        balancoMensalEntity.setLucroBruto(15600.0);
        return balancoMensalEntity;
    }

    public BalancoMensalEntity getBalancoMensalAtualizado() {
        BalancoMensalEntity balancoMensalEntity = new BalancoMensalEntity();
        balancoMensalEntity.setIdBalancoMensal(3);
        balancoMensalEntity.setMes(8);
        balancoMensalEntity.setAno(2022);
        balancoMensalEntity.setLucroBruto(15690.0);
        return balancoMensalEntity;
    }

    public BalancoMensalEntity getBalancoMensalUnico() {
        BalancoMensalEntity balancoMensalEntity = new BalancoMensalEntity();
        balancoMensalEntity.setIdBalancoMensal(0);
        balancoMensalEntity.setMes(8);
        balancoMensalEntity.setAno(2022);
        balancoMensalEntity.setLucroBruto(90.0);
        return balancoMensalEntity;
    }

    public Optional<BalancoMensalEntity> getBalancoMensalEmpty() {
        return Optional.empty();
    }

    public PedidoEntity getPedidoEntity() {
        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setIdPedido(8);
        pedidoEntity.setDataEHora(LocalDateTime.now());
        pedidoEntity.setServico(TipoServico.BANHO);
        pedidoEntity.setValor(90.0);
        pedidoEntity.setDescricao("SD");
        pedidoEntity.setStatus(StatusPedido.ABERTO);
        return pedidoEntity;
    }
}
