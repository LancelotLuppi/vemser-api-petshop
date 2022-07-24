package br.com.vemser.petshop.service;

import br.com.vemser.petshop.entity.ClienteEntity;
import br.com.vemser.petshop.entity.PedidoEntity;
import br.com.vemser.petshop.enums.StatusPedido;
import br.com.vemser.petshop.exception.EntidadeNaoEncontradaException;
import br.com.vemser.petshop.exception.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegraStatusPedidoService {
    @Autowired
    private ClienteService clienteService;

    public void updateStatus(PedidoEntity pedido, StatusPedido status) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        if(pedido.getStatus().equals(status)) {
            throw new RegraDeNegocioException("O pedido já tem o status informado!");
        }
        if(pedido.getStatus().equals(StatusPedido.ABERTO)) {
            if(status.equals(StatusPedido.CONCLUIDO)) {
                throw new RegraDeNegocioException("Pedido em ABERTO não pode ser concluído " +
                        "sem antes ficar EM_ANDAMENTO");
            } else if(status.equals(StatusPedido.CANCELADO)) {
                removeValorPedidoDoCliente(pedido);
            }
        }
        else if(pedido.getStatus().equals(StatusPedido.EM_ANDAMENTO)) {
            if(status.equals(StatusPedido.CANCELADO)) {
                removeValorPedidoDoCliente(pedido);
            } else if(status.equals(StatusPedido.CONCLUIDO)) {
                removeValorPedidoDoCliente(pedido);
            }
        }
        else if(pedido.getStatus().equals(StatusPedido.CONCLUIDO)) {
            if(status.equals(StatusPedido.CANCELADO)) {
                throw new RegraDeNegocioException("Pedidos CONCLUIDOS não podem ser CANCELADOS");
            } else if(status.equals(StatusPedido.ABERTO)) {
                throw new RegraDeNegocioException("Pedidos CONCLUIDOS não podem ser ABERTOS");
            } else if(status.equals(StatusPedido.EM_ANDAMENTO)) {
                throw new RegraDeNegocioException("Pedidos CONCLUIDOS não podem entrar EM_ANDAMENTO");
            }
        }
        else if(pedido.getStatus().equals(StatusPedido.CANCELADO)) {
            if(status.equals(StatusPedido.ABERTO)) {
                pedido.setStatus(status);
                adicionaValorPedidoDoCliente(pedido);
            } else if(status.equals(StatusPedido.EM_ANDAMENTO)) {
                throw new RegraDeNegocioException("Pedidos CANCELADOS não podem entrar EM_ANDAMENTO");
            } else if(status.equals(StatusPedido.CONCLUIDO)) {
                throw new RegraDeNegocioException("Pedidos CANCELADOS não podem ser CONCLUIDOS");
            }
        }
    }

    private void removeValorPedidoDoCliente(PedidoEntity pedido) throws EntidadeNaoEncontradaException {
        ClienteEntity cliente = clienteService.retornarPorIdVerificado(pedido.getCliente().getIdCliente());
        cliente.setQuantidadeDePedidos(cliente.getQuantidadeDePedidos() - 1);
        cliente.setValorPagamento(cliente.getValorPagamento() - pedido.getValor());
    }
    private void adicionaValorPedidoDoCliente(PedidoEntity pedido) throws EntidadeNaoEncontradaException {
        ClienteEntity cliente = clienteService.retornarPorIdVerificado(pedido.getCliente().getIdCliente());
        cliente.setQuantidadeDePedidos(cliente.getQuantidadeDePedidos() + 1);
        cliente.setValorPagamento(cliente.getValorPagamento() + pedido.getValor());
    }
}
