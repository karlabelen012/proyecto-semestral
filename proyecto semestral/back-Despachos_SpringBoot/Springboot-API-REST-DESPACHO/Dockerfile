# =========================
# ETAPA 1 - BUILD
# =========================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar pom.xml
COPY pom.xml .

# Descargar dependencias
RUN mvn dependency:go-offline

# Copiar código fuente
COPY src ./src

# Compilar proyecto
RUN mvn clean package -DskipTests

# =========================
# ETAPA 2 - RUNTIME
# =========================
FROM eclipse-temurin:21-jre

# Crear usuario seguro
RUN addgroup spring && adduser --system spring --ingroup spring

USER spring:spring

WORKDIR /app

# Copiar JAR generado
COPY --from=build /app/target/*.jar app.jar

# Puerto Spring
EXPOSE 8081

# Ejecutar aplicación
ENTRYPOINT ["java","-jar","app.jar"]