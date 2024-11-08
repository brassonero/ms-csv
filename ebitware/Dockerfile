FROM openjdk:11-jdk-slim

# Establecer directorio de trabajo
WORKDIR /app

# Añadir el JAR de la aplicación
COPY target/*.jar app.jar

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# Puerto expuesto
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java","-jar","app.jar"]