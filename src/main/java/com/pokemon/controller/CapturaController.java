package com.pokemon.controller;

import com.pokemon.model.Captura;
import com.pokemon.service.CapturaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/capturas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CapturaController {

    private final CapturaService capturaService;

    @GetMapping
    public List<Captura> getAll() {
        return capturaService.findAll();
    }

    @GetMapping("/entrenador/{id}")
    public List<Captura> getByEntrenador(@PathVariable Integer id) {
        return capturaService.findByEntrenadorId(id);
    }

    @GetMapping("/pokemon/{id}")
    public List<Captura> getByPokemon(@PathVariable Integer id) {
        return capturaService.findByPokemonId(id);
    }

    @PostMapping
    public ResponseEntity<Captura> create(@Valid @RequestBody CapturaRequest request) {
        Captura captura = capturaService.registrarCaptura(request.getPokemonId(), request.getEntrenadorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(captura);
    }

    @DeleteMapping("/pokemon/{pokemonId}/entrenador/{entrenadorId}")
    public ResponseEntity<Void> release(@PathVariable Integer pokemonId, @PathVariable Integer entrenadorId) {
        capturaService.liberarPokemon(pokemonId, entrenadorId);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class CapturaRequest {
        @NotNull(message = "El ID del pokemon es obligatorio")
        private Integer pokemonId;

        @NotNull(message = "El ID del entrenador es obligatorio")
        private Integer entrenadorId;
    }
}
