package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.entity.BalancoMensalEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.service.BalancoMensalService;
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

    @GetMapping("/mes-atual")
    public ResponseEntity<BalancoMensalEntity> getMesAtual() throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(balancoMensalService.getBalancoMesAtual(), HttpStatus.OK);
    }

    @GetMapping("/mes-ano")
    public ResponseEntity<BalancoMensalEntity> getByMesAno(Integer mes,  Integer ano) throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(balancoMensalService.getBalancoByMesAndAno(mes, ano), HttpStatus.OK);
    }
}
