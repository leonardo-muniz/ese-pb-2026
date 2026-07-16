# Etapa 1: Build (Compilação)
FROM maven:3.9.16-eclipse-temurin-25 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Execução (Runtime)
FROM eclipse-temurin:25-jre-alpine

# Criação de um grupo e usuário sem privilégios
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Ajuste de permissões: garante que o usuário spring seja dono da pasta
COPY --from=builder /app/target/*.jar app.jar
RUN chown spring:spring app.jar

# Define o usuário para rodar a aplicação
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]