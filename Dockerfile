FROM openjdk:17-alpine

WORKDIR /cr-backend
COPY ./.mvn .mvn
COPY ./src src
COPY ./mvnw mvnw
COPY ./mvnw.cmd mvnw.cmd
COPY ./pom.xml pom.xml

RUN chmod +x mvnw
RUN ./mvnw clean package
RUN cp ./target/CookingRecipesBackend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]