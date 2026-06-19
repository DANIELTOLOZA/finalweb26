package com.pokemon.service;

import com.pokemon.model.Pokemon;
import com.pokemon.model.TipoPokemon;
import com.pokemon.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PokemonService {

    private final PokemonRepository pokemonRepository;
    private final TipoPokemonService tipoPokemonService;

    @Transactional(readOnly = true)
    public List<Pokemon> findAll() {
        return pokemonRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pokemon findById(Integer id) {
        return pokemonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pokémon no encontrado con ID: " + id));
    }

    @Transactional
    public Pokemon save(Pokemon pokemon) {
        if (pokemon.getTipoPokemon() != null && pokemon.getTipoPokemon().getId() != null) {
            TipoPokemon tipo = tipoPokemonService.findById(pokemon.getTipoPokemon().getId());
            pokemon.setTipoPokemon(tipo);
        }
        return pokemonRepository.save(pokemon);
    }

    @Transactional
    public Pokemon update(Integer id, Pokemon updatedPokemon) {
        Pokemon existing = findById(id);
        existing.setNombre(updatedPokemon.getNombre());
        existing.setDescripcion(updatedPokemon.getDescripcion());
        existing.setFechaDescubrimiento(updatedPokemon.getFechaDescubrimiento());
        existing.setGeneracion(updatedPokemon.getGeneracion());

        if (updatedPokemon.getTipoPokemon() != null && updatedPokemon.getTipoPokemon().getId() != null) {
            TipoPokemon tipo = tipoPokemonService.findById(updatedPokemon.getTipoPokemon().getId());
            existing.setTipoPokemon(tipo);
        }

        return pokemonRepository.save(existing);
    }

    @Transactional
    public void delete(Integer id) {
        Pokemon pokemon = findById(id);
        pokemonRepository.delete(pokemon);
    }
}
