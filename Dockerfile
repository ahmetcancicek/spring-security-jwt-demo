# Maven image
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src
# Build the application
RUN mvn clean package -DskipTests

# OpenJDK image
FROM openjdk:17-alpine
WORKDIR /app
# Copy the build artifact from the build stage
COPY --from=build /app/target/*.jar app.jar
# Expose the port
EXPOSE 8080
# Run
CMD ["java","-jar","app.jar"]