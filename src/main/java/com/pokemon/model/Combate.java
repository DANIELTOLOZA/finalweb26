package com.pokemon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "combate", schema = "pokemon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Combate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La fecha del combate es obligatoria")
    @Column(name = "fecha_combate", nullable = false)
    private LocalDate fechaCombate;

    @NotBlank(message = "El resultado es obligatorio")
    @Size(max = 50, message = "El resultado no puede exceder los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String resultado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entrenador_id", nullable = false)
    @NotNull(message = "El entrenador participante es obligatorio")
    private Entrenador entrenador;

    @Column(nullable = false, length = 100)
    private String uuid;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null || this.uuid.isEmpty()) {
            this.uuid = java.util.UUID.randomUUID().toString();
        }
    }
}
