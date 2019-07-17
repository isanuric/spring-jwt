# spring-jwt
* REST
* Spring Boot
* JWT
* Docker
* Minikube


# Quick Start
* clone repository
* $ mvn clean install

# Branches
* **branch master:** main functionality.
* **branch workshop:** integration test for Authorization header.

# Deployment
## Spring 
* $ mvn spring-boot:run
* http://localhost:8080/
## Docker
* $ mvn clean install
* $ docker build -t bar .  
* $ docker run -d -p 8080:8080 bar:latest
* http://localhost:8080/
## Minikube (Kubernetes v1.14.3)
* Download and start latest [minikube](https://kubernetes.io/docs/setup/minikube/) version 
* $ mvn clean install
* $ eval $(minikube docker-env)
* $ docker build -t bar .  
* $ kubectl apply -f bar.yaml  
* $ minikube service bar --url  
* $ click on result of last step and add _/index_ to path: <MINIKUBE-SERVICE-URL:PORT>/index


# Unit test
## Integration test
#### Run/debug createAndUseTokenToAccessController_success() test:

* first step. The user sends a POST request with an csrf token and a valid credential/principal as body: 

**Request:**  
POST http://localhost:49609/login  
Content-Type: [application/x-www-form-urlencoded;charset=UTF-8]  
WebTestClient-Request-Id: [1]
Content-Length: [39]  
CSRF-TOKEN: [8ae3e96e-8c98-414e-9db1-e027ea034b3a]
username=testUser01&password=testUser01  


* second step. After successful authentication, the server creates a signed JWT (JWS) and returns it as Authorization Header: 
 
**Response:**  
200 OK OK  
**Authorization: [Bearer eyJhbGciOiJ...syYYikNDrWn34mY]**  
X-Content-Type-Options: [nosniff]  
X-XSS-Protection: [1; mode=block]  
Cache-Control: [no-cache, no-store, max-age=0, must-revalidate]  
Pragma: [no-cache]  
Expires: [0]  
X-Frame-Options: [DENY]  
Content-Length: [0]  
Date: [Sun, 05 May 2019 15:20:31 GMT]

* third step. Now the user can access the protected end point, using this valid Authorization token.


