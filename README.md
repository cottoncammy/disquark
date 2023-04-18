# DisQuark

[![Version](https://img.shields.io/maven-central/v/io.disquark/disquark-rest?logo=apachemaven&style=for-the-badge)](https://search.maven.org/artifact/io.disquark/disquark-rest)
[![License](https://img.shields.io/github/license/cottoncammy/disquark?style=for-the-badge&logo=mozilla)](https://www.mozilla.org/en-US/MPL/2.0/)
[![Main branch build](<https://img.shields.io/github/actions/workflow/status/cottoncammy/disquark/ci-main.yml?branch=main&style=for-the-badge&logo=github>)](https://github.com/cottoncammy/disquark/actions/workflows/ci-main.yml)

DisQuark is a reactive library that enables developers to write fast and performant JVM applications that leverage [Discord's REST API](https://discord.com/developers/docs/intro). Includes support for REST interactions, OAuth2, and responding to interactions and executing webhooks without a bot token.

Powered by [Vert.x](https://vertx.io), [SmallRye Mutiny](https://smallrye.io/smallrye-mutiny), and [Immutables](https://immutables.github.io). 

## Installation

Get DisQuark by importing the `io.disquark:disquark-rest` artifact (or the `io.disquark:disquark-rest-kotlin` artifact if you prefer Kotlin) dependency using your preferred build tool. 

## Getting Started

The entrypoint to your DisQuark application depends on your use case.

* `DiscordBotClient` - a client tied to a bot account capable of doing anything a bot token allows you to do; can also receive and respond to interactions and execute webhooks using interaction and webhook tokens

* `DiscordOAuth2Client` - a client tied to a user account's OAuth2 token capable of doing anything a bearer token allows you to do; can also receive and respond to interactions and execute webhooks using interaction and webhook tokens

* `DiscordInteractionsClient` - a client capable of receiving and responding to incoming interactions using interaction tokens, not tied to a bot or user account

* `DiscordWebhookClient` - a client capable of executing webhooks using webhook tokens, not tied to a bot or user account

Each client is created using `create` or `builder` static methods. 

## Kotlin API

DisQuark provides a (WIP) idiomatic Kotlin API for users who prefer Kotlin. The API provides Kotlin DSLs as alternatives to Java builders and offers extension methods to translate SmallRye Mutiny constructs to Kotlin Coroutine `Flow`s and suspended values. **The Kotlin API is strongly recommended**; the Kotlin DSL syntax is much easier to use to build requests than the Java builder syntax.

Get the Kotlin API by importing the `io.disquark:disquark-rest-kotlin` dependency using your preferred build tool.

## API Status

DisQuark's API should not be considered stable for now.

**DisQuark currently does not support Discord's Gateway or Voice APIs**. Interest in the project will determine whether time is invested to develop corresponding modules. For now, the project's development efforts are focused on improving and maintaining the REST module.

## Examples

### Creating a message

Using `io.disquark:disquark-rest`:

```java
class MyApp {
    public static void main(String[] args) {
        var botClient = DiscordBotClient.create(Vertx.vertx(), "TOKEN");
        
        botClient.createMessage(new Snowflake(0L))
                .withContent("Hello World!")
                .subscribe().with(x -> {});
    }
}
```

Using `io.disquark:disquark-rest-kotlin`:

```kotlin
suspend fun main() {
    val botClient = DiscordBotClient.create(Vertx.vertx(), "TOKEN") 
    
    botClient.createMessage(channelId = Snowflake(0L)) {
        content = "Hello World!"
    }.awaitSuspending()
}
```

### Creating application commands

Using `io.disquark:disquark-rest`:

```java
class MyApp {
    public static void main(String[] args) {
        var botClient = DiscordBotClient.create(Vertx.vertx(), "TOKEN");
       
        botClient.bulkOverwriteGlobalApplicationCommands(new Snowflake(0L))
                .withOverwrites(new GlobalApplicationCommandOverwrite("foo")
                    .withType(ApplicationCommand.Type.CHAT_INPUT)
                    .withDescription("Foo commands")
                    .withOptions(List.of(
                        new ApplicationCommand.Option(ApplicationCommand.Option.Type.SUB_COMMAND, "bar", "Foo bar command")
                            .withOptions(List.of(
                                new ApplicationCommand.Option(ApplicationCommand.Option.Type.STRING, "baz", "Baz option")
                                    .withRequired(true)))))
                 ).subscribe().with(x -> {});
    }
}
```

Using `io.disquark:disquark-rest-kotlin`:

```kotlin
suspend fun main() {
    val botClient = DiscordBotClient.create(Vertx.vertx(), "TOKEN") 
    
    botClient.bulkOverwriteGlobalApplicationCommands(applicationId = Snowflake(0L)) {
        chatInputCommand(name = "foo") {
            description = "Foo commands"
            
            subcommand(name = "bar", description = "Foo bar command") {
                stringOption(name = "baz", description = "Baz option") {
                    required = true
                }
            }
        }
    }.onItem().ignoreAsUni().awaitSuspending()
}
```

### Listening and responding to incoming interactions via HTTP

Verification of incoming interactions requires the `org.bouncycastle:bcprov-jdk18on` dependency by default. If not installed, incoming interactions won't have their signatures verified. By default, the web server launched by DisQuark will listen for incoming interactions on `localhost:80`.

```java
class MyApp {
    public static void main(String[] args) {
        var botClient = DiscordBotClient.create(Vertx.vertx(), "TOKEN");
        
        botClient.on(applicationCommand().name("foo").with(option().type(ApplicationCommand.Option.Type.SUB_COMMAND).name("bar").with(option().type(ApplicationCommand.Option.Type.STRING).name("baz"))))
            .onItem().transformToUniAndMerge(interaction -> interaction.respond().withContent("Hello World!"))
            .onItem().ignoreAsUni().await().indefinitely();
    }
}
```

**It's strongly recommended to use DisQuark along with our [Quarkiverse extension](https://github.com/quarkiverse/quarkus-disquark) which minimizes the boilerplate needed to listen and respond to interactions.**

## Using Snapshots

DisQuark snapshots are automatically published with the version `999-SNAPSHOT` when commits are pushed to the main branch. 

To use the latest snapshot in Maven, add the following repository to your `pom.xml`:
```xml
<repositories>
    <repository>
        <id>ossrh</id>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```
