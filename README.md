# ConsultationWeb
[![Build Status](https://travis-ci.org/JaanJanno/ConsultationWeb.svg?branch=master)](https://travis-ci.org/JaanJanno/ConsultationWeb)

[Front-end server](http://consultationweb.herokuapp.com/)

[Rest server](http://consultationwebserver.herokuapp.com/)

Web application for arranging consultations for academic writing in University of Tartu.


## Team members:

Jaan Janno,
Tõnis Kasekamp, 
Salman Lashkarara

## API Definition
The API of the backend with examples can be observed in the following link:
http://docs.consultationweb.apiary.io/#

## How to run the server
The project is a web application and uses Spring Boot, Maven and Java 8. If those things sound unfamiliar to you, fear not, for the help is here!
The web server is able to use a local in-memory database, a local Postgres database and a Heroku database.

#### Necessary
* Maven. Good setup guide here http://www.mkyong.com/maven/how-to-install-maven-in-windows/. On Linux follow http://iambusychangingtheworld.blogspot.com/2014/04/install-and-configure-java-and-maven-in.html. On Mac use Google.

#### Recommended
* Eclipse (preferably Spring Tool Suite)
* Eclipse m2e plugin (without it Eclipse will cry and think the project is broken). Use "Install new software" to add, page http://download.eclipse.org/technology/m2e/releases
* Postgres database (really optional)

#### Project setup
1. Clone the git repo
2.  To use the in-memory database go to the file src/main/resources/application.properties and change the row from  `spring.profiles.active=default` to `spring.profiles.active=test`. Write `postgres` for local Postgres database. 
3. In command line write `mvn spring-boot:run`
4. Server should now be accessible from `localhost:8080`. 
5. `mvn eclipse:eclipse` will generate the files to import to Eclipse. After that you can `import existing projects`.

To run the server you only need `mvn spring-boot:run`

#### Running tests
Use maven to run tests with `mvn test`.
