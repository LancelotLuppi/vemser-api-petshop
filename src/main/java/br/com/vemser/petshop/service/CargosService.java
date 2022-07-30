package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.CargoEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.TipoCargo;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.CargoRepository;
import br.com.vemser.petshop.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class CargosService {

    private final UsuarioService usuarioService;
    private final CargoRepository cargoRepository;
    private final UsuarioRepository usuarioRepository;

    public void updateCargo(Integer idUsuario, TipoCargo tipoCargo) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        UsuarioEntity user = usuarioService.findById(idUsuario);
        CargoEntity cargo = returnByEnum(tipoCargo);

        if (user.getCargos().stream().anyMatch((Predicate<? super CargoEntity>) cargo)) {
            throw new RegraDeNegocioException("O usuario já tem esse cargo!");
        }

        user.setCargos(Set.of(cargo));
        usuarioRepository.save(user);
    }

    public CargoEntity returnByEnum(TipoCargo tipoCargo) throws EntidadeNaoEncontradaException {
        if(tipoCargo.equals(TipoCargo.ADMIN)) {
            return findById(0);
        } else if(tipoCargo.equals(TipoCargo.ATENDENTE)) {
            return findById(2);
        } else if(tipoCargo.equals(TipoCargo.TOSADOR)) {
            return findById(1);
        } else if(tipoCargo.equals(TipoCargo.USUARIO)) {
            return findById(3);
        }
        return null;
    }

    public CargoEntity findById(Integer idCargo) throws EntidadeNaoEncontradaException {
        return cargoRepository.findById(idCargo)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cargo não encontrado"));
    }


}
