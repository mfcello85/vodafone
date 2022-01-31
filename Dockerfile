FROM openjdk:8-jdk-alpine
RUN addgroup -S vodafone && adduser -S vodafone -G vodafone
USER vodafone:vodafone
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]