FROM openjdk:17

WORKDIR /build

COPY target/data-base-app-0.0.1-SNAPSHOT.jar /build/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]