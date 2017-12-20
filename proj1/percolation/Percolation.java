import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private WeightedQuickUnionUF gridN_WQUF;
	private int gridSize; // gridSize = n
	private int[] status; // the status of sites: OPEN or BLOCKED, 1 or 0
	private int siteTop; // a virtual top site 
	private int siteBottom; // a virtual bottom site
	private int int_numberOfOpenSites;

	// create n-by-n grid, with all sites blocked
	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		/** virtual siteTop and siteBottom 
		* are numbered 0 and n^2+1 respectively
		*/
		gridN_WQUF = new WeightedQuickUnionUF(n*n+2);
		siteTop = 0;
		siteBottom = n * n+1;
		gridSize = n;
		status = new int[n*n+2]; // initially all blocked except siteTop and siteBottom
		status[siteTop] = 1;
		status[siteBottom] = 1;
		int_numberOfOpenSites = 0;

	}

	// mapping (row, col) to N (which varies from 1 to n^2)
	private int rowCol2N(int row, int col){
		return (row-1)*gridSize+col;
	}

	private boolean isInGrid(int row, int col) {
		// return true;
		return (((1 <= row) && (row <= gridSize)) && ((1 <= col) && (col <= gridSize))); 
	}

	private boolean isTopSite(int row, int col) {
		return row == 1;
	}

	private boolean isBottomSite(int row, int col) {
		return row == gridSize;
	}

	// connect with adjacent site, if possible
	private void connectAdjacentSite(int row, int col, int rowAdj, int colAdj) {
		if (!isInGrid(rowAdj, colAdj)) {
			return; // adjacent site not in grid
		}
		if (!isOpen(rowAdj, colAdj)) {
			return; // adjacent site not open
		}
		int N = rowCol2N(row, col);
		int NAdj = rowCol2N(rowAdj, colAdj);
		if (gridN_WQUF.connected(N, NAdj)) {
			return; // already connected
		} else {
			gridN_WQUF.union(N, NAdj); 
			// System.out.println("site("+row+","+col+") is now connected to site("+rowAdj+","+colAdj+").");
		}

	}

	// connect with virtual siteTop, if possible
	private void connectSiteTop(int row, int col) {
		if (isTopSite(row, col)) {
			int N = rowCol2N(row, col);
			if (!gridN_WQUF.connected(N, siteTop)) {
				gridN_WQUF.union(N,siteTop);
				// System.out.println("site("+row+","+col+") is now connected to siteTop.");	
			}			
		}
	}

	// connect with virtual siteBottom, if possible
	private void connectSiteBottom(int row, int col) {
		if (isBottomSite(row, col)) {
			int N = rowCol2N(row, col);
			if (!gridN_WQUF.connected(N, siteBottom)) {
				gridN_WQUF.union(N,siteBottom);
				// System.out.println("site("+row+","+col+") is now connected to siteBottom.");	
			}		
		}
	}

	// open site (row, col) if it is not open already
	public void open(int row, int col) {
		if (!isInGrid(row, col)) {
			throw new IndexOutOfBoundsException();
		}
		if (isOpen(row, col)) { // already open 
			return;
		}
		// open site(row, col);
		int N = rowCol2N(row, col);
		status[N] = 1;
		int_numberOfOpenSites += 1;
		// connect with adjacent sites
		connectAdjacentSite(row, col, row, col-1);
		connectAdjacentSite(row, col, row, col+1);
		connectAdjacentSite(row, col, row-1, col);
		connectAdjacentSite(row, col, row+1, col);
		connectSiteBottom(row, col);
		connectSiteTop(row, col);
	}
	

	// is site (row, col) open?
	public boolean isOpen(int row, int col) {
		if (!isInGrid(row, col)) {
			throw new IndexOutOfBoundsException();
		}
		int N = rowCol2N(row, col);
		if (status[N] == 1) {
			return true;
		} else {
			return false;
		}
	}

	// is site (row, col) full?
	public boolean isFull(int row, int col) {
		if (!isInGrid(row, col)) {
			throw new IndexOutOfBoundsException();
		}
		int N = rowCol2N(row, col);
		return gridN_WQUF.connected(N, siteTop);
	}

	// number of open sites
	public int numberOfOpenSites() {
		return int_numberOfOpenSites;
	}

	// does the syste percolate?
	public boolean percolates() {
		return (gridN_WQUF.connected(siteTop, siteBottom));
	}


	// test client (optional)
	public static void main(String[] args) {
		// test 1, a 2-by-2 grid
		Percolation g2 = new Percolation(2);
		System.out.println("number of open sites: " + g2.numberOfOpenSites() + "; Percolates?: "+g2.percolates());
		g2.open(1, 1);
		System.out.println("number of open sites: " + g2.numberOfOpenSites() + "; Percolates?: "+g2.percolates());
		// g2.open(2,2);
		// System.out.println("number of open sites: " + g2.numberOfOpenSites() + "; Percolates?: "+g2.percolates());
		g2.open(2, 1);
		System.out.println("number of open sites: " + g2.numberOfOpenSites() + "; Percolates?: "+g2.percolates());	
	}

}	
