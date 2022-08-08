package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.dto.balancomensal.BalancoMensalDTO;
import br.com.vemser.petshop.dto.pedidomensal.PedidoMensalDto;
import br.com.vemser.petshop.entity.BalancoMensalEntity;
import br.com.vemser.petshop.entity.PedidoMensalEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.service.BalancoMensalService;
import br.com.vemser.petshop.service.PedidosMensalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balanco-mensal")
@RequiredArgsConstructor
@Validated
public class BalancoMensalController {
    private final BalancoMensalService balancoMensalService;

    private final PedidosMensalService pedidosMensalService;

    @Operation(summary = "Recuperar balanço mensal atual", description = "Retorna os dados do balanço " +
            "mensal do mês atual")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o balanço"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Balanço mensal não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @GetMapping("/mes-atual")
    public ResponseEntity<BalancoMensalDTO> getMesAtual() throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(balancoMensalService.getBalancoMesAtual(), HttpStatus.OK);
    }

    @Operation(summary = "Recuperar balanço mensal", description = "Retorna os dados do balanço " +
            "mensal a partir de um mês e ano informado")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o balanço"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Balanço mensal não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @GetMapping("/mes-ano")
    public ResponseEntity<BalancoMensalDTO> getByMesAno(Integer mes,  Integer ano) throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(balancoMensalService.getBalancoByMesAndAno(mes, ano), HttpStatus.OK);
    }

    @Operation(summary = "Recuperar pedidos do mês atual", description = "Retorna os pedidos gerados no mês atual")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna os pedidos"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Registro de pedidos não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @GetMapping("/pedidos-mes-atual")
    public ResponseEntity<PedidoMensalDto> getPedidosMesAtual() throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(pedidosMensalService.getPedidoMesAtual());
    }

    @Operation(summary = "Recuperar pedidos a partir do mês e ano", description = "Retorna os pedidos gerados no mês e ano informados")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna os pedidos"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Registro de pedidos não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @GetMapping("/pedidos-mes-ano")
    public ResponseEntity<PedidoMensalDto> getPedidosByMesAno(Integer mes,  Integer ano) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(pedidosMensalService.getPedidoByMesAndAno(mes, ano));
    }
}
