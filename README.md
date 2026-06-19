Pokémon Capture Management – README
(project root: C:\Users\estudiantes\Desktop\final\finalweb26)

📖 Overview
A Spring Boot REST API that lets you:

Feature	Endpoint	Description
Trainer management	GET /api/entrenadores/{uuid}	Retrieve a trainer by its UUID
List trainer’s Pokémon	GET /api/entrenadores/{uuid}/pokemons	All Pokémon that belong to the trainer
Add a Pokémon to a trainer	POST /api/entrenadores/{uuid}/pokemons/{pokemonUuid}	Attach an existing Pokémon to the trainer
Other CRUD	Standard Spring Data‑JPA repositories for pokemon, pueblo, tipo_pokemon, captura	Direct DB access via the generated repository beans
Database	Supabase PostgreSQL (or local H2) – schema defined in src/main/resources/ddl.sql	Use the DDL to recreate the schema locally
Swagger UI	http://localhost:8080/swagger-ui.html	Interactive API documentation
CORS	Allows any origin (*) in development – configurable in WebConfig	Front‑end apps (React/Vue/Angular) can call the API without cross‑origin errors
🛠️ Prerequisites
Tool	Minimum version
Java	17 (installed & JAVA_HOME set)
Maven	3.9+
Git	any recent version (used for commits)
PostgreSQL client (optional)	psql for manual CSV export/import
Supabase account (optional)	DB URL postgresql://postgres:<PWD>@<HOST>:5432/postgres
Node (optional)	For front‑end experiments – not required for the backend
🚀 Getting Started
Clone the repo (if you haven’t already)

bash
git clone <repo‑url>
cd finalweb26
Configure the database

Option A – Supabase (cloud)

Edit src/main/resources/application.yml → prod profile:

yaml
spring:
  datasource:
    url: jdbc:postgresql://<HOST>:5432/postgres
    username: postgres
    password: <YOUR_PASSWORD>
Option B – Local H2 (in‑memory, for quick tests)

The dev profile already points to an H2 DB, just run the app.

Build & run

bash
mvn clean install          # compile, run unit tests
mvn spring-boot:run       # start the server (default port 8080)
You should see:

Started PokemonApplication in X.xxx seconds (JVM running for X.xxx)
Open Swagger UI

Navigate to http://localhost:8080/swagger-ui.html.
All endpoints are listed with request/response models.
You can try the API directly from this page.

CORS

In development the API accepts any origin (*).
For production, edit src/main/java/com/pokemon/config/WebConfig.java and replace the setAllowedOrigins list with your front‑end domain(s).

📂 Project Structure
src/
 ├─ main/
 │   ├─ java/com/pokemon/
 │   │   ├─ controller/          # REST controllers
 │   │   ├─ model/                # JPA entities
 │   │   ├─ repository/           # Spring Data JPA interfaces
 │   │   ├─ service/              # Business logic
 │   │   └─ config/                # WebConfig (CORS)
 │   └─ resources/
 │       ├─ application.yml       # Spring profiles (dev/prod)
 │       ├─ ddl.sql               # Full DDL (DROP/CREATE)
 │       ├─ schema.sql            # Original schema used for Boot
 │       └─ data.sql              # Seed data
 └─ test/
     └─ java/com/pokemon/          # Unit tests (PokemonApplicationTests)
Important Files
File	Purpose
pom.xml	Maven dependencies – includes springdoc-openapi-starter-webmvc-ui for Swagger
WebConfig.java	CORS filter bean
ddl.sql	Full DDL with DROP IF EXISTS – run on a fresh DB to recreate the schema
CSVs (entrenador.csv, pokemon.csv, …)	Ready‑to‑import/export files for Supabase
README.md	This file – usage guide
📦 CSV Export / Import
The repository ships empty CSV templates that match the table columns exactly:

c:/.../finalweb26/entrenador.csv
c:/.../finalweb26/pokemon.csv
c:/.../finalweb26/pueblo.csv
c:/.../finalweb26/tipo_pokemon.csv
c:/.../finalweb26/captura.csv
Import to Supabase

Open the Supabase dashboard → Table editor → Import.
Select the appropriate CSV file.
Confirm the column mapping (headers already match).
Export from Supabase (manual)

If you have psql installed, run:

bash
psql "postgresql://postgres:<PWD>@<HOST>:5432/postgres" \
    -c "\copy (SELECT * FROM pokemon.entrenador) TO 'entrenador.csv' CSV HEADER
# repeat for each table, changing the table name and CSV name
The command overwrites the existing CSV files in the project root.

🧪 Testing
Run the JUnit suite:

bash
mvn test
The default test (PokemonApplicationTests) boots the Spring context and verifies that the API starts without errors.

You can also use Postman or curl:

bash
# List a trainer's Pokémon
curl -X GET "http://localhost:8080/api/entrenadores/f3262c24-473d-437d-a5cf-e87683637954/pokemons"
🔧 Customisation
Add new endpoints – create a new controller method and, if needed, a service method.
Change DB – edit the datasource URL/driver in application.yml.
Secure the API – add Spring Security and replace the permissive CORS config with a whitelist.
📜 License
MIT – feel free to fork, extend, or embed this API in your own projects.

Enjoy building Pokémon teams!
