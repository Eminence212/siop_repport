# Dockerfile pour SIOP Spring Boot Application
FROM openjdk:17-jre-slim

# Métadonnées
LABEL maintainer="ScolarisPlus"
LABEL version="1.0.0"
LABEL description="SIOP Spring Boot Application"

# Variables d'environnement
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV SPRING_PROFILES_ACTIVE=prod
ENV TZ=Africa/Kinshasa

# Création de l'utilisateur non-root
RUN groupadd -r siop && useradd -r -g siop siop

# Répertoires de travail
WORKDIR /app

# Copie du JAR
COPY target/siop-spring-boot-1.0.0.jar app.jar

# Création des répertoires de logs
RUN mkdir -p /app/logs && chown -R siop:siop /app

# Changement d'utilisateur
USER siop

# Exposition du port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Point d'entrée
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
