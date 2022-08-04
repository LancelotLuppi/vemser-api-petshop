package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.enums.*;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CalculadoraServiceTest {

    @InjectMocks
    private CalculadoraService calculadoraService;

    private static final Integer DELTA = 0;


    @Test
    public void deveTestarCalcularValorDoPedidoParaBanhoDeCachorroPequeno() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petCachorroPequeno = getCachorroPortePequeno(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoBanho(clienteEntity, petCachorroPequeno);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petCachorroPequeno);

        assertNotNull(valorDoPedido);
        assertEquals(50.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaBanhoDeCachorroMedio() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petCachorroMedio = getCachorroPorteMedio(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoBanho(clienteEntity, petCachorroMedio);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petCachorroMedio);

        assertNotNull(valorDoPedido);
        assertEquals(65.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaBanhoDeCachorroGrande() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petCachorroGrande = getCachorroPorteGrande(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoBanho(clienteEntity, petCachorroGrande);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petCachorroGrande);

        assertNotNull(valorDoPedido);
        assertEquals(90.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaBanhoDeGatoPequeno() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petGatoPequeno = getGatoPortePequeno(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoBanho(clienteEntity, petGatoPequeno);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petGatoPequeno);

        assertNotNull(valorDoPedido);
        assertEquals(25.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaBanhoDeGatoMedio() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petGatoMedio = getGatoPorteMedio(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoBanho(clienteEntity, petGatoMedio);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petGatoMedio);

        assertNotNull(valorDoPedido);
        assertEquals(45.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaBanhoDeGatoGrande() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petGatoGrande = getGatoPorteGrande(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoBanho(clienteEntity, petGatoGrande);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petGatoGrande);

        assertNotNull(valorDoPedido);
        assertEquals(60.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaTosaDeCachorroComPelagemCurta() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petCachorroPelagemCurto = getCachorroPelagemCurto(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoTosa(clienteEntity, petCachorroPelagemCurto);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petCachorroPelagemCurto);

        assertNotNull(valorDoPedido);
        assertEquals(35.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaTosaDeCachorroComPelagemMedia() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petCachorroPelagemMedio = getCachorroPelagemMedio(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoTosa(clienteEntity, petCachorroPelagemMedio);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petCachorroPelagemMedio);

        assertNotNull(valorDoPedido);
        assertEquals(55.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaTosaDeCachorroComPelagemLonga() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petCachorroPelagemLongo = getCachorroPelagemLongo(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoTosa(clienteEntity, petCachorroPelagemLongo);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petCachorroPelagemLongo);

        assertNotNull(valorDoPedido);
        assertEquals(70.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaTosaDeGatoComPelagemCurta() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petGatoPelagemCurto = getGatoPelagemCurto(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoTosa(clienteEntity, petGatoPelagemCurto);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petGatoPelagemCurto);

        assertNotNull(valorDoPedido);
        assertEquals(30.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaTosaDeGatoComPelagemMedia() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petGatoPelagemMedio = getGatoPelagemMedio(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoTosa(clienteEntity, petGatoPelagemMedio);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petGatoPelagemMedio);

        assertNotNull(valorDoPedido);
        assertEquals(45.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaTosaDeGatoComPelagemLonga() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petGatoPelagemLongo = getGatoPelagemLongo(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoTosa(clienteEntity, petGatoPelagemLongo);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petGatoPelagemLongo);

        assertNotNull(valorDoPedido);
        assertEquals(55.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaCorteDeUnhaDeCachorroPortePequeno() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petCachorroPortePequeno = getCachorroPortePequeno(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoCorteDeUnha(clienteEntity, petCachorroPortePequeno);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petCachorroPortePequeno);

        assertNotNull(valorDoPedido);
        assertEquals(15.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaCorteDeUnhaDeCachorroPorteMedio() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petCachorroPorteMedio = getCachorroPorteMedio(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoCorteDeUnha(clienteEntity, petCachorroPorteMedio);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petCachorroPorteMedio);

        assertNotNull(valorDoPedido);
        assertEquals(20.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaCorteDeUnhaDeCachorroPorteGrande() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petCachorroPorteGrande = getCachorroPorteGrande(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoCorteDeUnha(clienteEntity, petCachorroPorteGrande);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petCachorroPorteGrande);

        assertNotNull(valorDoPedido);
        assertEquals(45.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaCorteDeUnhaDeGatoPortePequeno() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petGatoPortePequeno = getGatoPortePequeno(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoCorteDeUnha(clienteEntity, petGatoPortePequeno);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petGatoPortePequeno);

        assertNotNull(valorDoPedido);
        assertEquals(25.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaCorteDeUnhaDeGatoPorteMedio() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petGatoPorteMedio = getGatoPorteMedio(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoCorteDeUnha(clienteEntity, petGatoPorteMedio);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petGatoPorteMedio);

        assertNotNull(valorDoPedido);
        assertEquals(40.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaCorteDeUnhaDeGatoPorteGrande() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petGatoPorteGrande = getGatoPorteGrande(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoCorteDeUnha(clienteEntity, petGatoPorteGrande);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petGatoPorteGrande);

        assertNotNull(valorDoPedido);
        assertEquals(50.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaAdestramentoDeCachorro() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petCachorro = getCachorroPorteGrande(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoAdestramento(clienteEntity, petCachorro);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petCachorro);

        assertNotNull(valorDoPedido);
        assertEquals(250.0, valorDoPedido, DELTA);
    }

    @Test
    public void deveTestarCalcularValorDoPedidoParaAdestramentoDeGato() {
        ClienteEntity clienteEntity = getClienteEntity();
        PetEntity petGato = getGatoPorteGrande(clienteEntity);
        PedidoEntity pedidoEntity = getPedidoAdestramento(clienteEntity, petGato);

        Double valorDoPedido = calculadoraService.calcularValorDoPedido(pedidoEntity, petGato);

        assertNotNull(valorDoPedido);
        assertEquals(120.0, valorDoPedido, DELTA);
    }


    private PedidoEntity getPedido(ClienteEntity clienteEntity, PetEntity petEntity) {
        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setIdPedido(80);
        pedidoEntity.setStatus(StatusPedido.ABERTO);
        pedidoEntity.setDataEHora(LocalDateTime.of(LocalDate.of(2022, 8, 2), LocalTime.of(21, 20)));
        pedidoEntity.setValor(0.0);
        pedidoEntity.setCliente(clienteEntity);
        pedidoEntity.setPet(petEntity);
        return pedidoEntity;
    }

    private PedidoEntity getPedidoBanho(ClienteEntity clienteEntity, PetEntity petEntity) {
        PedidoEntity pedidoEntity = getPedido(clienteEntity, petEntity);
        pedidoEntity.setServico(TipoServico.BANHO);
        return pedidoEntity;
    }

    private PedidoEntity getPedidoTosa(ClienteEntity clienteEntity, PetEntity petEntity) {
        PedidoEntity pedidoEntity = getPedido(clienteEntity, petEntity);
        pedidoEntity.setServico(TipoServico.TOSA);
        return pedidoEntity;
    }

    private PedidoEntity getPedidoCorteDeUnha(ClienteEntity clienteEntity, PetEntity petEntity) {
        PedidoEntity pedidoEntity = getPedido(clienteEntity, petEntity);
        pedidoEntity.setServico(TipoServico.CORTE_DE_UNHA);
        return pedidoEntity;
    }

    private PedidoEntity getPedidoAdestramento(ClienteEntity clienteEntity, PetEntity petEntity) {
        PedidoEntity pedidoEntity = getPedido(clienteEntity, petEntity);
        pedidoEntity.setServico(TipoServico.ADESTRAMENTO);
        return pedidoEntity;
    }

    private PetEntity getPet(ClienteEntity clienteEntity) {
        PetEntity petEntity = new PetEntity();
        petEntity.setIdPet(42);
        petEntity.setNome("Riolu");
        petEntity.setRaca("SRD");
        petEntity.setCliente(clienteEntity);
        return petEntity;
    }

    private PetEntity getCachorro(ClienteEntity clienteEntity) {
        PetEntity petEntity = getPet(clienteEntity);
        petEntity.setTipoPet(TipoPet.CACHORRO);
        return petEntity;
    }

    private PetEntity getCachorroPortePequeno(ClienteEntity clienteEntity) {
        PetEntity pet = getCachorro(clienteEntity);
        pet.setPorte(PortePet.PEQUENO);
        return pet;
    }

    private PetEntity getCachorroPorteMedio(ClienteEntity clienteEntity) {
        PetEntity pet = getCachorro(clienteEntity);
        pet.setPorte(PortePet.MEDIO);
        return pet;
    }

    private PetEntity getCachorroPorteGrande(ClienteEntity clienteEntity) {
        PetEntity pet = getCachorro(clienteEntity);
        pet.setPorte(PortePet.GRANDE);
        return pet;
    }

    private PetEntity getCachorroPelagemCurto(ClienteEntity clienteEntity) {
        PetEntity pet = getCachorro(clienteEntity);
        pet.setPelagem(PelagemPet.CURTO);
        return pet;
    }

    private PetEntity getCachorroPelagemMedio(ClienteEntity clienteEntity) {
        PetEntity pet = getCachorro(clienteEntity);
        pet.setPelagem(PelagemPet.MEDIO);
        return pet;
    }

    private PetEntity getCachorroPelagemLongo(ClienteEntity clienteEntity) {
        PetEntity pet = getCachorro(clienteEntity);
        pet.setPelagem(PelagemPet.LONGO);
        return pet;
    }

    private PetEntity getGato(ClienteEntity clienteEntity) {
        PetEntity petEntity = getPet(clienteEntity);
        petEntity.setTipoPet(TipoPet.GATO);
        return petEntity;
    }

    private PetEntity getGatoPortePequeno(ClienteEntity clienteEntity) {
        PetEntity pet = getGato(clienteEntity);
        pet.setPorte(PortePet.PEQUENO);
        return pet;
    }

    private PetEntity getGatoPorteMedio(ClienteEntity clienteEntity) {
        PetEntity pet = getGato(clienteEntity);
        pet.setPorte(PortePet.MEDIO);
        return pet;
    }

    private PetEntity getGatoPorteGrande(ClienteEntity clienteEntity) {
        PetEntity pet = getGato(clienteEntity);
        pet.setPorte(PortePet.GRANDE);
        return pet;
    }

    private PetEntity getGatoPelagemCurto(ClienteEntity clienteEntity) {
        PetEntity pet = getGato(clienteEntity);
        pet.setPelagem(PelagemPet.CURTO);
        return pet;
    }

    private PetEntity getGatoPelagemMedio(ClienteEntity clienteEntity) {
        PetEntity pet = getGato(clienteEntity);
        pet.setPelagem(PelagemPet.MEDIO);
        return pet;
    }

    private PetEntity getGatoPelagemLongo(ClienteEntity clienteEntity) {
        PetEntity pet = getGato(clienteEntity);
        pet.setPelagem(PelagemPet.LONGO);
        return pet;
    }

    private ClienteEntity getClienteEntity() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setIdCliente(10);
        clienteEntity.setNome("Luke Skywalker");
        clienteEntity.setQuantidadeDePedidos(0);
        clienteEntity.setValorPagamento(0.0);
        clienteEntity.setEmail("ImYourSon@skywalker.com");
        return clienteEntity;
    }
}
