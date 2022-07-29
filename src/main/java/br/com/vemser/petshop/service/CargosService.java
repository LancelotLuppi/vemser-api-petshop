package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.CargoEntity;
import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.TipoCargo;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;

public class CargosService {


    private UsuarioService usuarioService;

    public void updateCargo(UsuarioEntity usuario, TipoCargo cargo) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        if(usuario.getCargos().equals(cargo)){
            throw new RegraDeNegocioException("O User já está com esse cargo!");
        }
        if(usuario.getCargos().equals(TipoCargo.USUARIO)){

        }

    }


}
