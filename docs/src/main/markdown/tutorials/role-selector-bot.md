# Role Selector Bot

Let's learn the fundamentals of DisQuark and SmallRye Mutiny by looking at a more advanced use case: a role selector bot.

## Prerequisites
* JDK 11+
* Maven or Gradle project with `io.disquark:disquark-rest:{{ versions.disquark }}` imported as a dependency
* [Discord application with a bot user](https://discord.com/developers/docs/getting-started#creating-an-app)
* URL to receive incoming interactions from Discord
* Deployment target for your application
* Discord server with your bot and appropriate permissions configured

## Task

Assume we're part of a Discord community and we've been tasked with creating an application that allows new users to select their own roles to identify themselves and grant them access to role-locked channels. The server admin has seen other bots and has decided that they want this bot to render message components that the user can interact with to choose their roles.

There's two possible approaches we have to create this solution (both using interactions):
1. Application command that the new user can invoke to send an ephemeral message to them with attached components they can use to select their roles
2. Channel we use to send messages with rendered message components that new users can interact with to select their roles

Let's go with option two to create a better new-user experience for the server.

!!! warning
    
    Before continuing, remember that DisQuark can only interact with Discord's REST API. This means that our application can't receive incoming interactions from Discord's Gateway API, and can only receive incoming interactions via HTTP. Therefore, you must have an interactions URL that you can configure in your application to receive incoming interactions from Discord.

## Requirements analysis

Let's think a bit more about the requirements of our application before we start coding. Our application needs to do three things:
1. Create messages in a channel with our message components
2. Receive and respond to message component interactions from Discord
3. Toggle user roles based on message component interaction input

The first thing our application does only needs to be done once. We don't want our application to send those messages each time the application is run, because our application might be run more than once. Therefore, in a production environment, it might be a good idea to separate the message creation logic into an application command, or separate it into a separate application like a webhook that is executed when a message creation web dashboard submits a request. However, for the purpose of this simple tutorial, we'll assume that this application will start once and will run forever without needing to be restarted.

In terms of the message component we will be attaching, let's just stay simple and attach a single `ROLE_SELECT_MENU` component.

Finally, notice that we need to send messages without an existing webhook and we need to toggle user roles. Both of these require access to a bot user, so we'll need a `DiscordBotClient`.

## Implementation

With those decisions and caveats out of the way, let's start coding. First, let's create our `DiscordBotClient` instance and create a message with a role select menu with a custom id of `roles`:
```java
{{ TODO }}
```

Keep a note of that custom ID, we'll need it for later.

Next, we need to configure our `DiscordBotClient` to receive interactions from Discord. Discord requires that interactions sent via HTTP be validated. By default, DisQuark requires the [*BouncyCastle*](https://bouncycastle.org/java.html) library to validate incoming interactions. If BouncyCastle isn't imported by your application and you haven't modified your client's `InteractionsValidator`, DisQuark won't validate any incoming interactions, and you'll be unable to configure your interactions URL unless you verify the interactions before the request reaches DisQuark's web server (which is the recommended approach in a production environment). For simplicity, let's add the BouncyCastle library to our application's build tool:

=== "pom.xml"
```xml
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15on</artifactId>
    <version>{{ versions.bouncycastle }}</version>
</dependency>
```

=== "build.gradle"
```groovy
dependencies {
    implementation 'org.bouncycastle:bcprov-jdk15on:{{ versions.bouncycastle }}'
}
```

=== "build.gradle.kts"
```kotlin
dependencies {
    implementation("org.bouncycastle:bcprov-jdk15on:{{ versions.bouncycastle }}")
}
```

To use BouncyCastle, we have to configure our application's Java `SecurityManager`. Let's do that in the code we've written so far:
```java
{{ TODO }}
```

Okay, now we can write the code to actually receive interactions. The entrypoint to handling interactions using DisQuark is the `on(InteractionSchema)` method which returns a `Multi` of incoming `CompletableInteraction`s matching that schema. 

!!! note
    
    DisQuark won't create or start a web server until `on(InteractionSchema)` is called for the first time. By default, the web server will listen for interactions on port 8080 at the `/` URL.

DisQuark has a number of static methods available to create `InteractionSchema`s, and you can chain methods together to refine that schema. We will use the following schema to receive a `Multi<MessageComponentInteraction>`:
```java
{{ TODO }}
```

This schema checks that the incoming interaction is a message component interaction from a `ROLE_SELECT_MENU` whose custom ID is `roles`. If our application has more interactions and we receive a different one, we won't receive it in this `Multi`. 

Now that we have a `Multi<MessageComponentInteraction>`, we need to process incoming interactions from the stream and respond to them. Because Discord requires that interactions be responded to in some way within three seconds of receiving them, let's respond to the interaction with `deferResponse(true)`. This means that we'll show a loading state to the user and respond once we're ready:
```java
{{ TODO }}
```

Now, let's examine the resolved data from the incoming interaction to obtain the user's submitted roles and assign the role to the user:
```java
{{ TODO }}
```

Notice that with this code, we're only assigning the role to the user. What we actually need to do is assign the role to *or* remove the role from the user based on whether the user has that role, like a toggle:
```java
{{ TODO }}
```

Okay, there's an additional issue: some roles, like roles managed by integrations, can't be assigned to users. Additionally, if there are roles with elevated permissions (like an admin or moderator role), we don't want the user to be able to grant that to or remove that from themselves. We can mitigate that like so:
```java
{{ TODO }}
```

Finally, there's another issue inherent to how roles work in Discord: if the invoking user's highest-level role (in the server's list of roles) is above the bot user's role in the list, the bot will be unable to manage that user's roles. So, we need the bot's server integration's role to be the highest role in the server. We can modify that in a request:
```java
{{ TODO }}
```

Okay, finally for real this time, there's one last thing we need to do before we write the code to respond to that interaction: handle failures. When a failure is received in a `Multi`, it will stop emitting items unless that failure is handled in such a way that the stream can continue emitting items. This is especially important in our case, because we actually want this `Multi` to keep emitting items *forever* in an unbounded fashion, meaning that we want to receive every message component interaction that Discord sends from the time our application runs until it is shut down. We will handle failures in our `Multi` by silently discarding them (which isn't recommended):
```java
{{ TODO }}
```

Let's respond to that interaction with a message indicating which roles we've added or removed:
```java
{{ TODO }}
```

It's time to subscribe to our streams so our application won't just do nothing when it is run. When writing a reactive application with an unbounded `Multi`, you need to `await()` the completion of the `Multi`, which will never occur, to keep the JVM alive forever and stop it from exiting immediately after starting your program. However, if you want your program to truly be reactive, you should only be awaiting one stream in your application (again, to prevent your JVM from exiting), so we'll have to chain our `Multi` and our `Uni` together in a single *pipeline*:
```java
{{ TODO }}
```

Phew, finally done! Deploy your application, setup your interactions endpoint URL in your Discord application, ingress traffic to your URL from your deployed app, and test it out. 

## Deploying
TODO