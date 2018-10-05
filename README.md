# LeonardPauli/chess [![Build Status](https://travis-ci.com/leonardpauli/lp-chess.svg?branch=master)](https://travis-ci.com/leonardpauli/lp-chess)
*code experiments with the board game chess as subject*

A Chess flavoured GameBoard implemented using edges of movements between tiles instead of a usual hardcoded 8x8 board. The idea is that this will allow for many variations of rules and board layouts with minimal code change (eg. chess with three kingdoms using a pentagon board).

Initially created as a project in the indatapluplus course at KTH.

See [screen capture](https://github.com/leonardpauli/lp-chess/blob/master/rim/screencapture-1oct2018.mp4?raw=true) of interaction (including game replay from PGN (Portable Game Notation) files).

![screenshot 6 oct 2018](https://github.com/leonardpauli/lp-chess/blob/master/rim/screenshot-6oct2018.png?raw=true)

## build system

- prerequisite: `JDK 10` + `maven` installed
- `git clone ... chess && cd $_`
- `mvn install`
- `mvn test`
- `mvn checkstyle:check`
- `mvn package`
- `java -jar target/*.jar`

## WIP

see `rim/Chess.Simple.rim` for current MVP goal, then the rest of the files in `rim/` for notes about the final goal.

`rim/Chess.Simple.rim` is being implemented in `src`; open the repo folder with intelliJ + see tests

### Process

- task was given
- wrote down declarative overview of game + code structure in rim, based on wikipedia etc; made separation between `BoardGame` and `ChessGame` (extending BoardGame)
- following the normalisation principle, got idea of using nodes + edges instead of matrix for board
- researched java language syntax, see `LeonardPauli/docs/app/java/`
- created a simplified rim version with BoardGame and ChessGame merged
- began implementing/translating the rim pseudo code to java (all classes in same file)
- splitted it up into multiple files
- shifted more and more to use edges instead of positions
- began implementing the REPL; using the ChessGame engine
- all code til this point was written in sublime text as plain text without any test compilations (~1000LOC)
- resolved syntax issues (pretty few!)
- resolved reference issues (gotta love intelliJ for this!)
- test the REPL interface (MVP is working!)

![16 sept 2018 MVP moves](rim/16sept2018-mvp-moves.png?raw=true "Proof of concept")

- finish implementation
- add some tests
- open project with intelliJ, run google linting
- run tests, from tiny to larger, while resolving logic issues
- abort for now because I absolutely don't have time for this
- get back, fix simple interface for handling simple 8x8 board
- work on tokenizer for different versions of chess notation
- tokenizer working
- create GUI using JavaFX (using paths instead of rects to support polygon board later on)
- implemented "load PGN file" + replay of game in GUI
- use build system + package manager
- CI: travis on PR (running maven test inc. check style)

then:

- fix adapter from ChessGame to interface
- fix GUI as separate package, with logic defered to the interface
- fix local network player mode using sockets and protobuf (?)
- implement discovery service + deploy using now
- implement optional proxying service (workaround for limited networks) + deploy using now
- implement adapter for indapluplus legacy json format/protocol + test with other engine
- implement gui adapter for someone else's engine
- add github issues + pr to others

then:

- implement UI for 3/multiple-kingdoms-chess + dimentional/teleport-chess + "Fischer Random Chess" combo + move-10s-max-dur
- CI: CLA check, PR + issue template

### notes

- using rim
- using js
- using java

#### idea: to create a general/abstract "BoardGame" base class (extended by Chess); following the normalisation principle.

Furthermore, a square board should not be assumed, rather: *board games* usually have *pieces* which can be placed on *tiles*, and *moved* between them in certain ways. Different pieces might have different kinds of *movement types*. Therefore; define the base class __Tile__, which are linked together with *edges*. A *movement type* can then be performed on different paths of edges, depending on the tiles, their content, the *piece type*, and the *game state*, etc. By modeling the board as a graph of tiles and movements, less assumptions becomes entangled which allows for a greater variety of extentions and modifications of the resulting game.

For instance, if tile graph is dynamically generated based on the number of players, no futher change would be needed in order to make the chess board go from square (2p), to hexagonal (3p), to 10-sided (5p). All rules would still apply (if defined in terms of movements between nodes/tiles instead of between predefined coordinates.). How to nicely visualize a 10-sided board is a different problem, but the underlying engine would just as well support a 4D version if wanted.


![three player chess board, twisted pentagon](rim/three-player-chess.png?raw=true "Three Player Chess")

---

Copyright © Leonard Pauli, sept 2018
