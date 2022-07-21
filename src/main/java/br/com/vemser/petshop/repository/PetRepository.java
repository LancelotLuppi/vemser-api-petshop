package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.config.ConexaoBancoDeDados;
import br.com.vemser.petshop.entity.PetEntity;
import br.com.vemser.petshop.enums.TipoPet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PetRepository {

    @Autowired
    private ConexaoBancoDeDados conexaoBancoDeDados;

    public Integer nextSeq() throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        String sql = "SELECT SEQ_ID_ANIMAL.nextval SEQ_ID_ANIMAL from DUAL";
        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        if (res.next()) {
            return res.getInt("SEQ_ID_ANIMAL");
        }
        return null;
    }

    public PetEntity adicionar(Integer idCliente, PetEntity petEntity) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            Integer proximoId = this.nextSeq();
            petEntity.setIdPet(proximoId);

            String sql = """
                    INSERT INTO ANIMAL\s
                    (ID_ANIMAL, ID_CLIENTE, NOME, TIPO, RACA, PELAGEM, PORTE, IDADE)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, petEntity.getIdPet());
            stmt.setInt(2, idCliente);
            stmt.setString(3, petEntity.getNome());
            stmt.setString(4, petEntity.getTipoPet().toString());
            stmt.setString(5, petEntity.getRaca());
            stmt.setInt(6, petEntity.getPelagem());
            stmt.setInt(7, petEntity.getPorte());
            stmt.setInt(8, petEntity.getIdade());

            stmt.executeUpdate();
            return petEntity;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return petEntity;
    }

    public boolean remover(Integer id) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            String sql = "DELETE FROM ANIMAL WHERE ID_ANIMAL = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

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

    public PetEntity update(Integer id, PetEntity petEntity) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        PetEntity petEntityAtualizado;
        try {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE animal SET \n");
            if(petEntity.getNome() != null) {
                sql.append("nome = ?,");
            }
            if(petEntity.getTipoPet() != null) {
                sql.append("tipo = ?,");
            }
            if(petEntity.getRaca() != null) {
                sql.append("raca = ?,");
            }
            if(petEntity.getPelagem() != null) {
                sql.append("pelagem = ?,");
            }
            if(petEntity.getPorte() != null) {
                sql.append("porte = ?,");
            }
            if(petEntity.getIdade() != null) {
                sql.append("idade = ?,");
            }

            sql.deleteCharAt(sql.length() - 1);
            sql.append("WHERE id_animal = ? and animal.id_cliente = ?");

            PreparedStatement stmt = connection.prepareStatement(sql.toString());

            int index =1;
            if(petEntity.getNome() != null) {
                stmt.setString(index++, petEntity.getNome());
            }
            if(petEntity.getTipoPet() != null) {
                stmt.setString(index++, petEntity.getTipoPet().toString());
            }
            if(petEntity.getRaca() != null) {
                stmt.setString(index++, petEntity.getRaca());
            }
            if(petEntity.getPelagem() != null) {
                stmt.setInt(index++, petEntity.getPelagem());
            }
            if(petEntity.getPorte() != null) {
                stmt.setInt(index++, petEntity.getPorte());
            }
            if(petEntity.getIdade() != null) {
                stmt.setInt(index++, petEntity.getIdade());
            }
            stmt.setInt(index++, id);
            stmt.setInt(index, petEntity.getIdCliente());

            if(stmt.executeUpdate() > 0) {
                petEntityAtualizado = returnByIdUtil(id);
                return petEntityAtualizado;
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

    public List<PetEntity> listar() throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<PetEntity> animais = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ANIMAL";

            Statement stmt = connection.prepareStatement(sql);
            ResultSet res = stmt.executeQuery(sql);

            while(res.next()) {
                PetEntity petEntity = getPetFromResultSet(res);
                animais.add(petEntity);
            }
            return animais;
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
        return animais;
    }

    public List<PetEntity> listarAnimalPorCliente(Integer id) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<PetEntity> petEntities = new ArrayList<>();
        try {
            String sql = """
                                SELECT a.*
                                FROM ANIMAL a
                                WHERE a.ID_CLIENTE = ?
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            while(res.next()) {
                PetEntity petEntity = getPetFromResultSet(res);
                petEntity.setIdCliente(id);
                petEntities.add(petEntity);
            }
            return petEntities;
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
        return petEntities;
    }

    public PetEntity returnByIdUtil(Integer id) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        PetEntity petEntity = null;
        String sql = """
                            SELECT a.*
                            FROM ANIMAL a
                            WHERE a.ID_ANIMAL = ?
                """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            petEntity = getPetFromResultSet(res);
        }
        return petEntity;
    }

    public PetEntity getPetPorId(Integer idPet) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        PetEntity animal = null;
        try {
            String sql = """
                                SELECT a.*
                                FROM ANIMAL a
                                WHERE a.ID_ANIMAL = ?
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPet);

            ResultSet res = stmt.executeQuery();

            if(res.next()) {
                animal = getPetFromResultSet(res);
            }
            return animal;
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

    public void removerPetPorIDCliente(Integer id) throws  SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            String sql = "DELETE FROM ANIMAL WHERE ID_CLIENTE = ?";

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

    private PetEntity getPetFromResultSet(ResultSet res) throws SQLException {
        PetEntity petEntity = new PetEntity();
        petEntity.setIdCliente(res.getInt("ID_CLIENTE"));
        petEntity.setIdPet(res.getInt("ID_ANIMAL"));
        petEntity.setNome(res.getString("NOME"));
        petEntity.setTipoPet(TipoPet.valueOf(res.getString("TIPO")));
        petEntity.setRaca(res.getString("RACA"));
        petEntity.setPelagem(res.getInt("PELAGEM"));
        petEntity.setPorte(res.getInt("PORTE"));
        petEntity.setIdade(res.getInt("IDADE"));
        return petEntity;
    }
}
