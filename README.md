# DisQuark

[![Version](https://img.shields.io/maven-central/v/io.disquark/disquark-rest?logo=apachemaven&style=for-the-badge)](https://search.maven.org/artifact/io.disquark/disquark-rest)
[![License](https://img.shields.io/github/license/disquark/disquark?style=for-the-badge&logo=mozilla)](https://www.mozilla.org/en-US/MPL/2.0/)
[![Main branch build](<https://img.shields.io/github/actions/workflow/status/disquark/disquark/ci-main.yml?branch=main&style=for-the-badge&logo=github>)](https://github.com/disquark/disquark/actions/workflows/ci-main.yml)

DisQuark is a reactive library that enables developers to write fast and performant JVM applications that leverage [Discord's REST API](https://discord.com/developers/docs/intro). Includes support for REST interactions, OAuth2, and responding to interactions and executing webhooks without a bot token.

Powered by [Vert.x](https://vertx.io), [SmallRye Mutiny](https://smallrye.io/smallrye-mutiny), and [Immutables](https://immutables.github.io). 

Read our [documentation](https://docs.disquark.io) to learn how to get started and more.

## Kotlin API

DisQuark provides a (WIP) [idiomatic Kotlin API]() for users who prefer to develop their Discord applications in Kotlin. The API provides Kotlin DSLs as alternatives to public Java builders and offers extension methods to translate SmallRye Mutiny constructs to Coroutine constructs. The Kotlin API is strongly recommended since the DSL syntax for building requests is _currently_ easier to use than the Java request builder syntax.

## API Status

DisQuark's API should be considered technical preview, but it will be stable [soon]().

**DisQuark currently does not support Discord's Gateway or Voice APIs**. Interest in the project will determine whether time is invested to develop corresponding modules. For now, the project's development efforts are focused on improving and maintaining the REST module (see the open issues).

## Why a sixth Java API wrapper?

DisQuark is the newest addition to the existing lineup of *five* Java libraries for Discord. DisQuark was conceived to address some of the frustrations I've had with the existing alternatives in terms of either their API (I prefer reactive semantics) or their support for REST-only applications and distributed application architectures.

## Should you use DisQuark?

It's recommended to use DisQuark only if you enjoy reactive programming syntax or if you intend to use our Kotlin API. DisQuark was developed to interact with the Discord API in [Quarkus](https://quarkus.io) applications. Therefore, it's strongly recommended to use DisQuark along with our [Quarkiverse extension]() which minimizes the boilerplate needed to develop HTTP interactions with DisQuark. If you intend to use [Spring Boot](https://spring.io/projects/spring-boot) or [Ktor](https://ktor.io), you should use [Discord4J](https://github.com/Discord4J/Discord4J) or [Kord](https://github.com/kordlib/kord), the two existing JVM wrappers that most closely resemble DisQuark, as your corresponding API wrapper instead to reduce your dependency requirements.

You shouldn't use DisQuark in production until its API is stable and until it's had more time to incubate in the Discord ecosystem. You shouldn't use DisQuark at all if you intend to use it to interface with the Gateway or Voice APIs. 

## Using Snapshots

DisQuark snapshots are automatically published with the version `999-SNAPSHOT` when commits are pushed to the main branch. To use the latest snapshot in Maven, add the following repository to your `pom.xml`:
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