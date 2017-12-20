import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

	public static void main (String[] args) {
		int k = Integer.parseInt(args[0]);
		RandomizedQueue<String> rq = new RandomizedQueue<>();
		while (!StdIn.isEmpty()) {
			String item = StdIn.readString();
			// System.out.println(item);
			rq.enqueue(item);
		}
		int i = 0;
		for (String s : rq) {
			if (i < k) {
				StdOut.println(s);
			}
			i += 1;
		}
	}
}