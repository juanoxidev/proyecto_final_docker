# Usar una imagen de Maven para construir la aplicación
FROM maven:3.8.4-openjdk-11 AS build

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar archivos de proyecto
COPY . .

# Construir el proyecto
RUN mvn clean package -DskipTests

# Usar una imagen de OpenJDK para ejecutar la aplicación
FROM openjdk:11-jre-slim

# Crear directorio en el contenedor para la aplicación
WORKDIR /app

# Copiar el archivo WAR generado
COPY --from=build /app/target/base-0.0.1-SNAPSHOT.jar app.jar

# Configurar el puerto en el que la aplicación escuchará
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]