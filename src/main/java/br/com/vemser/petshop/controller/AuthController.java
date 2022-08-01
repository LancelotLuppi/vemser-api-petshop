package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.documentation.AuthDocumentation;
import br.com.vemser.petshop.dto.login.LoginCreateDTO;
import br.com.vemser.petshop.dto.login.LoginDTO;
import br.com.vemser.petshop.dto.login.LoginStatusDTO;
import br.com.vemser.petshop.dto.login.LoginUpdateDTO;
import br.com.vemser.petshop.enums.EnumDesativar;
import br.com.vemser.petshop.enums.TipoCargo;
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
import java.util.Set;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController implements AuthDocumentation {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public String auth(@RequestBody @Valid LoginCreateDTO login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        login.getUsername(),
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

    @PutMapping("/logged/password")
    public ResponseEntity<String> putLoggedPassword(String newPassword) {
        return ResponseEntity.ok(usuarioService.updateLoggedPassword(newPassword));
    }

    @PutMapping("/logged/username")
    public ResponseEntity<LoginDTO> putLoggedUsername(LoginUpdateDTO loginUpdateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.updateLoggedUsername(loginUpdateDTO));
    }

    @PutMapping("/status/{idUsuario}")
    public ResponseEntity<LoginStatusDTO> putStatusLogin(@PathVariable("idUsuario") @Valid Integer idUsuario, @RequestParam EnumDesativar desativarUsuario) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(usuarioService.desativarConta(idUsuario, desativarUsuario));
    }

    @PutMapping("/cargos")
    public ResponseEntity<LoginDTO> updateCargo(@RequestParam(value = "idUsuario") Integer idUsuario, @RequestParam(value = "cargos") Set<TipoCargo> cargos) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(usuarioService.updateCargos(idUsuario, cargos));
    }

    @DeleteMapping
    public void deleteUsuario(Integer idUsuario) {
        usuarioService.deleteUser(idUsuario);
    }
}
