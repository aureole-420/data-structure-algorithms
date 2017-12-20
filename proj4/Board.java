import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import java.util.Arrays;

/*
* author Kevin Tong
*/

public class Board {

	private final int[][] tiles; // board
	private final int N; // dimension

	private static final int SPACE = 0;

	// construct a board from an n-by-n array of blocks
	public Board(int[][] blocks) {
		N = blocks.length;
		tiles = new int[N][N];
		for (int i = 0; i < N; i++) 
			for (int j = 0; j < N; j++) 
				tiles[i][j] = blocks[i][j];
	}

	// board dimension n
	public int dimension() {
		return N;
	}


	// number of blocks out of place
	public int hamming() {
		int cnt = 0;
		for (int row = 0; row < dimension(); row++) 
			for (int col = 0; col < dimension(); col++) {
				if (tiles[row][col] != SPACE && tiles[row][col] != goalElement(row, col)){
					cnt++;
				} 
			}

		return cnt;
	}

	// sum of Manhattan distances
	public int manhattan() {
		int sum = 0;
		for (int row = 0; row < N; row++) 
			for (int col = 0; col < N; col++)
				if (tiles[row][col] != SPACE)
					sum += mht(tiles[row][col], row, col);

		return sum;
	}

	// is this board the goal board?
	public boolean isGoal() {
		boolean flag = true;
		for (int row = 0; row < dimension(); row++)
			for (int col = 0; col < dimension(); col++)
				if (tiles[row][col] != goalElement(row, col))
					flag = false;

		return flag;
	}

	// a board that is obtained by exchanging any pair of blocks
	public Board twin() {
		int rRow = StdRandom.uniform(dimension());
		int rCol = StdRandom.uniform(dimension());
		// StdOut.println("(" + rRow + ", " + rCol + ")");
		if (tiles[rRow][rCol] != SPACE 
			&& rRow+1 < dimension() 
			&& tiles[rRow+1][rCol] != SPACE) {
			// swap [rRow][rCol] and [rRow + 1][rCol]
			int[][] twTiles = copyTiles(tiles);
			
			int swap = twTiles[rRow][rCol];
			twTiles[rRow][rCol] = twTiles[rRow + 1][rCol];
			twTiles[rRow + 1][rCol] = swap;

			return new Board(twTiles);
		} 
		else return twin();
	}


	// does this board equal y?
	public boolean equals(Object y) {
		if (y == this) return true; 
		if (y == null) return false;
		if (y.getClass() != this.getClass())
			return false;

		Board that = (Board) y;
		if (this.dimension() != that.dimension())
			return false;

		boolean flag = true;
		for (int i = 0; i < this.dimension(); i++) 
			if (Arrays.equals(this.tiles[i],that.tiles[i]) == false)
				flag = false;
		
		return flag == true;
	}


	// all neighboring boards
	public Iterable<Board> neighbors() {
		// search for SPACE
		int sRow =0, sCol = 0;
		for (int row = 0; row < dimension(); row++)
			for (int col = 0; col < dimension(); col++) {
				if (tiles[row][col] == SPACE) {
					sRow = row;
					sCol = col;
					break;
				}
			}

		Queue<Board> queue = new Queue<>();
		
		// upper Board
		if (sRow > 0) queue.enqueue(neighborBoard(sRow, sCol, sRow - 1, sCol));
		// right Board
		if (sCol < N-1) queue.enqueue(neighborBoard(sRow, sCol, sRow, sCol + 1));
		// lower Board
		if (sRow < N-1) queue.enqueue(neighborBoard(sRow, sCol, sRow + 1, sCol));
		// left Board
		if (sCol > 0) queue.enqueue(neighborBoard(sRow, sCol, sRow, sCol - 1));
		
		return queue;
	}


	// string representation of this board (in the output format specified below)
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(dimension() + "\n");
		for (int row = 0; row < dimension(); row++) {
			for (int col = 0; col < dimension(); col++){
				str.append(String.format("%2d ", tiles[row][col]));
			}
			str.append("\n");
		}
		return str.toString();
	}




	///////////////////////////////////////
	////////// helper methods /////////////
	///////////////////////////////////////

	// is SPACE?
	private boolean isSpace(int tile) {
		return tile == SPACE;
	}

	// the element in goal board
	private int goalElement(int row, int col) {

		if (row == dimension()-1 && col == dimension()-1)
			return SPACE;

		return row*dimension() + col + 1;
	}

	// copy a 2D array
	private int[][] copyTiles(int[][] tiles) {
		int[][] copy = new int[tiles.length][tiles[0].length];
		for (int i = 0; i < tiles.length; i++) 
			copy[i] = tiles[i].clone();
		return copy;
	}

	// compute manhatan priority for a single tile (jRow, jCol)
	private int mht(int tile, int jRow, int jCol) {
		if (tile == SPACE)
			return 0;
		int col = (tile - 1) % dimension();
		int row = (tile - 1 - col) / dimension();

		return Math.abs(col - jCol) + Math.abs(row - jRow);
	}


	// create neighbour Board
	private Board neighborBoard(int sRow, int sCol, int nRow, int nCol) {
		int[][] ngbTiles = copyTiles(tiles);

		int swap = ngbTiles[sRow][sCol];
		ngbTiles[sRow][sCol] = ngbTiles[nRow][nCol];
		ngbTiles[nRow][nCol] = swap;

		return new Board(ngbTiles);
	}



	///////////////////////////////////////
	////////////// unit tests /////////////
	///////////////////////////////////////

	public static void main (String[] args){
		int[][] arr = new int[][]{{1,2},{3,0}};
		Board bd = new Board(arr);
		System.out.println(bd.toString());

		Board twBD = bd.twin();
		System.out.println(twBD.toString());

		System.out.println("Neighboring Boards");
		Iterable<Board> ngbBoards = bd.neighbors();
		for (Board b : ngbBoards) {
			System.out.println(b.toString());
		}

		System.out.println("current Board:");
		System.out.println(bd.toString());

		System.out.println(bd.isGoal());

		Board bd1 = new Board(arr);
		System.out.println("bd == bd1: " + bd.equals(bd1));
		System.out.println("bd == twBD: " + bd.equals(twBD));

	}

}