package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.log.LogDTO;
import br.com.vemser.petshop.entity.LogEntity;
import br.com.vemser.petshop.repository.LogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final MongoTemplate mongoTemplate;
    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    public String info(String message) {
        LogEntity log = new LogEntity();
        log.setLevel("INFO");
        log.setMessage(message);
        log.setDate(new Date());
        mongoTemplate.save(log, "logs");

        return message;
    }

    public List<LogDTO> findAll(){
        return logRepository.findAll().stream()
                .map(logEntity -> entityToDto(logEntity))
                .toList();
    }

    public LogDTO entityToDto(LogEntity logEntity) {
        return objectMapper.convertValue(logEntity, LogDTO.class);
    }
}
