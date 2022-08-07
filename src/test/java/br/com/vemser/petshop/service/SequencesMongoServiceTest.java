package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.SequencesMongoEntity;
import br.com.vemser.petshop.repository.SequencesMongoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SequencesMongoServiceTest {
    @InjectMocks
    private SequencesMongoService sequencesMongoService;
    @Mock
    private SequencesMongoRepository sequencesMongoRepository;
    @Mock
    private MongoTemplate mongoTemplate;

    @Test
    public void deveTestarGetIdByEntidadeComSequencePresente() {
        SequencesMongoEntity sequencesMongo = getSequence();

        when(sequencesMongoRepository.findSequencesMongoEntitiesByEntidade(anyString()))
                .thenReturn(Optional.of(sequencesMongo));

        sequencesMongoService.getIdByEntidade(anyString());

        assertEquals(3, sequencesMongo.getAtual().intValue());
    }

    @Test
    public void deveTestarGetIdByEntidadeSemSequencePresente() {
        SequencesMongoEntity sequencesMongo = getSequence();

        when(sequencesMongoRepository.findSequencesMongoEntitiesByEntidade(anyString()))
                .thenReturn(Optional.empty());
        when(sequencesMongoRepository.save(any())).thenReturn(sequencesMongo);

        sequencesMongoService.getIdByEntidade(anyString());

        assertEquals(2, sequencesMongo.getAtual().intValue());
    }

    public SequencesMongoEntity getSequence() {
        SequencesMongoEntity sequencesMongoEntity = new SequencesMongoEntity();
        sequencesMongoEntity.setIdSequence(2);
        sequencesMongoEntity.setAtual(2);
        sequencesMongoEntity.setEntidade("sequence");
        return  sequencesMongoEntity;
    }
}
