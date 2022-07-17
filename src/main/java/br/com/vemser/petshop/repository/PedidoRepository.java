package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.config.ConexaoBancoDeDados;
import br.com.vemser.petshop.entity.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PedidoRepository {

    @Autowired
    private ConexaoBancoDeDados conexaoBancoDeDados;

    public Integer nextSeq() throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        String sql = "SELECT seq_id_pedido.nextval seqPedido from DUAL";
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        if (res.next()) {
            return res.getInt("seqPedido");
        }
        return null;
    }

    public Pedido adicionar(Integer idPet, Pedido pedido) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {
            Integer proximoId = this.nextSeq();
            pedido.setIdPedido(proximoId);

            String sql  = """
                    INSERT INTO PEDIDO
                    (ID_PEDIDO, ID_CLIENTE, ID_ANIMAL, VALOR, DESCRICAO)
                    VALUES(?, ?, ?, ?, ?)
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, pedido.getIdPedido());
            stmt.setInt(2, pedido.getIdCliente());
            stmt.setInt(3, idPet);
            stmt.setDouble(4, pedido.getValor());
            stmt.setString(5, pedido.getDescricao());

            int res = stmt.executeUpdate();
            System.out.println("adicionarPedido=" + res);
            return pedido;
        } catch (SQLException e) {
            throw new SQLException(e.getCause());
        } finally {
            try {
                if(!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void remover(Integer id) throws  SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            String sql = "DELETE FROM PEDIDO WHERE ID_PEDIDO = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Pedido update(Integer id, Pedido pedido) throws  SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        Pedido pedidoAtualizado;
        try {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE pedido SET \n");
            sql.append(" valor = ?,");
            sql.append(" descricao = ?,");

            sql.deleteCharAt(sql.length() -1);
            sql.append("WHERE id_pedido = ? ");

            PreparedStatement  stmt = connection.prepareStatement(sql.toString());

            int index = 1;
            stmt.setDouble(index++, pedido.getValor());
            stmt.setString(index++, pedido.getDescricao());

            stmt.setInt(index++, id);

            if(stmt.executeUpdate() > 0) {
                pedidoAtualizado = returnByIdUtil(id);
                return pedidoAtualizado;
            }

        } catch (SQLException e) {
            throw new SQLException(e.getCause());
        } finally {
            try {
                if(!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<Pedido> listar(Integer idCliente) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<Pedido> pedidos = new ArrayList<>();
        try {;
            String sql = """
                                SELECT p.*
                                FROM PEDIDO p
                                WHERE p.ID_CLIENTE = ?
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCliente);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                Pedido pedido = getPedidoFromResultSet(res);
                pedido.setIdCliente(idCliente);
                pedidos.add(pedido);
            }
            return pedidos;
        } catch (SQLException e) {
            throw new SQLException(e.getCause());
        } finally {
            try{
                if(!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Pedido> listarPedidosPorPet(Integer idPet) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<Pedido> pedidos = new ArrayList<>();
        try {

            String sql = "SELECT P.*" +
                    "       FROM PEDIDO P " +
                    "      WHERE P.ID_ANIMAL = ? ";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPet);

            ResultSet res = stmt.executeQuery();
            while(res.next()) {
                Pedido pedido = getPedidoFromResultSet(res);
                pedido.setIdPedido(res.getInt("ID_PEDIDO"));
                pedido.setIdCliente(res.getInt("ID_CLIENTE"));
                pedido.setIdPet(res.getInt("ID_ANIMAL"));
                pedido.setValor(res.getInt("VALOR"));
                pedido.setDescricao(res.getString("DESCRICAO"));
                pedidos.add(pedido);
            }
            return pedidos;
        } catch (SQLException e) {
            throw new SQLException(e.getCause());
        } finally {
            try {
                if(!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Pedido returnByIdUtil(int idPedido) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        Pedido pedido = null;
        String sql = """
                            SELECT p.*
                            FROM PEDIDO p
                            WHERE p.ID_PEDIDO = ?
                """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, idPedido);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            pedido = getPedidoFromResultSet(res);
        }
        return pedido;
    }

    private Pedido getPedidoFromResultSet(ResultSet res) throws  SQLException {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(res.getInt("id_pedido"));
        pedido.setValor(res.getInt("valor"));
        pedido.setDescricao(res.getString("descricao"));
        pedido.setIdPet(res.getInt("id_animal"));
        return pedido;
    }




}
