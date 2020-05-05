# MicroStream BookStore Demo

Deploy this demo to Gitpod:

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/microstream-one/bookstore-demo)

## Running locally

This is a [Spring Boot](https://spring.io/guides/gs/spring-boot/) application built using [Maven](https://spring.io/guides/gs/maven/) and [npm](https://www.npmjs.com/). You can build and run it from the command line:

```
git clone https://github.com/microstream-one/bookstore-demo.git
cd bookstore-demo
mvn spring-boot:run
```

Then open [http://localhost:8080/](http://localhost:8080/)

![Screenshot](./src/main/resources/META-INF/resources/frontend/images/ui.jpg)

## Description

The MicroStream BookStore Demo is a fully fledged sample application.
It shows how to design an application with MicroStream from ground up.

The model classes can be designed in any way you want. 
No need to extend or implement special types, nor to use any annotations.
Just use the interface based design like in this example or plain Pojos.

The [data layer](./src/main/java/one/microstream/demo/bookstore/data/) also contains validation and concurrency handling. 

We used [Vaadin](https://vaadin.com) to create the [UI](./src/main/java/one/microstream/demo/bookstore/ui/).
Start point is the main class [VaadinApplication](./src/main/java/one/microstream/demo/bookstore/VaadinApplication.java).

## If you find a bug or want to suggest an improvement

Please feel free to report issues here: [https://github.com/microstream-one/bookstore-demo/issues](https://github.com/microstream-one/bookstore-demo/issues)

## License

The MicroStream BookStore Demo is released under the [MIT License](https://opensource.org/licenses/MIT).




