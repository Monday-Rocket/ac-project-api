FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV GOOGLE_APPLICATION_CREDENTIALS="serviceAccountKey.json"
ENTRYPOINT ["java", "-jar", "app.jar"]