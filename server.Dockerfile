FROM openjdk:17-jdk-slim
COPY build/libs/webflux-armeria-grpc-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT exec java -jar app.jar