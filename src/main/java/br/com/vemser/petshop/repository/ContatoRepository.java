package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.config.ConexaoBancoDeDados;
import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.entity.Contato;
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

    public Contato adicionar(Integer idCliente, Contato contato) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {
            Integer proximoId = this.nextSeq();
            contato.setIdContato(proximoId);

            String sql = "INSERT INTO CONTATO\n" +
                    "(ID_CONTATO, ID_CLIENTE, TELEFONE, DESCRICAO)\n" +
                    "VALUES(?, ?, ?, ?)\n";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, contato.getIdContato());
            stmt.setInt(2, idCliente);
            stmt.setString(3, contato.getTelefone());
            stmt.setString(4, contato.getDescricao());

            int res = stmt.executeUpdate();
            return contato;
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
        return contato;
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

    public Contato atualizar(Integer id, Contato contato) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        Contato contatoAtualizado;
        try {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE contato SET \n");
            if (contato.getTelefone() != null) {
                sql.append(" telefone = ?,");
            }

            if (contato.getDescricao() != null) {
                sql.append(" descricao = ?,");
            }

            sql.deleteCharAt(sql.length() - 1);
            sql.append(" WHERE id_contato = ? and contato.id_cliente = ?");

            PreparedStatement stmt = connection.prepareStatement(sql.toString());

            int index = 1;
            if (contato.getTelefone() != null) {
                stmt.setString(index++, contato.getTelefone());
            }
            if (contato.getDescricao() != null) {
                stmt.setString(index++, contato.getDescricao());
            }
            stmt.setInt(index++, id);
            stmt.setInt(index, contato.getIdCliente());

            if(stmt.executeUpdate() > 0){
                contatoAtualizado = returnByIdUtil(id);
                return contatoAtualizado;
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

    public List<Contato> listar() throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<Contato> contatos = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();

            String sql = "SELECT * FROM CONTATO";

            // Executa-se a consulta
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Contato contato = new Contato();
                contato.setIdContato(res.getInt("ID_CONTATO"));
                contato.setIdCliente(res.getInt("ID_CLIENTE"));
                contato.setTelefone(res.getString("TELEFONE"));
                contato.setDescricao(res.getString("DESCRICAO"));
                contatos.add(contato);
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
        return contatos;
    }

    public List<Contato> listarContatosPorCliente(Integer idCliente) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<Contato> contatos = new ArrayList<>();
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
                Contato contato = getContatoFromResultSet(res);
                contato.setIdCliente(idCliente);
                contatos.add(contato);
            }
            return contatos;
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
        return contatos;
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

    public Contato returnByIdUtil(Integer id) throws SQLException{
        Connection connection = conexaoBancoDeDados.getConnection();
        Contato contato = null;
        String sql = """
                            SELECT ctt.*
                            FROM CONTATO ctt
                            WHERE ctt.ID_CONTATO = ?
                """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            contato = getContatoFromResultSet(res);
        }
        return contato;
    }

    private Contato getContatoFromResultSet(ResultSet res) throws SQLException {
        Contato contato = new Contato();
        contato.setIdCliente(res.getInt("ID_CLIENTE"));
        contato.setIdContato(res.getInt("ID_CONTATO"));
        contato.setTelefone(res.getString("TELEFONE"));
        contato.setDescricao(res.getString("DESCRICAO"));
        return contato;
    }
}


