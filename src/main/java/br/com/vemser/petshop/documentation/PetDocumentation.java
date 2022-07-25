package br.com.vemser.petshop.documentation;

import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.PetCreateDTO;
import br.com.vemser.petshop.dto.PetDTO;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

public interface PetDocumentation {

    @Operation(summary = "Criar cadastro de pet", description = "Cria o cadastro de um pet a partir " +
            "do ID do cliente dono como parâmetro na requisição")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o cadastro do pet criado"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<PetDTO> post(Integer idCliente, @Valid @RequestBody PetCreateDTO petDto) throws RegraDeNegocioException, EntidadeNaoEncontradaException;

    @Operation(summary = "Listar pets do cliente", description = "Lista todos os pets cadastrados " +
            "com o ID do cliente desejado")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna a lista de pets do cliente"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<List<PetDTO>> get(Integer idCliente) throws EntidadeNaoEncontradaException;

    @Operation(summary = "Retornar um pet por seu ID", description = "Retorna as informações do pet atravésde seu ID")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o pet"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<PetDTO> getByPetId(Integer idPet) throws EntidadeNaoEncontradaException;

    @Operation(summary = "Listar pets por cliente paginado", description = "Lista os pets de um cliente, " +
            "caso não seja informado o id de um cliente, irá listar todos os pets registrados no sistema. " +
            "Informe o número da página e a quantidade de registros por página")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o pet"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    PageDTO<PetDTO> paginarPetsCliente(Integer idCliente, Integer pagina, Integer quantidadeRegistro);

    @Operation(summary = "Atualizar informações do pet", description = "Altera as informações do " +
            "pet desejado a partir de seu ID")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o cadastro com as informações atualizadas"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<PetDTO> put(Integer idPet, @Valid @RequestBody PetCreateDTO petDto) throws  EntidadeNaoEncontradaException;

    @Operation(summary = "Deletar cadastro do pet", description = "Remove o cadastro do pet desejado " +
            "a partir de seu ID, removerá também os pedidos atrelados ao seu ID")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Remove o cadastro"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    void delete(Integer idPet) throws EntidadeNaoEncontradaException;
}
