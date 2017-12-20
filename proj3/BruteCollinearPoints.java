import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class BruteCollinearPoints {

	private ArrayList<LineSegment> arrayListSegments;

	private static boolean less(Point q, Point p) {
		return q.compareTo(p) < 0;
	}

	// check if Point[] points has repeated points
	private boolean hasDuplicate(Point[] points) {
		for (int i = 0; i < points.length-1; i++) {
			// corner case: null point
			if (points[i] == null) 
				throw new NullPointerException("A point is null!");

			if (points[i].compareTo(points[i+1]) == 0) {
				return true;
			}
		}
		return false;
	} 

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {

		// corner case: null points
		if (points == null) {
			throw new NullPointerException();
		}

		Point[] jPoints = points.clone();
		Arrays.sort(jPoints); 

		// corner case: has repeated points
		if (hasDuplicate(jPoints)) {
			throw new IllegalArgumentException();
		}

		arrayListSegments = new ArrayList<>();
		for (int i = 0; i < jPoints.length; i += 1) {
			for (int j = i+1; j < jPoints.length; j += 1) {
				for (int k = j+1; k < jPoints.length; k += 1) {
					for (int l = k+1; l < jPoints.length; l += 1) {
						if (areCollinear(jPoints[i], jPoints[j], jPoints[k], jPoints[l])) {
							LineSegment newSegment = new LineSegment(jPoints[i], jPoints[l]);
							arrayListSegments.add(newSegment);
						}
					}
				}
			}
		}
	}

	private boolean areCollinear(Point q0, Point q1, Point q2, Point q3) {
		double sl01 = q0.slopeTo(q1);
		double sl02 = q0.slopeTo(q2);
		double sl03 = q0.slopeTo(q3);
		if (sl01 == sl02 && sl01 == sl03) {
			return true;
		} else {
			return false;
		}
	}

	// the number of line segments
	public int numberOfSegments() {
		return arrayListSegments.size();
	}

	// the line segments
	public LineSegment[] segments() {
		return arrayListSegments.toArray(new LineSegment[arrayListSegments.size()]);
	}


	//////////////////////////////////////////
	/////// Client for unit testing //////////
	//////////////////////////////////////////

	public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
        int x = in.readInt();
        int y = in.readInt();
        points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
        p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
}

}