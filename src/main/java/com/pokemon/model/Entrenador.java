package com.pokemon.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "entrenador", schema = "pokemon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    @Column(nullable = false, length = 50)
    private String apellido;

    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    @Column(unique = true, length = 100)
    private String email;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull(message = "La fecha de vinculación es obligatoria")
    @Column(name = "fecha_vinculacion", nullable = false)
    private LocalDate fechaVinculacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pueblo_id", nullable = false)
    @NotNull(message = "El pueblo de origen es obligatorio")
    private Pueblo pueblo;

    @Column(nullable = false, length = 100)
    private String uuid;

    public Integer getId() { return this.id; }
    public String getNombre() { return this.nombre; }
    public String getUuid() { return this.uuid; }
    public java.time.LocalDate getFechaVinculacion() { return this.fechaVinculacion; }
    public String getEmail() { return this.email; }

    @PrePersist
    public void prePersist() {
        if (this.uuid == null || this.uuid.isEmpty()) {
            this.uuid = java.util.UUID.randomUUID().toString();
        }
    }
}
