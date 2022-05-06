# Piston-game
A Minecraft-themed minigame where you win the game by using pistons to move a block to its destination.
This game was made with Java Swing.

Normal pistons push blocks directly in front of them. Sticky pistons can both push and pull blocks.

## Levels
Example level:

10 10 10 10 10 10<br/>
10 21 40 00 33 10<br/>
10 10 10 12 10 10

What it looks like in-game:

![In-game screenshot of the level above](https://i.imgur.com/GDh58Kg.png)

Every pair of 2 digits represents a tile, the first digit determines the tile type and the second digit its rotation.

Tile types:

0: background tile,
1: foreground tile,
2: piston,
3: sticky piston,
4: movable tile

Rotations:

0: up,
1: right,
2: down,
3: left

## To do
- Improve game loop
