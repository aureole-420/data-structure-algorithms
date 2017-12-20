import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {

	private ArrayList<LineSegment> foundSegments;

	// has repeated points
	private boolean hasDuplicate(Point[] points) {

		Point[] jPoints = points.clone();
		Arrays.sort(jPoints);

		for (int i =0; i< jPoints.length-1; i++) {

			// corner case: null point
			if (jPoints[i] == null) 
				throw new NullPointerException("A point is NULL!");

			// corner case: repeated points
			if (jPoints[i].compareTo(jPoints[i+1]) == 0)
				return true;
		}

		return false;
	}

	private boolean isNewSegment (boolean pIsMinPoint, int lengthSegment) {
		return pIsMinPoint && (lengthSegment >= 4);
	}

	// segments containing point p
	private void checkPoint(Point[] points, Point p) {

		Point[] jPoints = points.clone();
		Arrays.sort(jPoints, p.slopeOrder());

		/* for checking
		System.out.println("p: " + p.toString());
		for (int k = 0; k < jPoints.length; k++)
			System.out.println(jPoints[k].toString() + " slope: " + p.slopeTo(jPoints[k]));
		*/

		// index j always points to the end of a segment
		int j;
		int lengthSegment;
		int endSegment;
		int i = 1; 
		while (i < jPoints.length - 2) {
			j = i;
			endSegment = i; // index denote the end of Segment
			lengthSegment = 1; // only 1 point: p
			double slPI = p.slopeTo(jPoints[i]);
			double slPJ = p.slopeTo(jPoints[j]);
			boolean pIsMinPoint = true;
			Point maxPoint = p;
			// find end of a segment
			while (j < jPoints.length && slPI == slPJ) {
				endSegment = j; // include jPoints[j]
				if (p.compareTo(jPoints[j]) > 0)
					pIsMinPoint = false;
				if (maxPoint.compareTo(jPoints[j]) < 0)
					maxPoint = jPoints[j];
				j++;
				lengthSegment++;

				if (j >= jPoints.length)
					break; // no more points

				slPJ = p.slopeTo(jPoints[j]);
			}

			// check if is New Segment
			if (isNewSegment(pIsMinPoint, lengthSegment)){
				// System.out.println(endSegment);
				// System.out.println(jPoints[endSegment].toString());
				foundSegments.add(new LineSegment(p, maxPoint));
			}

			i = endSegment + 1;
		}

	}

	// finds all line segments contain 4 or more points
	public FastCollinearPoints(Point[] points) {

		foundSegments = new ArrayList<>();
		
		// corner case: null points
		if (points == null) 
			throw new NullPointerException("The input Point array is NULL!");
		
		// corner case: has repeated points
		if (hasDuplicate(points)) 
			throw new IllegalArgumentException("The input points have repeated points!");

		for (Point p : points)
			checkPoint(points, p);
	}

	// the number of line segments
	public int numberOfSegments() {
		return foundSegments.size();
	}

	// the line segments
	public LineSegment[] segments() {
		return foundSegments.toArray(new LineSegment[foundSegments.size()]);
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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    for (LineSegment segment : collinear.segments()) {
        StdOut.println(segment);
        segment.draw();
    }
    StdDraw.show();
}

}