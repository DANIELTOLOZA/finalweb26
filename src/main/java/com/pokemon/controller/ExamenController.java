package com.pokemon.controller;

import com.pokemon.model.*;
import com.pokemon.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExamenController {

    private final EntrenadorService entrenadorService;
    private final PokemonService pokemonService;
    private final TipoPokemonService tipoPokemonService;
    private final CapturaService capturaService;
    private final PuebloService puebloService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    // 1. POST /entrenador
    @PostMapping("/entrenador")
    public ResponseEntity<Map<String, String>> registrarEntrenador(@RequestBody Map<String, String> request) {
        String nombre = request.get("nombre");
        String fechaDescStr = request.get("fecha_descubrimiento");

        LocalDate fechaVinculacion = parseDate(fechaDescStr);

        // Buscar pueblo por defecto
        List<Pueblo> pueblos = puebloService.findAll();
        Pueblo defaultPueblo = pueblos.isEmpty() ? null : pueblos.get(0);

        Entrenador entrenador = Entrenador.builder()
                .nombre(nombre != null ? nombre : "Programador")
                .apellido("Examen")
                .fechaNacimiento(LocalDate.of(2000, 1, 1))
                .fechaVinculacion(fechaVinculacion)
                .pueblo(defaultPueblo)
                .build();

        Entrenador guardado = entrenadorService.save(entrenador);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("id", String.valueOf(guardado.getId()));
        response.put("nombre", guardado.getNombre());
        response.put("fecha_descubrimiento", formatDate(guardado.getFechaVinculacion()));
        response.put("generacion", "1");
        response.put("uuid", guardado.getUuid());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. GET /entrenador/{uuid}
    @GetMapping("/entrenador/{uuid}")
    public ResponseEntity<Map<String, String>> obtenerEntrenador(@PathVariable String uuid) {
        Entrenador entrenador = entrenadorService.findAll().stream()
                .filter(e -> uuid.equals(e.getUuid()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Entrenador no encontrado con UUID: " + uuid));

        Map<String, String> response = new LinkedHashMap<>();
        response.put("id", String.valueOf(entrenador.getId()));
        response.put("nombre", entrenador.getNombre());
        response.put("fecha_descubrimiento", formatDate(entrenador.getFechaVinculacion()));
        response.put("generacion", "1");
        response.put("uuid", entrenador.getUuid());

        return ResponseEntity.ok(response);
    }

    // 3. DELETE /entrenador/{uuid}
    @DeleteMapping("/entrenador/{uuid}")
    public ResponseEntity<Map<String, Object>> eliminarEntrenador(@PathVariable String uuid) {
        Entrenador entrenador = entrenadorService.findAll().stream()
                .filter(e -> uuid.equals(e.getUuid()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Entrenador no encontrado con UUID: " + uuid));

        entrenadorService.delete(entrenador.getId());
        return ResponseEntity.ok(Collections.emptyMap());
    }

    // 4. GET /pokemon/{id}
    @GetMapping("/pokemon/{id}")
    public ResponseEntity<List<Map<String, String>>> listarPokemonsConId(@PathVariable String id) {
        return ResponseEntity.ok(getPokemonsMapped());
    }

    // Endpoint complementario sin ID para listar todos
    @GetMapping("/pokemon")
    public ResponseEntity<List<Map<String, String>>> listarPokemons() {
        return ResponseEntity.ok(getPokemonsMapped());
    }

    // 5. POST /pokemons
    @PostMapping({"/pokemons", "/pokemon"})
    public ResponseEntity<Pokemon> registrarPokemonExamen(@RequestBody Map<String, String> request) {
        String nombre = request.get("nombre");
        String tipoDescripcion = request.get("tipo");

        // Resolver o crear el Tipo de Pokémon
        TipoPokemon tipo = tipoPokemonService.findAll().stream()
                .filter(t -> t.getDescripcion().equalsIgnoreCase(tipoDescripcion))
                .findFirst()
                .orElseGet(() -> tipoPokemonService.save(
                        TipoPokemon.builder()
                                .descripcion(tipoDescripcion != null ? tipoDescripcion : "Desconocido")
                                .build()
                ));

        Pokemon pokemon = Pokemon.builder()
                .nombre(nombre != null ? nombre : "Charmander")
                .descripcion("Registrado mediante el endpoint de examen.")
                .tipoPokemon(tipo)
                .fechaDescubrimiento(LocalDate.now())
                .generacion(1)
                .build();

        Pokemon guardado = pokemonService.save(pokemon);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    private List<Map<String, String>> getPokemonsMapped() {
        List<Pokemon> pokemons = pokemonService.findAll();
        List<Map<String, String>> response = new ArrayList<>();

        for (Pokemon p : pokemons) {
            List<Captura> captures = capturaService.findByPokemonId(p.getId());
            String trainerName = "Salvaje";
            if (!captures.isEmpty()) {
                trainerName = captures.get(0).getEntrenador().getNombre();
            }

            Map<String, String> item = new LinkedHashMap<>();
            item.put("id", String.valueOf(p.getId()));
            item.put("nombre", p.getNombre());
            item.put("fecha_avistamiento", formatDate(p.getFechaDescubrimiento()));
            item.put("tipo", p.getTipoPokemon() != null ? p.getTipoPokemon().getDescripcion() : "Desconocido");
            item.put("nombre_entrenador", trainerName);

            response.add(item);
        }
        return response;
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return LocalDate.now();
        try {
            if (dateStr.contains("/")) {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            } else if (dateStr.contains("-")) {
                return LocalDate.parse(dateStr);
            }
        } catch (Exception e) {
            // fallback
        }
        return LocalDate.now();
    }

    private String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_FORMATTER);
    }
}
