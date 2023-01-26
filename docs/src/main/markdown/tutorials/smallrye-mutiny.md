# Getting Started with SmallRye Mutiny

DisQuark is powered by [SmallRye Mutiny](https://smallrye.io/smallrye-mutiny), an event-driven reactive programming library that allows developers to write fully asynchronous code. In a nutshell, Mutiny enables Discord applications written with DisQuark to be completely asynchronous by wrapping all blocking code in asynchronous event sources called `Uni` and `Multi`.

## What?

`Uni<T>`

: represents an asynchronous action that emits a stream of a single event (one item or failure)

`Multi<T>`

: represents an asynchronous action that emits a stream of potentially endless events (n items or one failure)

Both types do nothing until you request events from the stream. You do this by either *subscribing* to the stream's events asynchronously, or synchronously *awaiting* the result.

## How?

All asynchronous actions in DisQuark are wrapped in Mutiny types, so if you want to do anything in DisQuark, you need to either `subscribe` to or `await` a returned Mutiny type. Subscribing to a Mutiny type means you will have to write your code to handle *events* in a *reactive* fashion, while awaiting means that you just wait for the *result* by *blocking*, whether that result is the expected item or an exception. 

Let's take a look at both approaches by creating a message in a channel:

=== "Reactive"
```java
{{  snippet('tutorials/SmallRyeMutiny.java', 'create-message-subscribe') }}
```

=== "Blocking"
```java
{{ snippet('tutorials/GettingStarted.java', 'create-message-await') }}
```

### Subscribing

If you're subscribing to a `Uni` or `Multi`, you handle the emitted events by chaining methods together that allow you to process the event in some way. `Uni`s and `Multi`s can fire many events, but in most cases, you will only want to handle `item` and `failure` events using `onItem()` and `onFailure()` method chains. Which methods to chain together depends entirely on your use case. 

Let's look at a few of the most common event processing methods at our disposal by expanding our reactive example from earlier:
```java
{{ snippet('guides/SmallRyeMutiny.java', 'create-message-events') }}
```

!!! tip 

    For a look into how to structure your reactive DisQuark application's code, check out the [Role Selector Bot tutorial](role-selector-bot.md).

### Awaiting

If you want to, you can ignore Mutiny entirely and just `await` everything that returns a `Uni` or `Multi`. However, this means that your application will be synchronous: requests to Discord will block the calling thread until a response is received. For most use cases, this is fine and your code will be performant anyway. But as your application grows and becomes more complex, blocking the calling thread will lead to longer wait times in your application.