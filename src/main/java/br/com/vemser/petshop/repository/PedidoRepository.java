package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.entity.Pedido;
import br.com.vemser.petshop.entity.Pet;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository {

    @Autowired
    private Connection connection;

    public Integer nextSeq() {
        try {
            String sql = "SELECT seq_id_pedido.nextval seqPedido from DUAL";
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            if (res.next()) {
                return res.getInt("seqPedido");
            }

            return null;
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
        return null;
    }

    public Pedido adicionar(Pedido pedido) throws SQLException {
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
            stmt.setInt(3, pedido.getIdPet());
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

    public boolean remover(Integer id) throws  SQLException {
        try {

            String sql = "DELETE FROM PEDIDO WHERE ID_PEDIDO = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);

            int res = stmt.executeUpdate();
            if(res > 0){
                System.out.println("Pedido removido com sucesso!");
            }
            return res > 0;
        } catch (SQLException e) {
            throw new SQLException(e.getCause());
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

    public boolean editar(Integer id, Pedido pedido) throws  SQLException {
        try {

            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE pedido SET \n");
            sql.append(" id_animal = ?,");
            sql.append(" valor = ?,");
            sql.append(" descricao = ?,");

            sql.deleteCharAt(sql.length() -1);
            sql.append("WHERE id_pedido = ? ");

            PreparedStatement  stmt = connection.prepareStatement(sql.toString());

            int index = 1;
            stmt.setInt(index++, pedido.getIdPet());
            if(pedido.getValor() != null) {
                stmt.setDouble(index++, pedido.getValor());
            }
            if(pedido.getDescricao() != null) {
                stmt.setString(index++, pedido.getDescricao());
            }

            stmt.setInt(index++, id);

            int res = stmt.executeUpdate();
            if(res > 0){
                System.out.println("Pedido alterado com sucesso!!");
            }

            return res>0;
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

    public List<Pedido> listar() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        try {;
            Statement stmt = connection.createStatement();

            String sql = "SELECT P.*" +
                    "          , C.NOME AS NOME_CLIENTE " +
                    "       FROM PEDIDO P " +
                    "       LEFT JOIN CLIENTE C ON (C.ID_CLIENTE = P.ID_CLIENTE) ";

            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Pedido pedido = getPedidoFromResultSet(res);
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

    public List<Pedido> listarPedidosPorCliente(Integer idCliente) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        try {

            String sql = "SELECT P.*" +
                    "          , C.NOME AS NOME_CLIENTE " +
                    "       FROM PEDIDO P " +
                    "      INNER JOIN CLIENTE C ON (C.ID_CLIENTE = P.ID_CLIENTE) " +
                    "      WHERE P.ID_CLIENTE = ? ";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCliente);

            ResultSet res = stmt.executeQuery();
            PetRepository animalRepository = new PetRepository();
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

    public Pedido getPedidoPorId(int idPedido) throws SQLException {
        Pedido pedido = null;
        try {
            String sql = """
                                SELECT p.*
                                FROM PEDIDO p
                                WHERE p.ID_PEDIDO = ?
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPedido);

            ResultSet res = stmt.executeQuery();

            if(res.next()) {
                pedido = getPedidoFromResultSet(res);
            }
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

    private Pedido getPedidoFromResultSet(ResultSet res) throws  SQLException {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(res.getInt("id_pedido"));
        pedido.setValor(res.getInt("valor"));
        pedido.setDescricao(res.getString("descricao"));
        pedido.setIdPet(res.getInt("id_animal"));
        return pedido;
    }




}
