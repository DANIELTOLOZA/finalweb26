package com.pokemon.controller;

import com.pokemon.model.Pokemon;
import com.pokemon.service.PokemonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pokemons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PokemonController {

    private final PokemonService pokemonService;

    @GetMapping
    public List<Pokemon> getAll() {
        return pokemonService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pokemon> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(pokemonService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Pokemon> create(@Valid @RequestBody Pokemon pokemon) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pokemonService.save(pokemon));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pokemon> update(@PathVariable Integer id, @Valid @RequestBody Pokemon pokemon) {
        return ResponseEntity.ok(pokemonService.update(id, pokemon));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pokemonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
