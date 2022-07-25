package br.com.vemser.petshop.documentation;

import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.PedidoCreateDTO;
import br.com.vemser.petshop.dto.PedidoDTO;
import br.com.vemser.petshop.dto.PedidoStatusRelatorioDTO;
import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface PedidoDocumentation {

    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido, ligando as informações " +
            "do {idPet} e {idCliente}")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o pedido criado"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<PedidoDTO> post(Integer idPet, @Valid @RequestBody PedidoCreateDTO pedidoDto) throws RegraDeNegocioException, EntidadeNaoEncontradaException;

    @Operation(summary = "Listar pedidos por cliente", description = "Lista todos os pedidos ligados " +
            "ao {idCliente} informado")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna a lista"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<List<PedidoDTO>> getByClientId(Integer idCliente) throws EntidadeNaoEncontradaException;

    @Operation(summary = "Listar pedidos por pet", description = "Lista todos os pedidos ligados " +
            "ao {idPet} informado")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna a lista"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<List<PedidoDTO>> getByPetId(Integer idPet) throws EntidadeNaoEncontradaException;

    @Operation(summary = "Atualizar pedido", description = "Atualiza as informações de um pedido a partir " +
            "do {idPedido}. As informações de {idPet} e {idCliente} não são modificáveis, recomendável remover " +
            "o pedido e gerar um novo caso uma dessas informações estejam erradas.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Retorna o pedido atualizado"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<PedidoDTO> put(Integer idPedido, @Valid @RequestBody PedidoCreateDTO pedidoDto) throws RegraDeNegocioException, EntidadeNaoEncontradaException;

    @Operation(summary = "Deletar pedido", description = "Deleta o pedido a partir do {idPedido} informado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Deleta o pedido"),
                    @ApiResponse(responseCode = "400", description = "Erro client-side"),
                    @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro server-side")
            }
    )
    void delete(Integer idPedido) throws RegraDeNegocioException, EntidadeNaoEncontradaException;

    @Operation(summary = "Alterar status do pedido", description = "ABERTO: pedido ainda que ainda será atendido. " +
            "EM_ANDAMENTO: pedido sendo atendido no momento. CONCLUIDO: pedido já atendido e finalizado. " +
            "CANCELADO: pedido que não foi continuado. Caso um pedido em ABERTO seja CANCELADO ou CONCLUIDO, " +
            "as informações do cliente dono serão atualizadas, retirando da quantidade de pedidos totais e o valor a pagar. " +
            "Caso um pedido CANCELADO ou CONCLUIDO seja ABERTO novamente, as informações de quantidade e valor a pagar " +
            "do cliente voltarão a contar.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Deleta o pedido"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "404", description = "Entidade não encontrada"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    ResponseEntity<PedidoDTO> putStatus(Integer idPedido, StatusPedido status) throws EntidadeNaoEncontradaException, RegraDeNegocioException;

    @Operation(summary = "Gerar relatorio", description = "Gera um relatório trazendo " +
            "as seguintes informações do pedido: idCliente, nome do cliente, email, " +
            "nome do pet, tipo do pet, status do pedido, serviço do pedido, valor do pedido. " +
            " Pode-se usar filtro por status do pedido, trazendo os pedidos com o status desejado, " +
            "caso nenhum status seja informado, será informado todos os pedidos registrados no banco de dados")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Deleta o pedido"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    PageDTO<PedidoStatusRelatorioDTO> gerarRelatorioPorStatus(StatusPedido status, Integer pagina, Integer registro);

    @Operation(summary = "Paginar pedidos", description = "Gera uma paginação dos pedidos " +
            "registrados no banco de dados. Pode-se usar filtro por {idCliente}, trazendo todos os " +
            "pedidos atrelados com o {idCliente} informado. Caso apenas o {idPet} seja informado, será " +
            "retornado apenas os pedidos com o {idPet} informado. Se ambos os ID's forem informados, será " +
            "retornado os pedidos que tenham aquele {idCliente} e {idPet} juntos. Caso nenhum ID seja informado, " +
            "o sistema retornará todos os pedidos registrados.")
        @ApiResponses(
                value = {
                        @ApiResponse(responseCode = "200", description = "Deleta o pedido"),
                        @ApiResponse(responseCode = "400", description = "Erro client-side"),
                        @ApiResponse(responseCode = "500", description = "Erro server-side")
                }
        )
    PageDTO<PedidoDTO> listarPedidosPaginados( Integer idCliente, Integer idPet, Integer pagina, Integer registro);
}
