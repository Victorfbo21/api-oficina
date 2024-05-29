# Etapa de build
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
RUN apt-get install maven -y

WORKDIR /app
COPY . .

RUN mvn clean install -DskipTests

# Verificação de arquivos gerados no build stage
RUN ls -l /app/target

# Etapa final
FROM openjdk:17-jdk-slim

WORKDIR /app
EXPOSE 8081

COPY --from=build /app/target/oficina-back-0.0.1-SNAPSHOT.jar app.jar

# Verificação de arquivos copiados no final stage
RUN ls -l /app

ENTRYPOINT ["java", "-jar", "app.jar"]
