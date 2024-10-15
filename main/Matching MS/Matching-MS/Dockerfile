# Use a base image with Java and Maven installed
FROM maven:3.8.1-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the project files into the working directory
COPY . .

# Run Maven to build the Spring Boot application
RUN mvn clean package -DskipTests

# Use a lightweight JDK base image to run the application
FROM openjdk:17-jdk-slim

# Copy the JAR file from the build stage
COPY --from=build /app/target/Matching-MS-0.0.1-SNAPSHOT.jar /app.jar

# Set the entry point for the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
