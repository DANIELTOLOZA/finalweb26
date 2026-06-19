package com.pokemon.service;

import com.pokemon.model.Entrenador;
import com.pokemon.model.Pueblo;
import com.pokemon.repository.EntrenadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;
    private final PuebloService puebloService;

    @Transactional(readOnly = true)
    public List<Entrenador> findAll() {
        return entrenadorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Entrenador findById(Integer id) {
        return entrenadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entrenador no encontrado con ID: " + id));
    }

    @Transactional
    public Entrenador save(Entrenador entrenador) {
        if (entrenador.getPueblo() != null && entrenador.getPueblo().getId() != null) {
            Pueblo pueblo = puebloService.findById(entrenador.getPueblo().getId());
            entrenador.setPueblo(pueblo);
        }
        return entrenadorRepository.save(entrenador);
    }

    @Transactional
    public Entrenador update(Integer id, Entrenador updatedEntrenador) {
        Entrenador existing = findById(id);
        existing.setNombre(updatedEntrenador.getNombre());
        existing.setApellido(updatedEntrenador.getApellido());
        existing.setFechaNacimiento(updatedEntrenador.getFechaNacimiento());
        existing.setFechaVinculacion(updatedEntrenador.getFechaVinculacion());

        if (updatedEntrenador.getPueblo() != null && updatedEntrenador.getPueblo().getId() != null) {
            Pueblo pueblo = puebloService.findById(updatedEntrenador.getPueblo().getId());
            existing.setPueblo(pueblo);
        }

        return entrenadorRepository.save(existing);
    }

    @Transactional
    public void delete(Integer id) {
        Entrenador entrenador = findById(id);
        entrenadorRepository.delete(entrenador);
    }
}
