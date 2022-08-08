package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.LogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class LogService {
    private final MongoTemplate mongoTemplate;

    public String info(String message) {
        LogEntity log = new LogEntity();
        log.setLevel("INFO");
        log.setMessage(message);
        log.setDate(new Date());
        mongoTemplate.save(log, "logs");

        return message;
    }
}
