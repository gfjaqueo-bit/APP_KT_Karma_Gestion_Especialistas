# APP_KT_Karma_Gestion_Especialistas

App para administrar los especialistas (psicólogos/psiquiatras) del centro médico **Karma**: listar, buscar por nombre o apellido, crear, editar y eliminar. El proyecto tiene dos partes — una API REST backend y una app Android nativa — que comparten el mismo modelo de datos (`Especialista`).

> Estado actual del repositorio: el **backend** (`especialistaapi_backend/`) ya está subido y es invocable. La **app Android** (Kotlin + Jetpack Compose) está documentada más abajo y se subirá a este mismo repositorio en un próximo paso.

## Estructura del repositorio

```
APP_KT_Karma_Gestion_Especialistas/
└── especialistaapi_backend/   -> API REST (Spring Boot + MySQL)
```

La carpeta de la app Android (ej. `app-android/`) se agregará cuando se suba el frontend.

## Modelo de datos

**`Especialista`**

| Campo | Tipo |
|---|---|
| id | int |
| nombre | String |
| apellido | String |
| especialidad | String |
| email | String |
| telefono | String |
| modalidad | `Modalidad` (`PRESENCIAL`, `ONLINE`, `AMBAS`) |
| tarifaHora | int |

## Backend — `especialistaapi_backend`

API REST Spring Boot (Java) + MySQL para gestionar la entidad `Especialista`.

| Componente | Valor |
|---|---|
| Framework | Spring Boot (`spring-boot-starter-parent` 4.1.0) |
| Java | 25 |
| Build | Maven |
| Persistencia | Spring Data JPA + Hibernate, MySQL (`mysql-connector-j`) |
| Validación | `spring-boot-starter-validation` (jakarta.validation) |

### Endpoints (`/api/especialistas`)

| Método | Ruta | Body / Params | Retorna |
|---|---|---|---|
| GET | `/api/especialistas` | — | `200` + lista de especialistas |
| GET | `/api/especialistas/{id}` | path `id` | `200` + especialista |
| POST | `/api/especialistas` | `InsertarEspecialista` | `201` + especialista creado |
| PUT | `/api/especialistas` | `ActualizarEspecialista` (el `id` viaja en el body) | `200` + especialista actualizado |
| DELETE | `/api/especialistas/{id}` | path `id` | `200` + mensaje |

Configuración local (`application.properties`): puerto `8080`, base de datos `especialistas_db` en MySQL local.

### Cómo correrlo

```bash
cd especialistaapi_backend
./mvnw spring-boot:run
```

## App Android (frontend) — próximamente

App 100% Jetpack Compose que consume esta API vía Retrofit, sin autenticación (el backend tampoco la implementa).

| Componente | Versión / detalle |
|---|---|
| Lenguaje | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material3 (sin XML, sin Fragments) |
| AGP | 9.2.1 |
| compileSdk / minSdk / targetSdk | 37 / 26 / 36 |
| Compose BOM | 2026.02.01 |
| Navegación | Navigation Compose 2.9.8 |
| Networking | Retrofit 2.11.0 + `GsonConverterFactory` |
| Persistencia local | Ninguna — la fuente de verdad es siempre la API |
| Arquitectura | MVVM: UI (Compose) → ViewModel (`StateFlow<UiState>`) → Repository → Retrofit |

**Pantallas**: Menú principal, Lista, Búsqueda, Editar (lista), Eliminar (lista), Formulario (crear/editar), Detalle.

Un único `EspecialistaViewModel` se crea en `NavGraph` y se comparte entre las 7 pantallas, de modo que crear/editar/eliminar en una pantalla se refleja automáticamente en las demás.

## Roadmap

- [ ] Subir el módulo de la app Android a este repositorio.
- [ ] Pruebas unitarias (backend y app Android).
