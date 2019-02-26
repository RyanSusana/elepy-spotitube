# Project: Spotitube

### How to run
There are 2 ways to run, with Docker or with an embedded MongoDB instance. They both spin up the backend at http://localhost:1997.
```
Server: http://localhost:1997
Username: ryan
Password: susana
```

#### How to run with Docker-Compose

1. Open terminal in the project and run `mvn clean package`
2. Install Docker and Docker-Compose
3. Run `docker-compose up`


#### How to run with an embedded Mongo Server(Non persistent)
1. Set the environmental variable `testing=true`
2. Run `com.ryansusana.spotitube.Main`

### Research: Throw the old way, ___away___!
For my research I would like to talk about swapping out all parts of the HAN spec for Elepy and MongoDB.
How to 

These are examples of questions I can ask throughout the research paper.

#### Why Mongo?

The offlineAvailable flag per track, per playlist. To get this to properly function in a relational database, I must create a column in a PlaylistTrack table and remove it from the simplified `Track` domain object.
I also foresee `offlineAvailable` causing more difficulties in a Relational Database. There is also access to a mocked version of mongo, _Fongo_. I can use this to avoid using many stubs in my Unit and Integration tests.


#### Why Elepy?
- The Data Access Layer and a big part of the Service and Routing Layer is generated for me.
- I made Elepy myself.
- Deployed in a JAR, not a WAR. This makes the application easier to deploy in a micro-service architecture. The front-end is completely different service. I also have the ability to deploy the DB, Front-end and Back-end side-by-side with tools such as Docker and Docker-Compose.
- I get much more control on things such as Security in comparison to JAX-RS and Spring Security. 


#### How will this project improve Elepy?


#### What are features that Elepy should implement based on doing this project?
 

#### Did I miss using JAX-RS/Spring?



