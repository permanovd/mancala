FROM maven:3.9.6-eclipse-temurin-21-alpine as build
# FROM eclipse-temurin:21-jre-alpine
RUN mkdir /opt/app
COPY . /opt/app/src
WORKDIR /opt/app/src
RUN mvn clean package
RUN cp target/game-session-service.jar /opt/app/game-session-service.jar
CMD ["java", "-jar", "/opt/app/game-session-service.jar"]