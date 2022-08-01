package br.com.vemser.petshop.documentation;

import br.com.vemser.petshop.dto.cliente.ClienteCreateDTO;
import br.com.vemser.petshop.dto.cliente.ClienteDTO;
import br.com.vemser.petshop.dto.cliente.ClienteDadosRelatorioDTO;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface ClienteDocumentation {

    @Operation(summary = "Criar cadastro de cliente", description = "Adiciona as informações do cadastro no banco de dados" +
            ", a quantidade de pedidos e o valor a pagar são definidas como 0 ao criar um novo cadastro. \n" +
            "Envia um email informando que foi feito o cadastro no sistema.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<ClienteDTO> post(@Valid @RequestBody ClienteCreateDTO clienteDto) throws EntidadeNaoEncontradaException, RegraDeNegocioException;

    @Operation(summary = "Retornar informação do cliente logado", description = "Retorna as informações de cliente do usuário logado")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna informação do cliente"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<ClienteDTO> getByLoggedUser() throws EntidadeNaoEncontradaException;

    @Operation(summary = "Retornar informações de um cliente", description = "Busca no banco de dados por um " +
            "cadastro com o ID fornecido na requisição, retornando as informações caso encontre")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o cliente"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<ClienteDTO> getById(Integer idCliente) throws EntidadeNaoEncontradaException;

    @Operation(summary = "Atualizar cadastro de cliente", description = "Atualiza as informações no banco de dados por uma nova, " +
            "baseando no ID do cliente para qual alterar. \n" +
            "Envia um email informando a atualização dos dados para o cliente.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Atualiza os dados"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<ClienteDTO> put(Integer idCliente, @Valid @RequestBody ClienteCreateDTO clienteDto) throws  EntidadeNaoEncontradaException;

    @Operation(summary = "Deletar cadastro de cliente", description = "Deleta as informações do cliente no banco de dados, " +
            "juntamente apagando as informações de (contato/pet/pedido) que estão ligados com o ID desse cliente. \n" +
            "Envia um email informando a deleção do cadastro para o cliente.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    void delete(Integer idCliente) throws EntidadeNaoEncontradaException;


    @Operation(summary = "Listar relatorio dos clientes", description = "Gera um relatório com as seguintes " +
            "informações dos clientes: idCliente, nome, email, nome do pet, tipo do pet, telefone, descrição do contato.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Lista os relatórios"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<List<ClienteDadosRelatorioDTO>> listClienteDados(Integer idCliente);
}
