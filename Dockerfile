FROM openjdk:8
COPY build/libs/sparql.jar /
CMD ["java", "-jar", "sparql.jar"]
EXPOSE 8080