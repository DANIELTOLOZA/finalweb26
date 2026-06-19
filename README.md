# Pokémon Capture Management – README

(Raíz del proyecto: `C:\Users\estudiantes\Desktop\final\finalweb26`)

## 📖 Descripción general

API REST construida con Spring Boot que permite gestionar entrenadores y sus Pokémon.

| Funcionalidad | Endpoint | Descripción |
|---------------|----------|-------------|
| Gestión de entrenadores | `GET /api/entrenadores/{uuid}` | Obtiene un entrenador por su UUID |
| Listar Pokémon de un entrenador | `GET /api/entrenadores/{uuid}/pokemons` | Todos los Pokémon pertenecientes al entrenador |
| Añadir un Pokémon a un entrenador | `POST /api/entrenadores/{uuid}/pokemons/{pokemonUuid}` | Asocia un Pokémon existente al entrenador |
| Otros CRUD | Repositorios estándar de Spring Data JPA para `pokemon`, `pueblo`, `tipo_pokemon`, `captura` | Acceso directo a la base de datos mediante los beans de repositorio generados |
| Base de datos | Supabase PostgreSQL (o H2 local) – el esquema está definido en `src/main/resources/ddl.sql` | Usa el DDL para recrear el esquema localmente |
| Swagger UI | `http://localhost:8080/swagger-ui.html` | Documentación interactiva de la API |
| CORS | Permite cualquier origen (`*`) en desarrollo – configurable en `WebConfig` | Las aplicaciones frontend (React/Vue/Angular) pueden llamar a la API sin errores de origen cruzado |

## 🛠️ Prerrequisitos

| Herramienta | Versión mínima |
|-------------|----------------|
| Java        | 17 (instalado y `JAVA_HOME` configurado) |
| Maven       | 3.9+ |
| Git         | cualquier versión reciente (para commits) |
| Cliente PostgreSQL (opcional) | `psql` para exportación/importación manual de CSV |
| Cuenta en Supabase (opcional) | URL de la DB: `postgresql://postgres:<PWD>@<HOST>:5432/postgres` |
| Node (opcional) | Para experimentos con el frontend – no requerido para el backend |

## 🚀 Primeros pasos

1. **Clonar el repositorio** (si no lo has hecho)

   ```bash
   git clone <repo‑url>
   cd finalweb26
   ```

2. **Configurar la base de datos**

   - **Opción A – Supabase (en la nube)**  
     Edita `src/main/resources/application.yml` → perfil `prod`:

     ```yaml
     spring:
       datasource:
         url: jdbc:postgresql://<HOST>:5432/postgres
         username: postgres
         password: <TU_CONTRASEÑA>
     ```

   - **Opción B – H2 local (en memoria, para pruebas rápidas)**  
     El perfil `dev` ya apunta a una BD H2, solo ejecuta la aplicación.

3. **Compilar y ejecutar**

   ```bash
   mvn clean install          # compila y ejecuta las pruebas unitarias
   mvn spring-boot:run        # inicia el servidor (puerto 8080 por defecto)
   ```

   Deberías ver un mensaje similar a:

   ```
   Started PokemonApplication in X.xxx seconds (JVM running for X.xxx)
   ```

4. **Abrir Swagger UI**  
   Navega a [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).  
   Allí se listan todos los endpoints con sus modelos de solicitud/respuesta. Puedes probar la API directamente desde esta página.

5. **CORS**  
   En desarrollo, la API acepta cualquier origen (`*`).  
   Para producción, edita `src/main/java/com/pokemon/config/WebConfig.java` y reemplaza la lista `setAllowedOrigins` con los dominios de tu frontend.

## 📂 Estructura del proyecto

```
src/
 ├─ main/
 │   ├─ java/com/pokemon/
 │   │   ├─ controller/          # Controladores REST
 │   │   ├─ model/               # Entidades JPA
 │   │   ├─ repository/          # Interfaces Spring Data JPA
 │   │   ├─ service/             # Lógica de negocio
 │   │   └─ config/              # WebConfig (CORS)
 │   └─ resources/
 │       ├─ application.yml      # Perfiles de Spring (dev/prod)
 │       ├─ ddl.sql              # DDL completo (DROP/CREATE)
 │       ├─ schema.sql           # Esquema original usado para Boot
 │       └─ data.sql             # Datos de prueba
 └─ test/
     └─ java/com/pokemon/        # Pruebas unitarias (PokemonApplicationTests)
```

### Archivos importantes

| Archivo | Propósito |
|---------|-----------|
| `pom.xml` | Dependencias de Maven – incluye `springdoc-openapi-starter-webmvc-ui` para Swagger |
| `WebConfig.java` | Bean del filtro CORS |
| `ddl.sql` | DDL completo con `DROP IF EXISTS` – ejecutar en una BD nueva para recrear el esquema |
| CSV (`entrenador.csv`, `pokemon.csv`, …) | Archivos listos para importar/exportar a Supabase |
| `README.md` | Este archivo – guía de uso |

## 📦 Exportación / Importación CSV

El repositorio incluye archivos CSV vacíos que coinciden exactamente con las columnas de las tablas:

- `c:/.../finalweb26/entrenador.csv`
- `c:/.../finalweb26/pokemon.csv`
- `c:/.../finalweb26/pueblo.csv`
- `c:/.../finalweb26/tipo_pokemon.csv`
- `c:/.../finalweb26/captura.csv`

**Importar a Supabase**  
Ve al panel de Supabase → Editor de tablas → Importar.  
Selecciona el archivo CSV correspondiente.  
Confirma el mapeo de columnas (los encabezados ya coinciden).

**Exportar desde Supabase (manual)**  
Si tienes `psql` instalado, ejecuta:

```bash
psql "postgresql://postgres:<PWD>@<HOST>:5432/postgres" \
    -c "\copy (SELECT * FROM pokemon.entrenador) TO 'entrenador.csv' CSV HEADER"
# Repite para cada tabla, cambiando el nombre de la tabla y el archivo CSV
```

Este comando sobrescribirá los archivos CSV existentes en la raíz del proyecto.

## 🧪 Pruebas

Ejecuta la suite de JUnit:

```bash
mvn test
```

La prueba por defecto (`PokemonApplicationTests`) inicia el contexto de Spring y verifica que la API arranque sin errores.

También puedes usar Postman o `curl`:

```bash
# Listar los Pokémon de un entrenador
curl -X GET "http://localhost:8080/api/entrenadores/f3262c24-473d-437d-a5cf-e87683637954/pokemons"
```

## 🔧 Personalización

- **Añadir nuevos endpoints**: crea un nuevo método en el controlador y, si es necesario, un método en el servicio.
- **Cambiar la base de datos**: edita la URL y el driver en `application.yml`.
- **Asegurar la API**: agrega Spring Security y reemplaza la configuración CORS permisiva por una lista blanca.

## 📜 Licencia

MIT – siéntete libre de bifurcar, extender o incorporar esta API en tus propios proyectos.

---

¡Disfruta construyendo equipos Pokémon!
📜 License
MIT – feel free to fork, extend, or embed this API in your own projects.

Enjoy building Pokémon teams!
