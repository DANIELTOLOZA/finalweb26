package com.pokemon.service;

import com.pokemon.model.Pueblo;
import com.pokemon.repository.PuebloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PuebloService {

    private final PuebloRepository puebloRepository;

    @Transactional(readOnly = true)
    public List<Pueblo> findAll() {
        return puebloRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Pueblo findById(Integer id) {
        return puebloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pueblo no encontrado con ID: " + id));
    }

    @Transactional
    public Pueblo save(Pueblo pueblo) {
        return puebloRepository.save(pueblo);
    }

    @Transactional
    public Pueblo update(Integer id, Pueblo updatedPueblo) {
        Pueblo existing = findById(id);
        existing.setNombre(updatedPueblo.getNombre());
        return puebloRepository.save(existing);
    }

    @Transactional
    public void delete(Integer id) {
        Pueblo pueblo = findById(id);
        puebloRepository.delete(pueblo);
    }
}
