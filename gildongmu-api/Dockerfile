FROM openjdk:17-alpine
ARG JAR_FILE=gildongmu-api/build/libs/gildongmu-api-0.0.1-SNAPSHOT.jar
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD
ARG JWT_SECRET
ARG PROFILE
COPY ${JAR_FILE} /app.jar
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV JWT_SECRET=${JWT_SECRET}
ENV PROFILE=${PROFILE}
ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILE}","-Duser.timezone=Asia/Seoul","-jar","/app.jar"]