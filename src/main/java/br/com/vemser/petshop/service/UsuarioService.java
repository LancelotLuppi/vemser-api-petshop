package br.com.vemser.petshop.service;


import br.com.vemser.petshop.dto.LoginDTO;
import br.com.vemser.petshop.entity.UsuarioEntity;
import br.com.vemser.petshop.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;

    public Optional<UsuarioEntity> findByLoginAndSenha(String login, String senha){
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }

    public Optional<UsuarioEntity> findById(Integer idUsuario){
        return usuarioRepository.findById(idUsuario);
    }

    public Optional<UsuarioEntity> findByLogin(String login){
        return usuarioRepository.findByLogin(login);
    }

    public LoginDTO cadastro(LoginDTO loginDTO){
        UsuarioEntity novoUser = objectMapper.convertValue(loginDTO, UsuarioEntity.class);
        novoUser.setSenha(new Argon2PasswordEncoder().encode(loginDTO.getSenha()));
        usuarioRepository.save(novoUser);
        return loginDTO;
    }
}
