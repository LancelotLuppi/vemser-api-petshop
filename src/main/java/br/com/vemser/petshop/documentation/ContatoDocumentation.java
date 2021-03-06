package br.com.vemser.petshop.documentation;

import br.com.vemser.petshop.dto.ContatoCreateDTO;
import br.com.vemser.petshop.dto.ContatoDTO;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

public interface ContatoDocumentation {

    @Operation(summary = "Retornar contato", description = "Retorna as iformações do contato caso " +
            "o ID do cliente exista")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o contato desejado"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<List<ContatoDTO>> get(Integer idCliente) throws EntidadeNaoEncontradaException;

    @Operation(summary = "Criar contato", description = "Cria um contato para um cliente, passando o ID " +
            "do cliente como parâmetro na requisição")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna contato criado"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<ContatoDTO> post(Integer idCliente, @Valid @RequestBody ContatoCreateDTO contatoDto) throws RegraDeNegocioException, EntidadeNaoEncontradaException;

    @Operation(summary = "Atualizar informações de contato", description = "Atualiza as informações de um contato existente " +
            "passando seu ID como parâmetro e os dados ")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o contato atualizado"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<ContatoDTO> put(Integer idContato,@Valid @RequestBody ContatoCreateDTO contatoAtualizadoDTO) throws RegraDeNegocioException, EntidadeNaoEncontradaException;

    @Operation(summary = "Deletar contato", description = "Deleta um contato passando  " +
            "passando seu ID como parâmetro e os dados ")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Deleta o contato"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    void delete(Integer idContato) throws RegraDeNegocioException, EntidadeNaoEncontradaException;
}
