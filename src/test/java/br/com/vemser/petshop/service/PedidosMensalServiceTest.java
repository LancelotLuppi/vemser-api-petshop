package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.entity.PedidoMensalEntity;
import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.enums.TipoServico;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.repository.PedidosMensalRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PedidosMensalServiceTest {
    @InjectMocks
    private PedidosMensalService pedidosMensalService;
    @Mock
    private PedidosMensalRepository pedidosMensalRepository;
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
        ReflectionTestUtils.setField(pedidosMensalService, "objectMapper", objectMapper);
    }


    @Test
    public void deveTestarGetPedidoMesAtualComSucesso() throws EntidadeNaoEncontradaException {
        PedidoMensalEntity pedidoMensalEntity = getPedidoMensalEntity();

        when(pedidosMensalRepository.findPedidosByMesAndAno(anyInt(), anyInt()))
                .thenReturn(Optional.of(pedidoMensalEntity));

        pedidosMensalService.getPedidoMesAtual();

        assertNotNull(pedidoMensalEntity);
        assertEquals(4, pedidoMensalEntity.getIdPedidoMensal().intValue());
        assertEquals(8, pedidoMensalEntity.getMes().intValue());
        assertEquals(2022, pedidoMensalEntity.getAno().intValue());
        assertEquals(21, pedidoMensalEntity.getTotalPedido().intValue());
    }

    @Test(expected = EntidadeNaoEncontradaException.class)
    public void deveTestarGetPedidoMesAtualComEntidadeNaoEncontrada() throws EntidadeNaoEncontradaException {

        when(pedidosMensalRepository.findPedidosByMesAndAno(anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        pedidosMensalService.getPedidoMesAtual();
    }

    @Test
    public void deveTestarGetPedidoByMesAndAnoComSucesso() throws EntidadeNaoEncontradaException {
        PedidoMensalEntity pedidoMensalEntity = getPedidoMensalEntity();

        when(pedidosMensalRepository.findPedidosByMesAndAno(anyInt(), anyInt()))
                .thenReturn(Optional.of(pedidoMensalEntity));

        pedidosMensalService.getPedidoByMesAndAno(1, 2022);

        assertNotNull(pedidoMensalEntity);
        assertEquals(4, pedidoMensalEntity.getIdPedidoMensal().intValue());
        assertEquals(8, pedidoMensalEntity.getMes().intValue());
        assertEquals(2022, pedidoMensalEntity.getAno().intValue());
        assertEquals(21, pedidoMensalEntity.getTotalPedido().intValue());
    }

    @Test
    public void deveTestarAtualizarPedidosComSucesso() {
        PedidoMensalEntity pedidoMensalEntity = getPedidoMensalEntity();
        PedidoEntity pedidoEntity = getPedidoEntity();

        when(pedidosMensalRepository.findPedidosByMesAndAno(anyInt(), anyInt()))
                .thenReturn(Optional.of(pedidoMensalEntity));

        pedidosMensalService.atualizarPedidos(pedidoEntity);

        assertNotNull(pedidoMensalEntity);
        assertEquals(4, pedidoMensalEntity.getIdPedidoMensal().intValue());
        assertEquals(8, pedidoMensalEntity.getMes().intValue());
        assertEquals(2022, pedidoMensalEntity.getAno().intValue());
        assertEquals(22, pedidoMensalEntity.getTotalPedido().intValue());
    }

    @Test
    public void deveTestarAtualizarPedidosSemRegistro() {
        PedidoMensalEntity pedidoMensalEntity = getPedidoMensalUnico();
        PedidoEntity pedidoEntity = getPedidoEntity();

        when(pedidosMensalRepository.findPedidosByMesAndAno(anyInt(), anyInt()))
                .thenReturn(Optional.empty());
        when(pedidosMensalRepository.save(any())).thenReturn(pedidoMensalEntity);

        pedidosMensalService.atualizarPedidos(pedidoEntity);

        assertNotNull(pedidoMensalEntity);
        assertEquals(5, pedidoMensalEntity.getIdPedidoMensal().intValue());
        assertEquals(8, pedidoMensalEntity.getMes().intValue());
        assertEquals(2022, pedidoMensalEntity.getAno().intValue());
        assertEquals(1, pedidoMensalEntity.getTotalPedido().intValue());
    }

    public PedidoMensalEntity getPedidoMensalEntity() {
        PedidoMensalEntity pedidoMensalEntity = new PedidoMensalEntity();
        pedidoMensalEntity.setIdPedidoMensal(4);
        pedidoMensalEntity.setMes(8);
        pedidoMensalEntity.setAno(2022);
        pedidoMensalEntity.setTotalPedido(21);
        return pedidoMensalEntity;
    }
    public PedidoMensalEntity getPedidoMensalUnico() {
        PedidoMensalEntity pedidoMensalEntity = new PedidoMensalEntity();
        pedidoMensalEntity.setIdPedidoMensal(5);
        pedidoMensalEntity.setMes(8);
        pedidoMensalEntity.setAno(2022);
        pedidoMensalEntity.setTotalPedido(1);
        return pedidoMensalEntity;
    }

    private PedidoEntity getPedidoEntity() {
        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setIdPedido(12);
        pedidoEntity.setServico(TipoServico.BANHO);
        pedidoEntity.setStatus(StatusPedido.ABERTO);
        pedidoEntity.setValor(90.0);
        pedidoEntity.setDescricao("SD");
        pedidoEntity.setDataEHora(LocalDateTime.of(LocalDate.of(2022, 8, 2), LocalTime.of(21, 20)));
        return pedidoEntity;
    }

}
