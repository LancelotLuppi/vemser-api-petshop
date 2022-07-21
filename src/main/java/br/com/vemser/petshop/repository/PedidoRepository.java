package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.config.ConexaoBancoDeDados;
import br.com.vemser.petshop.entity.PedidoEntity;
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

    public PedidoEntity adicionar(Integer idPet, PedidoEntity pedidoEntity) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {
            Integer proximoId = this.nextSeq();
            pedidoEntity.setIdPedido(proximoId);

            String sql  = """
                    INSERT INTO PEDIDO
                    (ID_PEDIDO, ID_CLIENTE, ID_ANIMAL, VALOR, DESCRICAO)
                    VALUES(?, ?, ?, ?, ?)
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, pedidoEntity.getIdPedido());
            stmt.setInt(2, pedidoEntity.getIdCliente());
            stmt.setInt(3, idPet);
            stmt.setDouble(4, pedidoEntity.getValor());
            stmt.setString(5, pedidoEntity.getDescricao());

            int res = stmt.executeUpdate();
            System.out.println("adicionarPedido=" + res);
            return pedidoEntity;
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


    public void removerPedidosPorIDAnimal(Integer id) throws  SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            String sql = "DELETE FROM PEDIDO WHERE ID_ANIMAL = ?";

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

    public void removerPedidosPorIDCliente(Integer id) throws  SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            String sql = "DELETE FROM PEDIDO WHERE ID_CLIENTE = ?";

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

    public PedidoEntity update(Integer id, PedidoEntity pedidoEntity) throws  SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        PedidoEntity pedidoEntityAtualizado;
        try {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE pedidoEntity SET \n");
            sql.append(" valor = ?,");
            sql.append(" descricao = ?,");

            sql.deleteCharAt(sql.length() -1);
            sql.append("WHERE id_pedido = ? ");

            PreparedStatement  stmt = connection.prepareStatement(sql.toString());

            int index = 1;
            stmt.setDouble(index++, pedidoEntity.getValor());
            stmt.setString(index++, pedidoEntity.getDescricao());

            stmt.setInt(index++, id);

            if(stmt.executeUpdate() > 0) {
                pedidoEntityAtualizado = returnByIdUtil(id);
                return pedidoEntityAtualizado;
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

    public List<PedidoEntity> listarPorIdCliente(Integer idCliente) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<PedidoEntity> pedidoEntities = new ArrayList<>();
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
                PedidoEntity pedidoEntity = getPedidoFromResultSet(res);
                pedidoEntity.setIdCliente(idCliente);
                pedidoEntities.add(pedidoEntity);
            }
            return pedidoEntities;
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

    public List<PedidoEntity> listarPorIdPet(Integer idPet) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<PedidoEntity> pedidoEntities = new ArrayList<>();
        try {

            String sql = "SELECT P.*" +
                    "       FROM PEDIDO P " +
                    "      WHERE P.ID_ANIMAL = ? ";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPet);

            ResultSet res = stmt.executeQuery();
            while(res.next()) {
                PedidoEntity pedidoEntity = getPedidoFromResultSet(res);
                pedidoEntity.setIdPedido(res.getInt("ID_PEDIDO"));
                pedidoEntity.setIdCliente(res.getInt("ID_CLIENTE"));
                pedidoEntity.setIdPet(res.getInt("ID_ANIMAL"));
                pedidoEntity.setValor(res.getInt("VALOR"));
                pedidoEntity.setDescricao(res.getString("DESCRICAO"));
                pedidoEntities.add(pedidoEntity);
            }
            return pedidoEntities;
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

    public List<PedidoEntity> listar() throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<PedidoEntity> pedidoEntities = new ArrayList<>();
        try {
            String sql = "SELECT * FROM PEDIDO";

            Statement stmt = connection.prepareStatement(sql);
            ResultSet res = stmt.executeQuery(sql);

            while(res.next()) {
                PedidoEntity pedidoEntity = getPedidoFromResultSet(res);
                pedidoEntities.add(pedidoEntity);
            }
            return pedidoEntities;
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
        return pedidoEntities;
    }

    public PedidoEntity returnByIdUtil(int idPedido) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        PedidoEntity pedidoEntity = null;
        String sql = """
                            SELECT p.*
                            FROM PEDIDO p
                            WHERE p.ID_PEDIDO = ?
                """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, idPedido);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            pedidoEntity = getPedidoFromResultSet(res);
        }
        return pedidoEntity;
    }

    private PedidoEntity getPedidoFromResultSet(ResultSet res) throws  SQLException {
        PedidoEntity pedidoEntity = new PedidoEntity();
        pedidoEntity.setIdPedido(res.getInt("id_pedido"));
        pedidoEntity.setValor(res.getInt("valor"));
        pedidoEntity.setIdCliente(res.getInt("ID_CLIENTE"));
        pedidoEntity.setDescricao(res.getString("descricao"));
        pedidoEntity.setIdPet(res.getInt("id_animal"));
        return pedidoEntity;
    }




}
