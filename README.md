# DisQuark

[![Version](https://img.shields.io/maven-central/v/io.disquark/disquark-rest?logo=apachemaven&style=for-the-badge)](https://search.maven.org/artifact/io.disquark/disquark-rest)
[![License](https://img.shields.io/github/license/disquark/disquark?style=for-the-badge&logo=mozilla)](https://www.mozilla.org/en-US/MPL/2.0/)
[![Main branch build](<https://img.shields.io/github/actions/workflow/status/disquark/disquark/ci-main.yml?branch=main&style=for-the-badge&logo=github>)](https://github.com/disquark/disquark/actions/workflows/ci-main.yml)

DisQuark is a reactive library that enables developers to write fast and performant JVM applications that leverage Discord's REST API. Includes support for REST interactions and OAuth2. Written from the ground up for use with the [Quarkus](https://quarkus.io) framework using our official [Quarkiverse extension]().

Powered by [Vert.x](https://vertx.io), [SmallRye Mutiny](https://smallrye.io/smallrye-mutiny), and [Immutables](https://immutables.github.io). 

Read our [documentation](https://docs.disquark.io) to learn how to get started and more.

# Kotlin

DisQuark provides a (WIP) [idiomatic Kotlin API]() for users who prefer to develop their Discord applications in Kotlin.

# Status

DisQuark's API should be considered technical preview, but it will be stable [soon](). 

DisQuark currently does not support Discord's Gateway or Voice APIs. Interest in the project will determine whether time is invested to develop corresponding modules.

# Snapshots

DisQuark snapshots are published with the version `999-SNAPSHOT` when commits are pushed to the main branch. To use the latest snapshot in Maven, add the following repository to your `pom.xml`:
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