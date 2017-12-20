import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
	private double[] fractionOfOpenSites; // a supplementary array for stats
	private int gridSize;
	private int numOfTrials;

	// perform trials independent experiments on an n-by-n grid
	public PercolationStats(int n, int trials){
		if (n <= 0 || trials <= 0){
			throw new IllegalArgumentException();
		}
		gridSize = n;
		numOfTrials = trials;
		fractionOfOpenSites = new double[trials];
		//StdRandom.setSeed(1234567);
		// do a simulation for every loop
		for (int i = 0; i<trials; i=i+1){
			Percolation perc = new Percolation(n);
			// open sites when possible
			while(!perc.percolates()){
				int row = StdRandom.uniform(1,n+1); // a random number 
				int col = StdRandom.uniform(1,n+1);
				if (!perc.isOpen(row, col)){
					perc.open(row, col);
				}
			}
			fractionOfOpenSites[i] = ((double) perc.numberOfOpenSites())/(n*n);
		}
	}

	// sample mean of percolation threshold
	public double mean(){
		return StdStats.mean(fractionOfOpenSites);
	}

	// sample standard deviation of percolation threshold 
	public double stddev(){
		return StdStats.stddev(fractionOfOpenSites);
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo(){
		return mean() - 1.96*stddev()/Math.sqrt(numOfTrials);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi(){
		return mean() + 1.96*stddev()/Math.sqrt(numOfTrials);
	}

	// test client
	public static void main(String[] args){
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);
		PercolationStats percStat = new PercolationStats(n,trials);
		// PercolationStats percStat = new PercolationStats(200,100);
		System.out.format("mean = %10f \n", percStat.mean());
		System.out.format("stddev = %10f \n", percStat.stddev());
		System.out.format("95 %% confidence interval = [%-10f, %-10f] \n", percStat.confidenceLo(), percStat.confidenceHi());
	}
}