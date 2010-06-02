#####################################################################
#
#   Makefile for Sudoku program
#
#####################################################################

Sudoku: Sudoku.class
	echo Main-class: Sudoku > Manifest
	jar cvfm Sudoku Manifest Sudoku.class
	rm Manifest
	chmod u+x Sudoku

Sudoku.class: Sudoku.java 
	javac -Xlint Sudoku.java

clean:
	rm -f Sudoku.class

spotless: clean
	rm -f Sudoku Sudoku.class
