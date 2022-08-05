package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.entity.EntityTestMongo;
import br.com.vemser.petshop.service.MongoTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teste-mongo")
public class MongoTestController {
    private final MongoTestService mongoTestService;

    @GetMapping
    public ResponseEntity<List<EntityTestMongo>> findAll() {
        return new ResponseEntity<>(mongoTestService.findAll(), HttpStatus.OK);
    }
}
