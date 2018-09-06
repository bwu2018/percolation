import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/******************************************************************************
 *  Name:    Brandon Wu
 *  Login:   bwu2018
 *  Precept: P01
 *
 *  Partner Name:    N/A
 *  Partner Login:   N/A
 *  Partner Precept: N/A
 * 
 *  Compilation:  javac-algs4 Percolation.java
 *  Execution:    java-algs4 Percolation
 *  Dependencies: StdIn.java StdRandom.java WeightedQuickUnionUF.java
 *
 *  Description:  Modeling Percolation like a boss. woot. woot.
 ******************************************************************************/
public class Percolation {

    private static final int VTOP = 0;
    private boolean[][] open;
    private int numOpen;
    private final int size; 
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF full;
    private final int vBot;

    /**
     * create n-by-n grid, with all sites blocked
     * @param n
     *        size of grid
     */
    public Percolation(int n)  {
        if (n <= 0) { 
            throw new IllegalArgumentException();
        }
        // size must be n+1 because starts at 1 instead of 0
        size = n;
        uf = new WeightedQuickUnionUF(size * size + 2);
        full = new WeightedQuickUnionUF(size * size + 1);
        open = new boolean[size][size];
        vBot = size * size + 1;
    }
    
    private void inBound(int row, int col) { 
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException();
        }
    }

    private int oneDindex(int row, int col) {
        inBound(row, col);
        return size * (row-1) + col;
    }
    
    /**
     * open site (row, col) if it is not open already
     * @param row
     *        row of grid
     * @param col
     *        column of grid
     */
    public void open(int row, int col) {
        inBound(row, col);
        if (isOpen(row, col)) return;
        open[row - 1][col - 1] = true;
        numOpen++;
        // if row is top, connect to virtual top
        if (row == 1) {
            uf.union(oneDindex(row, col), VTOP);
            full.union(oneDindex(row, col), VTOP);
        }
        // if row is bottom, connect to virtual bottom
        if (row == size) {
            uf.union(oneDindex(row, col), vBot);
        }
        // check below
        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(oneDindex(row, col), oneDindex(row - 1, col));
            full.union(oneDindex(row, col), oneDindex(row - 1, col));
        }
        // check above
        if (row < size && isOpen(row + 1, col)) {
            uf.union(oneDindex(row, col), oneDindex(row + 1, col));
            full.union(oneDindex(row, col), oneDindex(row + 1, col));
        }
        // check left
        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(oneDindex(row, col), oneDindex(row, col - 1));
            full.union(oneDindex(row, col), oneDindex(row, col - 1));
        }
        // check right
        if (col < size && isOpen(row, col + 1)) {
            uf.union(oneDindex(row, col), oneDindex(row, col + 1));
            full.union(oneDindex(row, col), oneDindex(row, col + 1));
        }
    }

    /**
     * is site (row, col) open?
     * @param row
     *        row of grid
     * @param col
     *        column of grid
     * @return
     */
    public boolean isOpen(int row, int col) {
        inBound(row, col);
        return open[row-1][col-1];
    }

    /**
     * is site (row, col) full?
     * @param row
     *        row of grid
     * @param col
     *        column of grid
     * @return
     */
    public boolean isFull(int row, int col) {
        inBound(row, col);
        return open[row-1][col-1] && uf.connected(VTOP, oneDindex(row, col)) && full.connected(VTOP, oneDindex(row, col));
    }
    
    /**
     * number of open sites
     * 
     * @return
     */
    public int numberOfOpenSites() {
        return numOpen;
    }

    /**
     * does the system percolate?
     * @return
     */
    public boolean percolates() {
        return uf.connected(VTOP, vBot);
    }
}
