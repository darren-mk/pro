# arena-challenge-clj
Written by Darren Kim for the coding challenge given by Owein on 3/2/2021.

## how to run
In the top directory of the project, please type in terminal:
$ lein run 

## input
The given input file provided with the question is located in "resources" folder in the project directory.

## output
The output is generated into "output" folder in the name of "result.csv".

## file structure
### core.clj
Contains all static information including names of source-file/result-file and threshold number on the repetition condition.
It triggers the pure functions and generates side effects.
### function.clj
Contains all pure functions used to solve the problem.
Consists of five granular functions and one combining function.
### test.clj
Tests all granular functions.
For review to be easy, it doens't follow unit testing convention but is written simple and casual. 
