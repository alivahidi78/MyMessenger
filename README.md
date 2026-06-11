# MyMessenger

A real-time desktop messaging application built with Java and JavaFX, using raw TCP sockets for client-server communication.

---

## Features

- **User accounts:** sign up and sign in with a username, password, and profile image
- **Direct messaging:** real-time one-on-one text chat
- **Group chat:** create groups and message multiple people at once
- **File transfer:** send and receive files within any conversation
- **Contact search:** find other users by username
- **Online/offline status:** live presence indicators updated as users connect and disconnect
- **Persistent storage:** the server serializes all users, groups, and message history to disk

---

## Architecture

The project follows a classic client-server model over TCP sockets. The client and server share a common `util` package that defines the serialized protocol objects (requests, answers, messages).

```
MyMessenger/
├── src/application/
│   ├── client/          # JavaFX desktop client
│   │   ├── controllers/ # UI controllers (MVC)
│   │   ├── modules/     # Network, cache, event handling
│   │   └── views/       # FXML layouts, CSS, images
│   ├── server/          # Socket server
│   │   └── modules/     # Database, network dispatcher, connection handlers
│   └── util/            # Shared protocol: requests, answers, messages, user models
```

### Client

The client maintains **three persistent socket connections** for the duration of a session:

| Connection | Purpose |
|---|---|
| Messaging | Bidirectional stream of `Message` objects for real-time chat |
| Search | Sends user search queries, receives matching `Info` objects |
| UserInfo | Fetches user/group metadata on demand |

Additional short-lived connections are opened for one-off operations (sign-up, group creation, file upload/download).

A background `MessageReceiver` thread listens on the messaging socket and dispatches incoming messages and server events to the UI via `Platform.runLater()`.

### Server

The server accepts connections on **port 8085** and dispatches each to the appropriate handler based on the first `Request` object received:

- **`MessagingConnection`:** authenticates the user, then relays messages to recipients in real time and broadcasts presence updates
- **`SearchConnection`:** accepts a query string and returns matching users
- **`UserInfoConnection`:** returns full `User` or `Group` metadata for a given ID

A singleton `Database` class holds all state in memory and flushes it to `data/DATA` via Java serialization on every write.

### Communication Protocol

All objects exchanged over the wire are standard Java serialized objects. The class hierarchy is:

- `Request` / `RequestType`: sent from client to server
- `Answer` / `AnswerType`: sent from server to client in response to requests
- `Message` / `MessageType`: bidirectional on the messaging connection
  - `TextMessage`: plain text
  - `FileMessage`: file metadata and payload
  - `ServerInfoMessage`: system events such as presence changes and group additions

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 10 |
| UI | JavaFX 10.0.1 (FXML + CSS) |
| Networking | `java.net.Socket` / `java.io.ObjectStream` |
| Persistence | Java object serialization |
| Build / IDE | IntelliJ IDEA |

No external libraries or frameworks are used beyond the JDK and JavaFX.

---

## Getting Started

### Prerequisites

- JDK 10 or later
- JavaFX 10 SDK (if not bundled with your JDK distribution)
- IntelliJ IDEA (recommended) or any IDE that supports `.iml` module files

### Run the server

Open `src/application/server/ServerMain.java` and run it. The server listens on `localhost:8085` and creates the `data/` directory on first launch to persist its database.

### Run the client

Open `src/application/client/ClientMain.java` and run it. By default the client connects to `localhost:8085`, so both processes need to run on the same machine (or you can update the host constant in `ClientMain.java`).

You can launch multiple client instances to test messaging between different accounts.

---

## License

This project is for personal/educational use. No license is currently specified.
