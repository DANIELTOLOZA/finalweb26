package com.pokemon.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "captura", schema = "pokemon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Captura {

    @EmbeddedId
    private CapturaId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("pokemonId")
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("entrenadorId")
    @JoinColumn(name = "entrenador_id", nullable = false)
    private Entrenador entrenador;
}
