# API IronFit - Finanzas

## Descripción

Microservicio encargado de la gestión financiera de los socios dentro del sistema IronFit.

Permite:

- Registrar pagos
- Consultar pagos
- Actualizar pagos
- Eliminar pagos
- Consultar historial de pagos de un socio
- Consultar pagos por estado, mes, año y período
- Verificar deudas mediante integración con la API Socios

---

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Oracle Database
- Oracle SQL Developer
- Swagger/OpenAPI
- Maven
- Lombok
- Jakarta Validation
- RestTemplate
- JUnit 5
- Mockito
- Global Exception Handler (`@RestControllerAdvice`)

---

## Configuración de base de datos

Configurar en `application.yml`:

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
```

Variables de entorno:

```env
DB_URL=jdbc:oracle:thin:@localhost:1521:xe
DB_USER=TU_USUARIO
DB_PASSWORD=TU_PASSWORD
```

> Para la evaluación se puede configurar directamente la conexión en `application.yml` si no se utilizarán variables de entorno.

---

## Script SQL

Crear tabla:

```sql
CREATE TABLE PAGO (
    ID_PAGO NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    RUT_SOCIO VARCHAR2(12) NOT NULL,
    MES NUMBER NOT NULL,
    ANIO NUMBER NOT NULL,
    MONTO NUMBER(10,2) NOT NULL,
    ESTADO VARCHAR2(20) NOT NULL
);
```

---

## Ejecución

- Clonar el repositorio.
- Configurar la conexión a Oracle.
- Ejecutar el script SQL.
- Ejecutar:

```bash
mvn clean install
mvn spring-boot:run
```

---

## Puerto

21503

---

## Swagger

```
http://localhost:21503/swagger-ui/index.html
```

---

# Endpoints principales

## Consultas

- GET /api/v4/pagos
- GET /api/v4/pagos/{id}
- GET /api/v4/pagos/estado/{estado}
- GET /api/v4/pagos/mes/{mes}
- GET /api/v4/pagos/anio/{anio}
- GET /api/v4/pagos/periodo?mes={mes}&anio={anio}
- GET /api/v4/pagos/historial/{rut}
- GET /api/v4/pagos/deuda/{rut}

## Gestión

- POST /api/v4/pagos
- PUT /api/v4/pagos/{id}
- PATCH /api/v4/pagos/{id}
- DELETE /api/v4/pagos/{id}

---

## Integración entre microservicios

La API Finanzas consume información de la API Socios mediante **RestTemplate** para verificar el estado del socio y determinar si posee deuda.

### Flujo

```
API Finanzas
      │
      ▼
Consulta API Socios
      │
      ▼
Obtiene estado del socio
      │
      ▼
Consulta historial de pagos
      │
      ▼
Determina si posee deuda
```

---

## Manejo global de excepciones

El proyecto implementa un Global Exception Handler para centralizar el manejo de errores.

### Errores gestionados

- 200 OK
- 201 Created
- 204 No Content
- 400 Bad Request
- 404 Not Found
- 500 Internal Server Error

---

### Formato de respuesta

```json
{
  "fecha": "2026-07-09T12:30:00",
  "status": 404,
  "error": "Not Found",
  "mensaje": "No existe un pago para el período indicado",
  "ruta": "/api/v4/pagos/periodo"
}
```

---

### Beneficios

- Manejo centralizado de excepciones.
- Respuestas consistentes para todos los endpoints.
- Facilita el mantenimiento.
- Facilita el diagnóstico de errores.
- Mejora la cobertura de pruebas.

---

## Testing

El proyecto incluye pruebas unitarias para:

- Model
- Service
- Controller

### Cobertura

- 200 OK
- 201 Created
- 204 No Content
- 400 Bad Request
- 404 Not Found
- 500 Internal Server Error

Se incluyen pruebas para:

- Consulta de pagos.
- Registro de pagos.
- Actualización de pagos.
- Eliminación.
- Historial de pagos.
- Consulta por estado.
- Consulta por mes.
- Consulta por año.
- Consulta por período.
- Verificación de deuda.
- Manejo de excepciones mediante Global Exception Handler.
