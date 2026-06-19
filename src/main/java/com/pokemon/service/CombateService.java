package com.pokemon.service;

import com.pokemon.model.Combate;
import com.pokemon.model.Entrenador;
import com.pokemon.repository.CombateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CombateService {

    private final CombateRepository combateRepository;
    private final EntrenadorService entrenadorService;

    @Transactional(readOnly = true)
    public List<Combate> findAll() {
        return combateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Combate findById(Integer id) {
        return combateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Combate no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Combate findByUuid(String uuid) {
        return combateRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Combate no encontrado con UUID: " + uuid));
    }

    @Transactional
    public Combate save(Combate combate) {
        if (combate.getEntrenador() != null && combate.getEntrenador().getId() != null) {
            Entrenador entrenador = entrenadorService.findById(combate.getEntrenador().getId());
            combate.setEntrenador(entrenador);
        }
        return combateRepository.save(combate);
    }

    @Transactional
    public Combate update(Integer id, Combate updatedCombate) {
        Combate existing = findById(id);
        existing.setFechaCombate(updatedCombate.getFechaCombate());
        existing.setResultado(updatedCombate.getResultado());

        if (updatedCombate.getEntrenador() != null && updatedCombate.getEntrenador().getId() != null) {
            Entrenador entrenador = entrenadorService.findById(updatedCombate.getEntrenador().getId());
            existing.setEntrenador(entrenador);
        }

        return combateRepository.save(existing);
    }

    @Transactional
    public void delete(Integer id) {
        Combate combate = findById(id);
        combateRepository.delete(combate);
    }
}
