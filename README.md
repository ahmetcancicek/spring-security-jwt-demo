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

**Swagger:** [localhost:8080/swagger-ui.html](localhost:8080/swagger-ui.html)

### Authentication

#### Register a New User

* URL: `/api/v1/auth/register`
* Method: `POST`
* Request Body:

```json
{
  "email": "billwilson@email.com",
  "username": "billwilson",
  "password": "12345",
  "registerAsAdmin": true,
  "firstName": "Bill",
  "lastName": "Wilson"
}
```

* Response:

```json
{
  "data": {
    "email": "billwilson@email.com",
    "username": "billwilson",
    "emailVerified": false,
    "active": true,
    "firstName": "Bill",
    "lastName": "Wilson"
  }
}
```

* **Request with Curl:**

```bash
curl -H 'Content-Type: application/json' \
 -d '{"email":"billwilson@email.com","username":"billwilson","password":"12345","registerAsAdmin":true,"firstName":"Bill","lastName":"Wilson"}' \
  -X POST \
   http://localhost:8080/api/v1/auth/register
```

### User Login

* URL: `/api/v1/auth/register`
* Method: `POST`
* Request Body:

```json
{
  "username": "billwilson",
  "password": "12345"
}
```

* Response:

```json
{
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYmlsbHdpbHNvbiIsImV4cCI6MTcxOTkzNzkzNCwiaWF0IjoxNzE5OTM3MDM0LCJhdXRob3JpdGllcyI6IlJPTEVfUk9MRV9BRE1JTixST0xFX1JPTEVfVVNFUiJ9.uagLQR1IxEXd0bQzmX_0ENshssxIobYudDRui7mtFC6d-P8dxQWBQPCnPr2-bzqvR33Q4TtVR851TLw8gRxRY-8m45uTtfeaTBm3jgDBXs81ZZgkqRWfcSLlpc-zEs2FzAYTm9idUu4-4yoC5wFU6lgqq0QjaeQCyAYElUGPeNmECK1849Ty8Vfn4j_yEjcMYMdZq5CENaJrOV4KMOHeLrHgEbD7jSV6b5VBL124AhORRyso6P0UiLzyoVlMOmJr5VTREUXJN78CfhkAyApYqP2SK4aQjxwHu3SZo1YV-eu8mXC-hHEG84L9MwStSkuCN8p5h82ZZLoySJujpXt7YQ",
    "refreshToken": "b3b6d7f1-49aa-4e4a-b885-4f0442600cde",
    "tokenType": "Bearer",
    "expiryDuration": 900000
  }
}
```

* **Request with Curl:**

```bash
curl -H 'Content-Type: application/json' \
 -d '{"username":"billwilson","password":"12345"}' \
  -X POST \
   http://localhost:8080/api/v1/auth/login
```

### Get User Info

* URL: `/api/v1/users/me`
* Method: `GET`
* Headers:

```bash
Authorization: Bearer <JWT_TOKEN>
```

* Response:

```json
{
  "data": {
    "email": "billwilson@email.com",
    "username": "billwilson",
    "firstName": "Bill",
    "lastName": "Wilson"
  }
}
```

* **Request with Curl:**

```bash
curl -H 'Content-Type: application/json' \
     -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -X GET \
   http://localhost:8080/api/v1/users/me 
```

### Update User Info

* URL: `/api/v1/auth/me`
* Method: `PUT`
* Headers:

```bash
Authorization: Bearer <JWT_TOKEN>
```

* Request Body:

```json
{
  "firstName": "Bill",
  "lastName": "Wilson"
}
```

* Response:

```json
{
  "data": {
    "email": "billwilson@email.com",
    "username": "billwilson",
    "firstName": "Bill",
    "lastName": "Wilson"
  }
}
```

* **Request with Curl:**

```bash
curl -H 'Content-Type: application/json' \
     -H "Authorization: Bearer <ACCESS_TOKEN>" \
 -d '{"firstName":"Bill","lastName":"Wilson"}' \
  -X PUT \
   http://localhost:8080/api/v1/users/me 
```

### Refresh Token

* URL: `/api/auth/v1/refresh`
* Method: `POST`
* Request Body:

```json
{
  "refreshToken": "b3b6d7f1-49aa-4e4a-b885-4f0442600cde"
}
```

* Response:

```json
{
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYmlsbHdpbHNvbiIsImV4cCI6MTcxOTkzODUyMCwiaWF0IjoxNzE5OTM3NjIwLCJhdXRob3JpdGllcyI6IlJPTEVfUk9MRV9BRE1JTixST0xFX1JPTEVfVVNFUiJ9.T_ROURHd6k_d66VXEPwKpFtQ9HakvsTdY4pHsnd4YCtitImP72V_XFK8F3tGp0Ycqrx45nO03jOpcYQYWSkueKaKSDKB2ZG31xcuXTb-pFi-nl-aeFlN8VD9X59F-GrvvgCCVaLmrBIot4VUrwPdVc7SJmrBAhE7YkzAyzefTAdVaFYfyrmoL012tPjM94U-rdgMOkL537aAfbko0GI4SzpFl1R2NndHI2yfQD-kF4M2fieagUGnsGSTfJ-jYY0zOs9vPdX_-plb253ZhyOf04q8UwZ1e4EzGoUGJ1d_WfoBkeDvVtz8VWyj_Nv-INpY7KTlKZIv75pPlG_VU-l7IQ",
    "refreshToken": "b3b6d7f1-49aa-4e4a-b885-4f0442600cde",
    "tokenType": "Bearer",
    "expiryDuration": 900000
  }
}
```

* **Request with Curl:**

```bash
curl -H 'Content-Type: application/json' \
 -d '{"refreshToken": "<REFRESH_TOKEN>"}' \
  -X POST \
  localhost:8080/api/v1/auth/refresh
```

### Confirm Email

* URL: `/api/auth/v1/confirm?token=<REFRESH_TOKEN>`
* Method: `GET`

* Response:

```json
{
  "data": {
    "username": "billwilson",
    "email": "billwilson@email.com",
    "emailVerified": true
  }
}
```

* **Request with Curl:**

```bash
curl -H 'Accept: application/json' \
  -X GET \
  "localhost:8080/api/v1/auth/confirm?token=<REFRESH_TOKEN>"
```

### Check Email In Use

* URL: `/api/v1/auth/checkEmailInUse?email=<EMAIL>`
* Method: `GET`
* Response:

```json
{
  "data": {
    "isEmailInUse": true,
    "email": "billwilson@email.com"
  }
}
```

* **Request with Curl:**

```bash
curl -H 'Accept: application/json' \
  -X GET \
  "localhost:8080/api/v1/auth/checkEmailInUse?email=<EMAIL>"  
```

### Check Username In Use

* URL: `/api/v1/auth/checkUsernameInUse?username=<USERNAME>`
* Method: `GET`
* Response:

```json
{
  "data": {
    "isUsernameInUse": true,
    "username": "billwilson"
  }
}
```

* **Request with Curl:**

```bash
curl -H 'Accept: application/json' \
  -X GET \
  "localhost:8080/api/v1/auth/checkUsernameInUse?username=<USERNAME>"  
```

### Resend Email Token

* URL: `/api/v1/auth/resendRegistrationToken?token=<TOKEN>`
* Method: `GET`


* **Request with Curl:**

```bash
curl -X GET \
  "localhost:8080/api/v1/auth/resendRegistrationToken?token=<TOKEN>"
```

### Password Reset Link

* URL: `/api/v1/auth/password/resetlink`
* Method: `POST`
* Request Body:

```json
{
    "email":"billwilson@email.com"
}
```

* **Request with Curl:**

```bash
curl -H 'Content-Type: application/json' \
     -H 'Accept: application/json' \
  -d '{"email":"billwilson@email.com"}' \
   -X POST \
  http://localhost:8080/api/v1/auth/password/resetlink
```

### Password Reset

* URL: `/api/v1/auth/password/reset`
* Method: `POST`
* Request Body: 

```json
{
    "email":"billwilson@email.com",
    "password":"12345",
    "confirmPassword":"12345",
    "token":"af95616a-388e-4249-add1-a64577266fdd"
}
```


* **Request with Curl:**

```bash
curl -H 'Content-Type: application/json' \
  -d '{"email":"billwilson@email.com","password":"12345","confirmPassword":"12345","token":"af95616a-388e-4249-add1-a64577266fdd"}' \
   -X POST \
  http://localhost:8080/api/v1/auth/password/reset
```