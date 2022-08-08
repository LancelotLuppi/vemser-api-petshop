package br.com.vemser.petshop.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {
    @InjectMocks
    private LogService logService;
    @Mock
    private MongoTemplate mongoTemplate;

    @Test
    public void deveTestarInfoComSucesso() {
        String message = "teste";

        String retorno = logService.info(message);

        assertEquals("teste", retorno);
    }
}
