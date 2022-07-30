package br.com.vemser.petshop.service;


import br.com.vemser.petshop.dto.login.LoginCreateDTO;
import br.com.vemser.petshop.dto.login.LoginDTO;
import br.com.vemser.petshop.dto.usuario.UsuarioDTO;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;

    private final static String NOT_FOUND_MESSAGE = "{idCliente} não encontrado";

    public Optional<UsuarioEntity> findByLoginAndSenha(String login, String senha){
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }

    public UsuarioEntity findById(Integer idUsuario) throws EntidadeNaoEncontradaException{
        return usuarioRepository.findById(idUsuario).stream()
                .findFirst()
                .orElseThrow(() -> new EntidadeNaoEncontradaException(NOT_FOUND_MESSAGE));
    }

    public Optional<UsuarioEntity> findByLogin(String login){
        return usuarioRepository.findByLogin(login);
    }

    public LoginDTO cadastro(LoginCreateDTO loginCreateDTO){
        UsuarioEntity novoUser = returnEntity(loginCreateDTO);
        novoUser.setSenha(new Argon2PasswordEncoder().encode(loginCreateDTO.getSenha()));
        UsuarioEntity user = usuarioRepository.save(novoUser);

        return new LoginDTO(user.getIdUsuario(), user.getUsername());
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

    private LoginDTO returnDTO(UsuarioEntity entity) {
        return objectMapper.convertValue(entity, LoginDTO.class);
    }

    private UsuarioEntity returnEntity(LoginCreateDTO dto) {
        return objectMapper.convertValue(dto, UsuarioEntity.class);
    }
}
