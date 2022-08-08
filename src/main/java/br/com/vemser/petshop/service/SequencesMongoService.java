package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.SequencesMongoEntity;
import br.com.vemser.petshop.repository.SequencesMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SequencesMongoService {
    private final SequencesMongoRepository sequencesMongoRepository;
    private final MongoTemplate mongoTemplate;

    public Integer getIdByEntidade(String entidade) {
        Optional<SequencesMongoEntity> sequencesMongoEntityOptional = sequencesMongoRepository.findSequencesMongoEntitiesByEntidade(entidade);
        if (sequencesMongoEntityOptional.isPresent()) {
            Integer atual = sequencesMongoEntityOptional.get().getAtual() + 1;
            sequencesMongoEntityOptional.get().setAtual(atual);
            mongoTemplate.save(sequencesMongoEntityOptional.get());
            return atual;
        } else {
            SequencesMongoEntity sequencesMongoEntity = new SequencesMongoEntity();
            sequencesMongoEntity.setEntidade(entidade);
            sequencesMongoEntity.setAtual(1);
            sequencesMongoRepository.save(sequencesMongoEntity);
            return sequencesMongoEntity.getAtual();
        }
    }
}
