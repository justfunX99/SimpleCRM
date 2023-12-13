FROM openjdk:11-jre-slim
COPY target/SimpleCRM-0.0.1-SNAPSHOT.jar /simple_crm.jar
CMD ["java", "-jar", "/simple_crm.jar"]