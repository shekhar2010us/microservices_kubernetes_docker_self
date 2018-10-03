# Microservices Docker
This project demonstrate a sample retail application using Java, JAX Rest API, Docker and Kubernetes <br>
This is a microservices project.

## Structure of the Project
#### MVC architecture
The app use redis database, to store all user data in database 01 and to store all product data in database 02

### Kubernetes Install

#### Controller - com.app.core.RetailController.java
Contains all code for the backend. This class implements the business logic, such as <br> 
> get user details <br>
> get product details <br>
> user buy a product <br>

#### POJO - com.app.core.Product.java
This class is the plain old java object and describes the attributes of individual products

#### POJO - com.app.core.User.java
This class is the plain old java object and describes the attributes of individual users

#### JAX Rest Beans - com.app.restfulws.resource.MyJax*.java
These classes implements the beans of Rest api

#### JAX Rest APIS - com.app.restfulws.resource.RetailAction.java
This is the main Rest API class that interacts with the APIs and calls relavant methods in the controller to get appropriate response.


References:-
https://www.oreilly.com/ideas/how-to-manage-docker-containers-in-kubernetes-with-java
https://github.com/danielbryantuk/oreilly-docker-java-shopping
