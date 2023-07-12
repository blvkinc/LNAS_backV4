# Build Stage
FROM maven:3-eclipse-temurin-19-alpine as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Package Stage
ARG SERVER_PORT=80
FROM eclipse-temurin:19-alpine
COPY --from=build /home/app/target/executable.jar /usr/local/lib/executable.jar
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java","-jar","/usr/local/lib/executable.jar"]
