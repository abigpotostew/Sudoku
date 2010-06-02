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

        //create both arrays
        int[][] Grid = new int[10][10];
        int[][][] possible = new int[10][10][10];

        //Convert file to Grid with getGrid()
        Grid = getGrid( Grid, sc );
        
        //print if it's filled or not, with proper message
        if (isFilled( Grid )){
            System.out.println("");
            printGrid( Grid );
            System.out.println("");
            System.out.println("This puzzle is complete.");
        } else {
              System.out.println("");
              printGrid( Grid );
              System.out.println("");
              System.out.println("This puzzle is not filled. Attempting to solve...");
        }
        
        initialPossible (Grid, possible);
        
        while ( !isFilled( Grid ) ){
            updatePossible(Grid, possible);
            updateGrid(Grid, possible);
        }
        
        /*
        System.out.println("");
        printPossible( possible );
        System.out.println("");
        System.out.println("(1) 1=" + possible[1][1][1] + " 2=" + possible[1][1][2] + " 3=" + possible[1][1][3] + " 4=" + possible[1][1][4] + " 5=" + possible[1][1][5] + " 6=" + possible[1][1][6] + " 7=" + possible[1][1][7] + " 8=" + possible[1][1][8] + " 9=" + possible[1][1][9]);
        System.out.println("(2) 1=" + possible[1][2][1] + " 2=" + possible[1][2][2] + " 3=" + possible[1][2][3] + " 4=" + possible[1][2][4] + " 5=" + possible[1][2][5] + " 6=" + possible[1][2][6] + " 7=" + possible[1][2][7] + " 8=" + possible[1][2][8] + " 9=" + possible[1][2][9]);
        System.out.println("(3) 1=" + possible[1][3][1] + " 2=" + possible[1][3][2] + " 3=" + possible[1][3][3] + " 4=" + possible[1][3][4] + " 5=" + possible[1][3][5] + " 6=" + possible[1][3][6] + " 7=" + possible[1][3][7] + " 8=" + possible[1][3][8] + " 9=" + possible[1][3][9]);
        System.out.println("(4) 1=" + possible[1][4][1] + " 2=" + possible[1][4][2] + " 3=" + possible[1][4][3] + " 4=" + possible[1][4][4] + " 5=" + possible[1][4][5] + " 6=" + possible[1][4][6] + " 7=" + possible[1][4][7] + " 8=" + possible[1][4][8] + " 9=" + possible[1][4][9]);
        System.out.println("(6) 1=" + possible[1][6][1] + " 2=" + possible[1][6][2] + " 3=" + possible[1][6][3] + " 4=" + possible[1][6][4] + " 5=" + possible[1][6][5] + " 6=" + possible[1][6][6] + " 7=" + possible[1][6][7] + " 8=" + possible[1][6][8] + " 9=" + possible[1][6][9]);

        updateGrid(Grid, possible);
        System.out.println("");
        printPossible( possible );
        System.out.println("(^POSSIBLE)");
        printGrid( Grid );
        System.out.println("(^GRID)");

        */
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
    
    //Use this to check how many candidates are in possible in each index 0 [i][j][0] box.
    static void printPossible (int[][][] P){
        for(int i=1; i<P.length; i++){
            for (int j=1; j<P[i].length; j++){
                if (P[i][j][0]==0){
                    System.out.print("- ");
                } else System.out.print(P[i][j][0]+" ");
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
        //only makes changes to possibility array
        
        int k;
        
        for(int i=1; i<G.length; i++){
            for (int j=1; j<G[i].length; j++){
                if (G[i][j]>0) {
                    int m = G[i][j];    //this is the candidate # being removed from P[][][]
                    
                    //remove m as candidate, from P[i][j][m] for rows
                    for(int ii=1; ii<G.length; ii++){
                        if (ii!=i){  //skip the current box
                            if (P[ii][j][0]>1 && P[ii][j][m]>0){ //only change candidate values if it's not filled
                                P[ii][j][m] = 0;    //set candidate value to 0
                                P[ii][j][0] -= 1;   //subtract 1 from total candidates for this cell
                            }
                        }
                    }
                    
                    //remove m from P[i][j][m] for columns
                    for(int jj=1; jj<G.length; jj++){
                        if (jj!=j){  //skip the current box
                            if (P[i][jj][0]>1 && P[i][jj][m]>0){ //only change candidate values if it's not filled
                                P[i][jj][m] = 0;    //set candidate value to 0
                                P[i][jj][0] -= 1;   //subtract 1 from total candidates for this cell
                            }
                        }
                    }
                    
                    //remove m from P[i][j][m] for box!
                    int I = getAnchor(i);
                    int J = getAnchor(j);
                    for(int s=I; s<=I+2; s++){
                        for(int t=J; t<=J+2; t++){
                            if((s!=i)||(t!=j)){
                                if (P[s][t][0]>1 && P[s][t][m]>0){ //only change candidate values if it's not filled
                                    P[s][t][m] = 0;    //set candidate value to 0
                                    P[s][t][0] -= 1;   //subtract 1 from total candidates for this cell
                                }
                            }
                        }
                    }
                    
                }
            }
        }
    }

    //returns single int for the anchor the current row or column
    static int getAnchor(int a){
        return a-((a-1)%3);
    }

    static void initialPossible(int[][] G, int[][][] P){
        for(int i=1; i<G.length; i++){
            for (int j=1; j<G[i].length; j++){  //read through Grid
                if (G[i][j] == 0){  //if it's unfilled
                    P[i][j][0]=9;   //set possible candidates to 9
                    for(int k=1; k<=9; k++){
                        //if (!k==G[i][j])
                            P[i][j][k]=1;   //set index of all candidates to 1
                    }
                } else {
                    P[i][j][0] = 1;
                    int m = G[i][j];
                    for(int k=1; k<=9; k++){
                            P[i][j][k]=0;   //set index of all candidates to 1
                    }
                    P[i][j][m] = 1;
                    /*for(int k=1; k<=9; k++){
                        if (k==m){}else
                            P[i][j][k]=1;   //set index of all candidates except
                                            //the index of filled # to 0. This cell is dunzo
                    }*/
                }
            }
        }
    }

    static void updateGrid(int[][] G, int[][][] P){
        //only makes changes to grid array
        
        //this method checks possibility array for any index 0 slots for value or 1
        //which means only one remaining candidate, and fills in Grid with proper number
        
        
        for(int i=1; i<G.length; i++){
            for (int j=1; j<G[i].length; j++){
                if (P[i][j][0]==1){ //if there's only one candidate left in P[i][j][0]
                    if (G[i][j]==0){    //double check it's not filled already
                        for (int k=1; k<G[i].length; k++){  //enter loop to check the THIRD DIMENSION
                            if (P[i][j][k]==1){     //grab the index number of the last candidate
                                G[i][j] = k;        //SMACK
                                }
                            }
                    }
                }
                
            }
            
        }
        
        
    }

}