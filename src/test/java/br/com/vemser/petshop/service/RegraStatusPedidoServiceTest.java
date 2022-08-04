package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.enums.*;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.ClienteRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegraStatusPedidoServiceTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private RegraStatusPedidoService regraStatusPedidoService;

    @Mock
    ClienteService clienteService;

    @Mock
    ClienteRepository clienteRepository;

    @Test
    public void deveTestarUpdateStatusCanceladoComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();
        ClienteEntity clienteEntity = getClienteEntity();

        when(clienteService.retornarPorIdVerificado(anyInt())).thenReturn(clienteEntity);

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.CANCELADO);

        assertEquals(Integer.valueOf(0), clienteEntity.getQuantidadeDePedidos());
        assertEquals(Double.valueOf(0), clienteEntity.getValorPagamento());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateStatusIgual() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.ABERTO);

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateStatusAbertoParaConcluido() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.CONCLUIDO);

    }

    @Test
    public void deveTestarUpdateStatusEmAndamentoPassandoStatusCanceladoComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();
        pedidoEntity.setStatus(StatusPedido.EM_ANDAMENTO);
        ClienteEntity clienteEntity = getClienteEntity();

        when(clienteService.retornarPorIdVerificado(anyInt())).thenReturn(clienteEntity);

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.CANCELADO);

        assertEquals(Integer.valueOf(0), clienteEntity.getQuantidadeDePedidos());
        assertEquals(Double.valueOf(0), clienteEntity.getValorPagamento());
    }

    @Test
    public void deveTestarUpdateStatusEmAndamentoPassandoStatusConcluidoComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();
        pedidoEntity.setStatus(StatusPedido.EM_ANDAMENTO);
        ClienteEntity clienteEntity = getClienteEntity();

        when(clienteService.retornarPorIdVerificado(anyInt())).thenReturn(clienteEntity);

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.CONCLUIDO);

        assertEquals(Integer.valueOf(0), clienteEntity.getQuantidadeDePedidos());
        assertEquals(Double.valueOf(0), clienteEntity.getValorPagamento());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateStatusConcluidoPassandoStatusCancelado() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();
        pedidoEntity.setStatus(StatusPedido.CONCLUIDO);

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.CANCELADO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateStatusConcluidoPassandoStatusAberto() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();
        pedidoEntity.setStatus(StatusPedido.CONCLUIDO);

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.ABERTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateStatusConcluidoPassandoStatusEmAndamento() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();
        pedidoEntity.setStatus(StatusPedido.CONCLUIDO);

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.EM_ANDAMENTO);
    }

    @Test
    public void deveTestarUpdateStatusCanceladoPassandoStatusAbertoComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();
        pedidoEntity.setStatus(StatusPedido.CANCELADO);
        ClienteEntity clienteEntity = getClienteEntity();
        clienteEntity.setQuantidadeDePedidos(0);
        clienteEntity.setValorPagamento(0.0);

        when(clienteService.retornarPorIdVerificado(anyInt())).thenReturn(clienteEntity);

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.ABERTO);

        assertEquals(StatusPedido.ABERTO, pedidoEntity.getStatus());
        assertEquals(Integer.valueOf(1), clienteEntity.getQuantidadeDePedidos());
        assertEquals(Double.valueOf(98), clienteEntity.getValorPagamento());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateStatusCanceladoPassandoStatusEmAndamento() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();
        pedidoEntity.setStatus(StatusPedido.CANCELADO);

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.EM_ANDAMENTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateStatusCanceladoPassandoStatusConcluido() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        PedidoEntity pedidoEntity = getPedidoEntity();
        pedidoEntity.setStatus(StatusPedido.CANCELADO);

        regraStatusPedidoService.updateStatus(pedidoEntity, StatusPedido.CONCLUIDO);
    }
    private static PedidoEntity getPedidoEntity(){
        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setIdPedido(1);
        pedidoEntity.setStatus(StatusPedido.ABERTO);
        pedidoEntity.setCliente(getClienteEntity());
        pedidoEntity.setServico(TipoServico.TOSA);
        pedidoEntity.setValor(98.0);
        pedidoEntity.setDataEHora(LocalDateTime.now());
        pedidoEntity.setPet(getPetEntity());
        pedidoEntity.getPet().setCliente(pedidoEntity.getCliente());
        pedidoEntity.setDescricao("Teste");

        return pedidoEntity;
    }

    private static ClienteEntity getClienteEntity(){
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNome("Jean Silva");
        clienteEntity.setEmail("jean@teste.com");
        clienteEntity.setValorPagamento(98.0);
        clienteEntity.setQuantidadeDePedidos(1);
        clienteEntity.setIdCliente(1);

        return clienteEntity;
    }

    private static PetEntity getPetEntity() {
        PetEntity petEntity = new PetEntity();
        petEntity.setNome("Smith");
        petEntity.setIdPet(76);
        petEntity.setTipoPet(TipoPet.CACHORRO);
        petEntity.setRaca("PITBULL");
        petEntity.setPelagem(PelagemPet.CURTO);
        petEntity.setPorte(PortePet.MEDIO);
        petEntity.setIdade(5);

        return petEntity;
    }
}
