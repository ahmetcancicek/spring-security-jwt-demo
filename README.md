# Spring Security JWT Demo

This project is a demo application for authentication using Spring Security 6, Spring Boot 3, and JWT.
The purpose of this project is to provide an example of how to implement secure authentication and
authorization. On the other hand, the demo application demonstrates providing authentication and
authorization without using a third-party token library.

## Docker

To run the application using Docker, you can use the provided Dockerfile with docker-compose.yml:

```bash
docker-compose up -d
```

## Configuration

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

## API Endpoints

| Method | Endpoint              | Description                          | Headers                                                           | Request Body                                                                                                     | Response |
|--------|-----------------------|--------------------------------------|-------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|----------|
| POST   | /api/v1/auth/register | Register a new user                  | Content-Type: application/json                                    | `{ "username": "String", "email": "String", "password": "String", "firstName": "String", "lastName": "String" }` |          |
| POST   | /api/v1/auth/login    | Authenticate a user and get a token  | Content-Type: application/json                                    | `{ "username": "String", "password": "String" }`                                                                 |          |
| POST   | /api/v1/auth/refresh  | Generate a new token                 | Content-Type: application/json                                    | `{ "refreshToken": "String"}`                                                                                    |          |
| GET    | /api/v1/users/me      | Get the authenticated user's info    | Authorization: Bearer <jwt-token>                                 | None                                                                                                             |          |
| POST   | /api/v1/users/me      | Update the authenticated user's info | Authorization: Bearer <jwt-token>, Content-Type: application/json | `{ "firstName": "String", "lastName": "String" }`                                                                |          |

## Example Usage

### Register a New User

**Curl Command**

```bash
curl -H 'Content-Type: application/json' \
 -d '{"email":"billwilson@email.com","username":"billwilson","password":"12345","registerAsAdmin":true,"firstName":"Bill","lastName":"Wilson"}' \
  -X POST \
   http://localhost:8080/api/v1/auth/register
```

### User Login

**Curl Command**

```bash
curl -H 'Content-Type: application/json' \
 -d '{"username":"billwilson","password":"12345"}' \
  -X POST \
   http://localhost:8080/api/v1/auth/login
```

### Get User Info

**Curl Command**

```bash
curl -H 'Content-Type: application/json' \
     -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -X GET \
   http://localhost:8080/api/v1/users/me 
```

### Update User Info

**Curl Command**

```bash
curl -H 'Content-Type: application/json' \
     -H "Authorization: Bearer <ACCESS_TOKEN>" \
 -d '{"firstName":"Bill","lastName":"Wilson"}' \
  -X POST \
   http://localhost:8080/api/v1/users/me 
```

### Refresh Token

**Curl Command**

```bash
curl -H 'Content-Type: application/json' \
 -d '{"refreshToken": "<REFRESH_TOKEN>"}' \
  -X POST \
  localhost:8080/api/v1/auth/refresh
```
