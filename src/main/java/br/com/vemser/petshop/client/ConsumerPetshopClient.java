package br.com.vemser.petshop.client;

import br.com.vemser.petshop.dto.balancomensal.BalancoMensalDTO;
import br.com.vemser.petshop.dto.pedidomensal.PedidoMensalDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value="consumer-petshop", url="localhost:8070")
@Headers({"Content-Type: application/json"})
public interface ConsumerPetshopClient {

    @RequestLine("GET /balanco-mensal/mes-atual")
    BalancoMensalDTO getMesAtual();

    @RequestLine("GET /balanco-mensal/mes/{mes}/ano/{ano}")
    BalancoMensalDTO getByMesAno(@Param("mes") Integer mes, @Param("ano") Integer ano);

    @RequestLine("GET /balanco-mensal/pedidos-mes-atual")
    PedidoMensalDto getPedidosMesAtual();

    @RequestLine("GET /balanco-mensal/pedidos/mes/{mes}/ano/{ano}")
    PedidoMensalDto getPedidosByMesAno(@Param("mes") Integer mes, @Param("ano") Integer ano);
}
