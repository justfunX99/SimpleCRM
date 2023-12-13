#
# Build stage
#
FROM maven:3.6.2-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/SimpleCRM-0.0.1-SNAPSHOT.jar /usr/local/lib/simple_crm.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/simple_crm.jar"]