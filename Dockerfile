FROM openjdk:8-jdk-alpine AS builder
WORKDIR target/dependency
ADD target/java-qintingfm-web.jar app.jar
RUN jar -xf ./app.jar