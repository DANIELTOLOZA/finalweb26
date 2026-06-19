package com.pokemon;

import com.pokemon.model.*;
import com.pokemon.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PokemonApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PuebloService puebloService;

    @Autowired
    private TipoPokemonService tipoPokemonService;

    @Autowired
    private PokemonService pokemonService;

    @Autowired
    private EntrenadorService entrenadorService;

    @Autowired
    private CapturaService capturaService;

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring cargue correctamente
    }

    @Test
    void testCrearEntidadesYCaptura() {
        Pueblo pueblo = Pueblo.builder()
                .nombre("Pueblo Lavanda")
                .build();
        Pueblo puebloGuardado = puebloService.save(pueblo);
        assertNotNull(puebloGuardado.getId());
        assertNotNull(puebloGuardado.getUuid());

        TipoPokemon tipo = TipoPokemon.builder()
                .descripcion("Fantasma")
                .build();
        TipoPokemon tipoGuardado = tipoPokemonService.save(tipo);
        assertNotNull(tipoGuardado.getId());

        Pokemon pokemon = Pokemon.builder()
                .nombre("Gastly")
                .descripcion("Pokémon gaseoso.")
                .tipoPokemon(tipoGuardado)
                .fechaDescubrimiento(LocalDate.of(1996, 2, 27))
                .generacion(1)
                .build();
        Pokemon pokemonGuardado = pokemonService.save(pokemon);
        assertNotNull(pokemonGuardado.getId());

        Entrenador entrenador = Entrenador.builder()
                .nombre("Gary")
                .apellido("Oak")
                .fechaNacimiento(LocalDate.of(1987, 6, 15))
                .fechaVinculacion(LocalDate.of(1997, 4, 1))
                .pueblo(puebloGuardado)
                .build();
        Entrenador entrenadorGuardado = entrenadorService.save(entrenador);
        assertNotNull(entrenadorGuardado.getId());

        Captura captura = capturaService.registrarCaptura(pokemonGuardado.getId(), entrenadorGuardado.getId());
        assertNotNull(captura);

        List<Captura> capturasGary = capturaService.findByEntrenadorId(entrenadorGuardado.getId());
        assertEquals(1, capturasGary.size());

        capturaService.liberarPokemon(pokemonGuardado.getId(), entrenadorGuardado.getId());
        assertTrue(capturaService.findByEntrenadorId(entrenadorGuardado.getId()).isEmpty());
    }

    @Test
    void testExamenEndpoints() throws Exception {
        // 1. Registrar entrenador (POST /entrenador)
        String entrenadorJson = "{\"nombre\": \"Programador Test\", \"fecha_descubrimiento\": \"2026/01/01\"}";
        
        String responseContent = mockMvc.perform(post("/entrenador")
                .contentType(MediaType.APPLICATION_JSON)
                .content(entrenadorJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Programador Test"))
                .andExpect(jsonPath("$.fecha_descubrimiento").value("2026/01/01"))
                .andExpect(jsonPath("$.generacion").value("1"))
                .andExpect(jsonPath("$.uuid").exists())
                .andReturn().getResponse().getContentAsString();

        // Extraer UUID de la respuesta para probar GET y DELETE
        String uuid = responseContent.split("\"uuid\":\"")[1].split("\"")[0];

        // 2. Obtener entrenador (GET /entrenador/{uuid})
        mockMvc.perform(get("/entrenador/" + uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Programador Test"))
                .andExpect(jsonPath("$.fecha_descubrimiento").value("2026/01/01"));

        // 3. Registrar Pokemon (POST /pokemons)
        String pokemonJson = "{\"nombre\": \"Charmander Test\", \"tipo\": \"Fuego\"}";
        mockMvc.perform(post("/pokemons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pokemonJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Charmander Test"));

        // 4. Listar Pokemons (GET /pokemon/{id})
        mockMvc.perform(get("/pokemon/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // 5. Eliminar entrenador (DELETE /entrenador/{uuid})
        mockMvc.perform(delete("/entrenador/" + uuid))
                .andExpect(status().isOk());
    }
}

