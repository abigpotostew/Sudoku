 /**************************************************************************
  * Sudoku.java
  * Stewart Bracken
  * bbracken
  * pa6
  * This program solves easy-mode sudoku puzzizzles. Have fun cheating!
  **************************************************************************/
  
import java.util.*;
import java.io.*;

class Sudoku{
    
    public static void main(String[] args){
        
        
        int m, n=0, i, l=1;
        Scanner sc = null;

        //Catch errors in input
        if (args.length>1) Usage();
        try {
            sc = new Scanner(new File(args[0]));
        } catch ( FileNotFoundException e ) {
            System.err.println(e.getMessage());
            Usage();
        } catch ( ArrayIndexOutOfBoundsException e ) {
            Usage();
        } catch ( NoSuchElementException e ) { 
            Usage();
        }

        int[][] Grid = new int[10][10];
        int[][][] possible = new int[10][10][10];

        //Convert file to Grid
        Grid = getGrid( Grid, sc );
        
        //Print unfilled puzzle
        if (isFilled( Grid )){
            System.out.println("");
            printGrid( Grid );
            System.out.println("");
            System.out.println("This puzzle is complete.");
        } else {
              System.out.println("");
              printGrid( Grid );
              System.out.println("");
              System.out.println("This puzzle is not filled. Solving...");
        }
        
        //Solve this damn sudoku!!!
        initialPossible (Grid, possible);
        while ( !isFilled( Grid ) ){
            updatePossible(Grid, possible);
            updateGrid(Grid, possible);
        }
        
        //Print solved puzzle
        System.out.println("");
        System.out.println("Here's your solved Sudoku:");
        System.out.println("");
        printGrid( Grid );
        System.out.println("");
        System.out.println("Next time, do it yourself.");
        System.out.println("");
        
        
    }
    
    //Usage()
    static void Usage(){
        System.err.println("Usage: java SudokuIO [InputFile]");
        System.exit(1);
    }
    
    //getGrid() will extract integers from input file and put them in the array
    static int[][] getGrid (int[][] G, Scanner sc){
        for(int i = 1; i < G.length; i++){
            for(int j = 1; j<G[i].length; j++){
                if (j==10) break;
                if(sc.hasNextInt()){
                    G[i][j] = sc.nextInt();
                }
            }
        }
        return G;
    }

    //printGrid() prints grid in a 9x9 grid with proper spacing. Zeroes print as dashes.
    static void printGrid (int[][] G){
        for(int i=1; i<G.length; i++){
            for (int j=1; j<G[i].length; j++){
                if (G[i][j]==0){
                    System.out.print("- ");
                } else System.out.print(G[i][j]+" ");
                if ( j==3) System.out.print("  ");
                if ( j==6) System.out.print("  ");
                if ( j==9) System.out.print("\n");
            }
            if ( i==3) System.out.print("\n");
            if ( i==6) System.out.print("\n");
        }
    }
    
    //isFilled() returns true if all entries are non-zero, false if it contains any 0's.
    static boolean isFilled (int[][] G){
        for(int i=1; i<G.length; i++){
            for (int j=1; j<G[i].length; j++){
                if (G[i][j]==0) {
                    return false;
                }
            }
        }
        return true;
    }

    //Removes candidates from boxes, rows, and columns; only updates possibility[][][]
    static void updatePossible(int[][] G, int[][][] P){
        for(int i=1; i<G.length; i++){
            for (int j=1; j<G[i].length; j++){
                if (G[i][j]>0) {
                    int m = G[i][j];
                    
                    //Remove m as candidate for row
                    for(int ii=1; ii<G.length; ii++){
                        if (ii!=i){
                            if (P[ii][j][0]>1 && P[ii][j][m]>0){
                                P[ii][j][m] = 0;
                                P[ii][j][0] -= 1;
                            }
                        }
                    }
                    //Remove m as candidate for column
                    for(int jj=1; jj<G.length; jj++){
                        if (jj!=j){
                            if (P[i][jj][0]>1 && P[i][jj][m]>0){
                                P[i][jj][m] = 0;
                                P[i][jj][0] -= 1;
                            }
                        }
                    }
                    //Remove m as candidate for box
                    int I = getAnchor(i);
                    int J = getAnchor(j);
                    for(int s=I; s<=I+2; s++){
                        for(int t=J; t<=J+2; t++){
                            if((s!=i)||(t!=j)){
                                if (P[s][t][0]>1 && P[s][t][m]>0){
                                    P[s][t][m] = 0;
                                    P[s][t][0] -= 1;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Returns int (either 1, 4, or 7) corresponding to the anchor of the current row or column
    static int getAnchor(int a){
        return a-((a-1)%3);
    }

    //Initial setup for possible[][][] array. Inputs 9 possible candidates to all unfilled cells, and 1 for filled cells.
    static void initialPossible(int[][] G, int[][][] P){
        for(int i=1; i<G.length; i++){
            for (int j=1; j<G[i].length; j++){
                if (G[i][j] == 0){
                    P[i][j][0]=9;
                    for(int k=1; k<=9; k++){
                            P[i][j][k]=1;
                    }
                } else {
                    P[i][j][0] = 1;
                    int m = G[i][j];
                    for(int k=1; k<=9; k++){
                            P[i][j][k]=0;
                    }
                    P[i][j][m] = 1;
                }
            }
        }
    }
    
    //Scans posibility[][][] for unfilled cells with 1 candidate left, and then updates Grid[][] with the candidate.
    static void updateGrid(int[][] G, int[][][] P){
        for(int i=1; i<G.length; i++){
            for (int j=1; j<G[i].length; j++){
                if (P[i][j][0]==1){
                    if (G[i][j]==0){
                        for (int k=1; k<G[i].length; k++){  //enter loop to check the THIRD DIMENSION!
                            if (P[i][j][k]==1){
                                G[i][j] = k;
                            }
                        }
                    }
                }
            }
        }
    }
}