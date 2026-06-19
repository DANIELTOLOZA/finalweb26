package com.pokemon.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CapturaId implements Serializable {

    @Column(name = "pokemon_id")
    private Integer pokemonId;

    @Column(name = "entrenador_id")
    private Integer entrenadorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapturaId that = (CapturaId) o;
        return Objects.equals(pokemonId, that.pokemonId) &&
               Objects.equals(entrenadorId, that.entrenadorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pokemonId, entrenadorId);
    }
}
