## Othello - with an AI! 

An Othello game with an AI that searches the game tree via a mini-max search with alpha/beta pruning.

### Requirements

* A terminal with 
  - Unicode support
  - xterm-256-color code support

* A font with Unicode characters (e.g. DejaVu Sans Mono)

In a supported terminal the game will appear as below:

At startup the program will prompt the user asking whether they would like to
start a new game or load a partially completed game. An [example
file](./instances) is included. The first 8 lines correspond to the board
positions, the 9th identifies the next player to move, and the final line is
the time limit for computer players.

### Details on the AI

#### Search algorithm 

The search algorithm is a straightforward mini-max search with alpha/beta
pruning and iterative deepening - the only extra twist is the implementation
of the so called "killer-heuristic". After each depth-limited search the
optimal path down the tree for that search is saved and is used as the search
path for the next depth limited search. This process can cause early
alpha/beta cutoffs (hence the "killer" in the name) allowing greater depths to
be reached in a time limited iterative deepening search.

Near the end of the game, when it has enough time, the AI will search the
remainder of the game tree down to the terminal states rather than use its
heuristic function which estimates its chances at winning.

The primary factor in determining how many nodes the search can visit within a
time limit is the performance of the function that computes the valid moves.
As such I spent a considerable amount of time designing the algorithm to have
minimum complexity. The algorithm starts by visiting the discs of the current
player, then visits all discs in the outwards directions keeping track of
potential candidate discs to be flipped in a doubly linked list (chosen for
constant time concatenation), as it continues in the outwards direction, if it
finds a valid move location, it inserts this location and the discs to be
flipped into a hash table (chosen for constant time insertion and lookup).
From here it moves to another of the player's discs, and continues outward
from there - if it revisits the potential move location with additional discs
to be flipped it looks up the candidate discs to be flipped in the hash table and
concatenates its candidates onto the list.

Aside from carefully designing the algorithm I also took care to optimize the
implementation by studying it with Valgrind. In Valgrind I noticed I could
cut down on implicit calls to `new` and `del` and vastly increase the amount
of memory reuse to speed up the implementation.
