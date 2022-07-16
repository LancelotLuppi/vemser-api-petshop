package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.entity.Contato;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoRepository {

    @Autowired
    private Connection connection;

    public Integer nextSeq(){
        try {
            String sql = "SELECT seq_id_contato.nextval seqContato from DUAL";
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            if (res.next()) {
                return res.getInt("seqContato");
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

    public Contato adicionar(Contato contato) throws SQLException {
        try {
            Integer proximoId = this.nextSeq();
            contato.setIdContato(proximoId);

            String sql = "INSERT INTO CONTATO\n" +
                    "(ID_CONTATO, ID_CLIENTE, TELEFONE, DESCRICAO)\n" +
                    "VALUES(?, ?, ?, ?)\n";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, contato.getIdContato());
            stmt.setInt(2, contato.getIdCliente());
            stmt.setInt(3, contato.getTelefone());
            stmt.setString(4, contato.getDescricao());

            int res = stmt.executeUpdate();
            System.out.println("adicionarContato.res=" + res);
            return contato;
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

    public boolean remover(Integer id) throws SQLException {
        try {

            String sql = "DELETE FROM CONTATO WHERE ID_CONTATO = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);

            int res = stmt.executeUpdate();
            System.out.println("removerContatoPorId.res=" + res);

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

    public boolean editar(Integer id, Contato contato) throws SQLException {
        try {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE contato SET \n");

            sql.append(" id_pessoa = ?,");

            if (contato.getTelefone() != null) {
                sql.append(" numero = ?,");
            }

            if (contato.getDescricao() != null) {
                sql.append(" descricao = ?,");
            }

            sql.deleteCharAt(sql.length() - 1); //remove o ultimo ','
            sql.append(" WHERE id_contato = ? ");

            PreparedStatement stmt = connection.prepareStatement(sql.toString());

            int index = 1;
            stmt.setInt(index++, contato.getIdCliente());
            if (contato.getTelefone() != null) {
                stmt.setInt(index++, contato.getTelefone());
            }
            if (contato.getDescricao() != null) {
                stmt.setString(index++, contato.getDescricao());
            }

            stmt.setInt(index++, id);

            // Executa-se a consulta
            int res = stmt.executeUpdate();
            System.out.println("editarContato.res=" + res);

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

    public List<Contato> listar() throws SQLException {
        List<Contato> contatos = new ArrayList<>();
        try {

            Statement stmt = connection.createStatement();

            String sql = "SELECT CTT.*, " +
                    "                    C.NOME AS NOME_CLIENTE " +
                    "               FROM CONTATO CTT " +
                    "               LEFT JOIN CLIENTE C ON (C.ID_CLIENTE = CTT.ID_CLIENTE) ";

            // Executa-se a consulta
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Contato contato = getContatoFromResultSet(res);
                contatos.add(contato);
            }
            return contatos;
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

    public List<Contato> listarContatosPorCliente(Integer idCliente) throws SQLException {
        List<Contato> contatos = new ArrayList<>();

        try {


            String sql = "SELECT ctt.* " +
                    "                  , ctt.NOME AS NOME_PESSOA " +
                    "               FROM CONTATO ctt " +
                    "             INNER JOIN CLIENTE c ON (c.ID_CLIENTE = ctt.ID_CLIENTE) " +
                    "             WHERE ctt.ID_CLIENTE = ? ";

            // Executa-se a consulta
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCliente);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                Contato contato = getContatoFromResultSet(res);
                contatos.add(contato);
            }
            return contatos;
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

    private Contato getContatoFromResultSet(ResultSet res) throws SQLException {
        Contato contato = new Contato();
        contato.setIdContato(res.getInt("id_Contato"));
        Cliente cliente = new Cliente();
        cliente.setNome(res.getString("nome"));
        cliente.setIdCliente(res.getInt("id_cliente"));
        contato.setTelefone(res.getInt("telefone"));
        contato.setDescricao(res.getString("descricao"));
        return contato;
    }
}

