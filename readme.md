
# Introduction
The application is as a SpringBoot micro-service. After cloning/unzipping the application it can be started from the command line using the following cmd: **mvn spring-boot:run** from the root of the application. 

## Usage
The REST API of the micro-service can be accessed and explored here:
> http://localhost:8080/swagger-ui.html.

To access the built in H2 database go here:
>http://localhost:8080/h2-console/login.do
>sa/password

The system comes provisioned with two order books, one closed and one open. The former having a few executions associated.

## Unit Test Coverage
I only provided samples of what I would normally use more extensively for the different aspects of the application. 
- Controllers covered with SpringCloud Contract
- Repository layer I deemed to simple to warrant tests.
- Service Layer covered with Mockito 

>Lacking entirely are Integration tests.

## Design
I designed the solution to solve the business logic noted in the task description, that is to say I didn't over think or ever-engineer it.
Hitherto the solution is not concerned with the complexities around the adding of orders or executions and by implication the validations required in such circumstances

  
