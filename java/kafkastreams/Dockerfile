FROM maven:latest
WORKDIR /build
COPY pom.xml .
RUN mvn -B dependency:resolve-plugins dependency:resolve

COPY src/ ./src
RUN mvn package
RUN mv target/kafka-streams-1.0-SNAPSHOT-jar-with-dependencies.jar /app.jar
WORKDIR /
CMD ["java", "-jar" ,"app.jar"]