FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install -y openjdk-17-jdk
RUN apt-get install -y maven

COPY . .

RUN mvn clean install

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/todolist-1.0.0.jar app.jar


ENV DATABASE_URL=jdbc:postgresql://db/todolist
ENV DATABASE_USER=postgres
ENV DATABASE_PASSWORD=postgres

ENTRYPOINT ["java", "-jar", "app.jar"]
