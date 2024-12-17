# Dockerfile for Comment Service

# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

ADD target/*.jar app.jar

# Expose port 8081
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
