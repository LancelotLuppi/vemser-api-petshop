package br.com.vemser.petshop.documentation;

import br.com.vemser.petshop.dto.ClienteCreateDTO;
import br.com.vemser.petshop.dto.ClienteDTO;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.sql.SQLException;

public interface ClienteDocumentation {

    @Operation(summary = "Criar cadastro de cliente", description = "Adiciona as informações do cadastro no banco de dados" +
            ", gerando um ID único automaticamente")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<ClienteDTO> post(@Valid @RequestBody ClienteCreateDTO clienteDto) throws SQLException, RegraDeNegocioException;

    @Operation(summary = "Retornar informações de um cliente", description = "Busca no banco de dados por um " +
            "cadastro com o ID fornecido na requisição, retornando as informações caso encontre")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o cliente"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<ClienteDTO> getById(Integer idCliente) throws SQLException, RegraDeNegocioException;

    @Operation(summary = "Atualizar cadastro de cliente", description = "Atualiza as informações no banco de dados por uma nova, " +
            "baseando no ID do cliente para qual alterar")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Atualiza os dados"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<ClienteDTO> put(Integer idCliente, ClienteCreateDTO clienteDto) throws SQLException, RegraDeNegocioException;

    @Operation(summary = "Deletar cadastro de cliente", description = "Deleta as informações do cliente no banco de dados, " +
            "juntamente apagando as informações de (contato/pet/pedido) que estão ligados com o ID desse cliente")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    void delete(Integer idCliente) throws SQLException, RegraDeNegocioException;
}
