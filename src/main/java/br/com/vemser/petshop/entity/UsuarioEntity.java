package br.com.vemser.petshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity(name = "usuario")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USUARIO_SEQUENCIA")
    @SequenceGenerator(name = "USUARIO_SEQUENCIA", sequenceName = "seq_usuario", allocationSize = 1)
    private Integer idUsuario;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "username")
    private String username;

    @Column(name = "senha")
    private String senha;


    private Set<CargoEntity> cargos;
}
