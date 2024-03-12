FROM openjdk:17-alpine
ARG JAR_FILE=gildongmu-api/build/libs/gildongmu-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-Duser.timezone=Asia/Seoul","-jar","/app.jar"]