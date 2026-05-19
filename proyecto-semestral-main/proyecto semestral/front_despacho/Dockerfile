# =========================
# ETAPA 1 - BUILD
# =========================
FROM node:20 AS build

WORKDIR /app

# Copiar dependencias
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar proyecto
COPY . .

# Compilar Vite
RUN npm run build

# =========================
# ETAPA 2 - PRODUCCION
# =========================
FROM nginx:alpine

# Copiar build generado
COPY --from=build /app/dist /usr/share/nginx/html

# Puerto nginx
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]