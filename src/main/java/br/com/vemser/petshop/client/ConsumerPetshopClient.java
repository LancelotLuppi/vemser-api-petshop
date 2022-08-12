package br.com.vemser.petshop.client;

import br.com.vemser.petshop.dto.balancomensal.BalancoMensalDTO;
import br.com.vemser.petshop.dto.login.LoginCreateDTO;
import br.com.vemser.petshop.dto.pedidomensal.PedidoMensalDto;
import feign.HeaderMap;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.Map;

@FeignClient(value="consumer-petshop", url="localhost:8070")
@Headers({"Content-Type: application/json"})
public interface ConsumerPetshopClient {

    @RequestLine("GET /balanco-mensal/mes-atual")
    BalancoMensalDTO getMesAtual(@HeaderMap Map<String, String> headerMap);

    @RequestLine("GET /balanco-mensal/mes/{mes}/ano/{ano}")
    BalancoMensalDTO getByMesAno(@Param("mes") Integer mes, @Param("ano") Integer ano, @HeaderMap Map<String, String> headerMap);

    @RequestLine("GET /balanco-mensal/pedidos-mes-atual")
    PedidoMensalDto getPedidosMesAtual(@HeaderMap Map<String, String> headerMap);

    @RequestLine("GET /balanco-mensal/pedidos/mes/{mes}/ano/{ano}")
    PedidoMensalDto getPedidosByMesAno(@Param("mes") Integer mes, @Param("ano") Integer ano, @HeaderMap Map<String, String> headerMap);

    @RequestLine("POST /auth")
    String auth(LoginCreateDTO loginCreateDTO);
}
