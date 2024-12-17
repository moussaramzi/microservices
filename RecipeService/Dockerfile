# Dockerfile for Recipe Service

# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file
COPY target/recipe-service.jar recipe-service.jar

# Expose port 8082
EXPOSE 8082

# Command to run the application
ENTRYPOINT ["java", "-jar", "recipe-service.jar"]
