package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {
}
