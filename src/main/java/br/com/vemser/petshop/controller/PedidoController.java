package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.documentation.PedidoDocumentation;
import br.com.vemser.petshop.dto.PageDTO;
import br.com.vemser.petshop.dto.pedido.PedidoCreateDTO;
import br.com.vemser.petshop.dto.pedido.PedidoDTO;
import br.com.vemser.petshop.dto.pedido.PedidoStatusRelatorioDTO;
import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import br.com.vemser.petshop.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@Validated
public class PedidoController implements PedidoDocumentation {
    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/{idPet}")
    public ResponseEntity<PedidoDTO> post(@PathVariable("idPet") Integer id, @Valid @RequestBody PedidoCreateDTO pedido) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        return ResponseEntity.ok(pedidoService.create(id, pedido));
    }

    @PostMapping("/logged-user")
    public ResponseEntity<PedidoDTO> postByLoggedUser(Integer idPet, @Valid @RequestBody PedidoCreateDTO pedido) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        return ResponseEntity.ok(pedidoService.createByLoggedUser(idPet, pedido));
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<List<PedidoDTO>> getByClientId(@PathVariable("idCliente") Integer id) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(pedidoService.list(id));
    }

    @GetMapping("/{idPet}/pet")
    public ResponseEntity<List<PedidoDTO>> getByPetId(@PathVariable("idPet") Integer idPet) throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(pedidoService.listByPetId(idPet));
    }

    @PutMapping("/{idPedido}")
    public ResponseEntity<PedidoDTO> put(@PathVariable("idPedido") Integer idPedido, @Valid @RequestBody PedidoCreateDTO pedidoAtualizado) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        return ResponseEntity.ok(pedidoService.update(idPedido, pedidoAtualizado));
    }

    @PutMapping("/logged-user")
    public  ResponseEntity<PedidoDTO> putByLoggedUser(Integer idPedido, @Valid @RequestBody PedidoCreateDTO pedidoAtualizado) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        return ResponseEntity.ok(pedidoService.updateByLoggedUser(idPedido, pedidoAtualizado));
    }

    @PutMapping("/atualizar-status")
    public ResponseEntity<PedidoDTO> putStatus(Integer idPedido, StatusPedido status) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        return ResponseEntity.ok(pedidoService.updateStatus(idPedido, status));
    }

    @DeleteMapping("/{idPedido}")
    public void delete(@PathVariable("idPedido") Integer idPedido) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        pedidoService.delete(idPedido);
    }


    @GetMapping("/relatorio-status")
    public PageDTO<PedidoStatusRelatorioDTO> gerarRelatorioPorStatus(@RequestParam(value = "status", required = false)StatusPedido status, Integer pagina, Integer registro) {
        return pedidoService.gerarRelatorioStatus(status, pagina, registro);
    }

    @GetMapping("/paginacao-ids")
    public PageDTO<PedidoDTO> listarPedidosPaginados(@RequestParam(value = "idCliente", required = false) Integer idCliente,
                                                     @RequestParam(value = "idPet", required = false) Integer idPet,
                                                     Integer pagina, Integer registro) {
        return pedidoService.listarPedidosPaginado(idCliente, idPet, pagina, registro);
    }

    @GetMapping("/pedido-id-logado")
    public ResponseEntity<List<PedidoDTO>> getByLoggedUser() throws EntidadeNaoEncontradaException {
        return ResponseEntity.ok(pedidoService.getByLoggedUser());
    }
}
