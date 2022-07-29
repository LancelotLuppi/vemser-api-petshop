package br.com.vemser.petshop.repository;

import br.com.vemser.petshop.entity.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<CargoEntity, Integer> {
}
