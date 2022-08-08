package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.dto.log.LogDTO;
import br.com.vemser.petshop.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @GetMapping
    public ResponseEntity<List<LogDTO>> findAll(){
        return new ResponseEntity<>(logService.findAll(), HttpStatus.OK);
    }
}
