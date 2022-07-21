package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.config.ConexaoBancoDeDados;
import br.com.vemser.petshop.entity.ClienteEntity;
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

    public ClienteEntity adicionar(ClienteEntity clienteEntity) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            clienteEntity.setIdCliente(this.nextSeq());

            String sql = "INSERT INTO CLIENTE\n" +
                    "(ID_CLIENTE, NOME, QUANTIDADE_PEDIDOS, EMAIL)\n" +
                    "VALUES(?, ?, ?, ?)\n";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, clienteEntity.getIdCliente());
            stmt.setString(2, clienteEntity.getNome());
            stmt.setInt(3, clienteEntity.getQuantidadeDePedidos());
            stmt.setString(4, clienteEntity.getEmail());


            if (stmt.executeUpdate() != 0) {
                log.info("Cliente adicionado com sucesso");
                return clienteEntity;
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

    public ClienteEntity update(Integer id, ClienteEntity clienteEntity) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        ClienteEntity clienteEntityAtualizado;
        try {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE clienteEntity SET\n");
            if (clienteEntity.getNome() != null) {
                sql.append("nome = ?,");
            }
            if (clienteEntity.getQuantidadeDePedidos() != null) {
                sql.append(" QUANTIDADE_PEDIDOS = ?,");
            }
            if (clienteEntity.getEmail() != null) {
                sql.append(" email = ?\n");
            }
            sql.append("WHERE id_cliente = ? ");

            PreparedStatement stmt = connection.prepareStatement(sql.toString());

            if (clienteEntity.getNome() != null) {
                stmt.setString(1, clienteEntity.getNome());
            }
            if (clienteEntity.getQuantidadeDePedidos() != null) {
                stmt.setInt(2, clienteEntity.getQuantidadeDePedidos());
            }
            if (clienteEntity.getEmail() != null) {
                stmt.setString(3, clienteEntity.getEmail());
            }
            stmt.setInt(4, id);
            // Executa-se a consulta
            if (stmt.executeUpdate() > 0) {
                log.info("Cliente editado com sucesso");
                clienteEntityAtualizado = returnByIdUtil(id);
                return clienteEntityAtualizado;
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

    public List<ClienteEntity> listar() throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<ClienteEntity> clienteEntities = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();

            String sql = "SELECT * FROM CLIENTE";

            // Executa-se a consulta
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                ClienteEntity clienteEntity = new ClienteEntity();
                clienteEntity.setIdCliente(res.getInt("ID_CLIENTE"));
                clienteEntity.setNome(res.getString("NOME"));
                clienteEntity.setQuantidadeDePedidos(res.getInt("QUANTIDADE_PEDIDOS"));
                clienteEntity.setEmail(res.getString("EMAIL"));
                clienteEntities.add(clienteEntity);
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
        return clienteEntities;
    }

    public ClienteEntity returnByIdUtil(Integer id) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        ClienteEntity clienteEntity = null;
        String sql = """
                            SELECT c.*
                            FROM CLIENTE c
                            WHERE c.ID_CLIENTE = ?
                    """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            clienteEntity = getClienteFromResultSet(res);
        }
        return clienteEntity;
    }

    private ClienteEntity getClienteFromResultSet(ResultSet res) throws SQLException {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setIdCliente(res.getInt("id_cliente"));
        clienteEntity.setNome(res.getString("nome"));
        clienteEntity.setQuantidadeDePedidos(res.getInt("quantidade_pedidos"));
        clienteEntity.setEmail(res.getString("email"));
        return clienteEntity;
    }
}
