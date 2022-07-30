package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.dto.login.LoginCreateDTO;
import br.com.vemser.petshop.dto.login.LoginDTO;
import br.com.vemser.petshop.dto.login.LoginUpdateDTO;
import br.com.vemser.petshop.dto.usuario.UsuarioDTO;
import br.com.vemser.petshop.enums.EnumDesativar;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.security.TokenService;
import br.com.vemser.petshop.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public String auth(@RequestBody @Valid LoginCreateDTO login) throws RegraDeNegocioException{
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        login.getLogin(),
                        login.getSenha()
                );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        return tokenService.generateToken(authentication);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<LoginDTO> cadastro(@RequestBody @Valid LoginCreateDTO loginCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.cadastro(loginCreateDTO));
    }

    @GetMapping("/logged")
    public ResponseEntity<LoginDTO> getLoggedUser() throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(usuarioService.getLoggedUser());
    }

    @PutMapping("/new-password")
    public ResponseEntity<String> putLoggedPassword(String newPassword) {
        return ResponseEntity.ok(usuarioService.updateLoggedPassword(newPassword));
    }

    @PutMapping("/desativar/{idUsuario}")
    public ResponseEntity<String> desativarUsuario(@PathVariable("idUsuario") @Valid Integer idUsuario, @RequestParam EnumDesativar desativarUsuario) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        return ResponseEntity.ok(usuarioService.desativarConta(idUsuario, desativarUsuario));
    }
}
