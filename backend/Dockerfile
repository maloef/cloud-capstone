FROM eclipse-temurin:19-jdk-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline
COPY src/main ./src/main
CMD ["./mvnw", "spring-boot:run"]