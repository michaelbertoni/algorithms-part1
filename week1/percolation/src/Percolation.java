import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by Michael on 26/02/2017.
 */
public class Percolation {
    private int size; // row/column size
    private int top = 0; // virtual top site
    private int bottom; // virtual bottom site
    private boolean[][] grid;
    private int openSites = 0;

    private WeightedQuickUnionUF uf, isFullUf;

    /**
     * create n-by-n grid, with all sites blocked
     * @param N
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be larger than 0.");
        }

        size = N;
        bottom = size * size + 1;
        uf = new WeightedQuickUnionUF(size * size + 2);
        isFullUf = new WeightedQuickUnionUF(size * size + 1);
        grid = new boolean[size][size];
    }

    private void checkInput(int row, int col) {
        if (row < 1 || size < row) {
            throw new IndexOutOfBoundsException("row must be between 1 and " + size);
        }
        if (col < 1 || size < col) {
            throw new IndexOutOfBoundsException("col must be between 1 and " + size);
        }
    }

    private int getIndex(int row, int col) {
        return (row - 1) * size + col;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        // Check parameters for errors
        checkInput(row, col);

        if (!isOpen(row, col)) {
            int siteIndex = getIndex(row, col);

            // Open site at row/column cordinates
            grid[row - 1][col - 1] = true;
            openSites++;


            // Top row ? link to virtual top site
            if (row == 1) {
                uf.union(top, siteIndex);
                isFullUf.union(top, siteIndex);
            }

            // Bottom row ? link to virtual bottom site
            if (row == size) {
                uf.union(bottom, siteIndex);
            }

            //// check for neighbours
            // left
            if (col > 1 && isOpen(row, col - 1)) {
                uf.union(siteIndex, getIndex(row, col - 1));
                isFullUf.union(siteIndex, getIndex(row, col - 1));
            }
            // right
            if (col < size && isOpen(row, col + 1)) {
                uf.union(siteIndex, getIndex(row, col + 1));
                isFullUf.union(siteIndex, getIndex(row, col + 1));
            }
            // bottom
            if (row > 1 && isOpen(row - 1, col)) {
                uf.union(siteIndex, getIndex(row - 1, col));
                isFullUf.union(siteIndex, getIndex(row - 1, col));
            }
            // top
            if (row < size && isOpen(row + 1, col)) {
                uf.union(siteIndex, getIndex(row + 1, col));
                isFullUf.union(siteIndex, getIndex(row + 1, col));
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        // Check parameters for errors
        checkInput(row, col);

        return grid[row -1][col - 1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        // Check parameters for errors
        checkInput(row, col);

        if (isOpen(row, col)) {
            int index = getIndex(row, col);
            return isFullUf.connected(top, index);
        }
        return false;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(top, bottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation pr = new Percolation(4);
        pr.open(1,1);
        pr.open(1,2);
        StdOut.println(pr.uf.connected(1,2)); // true
        StdOut.println(pr.percolates()); // false
        StdOut.println(pr.numberOfOpenSites()); // 2
        pr.open(2, 2);
        pr.open(3, 2);
        pr.open(4, 2);
        pr.open(4, 4);
        StdOut.println(pr.percolates()); // true
    }

}
