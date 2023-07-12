# Package Stage
# Remember to Build Before This Stage
ARG SERVER_PORT=8080

FROM eclipse-temurin:19-alpine
COPY target/executable.jar /usr/local/lib/executable.jar
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java","-jar","/usr/local/lib/executable.jar"]
