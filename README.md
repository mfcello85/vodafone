Instructions to run the project with docker

mvn clean package
docker build -t vodafone .
docker run -p8080:8080  vodafone

Or just download the docker image from docker hub
docker image pull mfcello85/vodafone:latest
docker run -p8080:8080  mfcello85/vodafone:latest

Instruction to run the project with maven

After installing openjdk-11 launch the following commands
on the console to start the application

mvn clean install
java -jar test-0.0.1-SNAPSHOT.jar

SWAGGER
Swagger UI ( OpenApi 3) can be found at http://localhost:8080/swagger-ui.html

Swagger documentation con be found at http://localhost:8080/v3/api-docs

