# ==========================
# Build Stage
# ==========================
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom.xml first (improves Docker cache)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# ==========================
# Runtime Stage
# ==========================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java","-jar","app.jar"]