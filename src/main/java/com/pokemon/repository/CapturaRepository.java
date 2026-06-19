package com.pokemon.repository;

import com.pokemon.model.Captura;
import com.pokemon.model.CapturaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CapturaRepository extends JpaRepository<Captura, CapturaId> {
    List<Captura> findByIdEntrenadorId(Integer entrenadorId);
    List<Captura> findByIdPokemonId(Integer pokemonId);
}
