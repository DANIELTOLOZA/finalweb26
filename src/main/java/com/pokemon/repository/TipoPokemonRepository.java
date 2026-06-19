package com.pokemon.repository;

import com.pokemon.model.TipoPokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TipoPokemonRepository extends JpaRepository<TipoPokemon, Integer> {
    Optional<TipoPokemon> findByUuid(String uuid);
}
