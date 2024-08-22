# Spring Security JWT Demo

This project is a demo application for authentication using Spring Security 6, Spring Boot 3, and JWT.
The purpose of this project is to provide an example of how to implement secure authentication and
authorization. On the other hand, the demo application demonstrates providing authentication and
authorization without using a third-party token library.

## Technologies

* Spring Boot
* MySQL
* Testcontainers
* Docker
* Maven
* Swagger
* Spring Data JPA
* Spring Security

## Requirements

* Docker

## Run with Docker

To run the application using Docker, you can use the provided Dockerfile with docker-compose.yml:

```bash
docker-compose up -d
```

## Building the project

It is needed to build and run the application

* Oracle JDK 17
* Maven 3.9      
* MySQL 8.3
* Docker

Clone the project and use Maven to build the application

```bash
mvn clean package 
```

## Configuration

Configuration can be received in application-dev.properties or using environment variables.

### RSA Key

If you want to replace the Private and Public Key:

1. Change the folder

```bash
cd src/main/resources/certs
```

2. Generate a Private Key with RSA:

```bash
openssl genpkey -algorithm RSA -out private-key.pem
```

3. Extract the Public Key from the Private Key

```bash
openssl rsa -pubout -in private-key.pem -out public-key.pem
```

## Running

In order to run using embedded Apache Tomcat server use:

```bash
java -jar target/spring-security-jwt-demo-0.0.1-SNAPSHOT.jar
```

## API Endpoints

**Swagger:** [localhost:8080/swagger-ui.html](localhost:8080/swagger-ui.html)

### Authentication

#### Register

```bash
curl -H 'Content-Type: application/json' \
 -d '{"email":"billwilson@email.com","username":"billwilson","password":"12345","registerAsAdmin":true,"firstName":"Bill","lastName":"Wilson"}' \
  -X POST \
   http://localhost:8080/api/v1/auth/register
```

### Login

```bash
curl -H 'Content-Type: application/json' \
 -d '{"username":"billwilson","password":"12345"}' \
  -X POST \
   http://localhost:8080/api/v1/auth/login
```

### Get User Info

```bash
curl -H 'Content-Type: application/json' \
     -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -X GET \
   http://localhost:8080/api/v1/users/me 
```

### Update User Info

```bash
curl -H 'Content-Type: application/json' \
     -H "Authorization: Bearer <ACCESS_TOKEN>" \
 -d '{"firstName":"Bill","lastName":"Wilson"}' \
  -X PUT \
   http://localhost:8080/api/v1/users/me 
```

### Refresh Token

```bash
curl -H 'Content-Type: application/json' \
 -d '{"refreshToken": "<REFRESH_TOKEN>"}' \
  -X POST \
  localhost:8080/api/v1/auth/refresh
```

### Confirm Email

```bash
curl -H 'Accept: application/json' \
  -X GET \
  "localhost:8080/api/v1/auth/confirm?token=<REFRESH_TOKEN>"
```

### Check Email In Use

```bash
curl -H 'Accept: application/json' \
  -X GET \
  "localhost:8080/api/v1/auth/checkEmailInUse?email=<EMAIL>"  
```

### Check Username In Use

```bash
curl -H 'Accept: application/json' \
  -X GET \
  "localhost:8080/api/v1/auth/checkUsernameInUse?username=<USERNAME>"  
```

### Resend Email Token

```bash
curl -X GET \
  "localhost:8080/api/v1/auth/resendRegistrationToken?token=<TOKEN>"
```

### Password Reset Link

```bash
curl -H 'Content-Type: application/json' \
     -H 'Accept: application/json' \
  -d '{"email":"billwilson@email.com"}' \
   -X POST \
  http://localhost:8080/api/v1/auth/password/resetlink
```

### Password Reset

```bash
curl -H 'Content-Type: application/json' \
  -d '{"email":"billwilson@email.com","password":"12345","confirmPassword":"12345","token":"af95616a-388e-4249-add1-a64577266fdd"}' \
   -X POST \
  http://localhost:8080/api/v1/auth/password/reset
```