package br.com.vemser.petshop.controller;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class AuthController {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    @Operation(summary = "Recuperar token", description = "Após realizar o 'login' informando " +
            "o username e senha corretos, retornará um token de autenticação para poder acessar " +
            "os outros endpoints da aplicação. Obs: o login do example é um ADMIN caso queiram testar algo :P")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o token"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
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

    @Operation(summary = "Realizar cadastro de login", description = "Informe um usuário e senha para " +
            "ser cadastrado em nosso sistema, podendo haver apenas usernames únicos no banco de dados. " +
            "O cargo padrão definido após todos os cadastros é de USER, o ADMIN irá cuidar para alterar " +
            "o cargo de alguém caso necessário." +
            "Não se preocupe, a senha é devidamente encriptada :)")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna informação do usuário"),
                        @ApiResponse(responseCode = "400", description = "Username informado já existe"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @PostMapping("/cadastro")
    public ResponseEntity<LoginDTO> cadastro(@Valid @RequestBody LoginCreateDTO loginCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.cadastro(loginCreateDTO));
    }


    @Operation(summary = "Retornar usuário logado", description = "Retorna as informações do usuário " +
            "que está logado no momento.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna informação do usuário"),
                        @ApiResponse(responseCode = "400", description = "Username informado já existe"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @GetMapping("/logged")
    public ResponseEntity<LoginDTO> getLoggedUser() throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(usuarioService.getLoggedUser());
    }


    @Operation(summary = "Alterar senha do usuário logado", description = "Informe a nova senha " +
            "desejada, caso seja alterada com sucesso, uma mensagem de sucesso será retornado para " +
            "evitar expor os dados do usuário.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @PutMapping("/logged/password")
    public ResponseEntity<String> putLoggedPassword(String newPassword) {
        return ResponseEntity.ok(usuarioService.updateLoggedPassword(newPassword));
    }


    @Operation(summary = "Alterar username do usuário logado", description = "Altera o username do usuário " +
            "que está logado no momento, será verificado se já existe o username informado no sistema.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Username alterado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Username já existe"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @PutMapping("/logged/username")
    public ResponseEntity<LoginDTO> putLoggedUsername(LoginUpdateDTO loginUpdateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.updateLoggedUsername(loginUpdateDTO));
    }


    @Operation(summary = "Alterar status da conta", description = "Altera o status de uma conta para " +
            "DESATIVADO ou ATIVADO, apenas o ADMIN tem acesso a este endpoint")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Username já existe"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @PutMapping("/status/{idUsuario}")
    public ResponseEntity<LoginStatusDTO> putStatusLogin(@PathVariable("idUsuario") @Valid Integer idUsuario, @RequestParam EnumDesativar desativarUsuario) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(usuarioService.desativarConta(idUsuario, desativarUsuario));
    }


    @Operation(summary = "Alterar cargos da conta", description = "Altera o cargo de uma conta para as informadas na " +
            "requisição, sendo elas: USER, TOSADOR, ATENDENTE, ADMIN. Apenas o ADMIN tem acesso a este endpoint")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Cargos alterados com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Username já existe"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @PutMapping("/cargos")
    public ResponseEntity<LoginDTO> updateCargo(@RequestParam(value = "idUsuario") Integer idUsuario, @RequestParam(value = "cargos") Set<TipoCargo> cargos) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(usuarioService.updateCargos(idUsuario, cargos));
    }

    @Operation(summary = "Deletar usuário", description = "Deleta um usuário do sistema E TODOS SEUS DADOS relacionados," +
            " recebendo seu ID como parâmetro. Apenas o ADMIN tem acesso a este endpoint.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Username já existe"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    @DeleteMapping
    public void deleteUsuario(Integer idUsuario) {
        usuarioService.deleteUser(idUsuario);
    }
}
