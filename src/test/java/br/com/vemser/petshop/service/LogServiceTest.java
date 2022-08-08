package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.log.LogDTO;
import br.com.vemser.petshop.entity.LogEntity;
import br.com.vemser.petshop.repository.LogRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {
    @InjectMocks
    private LogService logService;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private LogRepository logRepository;
    private ObjectMapper objectMapper = new ObjectMapper();


    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(logService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarInfoComSucesso() {
        LogEntity log = getLogEntity();
        String message = "teste";

        String retorno = logService.info(message);

        assertEquals("teste", retorno);
    }

    @Test
    public void deveTestarFindAllComSucesso() {
        LogEntity log = getLogEntity();


        when(logRepository.findAll()).thenReturn(List.of(log));
        List<LogDTO> retorno = logService.findAll();

        assertNotNull(retorno);
        assertEquals( log.getMessage(), retorno.get(0).getMessage());
        assertEquals( log.getDate(), retorno.get(0).getDate());
        assertEquals( log.getLevel(), retorno.get(0).getLevel());
    }

    public LogEntity getLogEntity() {
        LogEntity log = new LogEntity();
        log.setId(new ObjectId());
        log.setDate(new Date());
        log.setLevel("INFO");
        log.setMessage("apenas um teste");
        return log;
    }
}
