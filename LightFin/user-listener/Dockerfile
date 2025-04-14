FROM maven:3-openjdk-18-slim as builder

COPY src /usr/app/src
COPY pom.xml /usr/app

RUN mvn -f /usr/app/pom.xml clean package -DskipTests

FROM openjdk:18-slim

COPY --from=builder /usr/app/target/*.jar /usr/app/app.jar

ENTRYPOINT ["java", "-jar", "/usr/app/app.jar"]