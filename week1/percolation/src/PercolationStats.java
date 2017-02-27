import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by Michael on 26/02/2017.
 */
public class PercolationStats {
    private int trialsNumber;
    private Percolation pr;
    private double[] fractions;

    /**
     * perform trials independent experiments on an n-by-n grid
     * @param n
     * @param trials
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be larger than 0");
        }
        trialsNumber = trials;
        int size = n*n;
        fractions = new double[trialsNumber];

        for (int trialNum = 0; trialNum < trialsNumber; trialNum++) {
            pr = new Percolation(n);
            while(!pr.percolates()) {
                int i = StdRandom.uniform(1, n+1);
                int j = StdRandom.uniform(1, n+1);
                if (!pr.isOpen(i, j)) {
                    pr.open(i, j);
                }
            }
            double fraction = (double) pr.numberOfOpenSites() / (size);
            fractions[trialNum] = fraction;
        }
    }

    /**
     * sample mean of percolation threshold
     * @return
     */
    public double mean() {
        return StdStats.mean(fractions);
    }

    /**
     * sample standard deviation of percolation threshold
     * @return
     */
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    /**
     * low endpoint of 95% confidence interval
     * @return
     */
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(trialsNumber));
    }

    /**
     * high endpoint of 95% confidence interval
     * @return
     */
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(trialsNumber));
    }

    /**
     * test client (described below)
     * @param args
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, T);

        String confidence = "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]";
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }

}
