# Kalah Game
Java RESTful Web Service implementation of 6-stone Kalah Game.

This web service enables 2 human players to play the game, each in his own computer.

## Prerequisites

[Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
and
[Maven](https://maven.apache.org/install.html) build tool.

## Installation

Unpack zip application archive into the current directory:
```bash
unzip kalah.zip
```

After that change the directory to kalah:
```bash
cd kalah
````

## Usage

* Run tests
```bash
mvn test
```

* Run web application:

```bash
mvn spring-boot:run
```

by default application runs on localhost port 8080

## Endpoint design specification

* Creation of the game should be possible with the command:
```bash
curl --header "Content-Type: application/json" --request POST http://<host>:<port>/games
```

Response:

*HTTP code: 201*

*Response Body:* { "id": "1234", "url": "http://<host>:<port>/games/1234" }

**id:** unique identifier of a game

**url:** link to the game created
* Make a move:
```bash
curl --header "Content-Type: application/json" --request PUT http://<host>:<port>/games/{gameId}/pits/{pitId}
```
**gameId:** unique identifier of a game

**pitId:** id of the pit selected to make a move.
*Pits are numbered from 1 to 14 where 7 and 14 are the kalah (or house)
of each player*

Response:

*HTTP code:* 200

*Response Body:*
"id":"1234","url":"http://<host>:<port>/games/1234",
"status":{ "1":"4","2":"4","3":"4","4":"4","5":"4","6":"4","7":"0","8":"4","9":"4","10":"4","11":"4","12":"4","13":"4","14":"0" } }

**status:** json object key-value, where key is the **pitId** and value is the **number of stones in the pit**

## License
[APACHE LICENSE, VERSION 2.0](http://www.apache.org/licenses/LICENSE-2.0)