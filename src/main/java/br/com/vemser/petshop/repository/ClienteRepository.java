package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    @Autowired
    private Connection connection;


    public Integer nextSeq() {
        try {
            String sql = "SELECT SEQ_ID_CLIENTE.nextval SEQ_ID_CLIENTE from DUAL";
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery(sql);
            if (res.next()) {
                return res.getInt("SEQ_ID_CLIENTE");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
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

    public Cliente adicionar(Cliente cliente) throws SQLException {
        try {

            cliente.setIdCliente(this.nextSeq());

            String sql = "INSERT INTO CLIENTE\n" +
                    "(ID_CLIENTE, NOME, QUANTIDADE_PEDIDOS)\n" +
                    "VALUES(?, ?, ?)\n";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, cliente.getIdCliente());
            stmt.setString(2, cliente.getNome());
            stmt.setInt(3, cliente.getQuantidadeDePedidos());

            if(stmt.executeUpdate() != 0){
                System.out.println("Cliente adicionado com sucesso");
                return cliente;
            }
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
        return null;
    }

    public boolean remover(Integer id) throws SQLException {
        try {

            String sql = "DELETE FROM CLIENTE WHERE ID_CLIENTE = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);

            if(stmt.executeUpdate() > 0){
                System.out.println("Cliente removido com sucesso");
                return true;
            }
            return false;
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

    public boolean editar(Integer id, Cliente cliente) throws SQLException {
        try {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE cliente SET \n");
            if (cliente != null) {
                if (cliente.getIdCliente() != null) {
                    sql.append(" id_cliente = ?,");
                }
            }
            assert cliente != null;
            if (cliente.getNome() != null) {
                sql.append(" nome = ?,");
            }
            if (cliente.getQuantidadeDePedidos() != null) {
                sql.append(" QUANTIDADE_PEDIDOS = ?,");
            }
            sql.deleteCharAt(sql.length() - 1); //remove o ultimo ','
            sql.append(" WHERE id_cliente = ? ");

            PreparedStatement stmt = connection.prepareStatement(sql.toString());

            int index = 1;
            if (cliente.getIdCliente() != null) {
                stmt.setInt(index++, cliente.getIdCliente());
            }
            if (cliente.getNome() != null) {
                stmt.setString(index++, cliente.getNome());
            }
            if (cliente.getQuantidadeDePedidos() != null) {
                stmt.setInt(index++, cliente.getQuantidadeDePedidos());
            }
            stmt.setInt(index, id);
            // Executa-se a consulta
            if(stmt.executeUpdate() > 0){
                System.out.println("Cliente editado com sucesso");
                return true;
            }

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
        return false;
    }


    public List<Cliente> listar() throws SQLException {

        List<Cliente> clientes = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();

            String sql = "SELECT * FROM PESSOA";

            // Executa-se a consulta
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(res.getInt("ID_CLIENTE"));
                cliente.setNome(res.getString("NOME"));
                cliente.setIdCliente(res.getInt("QUANTIDADE_PEDIDOS"));
                clientes.add(cliente);
            }
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
        return clientes;
    }

    public int incrementarQuantidadeDePedidosNoBanco(int idCliente) throws SQLException {
        try {
            String sql = """
                                SELECT c.QUANTIDADE_PEDIDOS
                                FROM CLIENTE c
                                WHERE c.ID_CLIENTE = ?
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCliente);
            ResultSet res = stmt.executeQuery();
            return res.getInt("QUANTIDADE_PEDIDOS");
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

    public void setPedidosBanco(int idCliente, int novaQuantidade) throws SQLException {
        try {

            String sql = """
                    UPDATE CLIENTE
                    SET QUANTIDADE_PEDIDOS = ?
                    WHERE ID_CLIENTE = ?
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);

            int index = 1;
            stmt.setInt(index++, novaQuantidade);
            stmt.setInt(index, idCliente);

            // Executa-se a consulta
            if(stmt.executeUpdate() > 0){
                System.out.println("Cliente editado com sucesso");
            }
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

    public Cliente getClientePeloId(Integer id) throws SQLException {
        Cliente cliente = null;
        try {
            String sql = """
                            SELECT c.*
                            FROM CLIENTE c
                            WHERE c.ID_CLIENTE = ?
                """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            if(res.next()) {
                cliente = getClienteFromResultSet(res);
            }
            return cliente;
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

    private Cliente getClienteFromResultSet(ResultSet res) throws  SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(res.getInt("id_cliente"));
        cliente.setNome(res.getString("nome"));
        cliente.setQuantidadeDePedidos(res.getInt("quantidade_pedidos"));
        return cliente;
    }
}
