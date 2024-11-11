# Use the official OpenJDK as a base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file created by Maven into the container
COPY target/Penalty-MS-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that Spring Boot listens to
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
