# Dockerfile for API Gateway

FROM openjdk:17-jdk-slim

ADD target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
