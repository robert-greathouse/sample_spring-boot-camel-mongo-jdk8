[![Build Status](http://img.shields.io/travis/ewerk/sample_spring-boot-camel-mongo-jdk8.svg?style=flat)](https://travis-ci.org/ewerk/sample_spring-boot-camel-mongo-jdk8) [![License](http://img.shields.io/badge/license-Apache-brightgreen.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

# SPRING-BOOT-CAMEL-MONGO-JDK8 

## Introduction
Sample Spring Boot application that integrates some of the most commonly used top-notch frameworks 
in the Java ecosystem. The example is also used for experimenting with all these frameworks, in
order to create a lightweight enterprise service, that runs independently from an external 
container but still provides enterprise ready features. 

Further more, most examples/tutorials out there focus on a specific technology for guaranteeing a
steeper learning curve. Experience shows that integrating different frameworks often yields unknown
problems. Another thing to consider is that often production grade features are also not regarded.
This sample aims at providing a full fledged prototype that could be used in production.

The following requirements will be 
fulfilled by the app.
 
* Standalone application
* REST API via embedded servlet container
* MongoDB used as data store
* TX management
* Vagrant+Ansible VM for development ease
* Multi module build, with profile activated integration tests

Please see the issues section for upcoming features and bugs.

## Used libraries (so far)
### General
* [JDK-8](http://www.oracle.com/technetwork/java/javase/downloads)
* [Gradle-2.2](http://www.gradle.org) multi module project
* [Spring Boot](http://projects.spring.io/spring-boot)

### Persistence
* [MongoDB](http://www.mongodb.org) for data persistence
* [spring-data-mongodb](http://projects.spring.io/spring-data-mongodb)

### Business processes
* [Apache Camel](http://camel.apache.org/) 

### Utilities
* [JSR310](http://docs.oracle.com/javase/tutorial/datetime) Date/Time API
* [Guava](https://code.google.com/p/guava-libraries)
* [AutoValue](https://github.com/google/auto/tree/master/value)

### Testing
* [TestNG](http://testng.org) - Testing framework/Test runner
* [Mockito](https://code.google.com/p/mockito) - Library for creating mock objects/behaviour for unit tests
* [AssertJ](http://joel-costigliola.github.io/assertj) - Fluent assertions for Java
* [REST-assured](https://code.google.com/p/rest-assured) - Easy testing of REST endpoints

## Building the project
The project is organized as a polyglot gradle multi-module project. You do not have to download 
Gradle standalone, because the project uses the Gradle wrapper.

### Prerequisites
* Download and install Oracle JDK-8
* Download and install Ansible

### Configuration
The `prototype-app` default configuration files (application.properties, logback.xml) are located under `src/main/resources`. This configuration is always active per default. It may be overridden by defining the `SPRING_CONFIG_LOCATION` environment property.

The integration test suite hosts an own application.properties file that overrides some properties (database URI) in order to run the integration tests against the test environment.

### Global project tasks
Run the following commands/tasks to get a starting point for the project (ensure `./gradlew` is executable).
* `./gradlew tasks` - Show a list of all available tasks

### Module specific tasks
#### prototype-vm
* `./gradlew vagrantUp` or native command `vagrant up` for running the VM
* `./gradlew vagrantHalt` or native command `vagrant halt` for halting the VM
* native command `vagrant ssh` for connecting to the VM via SSH

#### prototype-app
* `./gradlew bootRun` to run the prototype app from the command line
* `./gradlew test` run the unit tests
* `./gradlew [clean] build` runs a full build

## Development virtual machine
The sample provides a [Debian 7](http://www.debian.org) (Wheezy) based [Vagrant](http://www.vagrantup.com) 
VM that hosts the MongoDB used. The MongoDB ports are forwarded to your localhost. The VM is located 
in the Gradle module `prototype-vm`. In order to start the VM, go to `prototype-vm` a 
type `vagrant up`. The command `vagrant halt` will stop the VM. For provisioning, 
we use [Ansible](http://www.ansible.com/home).

The VM provisioning by Ansible is organized into roles. The following roles are currently present:

* `shell-utils` - A role for installing collection of common bash utilities like `vim`, `htop`, `curl`, `wget`, ...
* `java` - A role for installing Oracle JDK-8 via APT. Please note that this non-interactive installation automatically accepts the Oracle license agreement.
* `mongodb` - A role that install and configures the MongoDB server
* `prototype` - A role that install/sets up all stuff needed by the `prototype-app`, e.g. accounts, permissions, ...

## Integration testing
The integration test suite can be find within the `prototype-app` folder `src/integration/java`. We
are using the EWERK `integration-test-plugin` to run the integration tests. This works like having
unit tests under `src/test/java`. The `integration-test-plugin` is available through 
[Gradle Plugin Portal](http://plugins.gradle.org).

In our opinion, this yields some advantages:

* Divide unit test and integration test sources
* More easy build config (execution)
* More simple build scripts for each module
* Use jacoco unit test and integration test coverage within a distinct module which can be used by sonar 

The suite is based on the [Spring Testing Framework](http://docs.spring.io/spring-framework/docs/current/spring-framework-reference/html/testing.html)
brought in via `spring-boot-starter-test`. It launches the complete Spring Boot application,
having specific configuration property overrides for the integration tests. These overrides
are triggered because of the additional active profile `integration-test` from the base integration
test class

The integration tests are normally not part of the build cycle, so that a full build with unit tests 
can be run from scratch. In order to launch the integration tests, please run the following command.

`$> ./gradlew integrationTest`

The REST API integration tests are run against a embedded Jetty container. The container boots with 
a random port number to avoid collisions with running systems, what gets really interesting
on CI environments.

Also you should have a look at AbstractIntegTest class, which is the fundamental heart of the 
integration test suite. It took some time figuring out getting all set up using the TestNG runner.

REST-assured framework provides a fluent DSL for easily testing REST endpoints.

## Camel integration
The Camel integration is presented in the `com.ewerk.prototype.proc` package. There are currently
two main routes configured that represented the stubbed business logic of the prototype.

## JSR-310
In order to make the JSR-310 date/time API working for our model beans, the following steps had to
be done:

* add Jackson JSR-310 module to classpath
* register `CustomConversions` bean (see `PersistenceConfiguration`) to enable persistence of `LocalDate` class

__Hint__: Spring Boot has an issue registering the Jackson JSR-310 module from classpath when 
using `@EnableWebMvc`. See [GH-1620](https://github.com/spring-projects/spring-boot/issues/1620) 
for details.
   
## Google AutoValue
Google `AutoValue` provides a convenient way to write __immutable__ Java beans without having to write
all the boilerplate code again and again (`toString`, `equals`, `hashCode`, private constructors, ...).

To integrate `AutoValue` to the sample prototype, the following things where necessary:
* add AutoValue to the `prototype-apps` dependencies
* use the new EWERK `auto-value-plugin` via Gradle plugin portal

Using `AutoValue` for immutable bean types really fastens up coding and also prevents you from doing 
all the mistakes that are associated with mutable types. See the fantastic book 
[Effective Java, Item 15](http://uet.vnu.edu.vn/~chauttm/e-books/java/Effective.Java.2nd.Edition.May.2008.3000th.Release.pdf) 
for details on that.

Still, there are some limitations. We have made the experience that `AutoValue` simply cannot be used
for annotated domain model beans (e.g. JPA entities, `@Document` classes, classes with XML or JSON
mapping annotations, ...). This is because most of the frameworks used for these domain objects, like 
`Hibernate`, `Jackson` or `spring-data-*`, imply some restrictions on the model classes. But this
may change in future.