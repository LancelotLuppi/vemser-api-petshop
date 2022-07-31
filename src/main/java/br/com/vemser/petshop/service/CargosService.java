package br.com.vemser.petshop.service;

import br.com.vemser.petshop.dto.login.LoginDTO;
import br.com.vemser.petshop.entity.CargoEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.TipoCargo;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.repository.CargoRepository;
import br.com.vemser.petshop.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CargosService {
    private final CargoRepository cargoRepository;

    public CargoEntity returnByEnum(TipoCargo tipoCargo) throws EntidadeNaoEncontradaException {
        if(tipoCargo.equals(TipoCargo.ADMIN)) {
            return findById(1);
        } else if(tipoCargo.equals(TipoCargo.ATENDENTE)) {
            return findById(4);
        } else if(tipoCargo.equals(TipoCargo.TOSADOR)) {
            return findById(3);
        } else if(tipoCargo.equals(TipoCargo.USUARIO)) {
            return findById(2);
        }
        return null;
    }

    public CargoEntity findById(Integer idCargo) throws EntidadeNaoEncontradaException {
        return cargoRepository.findById(idCargo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cargo não encontrado"));
    }
}
