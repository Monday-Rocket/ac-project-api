FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY ./serviceAccountKey.json serviceAccountKey.json
ENV GOOGLE_APPLICATION_CREDENTIALS="serviceAccountKey.json"
ENTRYPOINT ["java", "-jar", "app.jar"]