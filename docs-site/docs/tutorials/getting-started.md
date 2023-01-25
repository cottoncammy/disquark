# Getting Started with DisQuark

## Installation

DisQuark requires Java 11 or later. Use DisQuark by including it as a dependency in your preferred build tool:

=== "Apache Maven"

=== Gradle

=== Gradle (Kotlin DSL)

## Introduction

DisQuark is a reactive library that enables developers to interface with Discord's REST API without having to worry about its underlying complexities. DisQuark currently does not support Discord's Gateway API. The typical DisQuark application will be written in one of three flavors:

* An application that handles incoming REST interactions from Discord (requires a URL)
* An application that executes existing webhooks in response to something
* A backend application that consumes a user's OAuth2 token sent from a frontend to do something

To support the use cases described above, we provide several entrypoints to start interfacing with the Discord API:

`DiscordBotClient`

: a client tied to a bot account capable of doing anything a bot token allows you to do, can also receive and respond to interactions and execute webhooks using interaction and webhook tokens

`DiscordOAuth2Client`

: a client tied to a user account's OAuth2 token capable of doing anything a bearer token allows you to do, can also receive and respond to interactions and execute webhooks using interaction and webhook tokens

`DiscordInteractionsClient`

: a client capable of receiving and responding to incoming interactions using interaction tokens, not tied to a bot or user account

`DiscordWebhookClient`

: a client capable of executing existing webhooks using webhook tokens, not tied to a bot or user account

You don't necessarily need a bot or user account to use DisQuark. If you're just receiving interactions and responding to them without needing to call the Discord API with a bot token, use the `DiscordInteractionsClient`. Likewise, if you're just executing webhooks using webhook tokens, use the `DiscordWebhookClient`. `DiscordBotClient`s and `DiscordOAuth2Client`s are capable of doing both, in addition to having access to the Discord API endpoints which require authentication.

## Usage

Let's look at an example by assuming we want to write a message in a channel using a bot account. For this use case, we will need a `DiscordBotClient` instance:

Now that we have our instance, we can create a message:

This example is slightly misleading, as at this point our code will actually do *nothing*. Notice that our local variable returns a `Uni<Message>`. This type, `Uni`, is a fundamental type in *SmallRye Mutiny*, the library that powers DisQuark's reactive approach to interfacing with the Discord API. You should check out our SmallRye Mutiny tutorial up next to learn what a `Uni` is and how to use it, but for now, if you want to actually send that message, just modify your code snippet: 

Discovering more Discord API methods is relatively straightforward: client method names map 1:1 to endpoint names documented in Discord's API docs. Otherwise, start typing and use your IDE's autocompletion. You can also look at the Javadocs for each client.