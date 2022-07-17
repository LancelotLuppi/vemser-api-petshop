package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.config.ConexaoBancoDeDados;
import br.com.vemser.petshop.entity.Cliente;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ClienteRepository {

    @Autowired
    private ConexaoBancoDeDados conexaoBancoDeDados;


    public Integer nextSeq() throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        String sql = "SELECT SEQ_ID_CLIENTE.nextval SEQ_ID_CLIENTE from DUAL";
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        if (res.next()) {
            return res.getInt("SEQ_ID_CLIENTE");
        }
        return null;
    }

    public Cliente adicionar(Cliente cliente) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            cliente.setIdCliente(this.nextSeq());

            String sql = "INSERT INTO CLIENTE\n" +
                    "(ID_CLIENTE, NOME, QUANTIDADE_PEDIDOS, EMAIL)\n" +
                    "VALUES(?, ?, ?, ?)\n";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, cliente.getIdCliente());
            stmt.setString(2, cliente.getNome());
            stmt.setInt(3, cliente.getQuantidadeDePedidos());
            stmt.setString(4, cliente.getEmail());


            if (stmt.executeUpdate() != 0) {
                log.info("Cliente adicionado com sucesso");
                return cliente;
            }
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

    public boolean remover(Integer id) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            String sql = "DELETE FROM CLIENTE WHERE ID_CLIENTE = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);

            if (stmt.executeUpdate() > 0) {
                log.info("Cliente removido com sucesso");
                return true;
            }
            return false;
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
        return false;
    }

    public Cliente update(Integer id, Cliente cliente) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        Cliente clienteAtualizado;
        try {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE cliente SET\n");
            if (cliente.getNome() != null) {
                sql.append("nome = ?,");
            }
            if (cliente.getQuantidadeDePedidos() != null) {
                sql.append(" QUANTIDADE_PEDIDOS = ?,");
            }
            if (cliente.getEmail() != null) {
                sql.append(" email = ?\n");
            }
            sql.append("WHERE id_cliente = ? ");

            PreparedStatement stmt = connection.prepareStatement(sql.toString());

            if (cliente.getNome() != null) {
                stmt.setString(1, cliente.getNome());
            }
            if (cliente.getQuantidadeDePedidos() != null) {
                stmt.setInt(2, cliente.getQuantidadeDePedidos());
            }
            if (cliente.getEmail() != null) {
                stmt.setString(3, cliente.getEmail());
            }
            stmt.setInt(4, id);
            // Executa-se a consulta
            if (stmt.executeUpdate() > 0) {
                log.info("Cliente editado com sucesso");
                clienteAtualizado = returnByIdUtil(id);
                return clienteAtualizado;
            }

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

    public List<Cliente> listar() throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<Cliente> clientes = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();

            String sql = "SELECT * FROM CLIENTE";

            // Executa-se a consulta
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(res.getInt("ID_CLIENTE"));
                cliente.setNome(res.getString("NOME"));
                cliente.setQuantidadeDePedidos(res.getInt("QUANTIDADE_PEDIDOS"));
                cliente.setQuantidadeDePedidos(res.getInt("EMAIL"));
                clientes.add(cliente);
            }
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
        return clientes;
    }

    public int incrementarQuantidadeDePedidosNoBanco(int idCliente) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
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
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPedidosBanco(int idCliente, int novaQuantidade) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
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
            if (stmt.executeUpdate() > 0) {
                log.info("Cliente editado com sucesso");
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

    public Cliente returnByIdUtil(Integer id) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        Cliente cliente = null;
        String sql = """
                            SELECT c.*
                            FROM CLIENTE c
                            WHERE c.ID_CLIENTE = ?
                    """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            cliente = getClienteFromResultSet(res);
        }
        return cliente;
    }

    public Cliente getById(Integer id) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {
            Cliente cliente = returnByIdUtil(id);
            return cliente;
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

    private Cliente getClienteFromResultSet(ResultSet res) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(res.getInt("id_cliente"));
        cliente.setNome(res.getString("nome"));
        cliente.setQuantidadeDePedidos(res.getInt("quantidade_pedidos"));
        cliente.setEmail(res.getString("email"));
        return cliente;
    }
}
