# Kalah Game
Java RESTful Web Service implementation of 6-stone [Kalah](https://en.wikipedia.org/wiki/Kalah) Game.

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

* Build project:
```bash
mvn clean install
```

* Run web application:

```bash
mvn spring-boot:run
```

by default application runs on localhost port 8080

## Kalah Rules
Each of the two players has **six pits** in front of him/her. To the right of the six pits, each player has a larger pit, his Kalah or house.

At the start of the game, six stones are put in each pit.


The player who begins picks up all the stones in any of their own pits, and sows the stones on to the right, one in each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah. If the players last stone lands in his own Kalah, he gets another turn. This can be repeated any number of times before it's the other player's turn.


When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the other players' pit) and puts them in his own Kalah.


The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps them and puts them in his/hers Kalah. The winner of the game is the player who has the most stones in his Kalah.

A detailed description and rules can be found here: https://en.wikipedia.org/wiki/Kalah

## Endpoint design specification

* Creation of the game should be possible with the command:
```bash
curl --header "Content-Type: application/json" --request POST http://<host>:<port>/games
```

Response:

*HTTP code: 201*

*Response Body:* { "id": "1234", "uri": "http://<host>:<port>/games/1234" }

**id:** unique identifier of a game

**uri:** link to the game created
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
"id":"1234","uri":"http://<host>:<port>/games/1234",
"status":{ "1":"4","2":"4","3":"4","4":"4","5":"4","6":"4","7":"0","8":"4","9":"4","10":"4","11":"4","12":"4","13":"4","14":"0" } }

**status:** json object key-value, where key is the **pitId** and value is the **number of stones in the pit**

## License
[APACHE LICENSE, VERSION 2.0](http://www.apache.org/licenses/LICENSE-2.0)

![Code Quality Score](https://www.code-inspector.com/project/27973/score/svg)
![Code Grade](https://www.code-inspector.com/project/27973/status/svg)
