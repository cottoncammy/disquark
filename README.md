# DisQuark

[![Version](https://img.shields.io/maven-central/v/io.disquark/disquark-rest?logo=apachemaven&style=for-the-badge)](https://search.maven.org/artifact/io.disquark/disquark-rest)
[![License](https://img.shields.io/github/license/disquark/disquark?style=for-the-badge&logo=mozilla)](https://www.mozilla.org/en-US/MPL/2.0/)
[![Main branch build](<https://img.shields.io/github/actions/workflow/status/disquark/disquark/ci-main.yml?branch=main&style=for-the-badge&logo=github>)](https://github.com/disquark/disquark/actions/workflows/ci-main.yml)

DisQuark is a reactive library that enables developers to write fast and performant JVM applications that leverage [Discord's REST API](https://discord.com/developers/docs/intro). Includes support for REST interactions and OAuth2. Written from the ground up for use with the [Quarkus](https://quarkus.io) framework using our official [Quarkiverse extension]().

Powered by [Vert.x](https://vertx.io), [SmallRye Mutiny](https://smallrye.io/smallrye-mutiny), and [Immutables](https://immutables.github.io). 

Read our [documentation](https://docs.disquark.io) to learn how to get started and more.

## Kotlin API

DisQuark provides a (WIP) [idiomatic Kotlin API]() for users who prefer to develop their Discord applications in Kotlin. The API provides Kotlin DSLs as alternatives to public-facing Java builders and offers translations of SmallRye Mutiny semantics to Coroutine semantics.

## API Status

DisQuark's API should be considered technical preview, but it will be stable [soon]().

DisQuark currently does not support Discord's Gateway or Voice APIs. Interest in the project will determine whether time is invested to develop corresponding modules.

## Should you use DisQuark?

DisQuark is the newest addition to the existing lineup of *six* JVM libraries.  DisQuark was conceived to address some of the frustrations I've had with the existing alternatives in terms of either their API (I prefer reactive semantics) or their support for high-level REST applications and distributed application architectures. Out of the existing JVM alternatives, DisQuark could be most closely compared to [Discord4J](https://github.com/Discord4J/Discord4J) and [Kord](https://github.com/kordlib/kord) if they only supported REST. 

DisQuark was developed to interact with the Discord API in [Quarkus](https://quarkus.io) applications. If you intend to use [Spring Boot](https://spring.io/projects/spring-boot) or [Ktor](https://ktor.io), you should use Discord4J or Kord as your corresponding API wrapper instead of DisQuark to reduce your dependency requirements. Additionally, it's strongly recommended to use DisQuark along with our [Quarkiverse extension]() which minimizes the boilerplate needed to develop HTTP interactions with DisQuark.

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