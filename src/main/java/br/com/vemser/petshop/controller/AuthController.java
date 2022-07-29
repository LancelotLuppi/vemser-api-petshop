package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.dto.LoginDTO;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.security.TokenService;
import br.com.vemser.petshop.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public String auth(@RequestBody @Valid LoginDTO login) throws RegraDeNegocioException{
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        login.getLogin(),
                        login.getSenha()
                );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String token = tokenService.generateToken(authentication);
        return token;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<LoginDTO> cadastro(@RequestBody @Valid LoginDTO loginDTO){
        return ResponseEntity.ok(usuarioService.cadastro(loginDTO));
    }
}
