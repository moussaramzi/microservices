# Dockerfile for Recipe Service

# Use OpenJDK 17 as the base image
FROM openjdk:17-jdk-slim

# Copy the application JAR file
ADD target/*.jar app.jar

# Expose port 8082
EXPOSE 8082

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
