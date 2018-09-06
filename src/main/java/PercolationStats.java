import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

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
public class PercolationStats {
    
    private static final double CONSTANT = 1.96;
    private final double[] experiments;
    private final int t;
    private int numOpen;
    private double mean = 0;
    private double stddev = 0;
    private double confidenceLo;
    private double confidenceHi;
    
     
   /**
    * perform trials independent experiments on an n-by-n grid
    * @param n
    * @param trials
    */
   public PercolationStats(int n, int trials) {
       if (n < 1 || trials < 1) {
           throw new IllegalArgumentException();
       }
       t = trials;
       experiments = new double[t];
       for (int i = 0; i < t; i++) { 
           Percolation percolation = new Percolation(n);
           numOpen = 0;
           while (!percolation.percolates()) {
               int num1 = StdRandom.uniform(1, n + 1);
               int num2 = StdRandom.uniform(1, n + 1);
               if (!percolation.isOpen(num1, num2)) {
                   percolation.open(num1, num2);
                   numOpen++;
               }
           }
           double num = (double) numOpen / (n * n);
           experiments[i] = num;
       }
   }
   
   /**
    * calculate sample mean of percolation threshold
    * @return
    */
   public double mean() {
       mean = StdStats.mean(experiments);
       return mean;
   }
   
   /**
    * calculate sample standard deviation of percolation threshold
    * @return
    */
   public double stddev() {
       stddev = StdStats.stddev(experiments);
       return stddev;
   }
   
   /**
    * return low  endpoint of 95% confidence interval
    * @return
    */
   public double confidenceLo() {
       if (mean == 0) { 
           mean = StdStats.mean(experiments);
       }
       if (stddev == 0) { 
           stddev = StdStats.stddev(experiments);
       }
       confidenceLo = mean - ((CONSTANT * stddev) / Math.sqrt(t));
       return confidenceLo;
   }
   
   /**
    * return high endpoint of 95% confidence interval
    * @return
    */
   public double confidenceHi() {
       if (mean == 0) { 
           mean = StdStats.mean(experiments);
       }
       if (stddev == 0) { 
           stddev = StdStats.stddev(experiments);
       }
       confidenceHi = mean + ((CONSTANT * stddev) / Math.sqrt(t));
       return confidenceHi;
   }

   /**
    * test client (described at http://coursera.cs.princeton.edu/algs4/assignments/percolation.html)
    * @param args
    */
   public static void main(String[] args) {
       PercolationStats percStats = new PercolationStats(200, 100);
       System.out.println("mean = " + percStats.mean());
       System.out.println("stddev = " + percStats.stddev());
       System.out.println("95% confidence interval = " + percStats.confidenceLo() + ", " + percStats.confidenceHi());
   }
}
