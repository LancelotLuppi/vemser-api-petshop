package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.entity.BalancoMensalEntity;
import br.com.vemser.petshop.service.BalancoMensalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balanco-mensal")
@RequiredArgsConstructor
public class BalancoMensalController {
    private final BalancoMensalService balancoMensalService;

    @GetMapping("/mes-atual")
    public ResponseEntity<BalancoMensalEntity> getMesAtual() {
        return new ResponseEntity<>(balancoMensalService.getBalancoMesAtual(), HttpStatus.OK);
    }
}
