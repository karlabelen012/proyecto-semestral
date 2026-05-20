# Proyecto con Docker + MySQL

Este proyecto utiliza MySQL para los backends de Ventas y Despachos, sin Azure.

## Qué incluye

- `docker-compose.yml` raíz que levanta:
  - `mysql_ventas` con base de datos `ventas_db`
  - `mysql_despacho` con base de datos `despacho_db`
  - `backend_ventas` Spring Boot en `8082`
  - `backend_despacho` Spring Boot en `8081`
  - `frontend` en `3000`

- Cada backend usa un contenedor MySQL dedicado.
- El frontend se sirve en el puerto `3000`.

## Cómo usar

Desde `proyecto semestral` ejecuta:

```bash
docker compose up --build
```

## Puertos

- Frontend: `http://localhost:3000`
- Backend Ventas: `http://localhost:8082`
- Backend Despachos: `http://localhost:8081`

## Notas


- Si hay puertos ocupados, ajusta `docker-compose.yml` para usar otros puertos.
- Si la base de datos no se crea automáticamente, puede deberse a volúmenes MySQL existentes. En ese caso elimina los volúmenes y vuelve a levantar los contenedores con `docker compose down -v && docker compose up --build`.
- No uses las credenciales privadas en el repositorio.

## CI/CD y despliegue en AWS (Terraform + GitHub Actions)

El repositorio incluye una carpeta `terraform/` y un workflow en `.github/workflows/ci-cd.yml` que:
- Crea recursos en AWS (ECR repos, EC2 con Docker y rol para ECR) usando Terraform.
- Construye las imágenes Docker y las publica en ECR.
- Copia un `docker-compose` a la instancia EC2 y lanza los contenedores.

Requisitos para ejecutar el pipeline desde GitHub Actions (añadir como Secrets del repo):
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_REGION` (por ejemplo `us-east-1`)

También puedes usar un archivo local `terraform/terraform.tfvars` basado en `terraform/terraform.tfvars.example`.

El repositorio incluye `deploy/deploy_ec2.sh` para generar el `docker-compose` de ECR y desplegar en EC2.

Cómo usar:
1. Asegúrate de tener los Secrets configurados.
2. Haz push a la rama `main` para disparar el workflow.
3. El workflow aplicará Terraform, publicará imágenes y desplegará en EC2.
4. Al finalizar, Terraform mostrará la IP pública en los outputs (ver logs del workflow).

Seguridad y buenas prácticas:
- Revisa los recursos creados por Terraform antes de usarlos en producción.
- No dejes claves o el `deploy_key.pem` en el repositorio.
- Limita el acceso SSH y el tamaño de la instancia según necesidad.

