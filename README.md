# LeonardPauli/chess
*code experiments with the board game chess as subject*

- using rim
- using js
- using java

### notes

#### idea: to create a general/abstract "BoardGame" base class (extended by Chess); following the normalisation principle.

Furthermore, a square board should not be assumed, rather: *board games* usually have *pieces* which can be placed on *tiles*, and *moved* between them in certain ways. Different pieces might have different kinds of *movement types*. Therefore; define the base class __Tile__, which are linked together with *edges*. A *movement type* can then be performed on different paths of edges, depending on the tiles, their content, the *piece type*, and the *game state*, etc. By modeling the board as a graph of tiles and movements, less assumptions becomes entangled which allows for a greater variety of extentions and modifications of the resulting game.

For instance, if tile graph is dynamically generated based on the number of players, no futher change would be needed in order to make the chess board go from square (2p), to hexagonal (3p), to 10-sided (5p). All rules would still apply (if defined in terms of movements between nodes/tiles instead of between predefined coordinates.). How to nicely visualize a 10-sided board is a different problem, but the underlying engine would just as well support a 4D version if wanted.


---

Copyright © Leonard Pauli, sept 2018

