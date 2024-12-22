FROM openjdk:17-jdk-slim as builder
LABEL maintainer = "Pratik Chavan"
ADD target/Chat-app-backend-0.0.1-SNAPSHOT.jar chat-app.jar
EXPOSE 7070
ENTRYPOINT ["java", "-jar", "chat-app-backend.jar"]


