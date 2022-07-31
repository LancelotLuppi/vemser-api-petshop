package br.com.vemser.petshop.service;


import br.com.vemser.petshop.dto.login.LoginCreateDTO;
import br.com.vemser.petshop.dto.login.LoginDTO;
import br.com.vemser.petshop.dto.login.LoginStatusDTO;
import br.com.vemser.petshop.dto.login.LoginUpdateDTO;
import br.com.vemser.petshop.entity.CargoEntity;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.enums.EnumDesativar;
import br.com.vemser.petshop.enums.TipoCargo;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.repository.CargoRepository;
import br.com.vemser.petshop.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;



@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final CargoRepository cargoRepository;

    private final static String NOT_FOUND_MESSAGE = "{idCliente} não encontrado";

    public Optional<UsuarioEntity> findByLoginAndSenha(String login, String senha){
        return usuarioRepository.findByUsernameAndSenha(login, senha);
    }

    public UsuarioEntity findById(Integer idUsuario) throws EntidadeNaoEncontradaException{
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    public Optional<UsuarioEntity> findByUsername(String login){
        return usuarioRepository.findByUsername(login);
    }

    public LoginDTO cadastro(LoginCreateDTO loginCreateDTO) throws RegraDeNegocioException {
        verificaUsername(loginCreateDTO.getUsername());
        UsuarioEntity novoUser = returnEntity(loginCreateDTO);
        novoUser.setSenha(new Argon2PasswordEncoder().encode(loginCreateDTO.getSenha()));
        novoUser.setAtivo(true);

        novoUser.setCargos(Set.of(cargoRepository.findById(2).get()));
        usuarioRepository.save(novoUser);

        return returnDTO(novoUser);
    }

    public LoginDTO cadastroAdmin(LoginCreateDTO loginCreateDTO) throws RegraDeNegocioException {
        verificaUsername(loginCreateDTO.getUsername());
        UsuarioEntity novoUser = returnEntity(loginCreateDTO);
        novoUser.setSenha(new Argon2PasswordEncoder().encode(loginCreateDTO.getSenha()));
        novoUser.setAtivo(true);

        novoUser.setCargos(Set.of(cargoRepository.findById(1).get()));
        usuarioRepository.save(novoUser);

        return returnDTO(novoUser);
    }

    public LoginDTO updateCargos(Integer idUsuario, Set<TipoCargo> cargos) throws EntidadeNaoEncontradaException {
        UsuarioEntity user = findById(idUsuario);
        user.getCargos().clear();
        user.getCargos().addAll(cargos.stream()
                .map(cargo -> cargoRepository.findById(cargo.getTipo()).get())
                .toList());
        usuarioRepository.save(user);
        return returnDTO(user);
    }

    public LoginStatusDTO desativarConta(Integer idUsuario, EnumDesativar status) throws EntidadeNaoEncontradaException {
        UsuarioEntity usuario = findById(idUsuario);

        usuario.setAtivo(!status.equals(EnumDesativar.DESATIVAR));

        return objectMapper.convertValue(usuario, LoginStatusDTO.class);
    }

    public LoginDTO updateLoggedUsername(LoginUpdateDTO loginUpdate) throws RegraDeNegocioException {
        verificaUsername(loginUpdate.getUsername());
        UsuarioEntity usuarioLogado = usuarioRepository.findById(getIdLoggedUser()).get();
        usuarioLogado.setUsername(loginUpdate.getUsername());
        return returnDTO(usuarioLogado);
    }

    public LoginDTO getLoggedUser() throws EntidadeNaoEncontradaException {
        return findByIdUser(getIdLoggedUser());
    }

    public String updateLoggedPassword(String newPassword) {
        Optional<UsuarioEntity> user = usuarioRepository.findById(getIdLoggedUser());
        UsuarioEntity userEntity = user.get();
        userEntity.setSenha(new Argon2PasswordEncoder().encode(newPassword));
        usuarioRepository.save(userEntity);
        return "Senha alterada com sucesso!";
    }

    public Integer getIdLoggedUser(){
        return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public LoginDTO findByIdUser(Integer idUsuario) throws EntidadeNaoEncontradaException {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("usuario não encontrado"));
        return returnDTO(usuario);
    }

    public void verificaUsername(String username) throws RegraDeNegocioException {
        if(usuarioRepository.findByUsername(username).isPresent()) {
            throw new RegraDeNegocioException("Este usuário já existe!");
        }
    }

    public LoginDTO returnDTO(UsuarioEntity entity) {
        LoginDTO dto = objectMapper.convertValue(entity, LoginDTO.class);
        dto.setCargos(entity.getCargos().stream()
                .map(CargoEntity::getNome).toList());
        return dto;
    }

    private UsuarioEntity returnEntity(LoginCreateDTO dto) {
        return objectMapper.convertValue(dto, UsuarioEntity.class);
    }
}
