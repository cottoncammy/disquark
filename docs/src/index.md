---
hide:
- navigation
- toc
---

# DisQuark

DisQuark is a reactive wrapper around Discord's REST API that enables developers to write fast and performant applications leveraging Discord's REST API. Includes support for REST interactions and OAuth2. Gateway API coming ~~soon(tm)~~ eventually.

## Reactive, but not required

DisQuark is written with [Vert.x](https://vertx.io), a toolkit for developing asynchronous JVM applications, and [SmallRye Mutiny](https://smallrye.io/smallrye-mutiny), an implementor of the [Reactive Streams protocol](http://www.reactive-streams.org), to ensure that your Discord application performs under load before you ever have to worry about horizontal or vertical scaling. That doesn't mean any restrictions are imposed on how you write your DisQuark applications: you can `await` Mutiny types to go back to the synchronous world.

## Rate limiting

DisQuark performs automatic global and bucket rate limiting against the Discord API, automatic rate limited request retries, and separates authenticated and unauthenticated requests to ensure you encounter as few `429`s as possible.

## Authentication not required

One of DisQuark's main goals is to support all kinds of Discord REST use cases (but not self-botting). 

You can use DisQuark without a bot user or an OAuth2 token if you are only executing webhooks or responding to interactions.

## ~~Distributed ready~~ soon

Unlike many other wrappers for the Discord API, DisQuark considers distributed applications a first-class citizen rather than an afterthought in its API design.

DisQuark includes support for distributed rate limiting with Bucket4J (and alternative rate limiting implementations, including no-op), and makes no assumptions about how you send a request to the Discord API: you can configure HTTP proxying with Vert.x, use a custom `Requester` (or one of our Vert.x messaging broker `Requester`s to integrate with your distributed system), and use a custom `Codec` or `Response` to configure how your downstream DisQuark application serializes a response into a POJO: from protobuf, JSON, or anything in between.

## ~~Quarkus integration~~ eventually

DisQuark was written from the ground up specifically for integration with the [Quarkus](https://quarkus.io) framework using the DisQuark Quarkus extension. Though it's not required to use DisQuark from a Quarkus application, the extension includes more bells-and-whistles than the standard DisQuark library which is intended to be lower-level.