package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        StdIn.setFile(file);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        grid = new boolean[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                grid[i][j] = StdIn.readBoolean();
            }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i == row && j == col) {
                    if (grid[i][j])
                        return true;
                    else
                        return false;
                }
            }
        }
        return false; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j])
                    count++;
            }
        }
        if (count > 0)
            return true;
        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {
        int count = 0;
        // horizontal neighbors
        if (col == grid[0].length - 1) {
            if (grid[row][col-1])
                count++;
            if (grid[row][0])
                count++;
        } else if (col == 0) {
            if (grid[row][col+1])
                count++;
            if (grid[row][grid[0].length-1])
                count++;
        } else {
            if (grid[row][col+1])
                count++;
            if (grid[row][col-1])
                count++;
        }
        // vertical neighbors
        if (row == grid.length-1) {
            if (grid[0][col])
                count++;
            if (grid[row-1][col])
                count++;
        } else if (row == 0) {
            if (grid[grid.length-1][col])
                count++;
            if (grid[row+1][col])
                count++;
        } else {
            if (grid[row-1][col])
                count++;
            if (grid[row+1][col])
                count++;
        }
        // diagonal neighbors
        int rowLen = grid.length;
        int colLen = grid[0].length;
        int r = 0;
        int c = 0;
        boolean[][] biggerGrid = new boolean[rowLen*3][colLen*3];
        for (int i = 0; i < biggerGrid.length; i++) {
            for (int j = 0; j < biggerGrid[0].length; j++) {
                if (r == rowLen-1 && c == colLen-1 && j == biggerGrid[0].length-1) {
                    biggerGrid[i][j] = grid[r][c];
                    r = 0;
                    c = 0;
                } else if (j == biggerGrid[0].length-1){
                    biggerGrid[i][j] = grid[r][c];
                    r++;
                    c = 0;
                } else if (c == colLen-1) {
                    biggerGrid[i][j] = grid[r][c];
                    c = 0;
                } else {
                    biggerGrid[i][j] = grid[r][c];
                    c++;
                }
            }
        }

        int adjRow = rowLen + row;
        int adjCol = colLen + col;

        if (biggerGrid[adjRow + 1][adjCol + 1])
            count++;
        if (biggerGrid[adjRow + 1][adjCol - 1])
            count++;
        if (biggerGrid[adjRow - 1][adjCol + 1])
            count++;
        if (biggerGrid[adjRow - 1][adjCol - 1])
            count++;

        return count; 
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (numOfAliveNeighbors(i, j) < 1)
                    newGrid[i][j] = false;
                else if (!grid[i][j] && numOfAliveNeighbors(i, j) == 3)
                    newGrid[i][j] = true;
                else if (grid[i][j] && (numOfAliveNeighbors(i, j) == 3 || numOfAliveNeighbors(i, j) == 2))
                    newGrid[i][j] = true;
                else if (grid[i][j] && numOfAliveNeighbors(i, j) >= 4)
                    newGrid[i][j] = false;
            }
        }
        return newGrid;
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
        boolean[][] updateGrid = computeNewGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = updateGrid[i][j];
            }
        }
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {
        for (int i = 0; i < n; i++) {
            nextGeneration();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */

    public int numOfCommunities() {
        WeightedQuickUnionUF unionObject = new WeightedQuickUnionUF(grid.length, grid[0].length);
        ArrayList<Integer> deadRows = new ArrayList<Integer>();
        ArrayList<Integer> aliveRows = new ArrayList<Integer>();
        ArrayList<Integer> deadCols = new ArrayList<Integer>();
        ArrayList<Integer> aliveCols = new ArrayList<Integer>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!grid[i][j]) {
                    deadRows.add(i);
                    deadCols.add(j);
                }
                else {
                    aliveRows.add(i);
                    aliveCols.add(j);
                }
            }
        }

        // unionize dead cells
        for (int i = 0; i < deadRows.size(); i++) {
            if (i == deadRows.size() - 1)
                break;
            else {
                unionObject.union(deadRows.get(i), deadCols.get(i), deadRows.get(i+1), deadCols.get(i+1));
            }
        }

        // remove alone alive cells
        for (int i = 0; i < aliveRows.size(); i++) {
            if (numOfAliveNeighbors(aliveRows.get(i), aliveRows.get(i)) == 0) {
                aliveRows.remove(i);
                aliveCols.remove(i);
            }
        }

        // union alive neighbors
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j]) {
                    if (i > 0 && i < grid.length-1 && j > 0 && j < grid[0].length-1) {
                        if (grid[i][j+1])
                            unionObject.union(i, j, i, j+1);
                        if (grid[i][j-1])
                            unionObject.union(i, j, i, j-1);
                        if (grid[i+1][j])
                            unionObject.union(i, j, i+1, j);
                        if (grid[i-1][j])
                            unionObject.union(i, j, i-1, j);
                        if (grid[i+1][j+1])
                            unionObject.union(i, j, i+1, j+1);
                        if (grid[i+1][j-1])
                            unionObject.union(i, j, i+1, j-1);
                        if (grid[i-1][j+1])
                            unionObject.union(i, j, i-1, j+1);
                        if (grid[i-1][j-1])
                            unionObject.union(i, j, i-1, j-1);
                    } else if (i == 0 && j > 0 && j < grid[0].length-1) {
                        if (grid[i][j+1])
                            unionObject.union(i, j, i, j+1);
                        if (grid[i][j-1])
                            unionObject.union(i, j, i, j-1);
                        if (grid[i+1][j])
                            unionObject.union(i, j, i+1, j);
                        if (grid[grid.length-1][j])
                            unionObject.union(i, j, grid.length-1, j);
                        if (grid[i+1][j+1])
                            unionObject.union(i, j, i+1, j+1);
                        if (grid[i+1][j-1])
                            unionObject.union(i, j, i+1, j-1);
                        if (grid[grid.length-1][j+1])
                            unionObject.union(i, j, grid.length-1, j+1);
                        if (grid[grid.length-1][j-1]) 
                            unionObject.union(i, j, grid.length-1, j-1);
                    } else if (i == grid.length-1 && j > 0 && j < grid[0].length-1) {
                        if (grid[i][j+1])
                            unionObject.union(i, j, i, j+1);
                        if (grid[i][j-1])
                            unionObject.union(i, j, i, j-1);
                        if (grid[i-1][j])
                            unionObject.union(i, j, i-1, j);
                        if (grid[0][j])
                            unionObject.union(i, j, 0, j);
                        if (grid[i-1][j+1])
                            unionObject.union(i, j, i-1, j+1);
                        if (grid[i-1][j-1])
                            unionObject.union(i, j, i-1, j-1);
                        if (grid[0][j+1])
                            unionObject.union(i, j, 0, j+1);
                        if (grid[0][j-1])
                            unionObject.union(i, j, 0, j-1);
                    } else if (j == 0 && i > 0 && i < grid.length-1) {
                        if (grid[i][j+1])
                            unionObject.union(i, j, i, j+1);
                        if (grid[i][grid[0].length-1])
                            unionObject.union(i, j, i, grid[0].length-1);
                        if (grid[i-1][j])
                            unionObject.union(i, j, i-1, j);
                        if (grid[i+1][j])
                            unionObject.union(i, j, i+1, j);
                        if (grid[i-1][j+1])
                            unionObject.union(i, j, i-1, j+1);
                        if (grid[i+1][j+1])
                            unionObject.union(i, j, i+1, j+1);
                        if (grid[i-1][grid[0].length-1])
                            unionObject.union(i, j, i-1, grid[0].length-1);
                        if (grid[i+1][grid[0].length-1])
                            unionObject.union(i, j, i+1, grid[0].length-1);
                    } else if (j == grid.length-1 && i > 0 && i < grid.length-1) {
                        if (grid[i][0])
                            unionObject.union(i, j, i, 0);
                        if (grid[i][j-1])
                            unionObject.union(i, j, i, j-1);
                        if (grid[i-1][j])
                            unionObject.union(i, j, i-1, j);
                        if (grid[i+1][j])
                            unionObject.union(i, j, i+1, j);
                        if (grid[i-1][0])
                            unionObject.union(i, j, i-1, 0);
                        if (grid[i+1][0])
                            unionObject.union(i, j, i+1, 0);
                        if (grid[i-1][j-1])
                            unionObject.union(i, j, i-1, j-1);
                        if (grid[i+1][j-1])
                            unionObject.union(i, j, i+1, j-1);
                    } else if (i == 0 && j == 0) {
                        if (grid[i][j+1])
                            unionObject.union(i, j, i, j+1);
                        if (grid[i+1][j+1])
                            unionObject.union(i, j, i+1, j+1);
                        if (grid[i+1][j])
                            unionObject.union(i, j, i+1, j);
                        if (grid[grid.length-1][j])
                            unionObject.union(i, j, grid.length-1, j);
                        if (grid[grid.length-1][j+1])
                            unionObject.union(i, j, grid.length-1, j+1);
                        if (grid[grid.length-1][grid[0].length-1])
                            unionObject.union(i, j, grid.length-1, grid[0].length-1);
                        if (grid[i][grid[0].length-1])
                            unionObject.union(i, j, i, grid[0].length-1);
                        if (grid[i+1][grid[0].length-1])
                            unionObject.union(i, j, i+1, grid[0].length-1);
                    } else if (i == 0 && j == grid[0].length-1) {
                        if (grid[i][j-1])
                            unionObject.union(i, j, i, j-1);
                        if (grid[i+1][j-1])
                            unionObject.union(i, j, i+1, j-1);
                        if (grid[grid.length-1][j-1])
                            unionObject.union(i, j, grid.length-1, j-1);
                        if (grid[grid.length-1][j])
                            unionObject.union(i, j, grid.length-1, j);
                        if (grid[grid.length-1][0])
                            unionObject.union(i, j, grid.length-1, 0);
                        if (grid[i][0])
                            unionObject.union(i, j, i, 0);
                        if (grid[i+1][0])
                            unionObject.union(i, j, i+1, 0);
                        if (grid[i+1][j])
                            unionObject.union(i, j, i+1, j);
                    } else if (i == grid.length-1 && j == grid[0].length-1) {
                        if (grid[i-1][j])
                            unionObject.union(i, j, i-1, j);
                        if (grid[0][j])
                            unionObject.union(i, j, 0, j);
                        if (grid[i][j-1])
                            unionObject.union(i, j, i, j-1);
                        if (grid[i][0])
                            unionObject.union(i, j, i, 0);
                        if (grid[i-1][j-1])
                            unionObject.union(i, j, i-1, j-1);
                        if (grid[0][j-1])
                            unionObject.union(i, j, 0, j-1);
                        if (grid[i-1][0])
                            unionObject.union(i, j, i-1, 0);
                        if (grid[0][0])
                            unionObject.union(i, j, 0, 0);
                    } else if (i == grid.length-1 && j == 0) {
                        if (grid[i-1][j])
                            unionObject.union(i, j, i-1, j);
                        if (grid[0][j])
                            unionObject.union(i, j, 0, j);
                        if (grid[i][j+1])
                            unionObject.union(i, j, i, j+1);
                        if (grid[i][grid[0].length-1])
                            unionObject.union(i, j, i, grid[0].length-1);
                        if (grid[i-1][j+1])
                            unionObject.union(i, j, i-1, j+1);
                        if (grid[0][j+1])
                            unionObject.union(i, j, 0, j+1);
                        if (grid[0][grid[0].length-1])
                            unionObject.union(i, j, 0, grid[0].length-1);
                        if (grid[i-1][grid[0].length-1])
                            unionObject.union(i, j, i-1, grid[0].length-1);
                    }
                }
            }
        }

        // counting unique roots
        ArrayList<Integer> uniqueElements = new ArrayList<Integer>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (uniqueElements.contains(unionObject.find(i, j)))
                    continue;
                else
                    uniqueElements.add(unionObject.find(i, j));
            }
        }
        return uniqueElements.size()-1; // return ^ arraylist size - 1 (for the id of the dead ones)
    }

}