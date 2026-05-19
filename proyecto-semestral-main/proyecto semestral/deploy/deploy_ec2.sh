#!/usr/bin/env bash
set -euo pipefail

if [[ -z "${EC2_IP:-}" || -z "${DEPLOY_KEY:-}" || -z "${AWS_REGION:-}" || -z "${ECR_BACKEND_VENTAS:-}" || -z "${ECR_BACKEND_DESPACHO:-}" || -z "${ECR_FRONTEND:-}" ]]; then
  echo "Missing required environment variables."
  echo "Required: EC2_IP DEPLOY_KEY AWS_REGION ECR_BACKEND_VENTAS ECR_BACKEND_DESPACHO ECR_FRONTEND"
  exit 1
fi

cat > docker-compose-ecr.yml <<EOF
version: '3.8'
services:
  mysql_ventas:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: ventas_db
    volumes:
      - mysql_ventas_data:/var/lib/mysql

  mysql_despacho:
    image: mysql:8
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: despacho_db
    volumes:
      - mysql_despacho_data:/var/lib/mysql

  backend_ventas:
    image: ${ECR_BACKEND_VENTAS}:latest
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql_ventas:3306/ventas_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    depends_on:
      - mysql_ventas

  backend_despacho:
    image: ${ECR_BACKEND_DESPACHO}:latest
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql_despacho:3306/despacho_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    depends_on:
      - mysql_despacho

  frontend:
    image: ${ECR_FRONTEND}:latest
    ports:
      - "80:80"
    depends_on:
      - backend_ventas
      - backend_despacho

volumes:
  mysql_ventas_data: {}
  mysql_despacho_data: {}
EOF

scp -o StrictHostKeyChecking=no -i "$DEPLOY_KEY" docker-compose-ecr.yml ec2-user@"$EC2_IP":/home/ec2-user/docker-compose-ecr.yml
ssh -o StrictHostKeyChecking=no -i "$DEPLOY_KEY" ec2-user@"$EC2_IP" \
  "REGISTRY=\$(echo '${ECR_BACKEND_VENTAS}' | cut -d'/' -f1); aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin \$REGISTRY; docker-compose -f /home/ec2-user/docker-compose-ecr.yml pull; docker-compose -f /home/ec2-user/docker-compose-ecr.yml up -d"
