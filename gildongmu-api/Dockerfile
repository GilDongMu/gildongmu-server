FROM openjdk:17-alpine
ARG JAR_FILE=gildongmu-api/build/libs/gildongmu-api-0.0.1-SNAPSHOT.jar
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD
ARG JWT_SECRET
ARG MONGO_URI
ARG REDIS_PASSWORD
ARG PROFILE
ARG GOOGLE_CLIENT_ID
ARG GOOGLE_CLIENT_SECRET
ARG GOOGLE_REDIRECT_URI
ARG KAKAO_CLIENT_ID
ARG KAKAO_CLIENT_SECRET
ARG KAKAO_REDIRECT_URI
ARG REDIS_HOST
COPY ${JAR_FILE} /app.jar
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV JWT_SECRET=${JWT_SECRET}
ENV MONGO_URI=${MONGO_URI}
ENV REDIS_PASSWORD=${REDIS_PASSWORD}
ENV REDIS_HOST=${REDIS_HOST}
ENV PROFILE=${PROFILE}
ENV GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
ENV GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
ENV GOOGLE_REDIRECT_URI=${GOOGLE_REDIRECT_URI}
ENV KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID}
ENV KAKAO_CLIENT_SECRET=${KAKAO_CLIENT_SECRET}
ENV KAKAO_REDIRECT_URI=${KAKAO_REDIRECT_URI}
ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILE}","-Duser.timezone=Asia/Seoul","-jar","/app.jar"]