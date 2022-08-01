package br.com.vemser.petshop.documentation;

import br.com.vemser.petshop.dto.login.LoginCreateDTO;
import br.com.vemser.petshop.dto.login.LoginDTO;
import br.com.vemser.petshop.dto.login.LoginStatusDTO;
import br.com.vemser.petshop.dto.login.LoginUpdateDTO;
import br.com.vemser.petshop.enums.EnumDesativar;
import br.com.vemser.petshop.enums.TipoCargo;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Set;

public interface AuthDocumentation {

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
    String auth(@RequestBody LoginCreateDTO login);

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
    ResponseEntity<LoginDTO> cadastro(@RequestBody @Valid LoginCreateDTO loginCreateDTO) throws RegraDeNegocioException;

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
    ResponseEntity<LoginDTO> getLoggedUser() throws EntidadeNaoEncontradaException;

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
    ResponseEntity<String> putLoggedPassword(String newPassword);

    @Operation(summary = "Alterar username do usuário logado", description = "Altera o username do usuário " +
            "que está logado no momento, será verificado se já existe o username informado no sistema.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Username alterado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Username já existe"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<LoginDTO> putLoggedUsername(LoginUpdateDTO loginUpdateDTO) throws RegraDeNegocioException;

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
    ResponseEntity<LoginStatusDTO> putStatusLogin(Integer idUsuario, @RequestParam EnumDesativar desativarUsuario) throws EntidadeNaoEncontradaException;

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
    ResponseEntity<LoginDTO> updateCargo(@RequestParam(value = "idUsuario") Integer idUsuario, @RequestParam(value = "cargos") Set<TipoCargo> cargos) throws EntidadeNaoEncontradaException;

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
    void deleteUsuario(Integer idUsuario);
}
