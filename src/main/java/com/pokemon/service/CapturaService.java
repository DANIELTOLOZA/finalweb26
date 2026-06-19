package com.pokemon.service;

import com.pokemon.model.Captura;
import com.pokemon.model.CapturaId;
import com.pokemon.model.Entrenador;
import com.pokemon.model.Pokemon;
import com.pokemon.repository.CapturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CapturaService {

    private final CapturaRepository capturaRepository;
    private final PokemonService pokemonService;
    private final EntrenadorService entrenadorService;

    @Transactional(readOnly = true)
    public List<Captura> findAll() {
        return capturaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Captura> findByEntrenadorId(Integer entrenadorId) {
        return capturaRepository.findByIdEntrenadorId(entrenadorId);
    }

    @Transactional(readOnly = true)
    public List<Captura> findByPokemonId(Integer pokemonId) {
        return capturaRepository.findByIdPokemonId(pokemonId);
    }

    @Transactional
    public Captura registrarCaptura(Integer pokemonId, Integer entrenadorId) {
        Pokemon pokemon = pokemonService.findById(pokemonId);
        Entrenador entrenador = entrenadorService.findById(entrenadorId);

        CapturaId id = new CapturaId(pokemonId, entrenadorId);
        if (capturaRepository.existsById(id)) {
            throw new IllegalArgumentException("Este Pokémon ya ha sido capturado por este entrenador.");
        }

        Captura captura = Captura.builder()
                .id(id)
                .pokemon(pokemon)
                .entrenador(entrenador)
                .build();

        return capturaRepository.save(captura);
    }

    @Transactional
    public void liberarPokemon(Integer pokemonId, Integer entrenadorId) {
        CapturaId id = new CapturaId(pokemonId, entrenadorId);
        if (!capturaRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró el registro de captura especificado.");
        }
        capturaRepository.deleteById(id);
    }
}
