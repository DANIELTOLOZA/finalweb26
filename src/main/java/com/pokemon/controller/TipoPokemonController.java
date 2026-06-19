package com.pokemon.controller;

import com.pokemon.model.TipoPokemon;
import com.pokemon.service.TipoPokemonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TipoPokemonController {

    private final TipoPokemonService tipoPokemonService;

    @GetMapping
    public List<TipoPokemon> getAll() {
        return tipoPokemonService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoPokemon> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoPokemonService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TipoPokemon> create(@Valid @RequestBody TipoPokemon tipoPokemon) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoPokemonService.save(tipoPokemon));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoPokemon> update(@PathVariable Integer id, @Valid @RequestBody TipoPokemon tipoPokemon) {
        return ResponseEntity.ok(tipoPokemonService.update(id, tipoPokemon));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoPokemonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
