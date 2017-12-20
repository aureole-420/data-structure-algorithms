import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public class Solver {

	private searchNode result;

	private class searchNode implements Comparable<searchNode> {
		private final Board board;
		private final searchNode prev; // parent searchNode
		private int priority;
		private int numOfMoves; // number of moves from initial Board

		public searchNode(Board board, searchNode prev) {
			this.board = board;
			this.prev = prev;
			if (prev == null) 
				numOfMoves = 0;
			else
				numOfMoves = prev.numOfMoves + 1;
			priority = board.manhattan() + numOfMoves;
		}

		@Override
		public int compareTo(searchNode that){
			int diff = this.priority - that.priority;
			// less 
			if (diff < 0 ) return -1;
			if (diff > 0) return +1;
			return +0;
		}

		public boolean isGoal() {
			return board.isGoal();
		}

		public int getNumOfMoves() {
			return numOfMoves;
		}

		public searchNode twin() {
			return new searchNode(board.twin(), prev);
		}
	}

	//find a solution to the initial board using A* algorithm
	public Solver(Board initial) {
		if (initial == null) 
			throw new IllegalArgumentException();
		// run solver
		result = solveIt(new searchNode(initial,null));
	}


	private searchNode calculateOneMove(MinPQ<searchNode> pq) {
		searchNode current = pq.delMin();
		for (Board ngb : current.board.neighbors()) {
			if (current.prev == null || !ngb.equals(current.prev.board))
				pq.insert(new searchNode(ngb, current));
		}
		return current;
	}

	// solving initial and initial.twin() boards simultaneously 
	private searchNode solveIt(searchNode initial) {
		searchNode last = initial;
		MinPQ<searchNode> snQueue = new MinPQ<>();
		snQueue.insert(last);

		MinPQ<searchNode> twQueue = new MinPQ<>();
		twQueue.insert(initial.twin());

		while (true) {
			last = calculateOneMove(snQueue);
			if (last.isGoal()) return last;

			// solve twin search node
			if (calculateOneMove(twQueue).isGoal()) 
				return null;
		}
	}


	// is the initial board solvable? 
	public boolean isSolvable() {
		if (result == null)
			return false;
		return true;
	}

	// min number of moves to solve initial board
	// -1 if unsolvabe
	public int moves() {
		if (!isSolvable()) {
			return -1;
		}
		return result.getNumOfMoves();
	}

	// sequence of boards in a shortest solution
	// null if unsolvable;
	public Iterable<Board> solution() {
		if (!isSolvable())
			return null;

		Stack<Board> solutionStack = new Stack<>();

		searchNode sn = result;
		while (sn != null) {
			solutionStack.push(sn.board);
			sn = sn.prev;
		}
		return solutionStack;
	}

	// solve a slider puzzle 
	public static void main(String[] args) {

    	// create initial board from file
    	In in = new In(args[0]);
    	int n = in.readInt();
    	int[][] blocks = new int[n][n];
    	for (int i = 0; i < n; i++)
        	for (int j = 0; j < n; j++)
            	blocks[i][j] = in.readInt();
    	Board initial = new Board(blocks);

    	// solve the puzzle
    	Solver solver = new Solver(initial);

    	// print solution to standard output
    	if (!solver.isSolvable())
        	StdOut.println("No solution possible");
    	else {
        	StdOut.println("Minimum number of moves = " + solver.moves());
        	for (Board board : solver.solution())
            	StdOut.println(board);
    		}
	}

}