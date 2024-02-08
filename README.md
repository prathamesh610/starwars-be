# Starwars Application

This is a Java-based web application built using Spring Boot and Gradle. The application is containerized using Docker.

## Technologies Used

- Java 17
- Spring Boot 3.2.2
- Gradle
- Docker

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java 17
- Docker
- Gradle

### Building the Application

To build the application, you would need to add the following values to application.properties:

```properties
spring.security.user.name=admin
spring.security.user.password=password
```

Now, navigate to the project directory and run the following command:
```bash
./gradlew clean build
```

This will generate a jar file in the `build/libs` directory.

### Running the Application Locally

To run the application, you can use the following command:

```bash
java -jar build/libs/starwars-app-0.0.1-SNAPSHOT.jar
```

The application will start and by default can be accessed at http://localhost:8080.

### Docker

A Dockerfile is provided. Build a Docker image by running:

```bash
docker build -t starwars-app .
```

Then, you can run the application in a Docker container with:

```bash
docker run -p 8081:8080 starwars-app
```

The application will start on docker and by default can be accessed at http://localhost:8081.

### Jenkins

The application has a `JenkinsFile` which contains a pipeline to build, test and deploy the application. The JenkinsFile is written in Groovy. This file will fetch from remote repository, build the application, run the tests and deploy the application to local docker container.

## Authors

- Prathamesh Jadhav

## Design and Implementation Approach

The Starwars Application is designed with a microservices architecture in mind, leveraging the power of Spring Boot.

### Design

The application follows the MVC (Model-View-Controller) design pattern. The `Controller` layer handles HTTP requests and responses, the `Service` layer contains business logic, and the `Repository` layer interacts with the database.

### Implementation

The application is implemented in Java, using the Spring Boot framework. 
The application is containerized using Docker, which ensures that it runs the same way in every environment.
The application flow diagram can be found at flow diagram.png at root of this directory

### Testing

Unit tests are written using the JUnit framework and Mockito for mocking dependencies.

### Security

The application uses Spring Security for authentication and authorization. It uses username and password form of login.

### API Documentation

The application uses Springdoc (Swagger) for API documentation. The API documentation is available at `/swagger-ui/index.html` endpoint.

### Error Handling

The application uses Spring Boot's `@ControllerAdvice` and `@ExceptionHandler` for global exception handling.


### Logging

The application uses Logback for logging. 

## SOLID Principles

This project uses different SOLID principles:

1. **Single Responsibility Principle (SRP)**: Each class in the project has a single responsibility. For example, the `ApiController` class is responsible for handling HTTP requests and responses, while the `OnlineService` and `OfflineService` classes are responsible for business logic.

2. **Open-Closed Principle (OCP)**: The project is structured in a way that allows new functionality to be added with minimal changes to existing code. For example, new services or controllers can be added without modifying existing ones.

3. **Interface Segregation Principle (ISP)**: The project uses interfaces to define the contract for services. This allows the implementation details of each service to be hidden from the rest of the application, and ensures that classes are not forced to depend on methods they do not use.

4. **Dependency Inversion Principle (DIP)**: The project uses dependency injection (a form of DIP) to reduce the coupling between classes. For example, `ApiController` depends on abstractions (`OnlineService` and `OfflineService`) rather than concrete classes.


## HATEOAS Principles in the Project

This project uses the HATEOAS (Hypermedia as the Engine of Application State) principles of RESTful API design:

1. The API responses include links. These links help the client navigate to other resources without needing to hard-code URLs.
2. The client can discover all the actions it can perform on a resource by examining the links in the resource's representation. This makes the API more flexible and easier to evolve over time. 
3. Each message includes enough information to describe how to process the message. This often involves including links with rel attributes that indicate the purpose of the link.
