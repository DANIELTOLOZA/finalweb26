package com.pokemon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "pokemon", schema = "pokemon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del pokemon es obligatorio")
    @Size(max = 100, message = "El nombre del pokemon no puede exceder los 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_pokemon", nullable = false)
    @NotNull(message = "El tipo de pokemon es obligatorio")
    private TipoPokemon tipoPokemon;

    @NotNull(message = "La fecha de descubrimiento es obligatoria")
    @Column(name = "fecha_descubrimiento", nullable = false)
    private LocalDate fechaDescubrimiento;

    @NotNull(message = "La generación es obligatoria")
    @Min(value = 1, message = "La generación debe ser al menos 1")
    @Column(nullable = false)
    private Integer generacion;

    @Column(nullable = false, length = 100)
    private String uuid;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null || this.uuid.isEmpty()) {
            this.uuid = java.util.UUID.randomUUID().toString();
        }
    }
}
