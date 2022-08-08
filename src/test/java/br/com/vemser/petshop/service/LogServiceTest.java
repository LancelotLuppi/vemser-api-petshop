package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.LogEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {
    @InjectMocks
    private LogService logService;
    @Mock
    private MongoTemplate mongoTemplate;

    @Test
    public void deveTestarInfoComSucesso() {
        LogEntity log = getLogEntity();
        String message = "teste";

        String retorno = logService.info(message);

        assertEquals("teste", retorno);
    }

    public LogEntity getLogEntity() {
        LogEntity log = new LogEntity();
        log.setId(2);
        log.setDate(new Date());
        log.setLevel("INFO");
        log.setMessage("apenas um teste");
        return log;
    }
}
