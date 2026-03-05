FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /work
COPY pom.xml ./
COPY src ./src
RUN mvn -q -DskipTests=true test || true
CMD ["mvn", "-q", "test"]