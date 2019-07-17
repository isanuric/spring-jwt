# docker file using jdk8
FROM openjdk:8-jre-alpine
COPY target/bar-0.0.1-SNAPSHOT.jar /
EXPOSE 8080
CMD java -jar /bar-0.0.1-SNAPSHOT.jar
