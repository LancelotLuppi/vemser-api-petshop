package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.EntityTestMongo;
import br.com.vemser.petshop.repository.MongoTesteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoTestService {
    private final MongoTesteRepository mongoTesteRepository;

    public List<EntityTestMongo> findAll() {
        return mongoTesteRepository.findAll();
    }
}
