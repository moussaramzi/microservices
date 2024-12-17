# Dockerfile for Comment Service

# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file
COPY target/comment-service.jar comment-service.jar

# Expose port 8081
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "comment-service.jar"]
