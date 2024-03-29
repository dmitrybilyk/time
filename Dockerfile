FROM alpine:3.14.1

RUN apk add openjdk11

COPY target/time-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]