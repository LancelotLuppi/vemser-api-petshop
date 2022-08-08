package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.dto.balancomensal.BalancoMensalDTO;
import br.com.vemser.petshop.dto.pedidomensal.PedidoMensalDto;
import br.com.vemser.petshop.entity.BalancoMensalEntity;
import br.com.vemser.petshop.entity.PedidoMensalEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.service.BalancoMensalService;
import br.com.vemser.petshop.service.PedidosMensalService;
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

    @GetMapping("/mes-atual")
    public ResponseEntity<BalancoMensalDTO> getMesAtual() throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(balancoMensalService.getBalancoMesAtual(), HttpStatus.OK);
    }

    @GetMapping("/mes-ano")
    public ResponseEntity<BalancoMensalDTO> getByMesAno(Integer mes,  Integer ano) throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(balancoMensalService.getBalancoByMesAndAno(mes, ano), HttpStatus.OK);
    }

    @GetMapping("/pedidos-mes-atual")
    public ResponseEntity<PedidoMensalDto> getPedidosMesAtual() throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(pedidosMensalService.getPedidoMesAtual());
    }

    @GetMapping("/pedidos-mes-ano")
    public ResponseEntity<PedidoMensalDto> getPedidosByMesAno(Integer mes,  Integer ano) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(pedidosMensalService.getPedidoByMesAndAno(mes, ano));
    }
}
