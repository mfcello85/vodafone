FROM openjdk:11 as builder
RUN groupadd -r myuser && useradd -r -g myuser myuser
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:11
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
USER myuser
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
