package com.pokemon.controller;

import com.pokemon.model.Pueblo;
import com.pokemon.service.PuebloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pueblos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PuebloController {

    private final PuebloService puebloService;

    @GetMapping
    public List<Pueblo> getAll() {
        return puebloService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pueblo> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(puebloService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Pueblo> create(@Valid @RequestBody Pueblo pueblo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(puebloService.save(pueblo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pueblo> update(@PathVariable Integer id, @Valid @RequestBody Pueblo pueblo) {
        return ResponseEntity.ok(puebloService.update(id, pueblo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        puebloService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
