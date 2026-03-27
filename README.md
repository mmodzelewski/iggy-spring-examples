# Iggy Spring Examples

Example Spring Boot applications demonstrating integration with [Apache Iggy](https://iggy.apache.org/) message streaming platform using the Java SDK.

## Modules

### iggy-producer

A message producer that connects to an Iggy server and:
- Creates a stream with `heartbeats` and `notifications` topics on startup
- Sends auto-generated messages to the `notifications` topic at a fixed rate
- Sends periodic heartbeats to the `heartbeats` topic
- Exposes a REST endpoint (`POST /send`) for sending messages manually

### iggy-consumer

A message consumer that connects to an Iggy server and:
- Joins a consumer group (creating it if it doesn't exist)
- Polls the `notifications` topic for new messages at a fixed rate
- Logs received messages with partition and offset information

## Prerequisites

- Java 25
- Running Iggy server (default: `localhost:8090`)

## Configuration

Both modules are configured via `application.yaml`. Key properties under the `iggy` prefix:

| Property | Description | Default |
|---|---|---|
| `host` | Iggy server host | `localhost` |
| `port` | Iggy server port | `8090` |
| `username` | Authentication username | `iggy` |
| `password` | Authentication password | `iggy` |
| `streamName` | Stream name | `dev` |
| `topicName` | Topic name | `notifications` |
| `consumerGroup` | Consumer group name (consumer only) | `consumer-group` |
