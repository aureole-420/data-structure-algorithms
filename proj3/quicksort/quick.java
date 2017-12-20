import edu.princeton.cs.algs4.StdRandom;

public class quick {

	private quick() {

	}

	public static void sort (Comparable[] a) {
		StdRandom.shuffle(a);
		sort(a, 0, a.length -1);
	}

	private static void sort (Comparable[] a, int lo, int hi) {

		// single element
		if (hi <= lo)
			return;

		int j = partition(a, lo, hi);

		sort(a, lo, j-1);
		sort(a, j+1, hi);
	}

	// if q is less than p
	private static boolean less(Comparable q, Comparable p) {
		return q.compareTo(p) < 0;
	}

	// exchange elements in E[] a
	private static void exch(Comparable[] a, int i, int j) {
		Comparable temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	private static int partition (Comparable[] a, int lo, int hi) {

		// partition into a[lo..i-1], a[i], a[i+1..hi].
		int i = lo; int j = hi+1;
		Comparable v = a[lo];
		while (true) {
			while (less(a[++i], v))
				if (i == hi)
					break;

			while (less(v, a[--j]))
				if (j == lo)
					break;
			
			if (i >= j)
				break;
			
			exch(a, i, j);
		}

		exch(a, lo, j);
		return j;
	}

	public static void main(String[] args) {
		Integer[] a = new Integer[]{2,9,1,7,4,3};
		quick.sort(a);
		for (Integer t : a) {
			System.out.println(t);
		}
	}
}