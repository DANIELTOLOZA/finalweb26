package com.pokemon.controller;

import com.pokemon.model.Entrenador;
import com.pokemon.service.EntrenadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/entrenadores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    @GetMapping
    public List<Entrenador> getAll() {
        return entrenadorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrenador> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(entrenadorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Entrenador> create(@Valid @RequestBody Entrenador entrenador) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entrenadorService.save(entrenador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrenador> update(@PathVariable Integer id, @Valid @RequestBody Entrenador entrenador) {
        return ResponseEntity.ok(entrenadorService.update(id, entrenador));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        entrenadorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
