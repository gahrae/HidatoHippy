![hidato](/src/main/resources/github/images/hidato.jpg)

Solution to [a Code Golf challenge](http://codegolf.stackexchange.com/questions/54251/we-love-our-weird-puzzles-us-brits) using [Hipster](http://www.hipster4j.org/).

Hidato Hippy
============

"Hidato Hippy" is a solution to a challenge that was posted at [Code Golf](http://codegolf.stackexchange.com/). The solution uses [A Star Search](https://en.wikipedia.org/wiki/A*_search_algorithm) with the help of  [Hipster4j](http://www.hipster4j.org/) ~ an Open Source Heuristic Search Java Library. 

What was the Code Golf challenge?
---------------------------------

*As described by the challenge poster...* <br />
In a few British newspapers there is a game known as [Hidato](https://en.wikipedia.org/wiki/Hidato). It is somewhat similar to [Sudoku](https://en.wikipedia.org/wiki/Sudoku), albeit instead of matching into an unchanging format, it's about matching numbers in that one may connect all numbers **diagonally, vertically or horizontally** (i.e. they must be touching blocks). Here's today's version (blocks of XX are for display – they'll be spaces in input)

```
XX XX XX XX -- 53 XX XX XX XX
XX XX XX XX -- -- XX XX XX XX
XX XX 56 -- -- -- 30 -- XX XX
XX XX -- -- -- -- -- -- XX XX
XX -- -- 20 22 -- -- -- -- XX
XX 13 -- 23 47 -- 41 -- 34 XX
-- -- 11 18 -- -- -- 42 35 37
-- -- -- -- 05 03 01 -- -- --
XX XX XX XX -- -- XX XX XX XX
XX XX XX XX 07 -- XX XX XX XX
```

Here is the solution, courtesy of [Vioz](http://codegolf.stackexchange.com/users/38417/vioz):

```
XX XX XX XX 52 53 XX XX XX XX
XX XX XX XX 54 51 XX XX XX XX
XX XX 56 55 28 50 30 31 XX XX
XX XX 26 27 21 29 49 32 XX XX
XX 25 24 20 22 48 45 44 33 XX
XX 13 19 23 47 46 41 43 34 XX
14 12 11 18 04 02 40 42 35 37
15 16 17 10 05 03 01 39 38 36
XX XX XX XX 09 06 XX XX XX XX
XX XX XX XX 07 08 XX XX XX XX
```

Your program will receive an input similar to this – blocks which are either numbers, spaces (blank, for shaping) or two dashes, indicating a space you can place a number. It should return a solved version, in which it is possible to go from 01 to the maximum value. If no solution can be found, return something [falsey](http://meta.codegolf.stackexchange.com/questions/2190/interpretation-of-truthy-falsey).

Note that there may not be a unique, or even the existence of, a solution – some may yield multiple due to their ambiguity, much like Sudoku, and some may be literally unsolvable. With that in mind, make sure your code takes only a minute or so at maximum to return a solution (brute force works, but careful logic may win out in this regard – remember, you need to determine if it is solvable in that minute).

Format your answers with the standard header (## Language name, X bytes). As always, the shortest code wins. I'll be testing each with some solvable, some mostly-sparse, and some intentionally unsolvable puzzles (all of which will be in different shapes and amounts of numbers), so don't hardcode any answers. Happy coding!


Running the solution
--------------------

#### Assembly to a Jar file
`mvn clean compile assembly:single`

#### Execute Jar file
```
cd target
java -jar HidatoHippy-0.0.1-SNAPSHOT-jar-with-dependencies.jar classes/sampleinput/SampleInput6.txt
```

Example output
--------------

Command
`java -jar HidatoHippy-0.0.1-SNAPSHOT-jar-with-dependencies.jar classes/sampleinput/SampleInput6.txt`

Output
```
Original...
-- -- -- -- 
12 --  1 -- 
-- 16 -- -- 
-- 14 --  6 

Finished...
Solution found!
11 10  2  3 
12  9  1  4 
13 16  8  5 
15 14  7  6 

Total solutions: 1
Total time: 90.49 ms
Total number of iterations: 398
+ Solution 1: 
 - States: 
	[(2,1)=1, (2,0)=2, (3,0)=3, (3,1)=4, (3,2)=5, (3,3)=6, (2,3)=7, (2,2)=8, (1,1)=9, (1,0)=10, (0,0)=11, (0,1)=12, (0,2)=13, (1,3)=14, (0,3)=15]
 - Actions: 
	[MOVE_UP, MOVE_RIGHT, MOVE_DOWN, MOVE_DOWN, MOVE_DOWN, MOVE_LEFT, MOVE_UP, MOVE_UP_LEFT, MOVE_UP, MOVE_LEFT, MOVE_DOWN, MOVE_DOWN, MOVE_DOWN_RIGHT, MOVE_LEFT]
 - Search information: 
	WeightedNode{state=(0,3)=15, cost=14.0, estimation=1.0, score=15.0}
```


What is this output?
====================

The output includes the challenge requirements to display the completed board.

It also displays `Additional details...` which is provided by Hipster to show which states were used, and actions taken.

Items in the list of states are of the form (x,y)=v. The `x` and `y` values are the coordinate location in the board, and `v` is the value at this spot. 
 

Performance
===========

Below are the run times for sample input files.

**SampleInput1.txt** : `Total time: 7.114 s`
```
XX XX XX XX -- 53 XX XX XX XX 
XX XX XX XX -- -- XX XX XX XX 
XX XX 56 -- -- -- 30 -- XX XX 
XX XX -- -- -- -- -- -- XX XX 
XX -- -- 20 22 -- -- -- -- XX 
XX 13 -- 23 47 -- 41 -- 34 XX 
-- -- 11 18 -- -- -- 42 35 37 
-- -- -- --  5  3  1 -- -- -- 
XX XX XX XX -- -- XX XX XX XX 
XX XX XX XX  7 -- XX XX XX XX 
```

**SampleInput2.txt** : `Total time: 138.8 ms`
```
-- 33 35 -- -- XX XX XX 
-- -- 24 22 -- XX XX XX 
-- -- -- 21 -- -- XX XX 
-- 26 -- 13 40 11 XX XX 
27 -- -- --  9 --  1 XX 
XX XX -- -- 18 -- -- XX 
XX XX XX XX --  7 -- -- 
XX XX XX XX XX XX  5 -- 
```

**SampleInput3_impossible.txt** : `Total time: 0 ms`
```
XX XX XX XX XX 
XX  1 XX -- XX 
XX XX XX XX XX 
```

**SampleInput4.txt** : `Total time: 860.1 ms`
```
30 -- 26 -- 16 -- -- 11 
-- -- -- 18 -- -- --  9 
-- 34 -- -- 20 -- 13 -- 
35 -- -- -- --  1 -- -- 
-- -- 40 -- 51 -- --  4 
63 64 -- -- -- 52 48 -- 
-- 59 56 55 -- -- -- -- 
61 -- -- -- -- -- 46 -- 
```

**SampleInput5.txt** : `Total time: 1.456 s`
```
-- -- -- -- 39 XX XX XX 
-- --  7 40 -- XX XX XX 
-- -- -- -- 11 -- XX XX 
--  4 -- -- -- 12 XX XX 
 3  1 -- 16 28 -- 26 XX 
XX XX -- -- -- -- -- XX 
XX XX XX XX -- 19 21 -- 
XX XX XX XX XX XX -- 23
``` 

**SampleInput6.txt** : `Total time: 89.34 ms`
```
-- -- -- -- 
12 --  1 -- 
-- 16 -- -- 
-- 14 --  6 
```

Challenge criteria
==================

The challenge poster requested that a runtime limit be put in place. I did not want to my application to prematurely exit before finding a solution. The algorithm will terminate eventually. And presumably boards will not be large enough to run for a significant duration.

Some detection of board states that cannot be solved was implemented. For example, when a board has duplicate or isolated numbers. But more could be done in this regard, especially with the following situation...

```
XX XX XX XX XX
XX 04 03 -- XX
XX -- 02 -- XX
XX -- 01 -- XX
XX XX XX XX XX
``` 

*The above problem checking every empty cell is touchable by the path node, eg the cell with the value `4` in it.* 


Lessons from the challenge
==========================

Where appropriate I used streams instead of for loops. So instead of iterating over a 2D array using row and columns variables, a Point is retrieved. Each point has an X and Y value property.
* It is not always appropriate to use a stream :S :-P
* Delaying use of terminal operations increased performance due to the amount of looping.
* Ordering which filter conditions are checked first by their "effort" increased performance.
* Care should be taken to check the heuristic is actually working.

References
==========
 
*Alternative Hidato solutions*
[http://rosettacode.org/wiki/Hidato](http://rosettacode.org/wiki/Hidato)

*Guide to writing Heuristics*
[http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html](http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html)

*Online "Hidoku" puzzle generator*
 [http://hidoku-solver.appspot.com](http://hidoku-solver.appspot.com)
 
*"Hipster" ~ An Open Source Java Library for Heuristic Search*
[http://www.hipster4j.org/](http://www.hipster4j.org/)
 

Future work
===========

This project was a first brush attempt at solving the problem. I welcome any suggestions for improvement or if you'd simply like to comment on the project. I don't plan on building on this example, but will incorporate any good suggestion.
