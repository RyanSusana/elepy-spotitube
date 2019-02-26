FROM openjdk:8-alpine

MAINTAINER Ryan Susana "ryansusana@live.com"

ADD /target/spotitube-1.0-SNAPSHOT.jar app.jar

EXPOSE 1997

ENTRYPOINT ["java","-jar","/app.jar"]