import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import java.awt.Color;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
	private final int VERTICAL = 1;
	private final int HORIZONTAL = 0;
	private KdNode root;
	private int size = 0;

	private class KdNode {
		private Point2D point;
		private KdNode left = null;
		private KdNode right = null;
		private int depth = 1;

		public KdNode(Point2D p, int d) {
			point = p;
			depth = d;
		}
	}


	public KdTree() {
		root = null;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public void insert(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException();
		if (contains(p)) return;
		root = insertKdNode(root, p, 1);
	}

	private KdNode insertKdNode(KdNode CurrentNode, Point2D p, int d) {
		// CurrentNode can be an empty root OR a null leaf
		if (CurrentNode == null) {
			size++;
			return new KdNode(p, d);
		}

		double isInLeftSBT = 0;
		// should be put into left substree?
		if (CurrentNode.depth == VERTICAL) 
			isInLeftSBT = p.x() - CurrentNode.point.x();
		else if (CurrentNode.depth == HORIZONTAL)
			isInLeftSBT = p.y() - CurrentNode.point.y();

		int NewDepth = (CurrentNode.depth+1) % 2;

		if (isInLeftSBT < 0) 
			CurrentNode.left = insertKdNode(CurrentNode.left, p, NewDepth);
		else 
			CurrentNode.right = insertKdNode(CurrentNode.right, p, NewDepth);

		return CurrentNode;
	}

	public boolean contains(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException();
		return contains(root, p);
	}

	private boolean contains(KdNode x, Point2D p) {
		if (x == null) 
			return false;
		/*
		int cmp = p.compareTo(x.point);
		if (cmp < 0) return contains(x.left, p);
		else if (cmp > 0) return contains(x.right, p);
		else return true;
		*/

		if (x.point.x() == p.x() && x.point.y() == p.y()) return true;

		boolean in_left_sbt;
		if (x.depth == VERTICAL) {
			in_left_sbt = p.x() < x.point.x();
		}
		else { // x.depth == HORIZONTAL
			in_left_sbt = p.y() < x.point.y();
		}
		if (in_left_sbt) return contains(x.left, p);
		else return contains(x.right, p);
	}

	public void draw() {
		draw(root);
	}

	private void draw(KdNode x) {
		if (x == null) return;

		StdDraw.setPenColor(Color.black);
		x.point.draw();
		if (x.depth == VERTICAL) {
			StdDraw.setPenColor(Color.red); 
			StdDraw.line(x.point.x(), 0, x.point.x(), 1);
		}
		else if (x.depth == HORIZONTAL) {
			StdDraw.setPenColor(Color.blue);
			StdDraw.line(0, x.point.y(), 1, x.point.y());
		} 

		draw(x.left);
		draw(x.right);
	}

	//////////////////////////////////////////////////
	///// find points contained in a rectangle ///////
	//////////////////////////////////////////////////
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new IllegalArgumentException();

		Queue<Point2D> queue = new Queue<>();
		rangeHelper(root, rect, queue);
		return queue;
	}
	
	private void rangeHelper(KdNode x, RectHV rect, Queue<Point2D> queue) {
		if (x == null) return;

		if (rect.contains(x.point)) queue.enqueue(x.point);

		double NEGATIVE_INFINITY = Double.NEGATIVE_INFINITY;
		double POSITIVE_INFINITY = Double.POSITIVE_INFINITY;
		RectHV xline;
		if (x.depth == VERTICAL) 
			xline = new RectHV(x.point.x(), NEGATIVE_INFINITY, x.point.x(), POSITIVE_INFINITY);
		else // x.depth == HORIZONTAL
			xline = new RectHV(NEGATIVE_INFINITY, x.point.y(), POSITIVE_INFINITY, x.point.y());
		
		boolean isIntersected = rect.intersects(xline);
		if (isIntersected) {
			rangeHelper(x.left, rect, queue);
			rangeHelper(x.right, rect, queue);
			return;
		}
		
		// not intersected 
		// prune right subtree if rect is to the left/bottom of x
		boolean prune_right_sbt = true;
		if (x.depth == VERTICAL)
			prune_right_sbt = rect.xmax() <= x.point.x();
		if (x.depth == HORIZONTAL)
			prune_right_sbt = rect.ymax() <= x.point.y();

		if (prune_right_sbt) {
			rangeHelper(x.left, rect, queue);
			return;
		} else { // prune left subtree
			rangeHelper(x.right, rect, queue);
			return;
		}
	}

	//////////////////////////////////////////////////
	///////// find the nearest to a query point///////
	//////////////////////////////////////////////////

	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new IllegalArgumentException();
		KdNode result = searchNN(root, p, root);
		if (result == null) return null;
		else return result.point;
	}

	private KdNode searchNN(KdNode x, Point2D p, KdNode current_best) {
		
		// corner case
		if (x == null) return current_best;

		if (p.distanceSquaredTo(x.point) < p.distanceSquaredTo(current_best.point))
			current_best = x;


		double dis_x_p = Double.POSITIVE_INFINITY;
		if (x.depth == VERTICAL)  dis_x_p = Math.abs(p.x() - x.point.x());
		if (x.depth == HORIZONTAL) dis_x_p = Math.abs(p.y() - x.point.y());  

		// is p to the left/bottom of x?
		boolean search_left_sbt_first = true;
		if (x.depth == VERTICAL) search_left_sbt_first = p.x() < x.point.x();
		if (x.depth == HORIZONTAL) search_left_sbt_first = p.y() < x.point.y();
		
		if (search_left_sbt_first) {
			if (x.left != null)
				if (p.distanceSquaredTo(x.left.point) < p.distanceSquaredTo(current_best.point))
					current_best = x.left;  

			boolean prune_right_sbt = p.distanceTo(current_best.point) <= dis_x_p;
			current_best = searchNN(x.left, p, current_best);
			// System.out.println("prune_right_sbt: " + prune_right_sbt);
			if (prune_right_sbt) 
				return current_best; 
			else 
				return searchNN(x.right, p, current_best);
		}	

		else {
			// if (!search_left_sbt_first) 
			if (x.right != null) 
				if (p.distanceSquaredTo(x.right.point) < p.distanceSquaredTo(current_best.point))
					current_best = x.right;  		

			boolean prune_left_sbt = p.distanceTo(current_best.point) <= dis_x_p;
			current_best = searchNN(x.right, p, current_best);
			if (prune_left_sbt)
				return current_best;
			else 
				return searchNN(x.left, p, current_best);
		}

	}
	
	//////////////////////////////////////////////////
	/////////////// testing client ///////////////////
	//////////////////////////////////////////////////

	public static void main(String[] args) {
		/*
		KdTree kdtree = new KdTree();
		kdtree.insert(new Point2D(0.5, 0.5));
		kdtree.insert(new Point2D(0.3,0.7));
		kdtree.insert(new Point2D(0.8,0.9));
		kdtree.insert(new Point2D(0.4,0.48));
		kdtree.insert(new Point2D(0.5, 0.5));
		kdtree.insert(new Point2D(0.5, 0.6));
		kdtree.draw();
		Point2D p = kdtree.nearest(new Point2D(0.39, 0.5));
		System.out.println(p.toString());
		System.out.println(kdtree.size());
		*/

		KdTree set = new KdTree();
		set.insert(new Point2D(0.375, 0.875));
		set.insert(new Point2D(0.4375, 0.625));
		System.out.println(set.nearest(new Point2D(0.25, 0.5625)).toString());
		/*
		// RectHV rect = new RectHV(0.4, 0.3, 0.5, 0.5);
		RectHV rect = new RectHV(0, 0 , 1, 1);
		System.out.println("Points included in Rectangle " + rect.toString() + " are:");
		for (Point2D point : kdtree.range(rect)) 
			System.out.println(point.toString());
			*/
	}
}