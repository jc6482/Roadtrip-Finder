FROM maven:3-eclipse-temurin-11-alpine as builder

WORKDIR /opt/RoadTripFinder

COPY . .

RUN mvn clean install dependency:copy-dependencies \
&& cp ./target/template-spring-0.0.1-SNAPSHOT.jar ./target/dependency/template-spring-0.0.1-SNAPSHOT.jar

FROM eclipse-temurin:8-jre-alpine

WORKDIR /opt/RoadTripFinder

COPY --from=0 /opt/RoadTripFinder/target/dependency .

EXPOSE 8080

ENTRYPOINT ["java", "-cp", "./template-spring-0.0.1-SNAPSHOT.jar:./*:.", "template.main.Main"]
