FROM amazoncorretto:11.0.18
ARG JAR_FILE=target/*.jar
COPY ./target/synonyms-1.0.0.jar synonym.jar
ENTRYPOINT ["java","-jar","/synonym.jar"]