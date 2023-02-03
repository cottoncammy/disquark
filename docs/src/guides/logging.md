# Logging

 DisQuark exposes useful trace- and debug-level logs using [SLF4J](https://slf4j.org). If you don't have an SLF4J implementation imported as a dependency in your application, you won't receive these logs. It's recommended to install an SLF4J implementation in a production environment so you can debug any issues that may arise during the runtime of your DisQuark application, especially since DisQuark is an incubating project. 

## SLF4J Simple

[SLF4J Simple](https://mvnrepository.com/artifact/org.slf4j/slf4j-simple) is a basic implementation of SLF4J that outputs info-, warn-, and error-level logs directly to `System.err`. This isn't recommended in production since you won't receive trace- and debug-level logs and is limited in its log-outputting mechanism, but it can be useful in a development environment.

## Log4J2

[Log4J2](https://logging.apache.org/log4j/2.x/) is the recommended SLF4J implementation in DisQuark applications due to its proven [superior performance](https://logging.apache.org/log4j/2.x/performance.html). You can use Log4J2 as an SLFJ implementation by importing [`log4j-core`](https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core) and [`log4j-slf4j-impl`](https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl) as dependencies using your preferred build tool. Find the configuration reference [here](https://logging.apache.org/log4j/2.x/manual/configuration.html). 

## Logback

Another popular SLF4J implementation is [Logback](https://logback.qos.ch/manual/introduction.html). You can use Logback by importing it from [Maven Central](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic) using your preferred build tool. Find the configuration reference [here](https://logback.qos.ch/manual/configuration.html).

## Java Util Logging

You can also use [`java.util.logging`](https://docs.oracle.com/en/java/javase/19/core/java-logging-overview.html), also called JDK 1.4 logging, as an SLF4J implementation by importing the [JDK14 binding](https://mvnrepository.com/artifact/org.slf4j/slf4j-jdk14). 