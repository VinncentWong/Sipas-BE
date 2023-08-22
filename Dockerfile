FROM openjdk:latest
RUN mvn clean package
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx64m", "-Xss256k", "-jar", "sipas-0.0.1-SNAPSHOT.jar"]