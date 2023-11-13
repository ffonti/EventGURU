# Configuro l'immagine di base per Docker.
FROM openjdk:17-oracle

# Dichiaro un build chiamato JAR_FILE.
ARG JAR_FILE=target/*.jar

# Copio JAR_FILE nell'immagine Docker, rinominandolo in app.jar.
COPY ${JAR_FILE} app.jar

# Informo Docker che il container ascolterà sulla porta di rete 8080.
EXPOSE 8080

# Imposto il punto di ingresso predefinito per il container.
ENTRYPOINT ["java","-jar","/app.jar"]