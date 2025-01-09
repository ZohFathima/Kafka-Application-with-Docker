# Spring Boot Kafka Application with Docker

## Overview

This project demonstrates how to build a Spring Boot application that integrates Kafka as a messaging platform. The application consists of:

- **Kafka Producer**: Sends messages to a Kafka topic.
- **Kafka Consumer**: Reads messages from the Kafka topic.
- **Docker**: Containerizes the application and its dependencies.
- **Docker Compose**: Orchestrates Kafka and Zookeeper services along with the application.

## Project Structure

```
springboot-kafka-docker/
|-- src/main/java/com/example/kafka/...  # Application source files
|-- Dockerfile                           # Defines container for Spring Boot app
|-- docker-compose.yml                   # Configures Kafka, Zookeeper, and app
|-- pom.xml                              # Maven dependencies and build info
```

## Prerequisites

- Java 17 or higher
- Apache Maven
- Docker and Docker Compose

## Dependencies

Listed in the `pom.xml` file:

- **Spring Boot Starter**: Core framework functionality.
- **Spring Boot Starter Web**: Provides REST API capabilities.
- **Spring Kafka**: Integrates Kafka with Spring applications.
- **Kafka Clients**: Kafka producer/consumer API.

## Kafka Setup

The application communicates with Kafka through Docker Compose, which sets up:

- **Zookeeper**: Manages Kafka clusters.
- **Kafka Broker**: Processes messages.

## Configuration Files

### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/kafka-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    ports:
      - "9092:9092"

  app:
    build: .
    depends_on:
      - kafka
    ports:
      - "8080:8080"
```

## Build and Run Instructions

### 1. Clone the Repository

Clone the project from GitHub:

```bash
git clone https://github.com/your-username/springboot-kafka-docker.git
cd springboot-kafka-docker
```

### 2. Build the Application

Use Maven to package the Spring Boot application into a JAR file:

```bash
mvn clean package
```

### 3. Start Services with Docker Compose

Start Zookeeper, Kafka, and the application using:

```bash
docker-compose up --build
```

This command builds the application image and starts the containers.

### 4. Test the Application

You can test the Kafka producer API using **Postman** or `curl`:

#### Request:

```bash
curl -X POST -H "Content-Type: application/json" \
     -d '{"message":"Hello, Kafka!"}' \
     http://localhost:8080/api/send
```

#### Response:

```
Message sent!
```

#### Verify Kafka Consumer:

Check the application logs to see the consumed message:

```
Received Message: Hello, Kafka!
```

## Key Classes

- ``: Main entry point of the Spring Boot app.
- ``: Configures Kafka Producer and Consumer settings.
- ``: Sends messages to Kafka topics.
- ``: Listens to and processes messages from Kafka topics.
- ``: REST API controller to handle requests for sending messages.

## Notes

- Ensure ports `9092` (Kafka) and `8080` (App) are available on your system.
- Modify `docker-compose.yml` if you need to change default Kafka configurations.

## Troubleshooting

- **Issue**: Kafka broker not reachable. **Solution**: Verify `KAFKA_ADVERTISED_LISTENERS` in `docker-compose.yml` is correct.
- **Issue**: Application not starting. **Solution**: Check Docker logs for Zookeeper/Kafka services.

## Conclusion

This project provides a robust example of integrating Spring Boot with Kafka, packaged and deployed using Docker. It demonstrates both producer and consumer capabilities and can be extended for more complex use cases.

