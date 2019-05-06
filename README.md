# spring-jwt
* REST
* Spring Boot
* JWT


# Quick Start
* clone repository
* $ mvn clean install
* run/debug createAndUseTokenToAccessController_success() test:

a. The user sends a POST request with an csrf token and a valid credential/principal as body: 
**Request:**  
POST http://localhost:49609/login  
Content-Type: [application/x-www-form-urlencoded;charset=UTF-8]  
WebTestClient-Request-Id: [1]
Content-Length: [39]  
CSRF-TOKEN: [8ae3e96e-8c98-414e-9db1-e027ea034b3a]

username=testUser01&password=testUser01  


b.  After successful authentication, the server creates a signed JWT (JWS) and returns it as Authorization Header:  
**Response:**  
200 OK OK  
Authorization: [Bearer eyJhbGciOiJ...syYYikNDrWn34mY]  
X-Content-Type-Options: [nosniff]  
X-XSS-Protection: [1; mode=block]  
Cache-Control: [no-cache, no-store, max-age=0, must-revalidate]  
Pragma: [no-cache]  
Expires: [0]  
X-Frame-Options: [DENY]  
Content-Length: [0]  
Date: [Sun, 05 May 2019 15:20:31 GMT]

c. Now the user can access the protected end point, using this valid Authorization token.


