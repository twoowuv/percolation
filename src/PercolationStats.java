import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private int N;
    private int T;
    private double[] thresholds;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {

        if (N < 1 || T < 1) {
            throw new java.lang.IllegalArgumentException("Either N or T is less than 1.");
        }

        this.N = N;
        this.T = T;
        thresholds = new double[T];

        for (int i = 0; i < T; i++) {
            Percolation perc = new Percolation(N);

            int openNodes = 0;

            while (!perc.percolates()) {
                openRandomNode(perc);
                openNodes++;
            }

            thresholds[i] = (double) openNodes / (N * N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(T));
    }

    private void openRandomNode(Percolation perc) {
        boolean openNode = true;
        int row = 0;
        int column = 0;

        // Repeat until we randomly find a closed node
        while (openNode) {
            // Generate random coordinates for the row and column
            row = StdRandom.uniform(1, N + 1);
            column = StdRandom.uniform(1, N + 1);

            openNode = perc.isOpen(row, column);
        }

        perc.open(row, column);
    }

    // test client (described below)
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        Stopwatch timer = new Stopwatch();

        PercolationStats stats = new PercolationStats(N, T);

        System.out.println("Running " + T + " experiments on a " + N + " by " + N + " grid took " + timer.elapsedTime() + "seconds");

        System.out.println("Mean: " + stats.mean());
        System.out.println("Standard Dev: " + stats.stddev());
        System.out.println("95% Confidence Interval: " + stats.confidenceLo() + ", " + stats.confidenceHi());
    }
}
