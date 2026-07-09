# API IronFit - Finanzas

## Descripción

Microservicio encargado de la gestión financiera de los socios dentro del sistema IronFit.

Permite:

- Registrar pagos  
- Consultar pagos  
- Actualizar pagos  
- Eliminar pagos  
- Verificar deudas  
- Integración con API Socios  

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
- Validation API  
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

- Clonar repositorio  
- Configurar variables de entorno  
- Ejecutar script SQL en Oracle  
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

http://localhost:21503/swagger-ui/index.html

---

## Endpoints principales

- GET /api/v3/pagos  
- GET /api/v3/pagos/{id}  
- GET /api/v3/pagos/rut/{rut}  
- GET /api/v3/pagos/estado/{estado}  
- POST /api/v3/pagos  
- PUT /api/v3/pagos/{id}  
- PATCH /api/v3/pagos/{id}  
- DELETE /api/v3/pagos/{id}  
- GET /api/v3/pagos/deuda/{rut}  

---

## Integración entre microservicios

La API Finanzas utiliza `RestTemplate` para comunicarse con la API Socios.

### Flujo:

API Finanzas → consulta API Socios → valida existencia del socio → verifica pagos/deudas

---

## Manejo global de excepciones

El proyecto implementa un Global Exception Handler para centralizar errores.

### Errores gestionados

- 400 Bad Request → datos inválidos  
- 404 Not Found → pago o socio no encontrado  
- 500 Internal Server Error → errores internos o fallos de integración  

---

### Formato de respuesta

```json
{
  "fecha": "2026-06-28T12:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "mensaje": "Error interno del servidor",
  "ruta": "/api/v3/pagos/deuda/12345678-9"
}
```

---

### Beneficios

- Manejo centralizado  
- Respuestas uniformes  
- Mejor diagnóstico  
- Mejor cobertura de pruebas  

---

## Testing

Incluye pruebas unitarias de:

- Model  
- Service  
- Controller  

### Cobertura

- 200 OK  
- 201 CREATED  
- 400 BAD REQUEST  
- 404 NOT FOUND  
- 500 INTERNAL SERVER ERROR  

Los errores son manejados mediante el Global Exception Handler.
