package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Integer> {
}
