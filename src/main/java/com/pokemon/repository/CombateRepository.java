package com.pokemon.repository;

import com.pokemon.model.Combate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CombateRepository extends JpaRepository<Combate, Integer> {
    Optional<Combate> findByUuid(String uuid);
    List<Combate> findByEntrenadorId(Integer entrenadorId);
}
