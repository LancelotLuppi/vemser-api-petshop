package br.com.vemser.petshop.security;

import br.com.vemser.petshop.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UsuarioService usuarioService;
}
