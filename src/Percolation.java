import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * A solution to the percolation problem. We create an N * N grid and nodes are opened
 * one at a time until the grid percolates. The grid is said to percolate when there is
 * a path of adjacent nodes from the top of the grid to the bottom.
 * ie. The first node is connected to the last node
 */
public class Percolation {

    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF backwashChecker;
    private int N;
    private int top;
    private int bottom;
    private boolean[] openNodes;

    public Percolation(int N) {
        if (N < 1) {
            throw new java.lang.IllegalArgumentException("N cannot be less than 1");
        }

        this.N = N;

        // Pad the length of our grid by 2 for an extra virtual node on the top and bottom
        grid = new WeightedQuickUnionUF(N * N + 2);

        // The back fill checker doesn't need a bottom virtual node
        backwashChecker = new WeightedQuickUnionUF(N * N + 1);

        top = getArrayIdx(N, N) + 1;
        bottom = getArrayIdx(N, N) + 2;

        openNodes = new boolean[N * N];
    }

    /**
     * Opens a node at the given coordinates if it is not already open and
     * unions it with any open surrounding nodes
     *
     * @param i row - The row number from the top starting at 1
     * @param j column - The column number from the left starting at 1
     */
    public void open(int i, int j) {
        doInBoundsCheck(i, j);

        // If the node is already open then return
        if (isOpen(i, j)) {
            return;
        }

        int idx = getArrayIdx(i, j);

        // Add it to the openNodes array
        openNodes[idx] = true;

        // If it's in the first row union with the top virtual node
        if (i == 1) {
            grid.union(top, idx);
            backwashChecker.union(top, idx);
        }

        // If it's in the last row union with the bottom virtual node
        if (i == N) {
            grid.union(bottom, idx);
        }

        // Union with the surrounding nodes if they are open and in bounds
        // TOP
        if (isInBounds(i - 1, j) && isOpen(i - 1, j)) {
            grid.union(getArrayIdx(i - 1, j), idx);
            backwashChecker.union(getArrayIdx(i - 1, j), idx);
        }

        // BOTTOM
        if (isInBounds(i + 1, j) && isOpen(i + 1, j)) {
            grid.union(getArrayIdx(i + 1, j), idx);
            backwashChecker.union(getArrayIdx(i + 1, j), idx);
        }

        // LEFT
        if (isInBounds(i, j - 1) && isOpen(i, j - 1)) {
            grid.union(getArrayIdx(i, j - 1), idx);
            backwashChecker.union(getArrayIdx(i, j - 1), idx);
        }

        // RIGHT
        if (isInBounds(i, j + 1) && isOpen(i, j + 1)) {
            grid.union(getArrayIdx(i, j + 1), idx);
            backwashChecker.union(getArrayIdx(i, j + 1), idx);
        }
    }

    /**
     * Checks whether a node is open at the coords given is open.
     * A node is said to be open if it is in the openNodes array.
     *
     * @param i - The row number from the top starting at 1
     * @param j - The column number from the left starting at 1
     *
     * @returns boolean
     */
    public boolean isOpen(int i, int j) {
        doInBoundsCheck(i, j);
        return openNodes[getArrayIdx(i, j)];
    }

    /**
     * Checks whether a node is open at the coords given is full.
     * A node is said to be full if it is connected to the top virtual node.
     *
     * @param i - The row number from the top starting at 1
     * @param j - The column number from the left starting at 1
     *
     * @returns boolean
     */
    public boolean isFull(int i, int j) {
        doInBoundsCheck(i, j);
        int idx = getArrayIdx(i, j);
        return backwashChecker.connected(idx, top);
    }

    /**
     * Checks whether the grid percolates. The grid percolates if the bottom
     * virtual node is connected to the top virtual node.

     * @returns boolean
     */
    public boolean percolates() {
        return grid.connected(top, bottom);
    }

    /**
     * Checks if the coords given are in the grid of size N.
     * eg. In a 10x10 grid neither coord can be larger than 10 or less than 1
     *
     * @param i - The row number from the top starting at 1
     * @param j - The column number from the left starting at 1
     *
     * @returns boolean
     */
    private boolean isInBounds(int i, int j) {
        return i > 0
                && j > 0
                && i <= N
                && j <= N;
    }

    /**
     * Throws an exception if the coords are outside of the grid
     */
    private void doInBoundsCheck(int i, int j) {
        if (!isInBounds(i, j)) {
            throw new IndexOutOfBoundsException("Values not in bounds for a " + N + " x " + N + " grid");
        }
    }

    /**
     * Converts x and y coordinates of a grid which starts at 1, 1 into an array index
     * which starts at 0 and goes to N
     */
    private int getArrayIdx(int x, int y) {
        //TODO: see if you can just do this once at the start
        doInBoundsCheck(x, y);
        return ((y - 1) * N) + (x - 1);
    }

}