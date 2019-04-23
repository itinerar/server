FROM maven:3.6-jdk-8
RUN mkdir /app
COPY pom.xml /app
WORKDIR /app
RUN mvn package
RUN mvn clean generate-sources install