package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.config.ConexaoBancoDeDados;
import br.com.vemser.petshop.entity.Cliente;
import br.com.vemser.petshop.entity.Pet;
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

    public Pet adicionar(Integer idCliente, Pet pet) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        try {

            Integer proximoId = this.nextSeq();
            pet.setIdPet(proximoId);

            String sql = """
                    INSERT INTO ANIMAL\s
                    (ID_ANIMAL, ID_CLIENTE, NOME, TIPO, RACA, PELAGEM, PORTE, IDADE)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pet.getIdPet());
            stmt.setInt(2, idCliente);
            stmt.setString(3, pet.getNome());
            stmt.setString(4, pet.getTipoPet().toString());
            stmt.setString(5, pet.getRaca());
            stmt.setInt(6, pet.getPelagem());
            stmt.setInt(7, pet.getPorte());
            stmt.setInt(8, pet.getIdade());

            int res = stmt.executeUpdate();
            return pet;
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
        return pet;
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

    public Pet update(Integer id, Pet pet) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        Pet petAtualizado;
        try {

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE animal SET \n");
            if(pet.getNome() != null) {
                sql.append("nome = ?,");
            }
            if(pet.getTipoPet() != null) {
                sql.append("tipo = ?,");
            }
            if(pet.getRaca() != null) {
                sql.append("raca = ?,");
            }
            if(pet.getPelagem() != null) {
                sql.append("pelagem = ?,");
            }
            if(pet.getPorte() != null) {
                sql.append("porte = ?,");
            }
            if(pet.getIdade() != null) {
                sql.append("idade = ?,");
            }

            sql.deleteCharAt(sql.length() - 1);
            sql.append("WHERE id_animal = ? and animal.id_cliente = ?");

            PreparedStatement stmt = connection.prepareStatement(sql.toString());

            int index =1;
            if(pet.getNome() != null) {
                stmt.setString(index++, pet.getNome());
            }
            if(pet.getTipoPet() != null) {
                stmt.setString(index++, pet.getTipoPet().toString());
            }
            if(pet.getRaca() != null) {
                stmt.setString(index++, pet.getRaca());
            }
            if(pet.getPelagem() != null) {
                stmt.setInt(index++, pet.getPelagem());
            }
            if(pet.getPorte() != null) {
                stmt.setInt(index++, pet.getPorte());
            }
            if(pet.getIdade() != null) {
                stmt.setInt(index++, pet.getIdade());
            }
            stmt.setInt(index++, id);
            stmt.setInt(index, pet.getIdCliente());

            if(stmt.executeUpdate() > 0) {
                petAtualizado = returnByIdUtil(id);
                return petAtualizado;
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

//    public List<Pet> listar() {
//        List<Pet> animais = new ArrayList<>();
//        try {
//            String sql = "SELECT A.* " +
//                    "       FROM ANIMAL A " +
//                    "       WHERE A.ID_CLIENTE = ? ";
//
//            Statement stmt = connection.prepareStatement(sql);
//            ResultSet res = stmt.executeQuery(sql);
//
//            while(res.next()) {
//                Pet animal = getPetFromResultSet(res);
//                animais.add(animal);
//            }
//            return animais;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(!connection.isClosed()) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        return animais;
//    }

    public List<Pet> listarAnimalPorCliente(Integer id) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        List<Pet> pets = new ArrayList<>();
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
                Pet pet = getPetFromResultSet(res);
                pet.setIdCliente(id);
                pets.add(pet);
            }
            return pets;
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
        return pets;
    }

    public Pet returnByIdUtil(Integer id) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        Pet pet = null;
        String sql = """
                            SELECT a.*
                            FROM ANIMAL a
                            WHERE a.ID_ANIMAL = ?
                """;

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet res = stmt.executeQuery();

        if (res.next()) {
            pet = getPetFromResultSet(res);
        }
        return pet;
    }

    public Pet getPetPorId(int idPet, int idUsuario) throws SQLException {
        Connection connection = conexaoBancoDeDados.getConnection();
        Pet animal = null;
        try {
            String sql = """
                                SELECT a.*
                                FROM ANIMAL a
                                WHERE a.ID_ANIMAL = ?
                                AND a.ID_CLIENTE = ?
                    """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idPet);
            stmt.setInt(2, idUsuario);

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

    private Pet getPetFromResultSet(ResultSet res) throws SQLException {
        Pet pet = new Pet();
        pet.setIdCliente(res.getInt("ID_CLIENTE"));
        pet.setIdPet(res.getInt("ID_ANIMAL"));
        pet.setNome(res.getString("NOME"));
        pet.setTipoPet(TipoPet.valueOf(res.getString("TIPO")));
        pet.setRaca(res.getString("RACA"));
        pet.setPelagem(res.getInt("PELAGEM"));
        pet.setPorte(res.getInt("PORTE"));
        pet.setIdade(res.getInt("IDADE"));
        return pet;
    }
}
