package br.com.vemser.petshop.documentation;

import br.com.vemser.petshop.dto.PedidoCreateDTO;
import br.com.vemser.petshop.dto.PedidoDTO;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

public interface PedidoDocumentation {

    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido, ligando as informações " +
            "do {idPet} e {idCliente}")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o pedido criado"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<PedidoDTO> post(Integer idPet, @Valid @RequestBody PedidoCreateDTO pedidoDto) throws SQLException, RegraDeNegocioException;

    @Operation(summary = "Listar pedidos por cliente", description = "Lista todos os pedidos ligados " +
            "ao {idCliente} informado")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna a lista"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<List<PedidoDTO>> getByClientId(Integer idCliente) throws SQLException, RegraDeNegocioException;

    @Operation(summary = "Listar pedidos por pet", description = "Lista todos os pedidos ligados " +
            "ao {idPet} informado")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna a lista"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<List<PedidoDTO>> getByPetId(Integer idPet) throws SQLException, RegraDeNegocioException;

    @Operation(summary = "Atualizar pedido", description = "Atualiza as informações de um pedido a partir " +
            "do {idPedido}")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o pedido atualizado"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<PedidoDTO> put(Integer idPedido, @Valid @RequestBody PedidoCreateDTO pedidoDto) throws SQLException, RegraDeNegocioException;

    @Operation(summary = "Deletar pedido", description = "Deleta o pedido a partir " +
            "do {idPedido} informado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Deleta o pedido"),
                    @ApiResponse(responseCode = "400", description = "Erro client-side"),
                    @ApiResponse(responseCode = "500", description = "Erro server-side")
            }
    )
    void delete(Integer idPedido) throws SQLException, RegraDeNegocioException;
}
