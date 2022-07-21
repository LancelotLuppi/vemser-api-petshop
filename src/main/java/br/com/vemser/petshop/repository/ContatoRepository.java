package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.config.ConexaoBancoDeDados;
import br.com.vemser.petshop.entity.ContatoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContatoRepository {

    @Autowired
    private ConexaoBancoDeDados conexaoBancoDeDados;

    public Integer nextSeq() throws SQLException{
        Connection connection = conexaoBancoDeDados.getConnection();
        String sql = "SELECT seq_id_contato.nextval seqContato from DUAL";
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        if (res.next()) {
            return res.getInt("seqContato");
        }
        return null;
    }

    public ContatoEntity adicionar(Integer idCliente, ContatoEntity contatoEntity) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {
            Integer proximoId = this.nextSeq();
            contatoEntity.setIdContato(proximoId);

            String sql = "INSERT INTO CONTATO\n" +
                    "(ID_CONTATO, ID_CLIENTE, TELEFONE, DESCRICAO)\n" +
                    "VALUES(?, ?, ?, ?)\n";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, contatoEntity.getIdContato());
            stmt.setInt(2, idCliente);
            stmt.setString(3, contatoEntity.getTelefone());
            stmt.setString(4, contatoEntity.getDescricao());

            int res = stmt.executeUpdate();
            return contatoEntity;
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
        return contatoEntity;
    }

    public void remover(Integer id) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            String sql = "DELETE FROM CONTATO WHERE ID_CONTATO = ?";

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

    public ContatoEntity atualizar(Integer id, ContatoEntity contatoEntity) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        ContatoEntity contatoEntityAtualizado;
        try {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE contatoEntity SET \n");
            if (contatoEntity.getTelefone() != null) {
                sql.append(" telefone = ?,");
            }

            if (contatoEntity.getDescricao() != null) {
                sql.append(" descricao = ?,");
            }

            sql.deleteCharAt(sql.length() - 1);
            sql.append(" WHERE id_contato = ? and contatoEntity.id_cliente = ?");

            PreparedStatement stmt = connection.prepareStatement(sql.toString());

            int index = 1;
            if (contatoEntity.getTelefone() != null) {
                stmt.setString(index++, contatoEntity.getTelefone());
            }
            if (contatoEntity.getDescricao() != null) {
                stmt.setString(index++, contatoEntity.getDescricao());
            }
            stmt.setInt(index++, id);
            stmt.setInt(index, contatoEntity.getIdCliente());

            if(stmt.executeUpdate() > 0){
                contatoEntityAtualizado = returnByIdUtil(id);
                return contatoEntityAtualizado;
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

    public List<ContatoEntity> listar() throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<ContatoEntity> contatoEntities = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();

            String sql = "SELECT * FROM CONTATO";

            // Executa-se a consulta
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                ContatoEntity contatoEntity = new ContatoEntity();
                contatoEntity.setIdContato(res.getInt("ID_CONTATO"));
                contatoEntity.setIdCliente(res.getInt("ID_CLIENTE"));
                contatoEntity.setTelefone(res.getString("TELEFONE"));
                contatoEntity.setDescricao(res.getString("DESCRICAO"));
                contatoEntities.add(contatoEntity);
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
        return contatoEntities;
    }

    public List<ContatoEntity> listarContatosPorCliente(Integer idCliente) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<ContatoEntity> contatoEntities = new ArrayList<>();
        try {


            String sql = """
                                SELECT ctt.*
                                , c.NOME
                                FROM CONTATO ctt
                                INNER JOIN CLIENTE c ON (c.ID_CLIENTE = ctt.ID_CLIENTE)
                                WHERE ctt.ID_CLIENTE = ?
                    """;
            // Executa-se a consulta
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCliente);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                ContatoEntity contatoEntity = getContatoFromResultSet(res);
                contatoEntity.setIdCliente(idCliente);
                contatoEntities.add(contatoEntity);
            }
            return contatoEntities;
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
        return contatoEntities;
    }

    public void removerContatosPorIDCliente(Integer id) throws  SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            String sql = "DELETE FROM CONTATO WHERE ID_CLIENTE = ?";

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

    public ContatoEntity returnByIdUtil(Integer id) throws SQLException{
        Connection connection = conexaoBancoDeDados.getConnection();
        ContatoEntity contatoEntity = null;
        String sql = """
                            SELECT ctt.*
                            FROM CONTATO ctt
                            WHERE ctt.ID_CONTATO = ?
                """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            contatoEntity = getContatoFromResultSet(res);
        }
        return contatoEntity;
    }

    private ContatoEntity getContatoFromResultSet(ResultSet res) throws SQLException {
        ContatoEntity contatoEntity = new ContatoEntity();
        contatoEntity.setIdCliente(res.getInt("ID_CLIENTE"));
        contatoEntity.setIdContato(res.getInt("ID_CONTATO"));
        contatoEntity.setTelefone(res.getString("TELEFONE"));
        contatoEntity.setDescricao(res.getString("DESCRICAO"));
        return contatoEntity;
    }
}


