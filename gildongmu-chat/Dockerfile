FROM openjdk:17-alpine
ARG JAR_FILE=gildongmu-chat/build/libs/gildongmu-chat-0.0.1-SNAPSHOT.jar
ARG MONGO_URI
ARG PROFILE
COPY ${JAR_FILE} /app.jar
ENV MONGO_URI=${MONGO_URI}
ENV PROFILE=${PROFILE}
ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILE}","-Duser.timezone=Asia/Seoul","-jar","/app.jar"]