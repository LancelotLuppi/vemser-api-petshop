package br.com.vemser.petshop.controller;

import br.com.vemser.petshop.dto.PedidoCreateDTO;
import br.com.vemser.petshop.dto.PedidoDTO;
import br.com.vemser.petshop.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@Validated
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/{idPet}")
    public ResponseEntity<PedidoDTO> post(@PathVariable("idPet") Integer id, @Valid @RequestBody PedidoCreateDTO pedido) throws SQLException {
        return ResponseEntity.ok(pedidoService.create(id, pedido));
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<List<PedidoDTO>> getByClientId(@PathVariable("idCliente") Integer id) throws SQLException {
        return ResponseEntity.ok(pedidoService.list(id));
    }

    @GetMapping("/{idPet}/pet")
    public ResponseEntity<List<PedidoDTO>> getByPetId(@PathVariable("idPet") Integer idPet) throws SQLException {
        return ResponseEntity.ok(pedidoService.listByPetId(idPet));
    }

    @PutMapping("/{idPedido}")
    public ResponseEntity<PedidoDTO> put(@PathVariable("idPedido") Integer idPedido, @Valid @RequestBody PedidoCreateDTO pedidoAtualizado) throws SQLException {
        return ResponseEntity.ok(pedidoService.update(idPedido, pedidoAtualizado));
    }

    @DeleteMapping("/{idPedido}")
    public void delete(@PathVariable("idPedido") Integer idPedido) throws SQLException {
        pedidoService.delete(idPedido);
    }
}
