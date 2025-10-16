# Java-ChatApplication

Java Console Chat Application
Project Overview

This Java console chat application is designed to demonstrate a simple real-time messaging system using sockets and multithreading.

The application consists of a server and multiple clients:

Server:

Listens for incoming client connections on a specific port (5678).

Manages all connected clients using a Map<String, ClientHandler>.

Broadcasts messages from one client to all other clients.

Optionally stores message history in a List<String> so new clients could see previous messages.

Client:

Connects to the server using a Socket.

Sends messages typed in the console to the server.

Receives messages from the server and displays them in real-time.

Runs two separate threads: one for reading incoming messages, one for sending messages, ensuring smooth communication without blocking.

This project is designed to teach core networking concepts in Java, including:

TCP socket communication

Multithreading for handling multiple clients simultaneously

Data structures (Map for clients, List for messages)

Basic I/O with BufferedReader and BufferedWriter

It can serve as a foundation for more advanced chat applications with features such as:

Message encryption/decryption

Chat rooms

GUI interface

Message history persistence

The goal is to provide a minimal, working example of a multi-client chat application that can be extended as needed.

Project Structure
ChatApp/           <- Eclipse project root
│
├─ src/
│   ├─ chatserver/
│   │   ├─ ChatServer.java
│   │   └─ ClientHandler.java
│   │
│   ├─ chatclient/
│   │   └─ ChatClient.java
│   │
│   └─ commonchat/
│       └─ EncryptionUtil.java   (optional utility class)
│
└─ README.txt

Packages & Classes
1. chatserver

ChatServer.java – Starts the server, listens for client connections, maintains active clients, and broadcasts messages.

ClientHandler.java – Handles communication with each client, reads messages, and sends messages.

2. chatclient

ChatClient.java – Connects to the server, sends messages, and receives messages from the server.

3. commonchat (optional)

EncryptionUtil.java – Utility class for encrypting/decrypting messages (placeholder).

How to Run
Step 1: Start the Server

Open Eclipse and import the project.

Run ChatServer.java.

Server starts on port 5678.

Console displays: Chat Server Started on port 5678.

Step 2: Start Clients

Run ChatClient.java for each client.

Enter a username when prompted.

Start typing messages.

Each client can send messages simultaneously, and all messages are broadcasted to all connected clients.

Features

Multi-client support using threads. Each client runs independently.

Real-time messaging between multiple clients.

Server maintains a list of connected clients and updates on join/leave.

Optional message history stored in a List<String> to track all chat messages.

Console-based interface for simplicity and easy testing.

Thread-safe communication using BufferedReader and BufferedWriter.

Optional encryption/decryption support using EncryptionUtil (can be implemented later).

Easy to extend with features like chat rooms, GUI interface, emojis, or file sharing.

Optional Enhancements

Encrypt/decrypt messages using EncryptionUtil.

Display previous message history when a new client joins.

Add emoji or formatting support for messages.

Notes

Ensure port 5678 is free before starting the server.

Close clients properly to remove them from the active client list.

Acknowledgement

Thank you for reviewing this project! Your feedback and suggestions are greatly appreciated. 🙏
