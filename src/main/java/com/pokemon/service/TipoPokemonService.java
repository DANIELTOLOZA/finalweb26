package com.pokemon.service;

import com.pokemon.model.TipoPokemon;
import com.pokemon.repository.TipoPokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoPokemonService {

    private final TipoPokemonRepository tipoPokemonRepository;

    @Transactional(readOnly = true)
    public List<TipoPokemon> findAll() {
        return tipoPokemonRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TipoPokemon findById(Integer id) {
        return tipoPokemonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de Pokémon no encontrado con ID: " + id));
    }

    @Transactional
    public TipoPokemon save(TipoPokemon tipoPokemon) {
        return tipoPokemonRepository.save(tipoPokemon);
    }

    @Transactional
    public TipoPokemon update(Integer id, TipoPokemon updatedTipo) {
        TipoPokemon existing = findById(id);
        existing.setDescripcion(updatedTipo.getDescripcion());
        return tipoPokemonRepository.save(existing);
    }

    @Transactional
    public void delete(Integer id) {
        TipoPokemon tipo = findById(id);
        tipoPokemonRepository.delete(tipo);
    }
}
