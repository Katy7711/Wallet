FROM openjdk:17

COPY build/libs/*.jar wallet-0.0.1-SNAPSHOT.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/wallet-0.0.1-SNAPSHOT.jar"]