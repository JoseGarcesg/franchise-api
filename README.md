# Franchise API

API reactiva para la gestión de **franquicias, sucursales y productos**
desarrollada con **Spring Boot**, **Spring WebFlux** y **MongoDB**.

Permite administrar: - Franquicias - Sucursales - Productos - Stock de
productos - Producto con mayor stock por sucursal

------------------------------------------------------------------------

# Tecnologías

-   Java 17
-   Spring Boot
-   Spring WebFlux
-   MongoDB
-   Gradle
-   Project Reactor
-   Docker
-   JUnit 5
-   Mockito

------------------------------------------------------------------------

# Arquitectura del proyecto

    src
     ├─ main
     │   ├─ controller
     │   ├─ service
     │   ├─ repository
     │   ├─ model
     │   ├─ interfaces
     │   │    └─ dto
     │   └─ config
     └─ test

------------------------------------------------------------------------

# Cómo ejecutar el proyecto


## 1. Clonar el repositorio

``` bash
git clone https://github.com/tu-usuario/franchise-api.git
cd franchise-api
```

## 2. Ejecutar MongoDB con Docker

``` bash
docker run -d -p 27017:27017 --name mongodb mongo
```

## 3. Ejecutar la aplicación

``` bash
./gradlew bootRun
```

La API estará disponible en:

    http://localhost:8080

------------------------------------------------------------------------

# Endpoints principales

## Crear franquicia

POST `/franchises`

Body:

``` json
{
  "name": "KFC"
}
```

------------------------------------------------------------------------

## Agregar sucursal

POST `/franchises/{franchiseId}/branches`

------------------------------------------------------------------------

## Agregar producto

POST `/franchises/{franchiseId}/branches/{branchId}/products`

------------------------------------------------------------------------

## Actualizar stock de producto

PUT
`/franchises/{franchiseId}/branches/{branchId}/products/{productId}/stock/{stock}`

------------------------------------------------------------------------

## Obtener producto con mayor stock por sucursal

GET `/franchises/{franchiseId}/top-products`

Respuesta:

``` json
[
  {
    "branchName": "Centro",
    "productName": "Pollo Broaster",
    "stock": 50
  }
]
```

------------------------------------------------------------------------

# Endpoints adicionales (Plus)

### Actualizar nombre de franquicia

PATCH `/franchises/{franchiseId}/name`

### Actualizar nombre de sucursal

PATCH `/franchises/{franchiseId}/branches/{branchId}/name`

### Actualizar nombre de producto

PATCH
`/franchises/{franchiseId}/branches/{branchId}/products/{productId}/name`

------------------------------------------------------------------------

# Pruebas

Para ejecutar las pruebas:

``` bash
./gradlew test
```

El reporte se genera en:

    build/reports/tests/test/index.html

------------------------------------------------------------------------

# Manejo de errores

La API implementa un **Global Exception Handler** que retorna respuestas
estandarizadas.

Ejemplo:

``` json
{
  "message": "Franchise not found"
}
```

## con Docker

Construir imagen

docker build -t franchise-api .

Ejecutar contenedor

docker run -p 8080:8080 franchise-api

## con Docker compose

docker compose up --build

------------------------------------------------------------------------

# Posibles mejoras

-   Implementar paginación
-   Documentación con OpenAPI / Swagger
-   Docker Compose para levantar todos los servicios
-   Mejorar separación con Clean Architecture

------------------------------------------------------------------------

# Autor

Desarrollado por José Garcés.
