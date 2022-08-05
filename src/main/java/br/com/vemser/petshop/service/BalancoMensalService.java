package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.BalancoMensalEntity;
import br.com.vemser.petshop.repository.BalancoMensalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BalancoMensalService {
    private final BalancoMensalRepository balancoMensalRepository;

    public BalancoMensalEntity getBalancoMesAtual(){
        LocalDate localDate = LocalDate.now();

        return balancoMensalRepository.findBalancoMensalEntitiesByMesAndAno(localDate.getMonthValue(), localDate.getYear());
    }
}
