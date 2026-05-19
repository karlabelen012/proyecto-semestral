# Proyecto Semestral (Docker + Spring Boot + React + MySQL)

Proyecto con dos **backends Spring Boot** (Ventas y Despachos) y un **frontend React (Vite)**, usando **MySQL en contenedores dedicados**. Incluye también configuración para **despliegue en AWS** con **Terraform + GitHub Actions**.

---

## ¿Qué despliega este proyecto?

En local (Docker Compose) despliega:

- **MySQL (Ventas)**: `mysql_ventas` → BD `ventas_db`
- **MySQL (Despachos)**: `mysql_despacho` → BD `despacho_db`
- **Backend Ventas (Spring Boot)**: `backend_ventas` → puerto `8082`
- **Backend Despachos (Spring Boot)**: `backend_despacho` → puerto `8081`
- **Frontend (React/Vite)**: `frontend` → puerto `3000`

En AWS (CI/CD):
- Infraestructura con Terraform (ECR, EC2, roles, etc.)
- Imágenes Docker publicadas a ECR
- Despliegue mediante `docker-compose` generado para EC2

---

## Descripción general

- **Ventas**: API REST para gestionar entidades de ventas.
- **Despachos**: API REST para gestionar entidades de despachos.
- **Frontend**: interfaz para administrar la información, consumiendo las APIs REST.

Ambos backends exponen documentación con **Swagger/OpenAPI** mediante `springdoc`:
- `http://localhost:8081/swagger-ui.html` (Despachos)
- `http://localhost:8082/swagger-ui.html` (Ventas)

---

## Estructura del proyecto

```text
proyecto semestral/
  docker-compose.yml
  README.md

  back-Despachos_SpringBoot/
    Springboot-API-REST-DESPACHO/
      Dockerfile
      src/main/java/... (controller/service/entity)

  back-Ventas_SpringBoot/
    Springboot-API-REST/
      Dockerfile
      src/main/java/... (controller/service/entity)

  front_despacho/
    Dockerfile
    package.json
    src/
      api/endpoints.js
      Routes/AppRoutes.jsx
      componentes/
```

---

## Requisitos

### Para ejecutar en local
- **Docker** y **Docker Compose**

*(Opcional para desarrollo local sin Docker: Node.js y JDK/Maven).*

### Para ejecutar el pipeline (AWS)
- Secrets en GitHub:
  - `AWS_ACCESS_KEY_ID`
  - `AWS_SECRET_ACCESS_KEY`
  - `AWS_REGION` (ej: `us-east-1`)

---

## Pasos para inicializar el proyecto (local con Docker)

Desde la carpeta raíz `proyecto semestral/`:

```bash
docker compose up --build
```

Verifica que todos los contenedores estén levantados:

```bash
docker compose ps
```

---

## Puertos / URLs

- **Frontend**: `http://localhost:3000`
- **Backend Ventas**: `http://localhost:8082`
- **Backend Despachos**: `http://localhost:8081`

Swagger:
- **Despachos**: `http://localhost:8081/swagger-ui.html`
- **Ventas**: `http://localhost:8082/swagger-ui.html`

---

## Endpoints (API)

### Despachos
Base path:
- `/api/v1/despachos`

Operaciones (CRUD):
- `POST   /api/v1/despachos`
- `PUT    /api/v1/despachos/{idDespacho}`
- `GET    /api/v1/despachos`
- `GET    /api/v1/despachos/{idDespacho}`
- `DELETE /api/v1/despachos/{idDespacho}`

### Ventas
Base path:
- `/api/v1/ventas`

Operaciones (CRUD):
- `POST   /api/v1/ventas`
- `PUT    /api/v1/ventas/{idVenta}`
- `GET    /api/v1/ventas`
- `GET    /api/v1/ventas/{idVenta}`
- `DELETE /api/v1/ventas/{idVenta}`

---

## ¿Cómo funciona? (Arquitectura)

1. El usuario interactúa con la **UI** del frontend.
2. El frontend hace llamadas REST a:
   - Ventas: `http://localhost:8082/api/v1/ventas`
   - Despachos: `http://localhost:8081/api/v1/despachos`
3. Cada backend aplica lógica (controller → service → repository) y persiste datos en su **MySQL dedicado**.
4. La respuesta regresa al frontend para actualizar la interfaz.

---

## Flujo del sistema

```mermaid
flowchart LR
  U[Usuario] --> F[Frontend React (3000)]
  F -->|REST /api/v1/ventas| BV[Backend Ventas (8082)]
  F -->|REST /api/v1/despachos| BD[Backend Despachos (8081)]

  BV --> MV[(MySQL Ventas: ventas_db)]
  BD --> MD[(MySQL Despachos: despacho_db)]

  MV --> BV
  MD --> BD
  BV --> F
  BD --> F
```

---

## Docker (cómo está armado)

El `docker-compose.yml` en la raíz define una **red** compartida `app_network` y:

- MySQL con:
  - `MYSQL_DATABASE` configurado
  - volumen persistente (`mysql_ventas_data`, `mysql_despacho_data`)
  - `healthcheck` para asegurar disponibilidad
- Backends construidos desde sus carpetas:
  - `backend_ventas` usa `SPRING_DATASOURCE_URL` apuntando a `mysql_ventas`
  - `backend_despacho` usa `SPRING_DATASOURCE_URL` apuntando a `mysql_despacho`
- Frontend servido como contenedor:
  - expone `3000:80`

---

## Notas / troubleshooting

- Si hay puertos ocupados, ajusta `docker-compose.yml`.
- Si necesitas reiniciar las BD desde cero:

```bash
docker compose down -v && docker compose up --build
```

- No subas credenciales privadas al repositorio.

---

## CI/CD y despliegue en AWS (Terraform + GitHub Actions)

Este repositorio incluye:
- Carpeta `terraform/` (infra)
- Workflow en `.github/workflows/ci-cd.yml`
- Script `deploy/deploy_ec2.sh`

### ¿Qué hace el pipeline?
- Crea infraestructura en AWS (VPC + Internet Gateway + Security Group + EC2 + ECR)
- Build de imágenes Docker
- Publica imágenes en ECR
- Genera/lanza `docker-compose` en EC2

---

## Despliegue de infraestructura con Terraform (AWS)

### Requisitos
- **Terraform CLI** versión **>= 1.0**
- Acceso a AWS mediante **GitHub Secrets** o variables de entorno:
  - `AWS_ACCESS_KEY_ID`
  - `AWS_SECRET_ACCESS_KEY`
  - `AWS_REGION` (por ejemplo: `us-east-1`)
- Cuenta/permiso para crear recursos en AWS (por ejemplo, permisos equivalentes a *Contributor* u *Owner*).

### Estructura del proyecto (Terraform)
```text
proyecto semestral/terraform/
  main.tf
  variables.tf
  outputs.tf
  terraform.tfvars
  docker-compose.yml (si aplica según tu flujo)
  README.md (si existe)
```

### Diagrama de arquitectura (alto nivel)
```mermaid
graph TD
  GitHubActions[GitHub Actions] --> Terraform[Terraform: terraform/main.tf]
  Terraform --> VPC[VPC + Subred pública + RouteTable + InternetGateway]
  Terraform --> SG[Security Group (puertos 22, 80, 8081, 8082, 3306)]
  Terraform --> ECR[ECR Repositories (backend-ventas, backend-despacho, frontend)]
  Terraform --> EC2[EC2 Instance]
  EC2 --> Docker[Docker + docker-compose en EC2]
  Docker --> ECRImage[Imágenes desde ECR]
```

### Cómo inicializar y ejecutar Terraform (local)
Desde la carpeta `proyecto semestral/`:

```bash
cd terraform
terraform init
terraform plan
terraform apply
```

Si tienes `terraform.tfvars`, asegúrate de configurar los valores requeridos (por ejemplo `aws_region`, `project_name`, `instance_type` y `key_pair_name`).

---

### Cómo usar el CI/CD
1. Configura los **Secrets** del repo.
2. Haz push a la rama `main`.
3. Revisa los logs del workflow: Terraform mostrará la salida relevante (por ejemplo, IP/recursos).

---

## Mejores prácticas incluidas

- **Separación por contenedores**: MySQL por dominio (Ventas vs Despachos).
- **Persistencia de datos**: volúmenes dedicados para cada base.
- **Healthchecks** en MySQL (mejora el arranque ordenado).
- **Swagger/OpenAPI** en ambos backends (`springdoc`).
- **CORS** habilitado desde los controladores (para consumo desde frontend).
- Docker basado en imágenes estándar (MySQL 8 + build de backends).

---

## Cómo extender este proyecto

### 1) Agregar nuevos endpoints (API)
En cada backend:
- Agregar/actualizar:
  - `Controller` (rutas)
  - `Service` (lógica)
  - `Entity` (modelo)
  - `Repository` (acceso a datos)

### 2) Agregar un nuevo microservicio
- Crear carpeta del servicio (p.ej. `back-Nuevo/Servicio/`)
- Agregar su `Dockerfile`
- Incorporarlo al `docker-compose.yml`:
  - `build.context`
  - puertos (si aplica)
  - variables de entorno (por ejemplo `SPRING_DATASOURCE_URL`)
  - `depends_on` si requiere DB

### 3) Cambiar base de datos
- Actualiza `SPRING_DATASOURCE_URL`, usuario/clave
- Ajusta `docker-compose.yml` si usas otro contenedor DB

### 4) Extender el CI/CD
- Asegura que el workflow construye/publica la nueva imagen
- Ajusta la plantilla `docker-compose` usada en EC2



