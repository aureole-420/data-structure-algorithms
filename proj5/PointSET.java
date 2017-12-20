import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class PointSET {
	private TreeSet<Point2D> Points;

	// construct an empty set of points
	public PointSET() {
		Points = new TreeSet<Point2D>();
	}

	// is the set empty?
	public boolean isEmpty() {
		return Points.isEmpty();
	}

	// number of points in the set
	public int size() {
		return Points.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException();
		Points.add(p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException();
		return Points.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		for (Point2D point : Points) 
			point.draw();
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new IllegalArgumentException();
		LinkedList<Point2D> linkedlist = new LinkedList<>();
		for (Point2D point : Points) {
			if (rect.contains(point))
				linkedlist.add(point);
		}
		return linkedlist;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException();
		int n = 0;
		double min_distance = 0;
		Point2D nearest_point = null;
		for (Point2D point : Points) {
			if (n == 0) {
				min_distance = point.distanceTo(p);
				nearest_point = point;
			} else {
				double current_distance = point.distanceTo(p);
				if (current_distance < min_distance) {
					min_distance = current_distance;
					nearest_point = point;
				}
			}
			n++;
		} 
		return nearest_point;
	}


	// testing client
    public static void main(String[] args) {

    	RectHV rect = new RectHV(0.0, 0.0, 2.0, 2.0);
    	Point2D p0 = new Point2D(0.0, 0.0);
    	Point2D p1 = new Point2D(2.0, 1.0);
    	Point2D p2 = new Point2D(3.0, 2.0);
    	Point2D p3 = new Point2D(0.5, 0.5);
    	Point2D p4 = new Point2D(-5, -6);

    	PointSET pointset = new PointSET();
    	pointset.insert(p1);
    	pointset.insert(p2);
    	pointset.insert(p3);
    	pointset.insert(p4);
    	System.out.println(pointset.nearest(p0).toString());

    	for (Point2D point : pointset.range(rect))
    		System.out.println(point.toString());
    	/*
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        PointSET pointset = new PointSET();
        int n = 1;
        while (true) {
            if (StdDraw.mousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.println("new point:" + n);
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                	StdOut.println("new point is contained by the rectangle.");
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    pointset.insert(p);
                    StdDraw.clear();
                    pointset.draw();
                    StdDraw.show();
                }
            	n++;
            }
            StdDraw.pause(200);          
        }
        */

    }

}