import java.util.Iterator;

public class Deque<Item> implements Iterable<Item>{
// public class Deque<Item> {
	// sentinel nodes
	private Node<Item> sentF; // at front
	private Node<Item> sentB; // at end
	private int numOfItems; // size 

	private class Node<Item> {
		public Item item;
		public Node<Item> prev;
		public Node<Item> next;

		// construction method;
		public Node (Item i, Node<Item> pr, Node<Item> nx) {
			item = i;
			prev = pr;
			next = nx;
		}
	}

	// construct an empty deque
	public Deque(){
		numOfItems = 0;
		sentF = new Node<Item>(null, null, null);
		sentB = new Node<Item>(null, null, null);
		sentF.next = sentB;
		sentB.prev = sentF;
	} 

	// is the deque empty
	public boolean isEmpty(){
		return numOfItems == 0;
	}

	// return the number of items on the deque
	public int size(){
		return numOfItems;
	}

	// add the item to the front
	public void addFirst(Item item){
		if (item == null) {
			throw new java.lang.NullPointerException();
		}

		numOfItems += 1;
		Node<Item> newNode = new Node<>(item, null, null);
		Node<Item> p = sentF.next;
		newNode.next = p;
		newNode.prev = sentF;
		sentF.next = newNode;
		p.prev = newNode;
	}

	// add the item to the end
	public void addLast(Item item){
		if (item == null) {
			throw new java.lang.NullPointerException();
		}
		numOfItems += 1;
		Node<Item> newNode = new Node<>(item, null, null);
		Node<Item> p = sentB.prev;
		newNode.next = sentB;
		newNode.prev = p;
		sentB.prev = newNode;
		p.next = newNode;
	}

	// remove and return the item from the front
	public Item removeFirst(){
		if (numOfItems == 0) {
			throw new java.util.NoSuchElementException();
		}

		Node<Item> p = sentF.next;
		sentF.next = p.next;
		sentF.next.prev = sentF;
		Item i = p.item;

		p.prev = null;
		p.next = null;
		p.item = null;

		numOfItems -= 1;
		return i;
	}

	// remove and return the item from the end
	public Item removeLast(){
		if (numOfItems == 0) {
			throw new java.util.NoSuchElementException();
		}
		Node<Item> p = sentB.prev;
		p.next.prev = p.prev;
		p.prev.next = p.next;
		Item i = p.item;

		p.prev = null;
		p.next = null;
		p.item = null;

		numOfItems -= 1;
		return i;
	}

	/*
	public void printDeque(){
		if (isEmpty()){
			System.out.println("This is an empty Deque.");
			return;
		}
		System.out.println("The Deque is: ");
		Node<Item> p = sentF.next;
		while (p.next != null) {
			System.out.print(p.item + " ");
			p = p.next;
		}
	}
	*/


	// return an iterator over items in order from front to end
	public Iterator<Item> iterator(){
		// dequeIterator is an object defined by the private
		// dequeIterator class
		return new dequeIterator();	
	 }

	// the private dequeIterator class
	private class dequeIterator implements Iterator<Item> {
		private Node<Item> current;
		
		public dequeIterator() {
			current = sentF.next;
		}
		
		public boolean hasNext(){
			return current.next != null; // if current is the sentB;
		}

		public Item next(){
			if (!hasNext()){
				throw new java.util.NoSuchElementException();
			}
			Item i = current.item;
			current = current.next;
			return i;
		}

		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}


	//unit testing (optional)
	public static void main(String[] args){

		Deque<Integer> dq1 = new Deque<>();
		System.out.println("the size of dq1 is: " + dq1.size());

		dq1.addFirst(3);
		dq1.addFirst(5);
		dq1.addLast(7);
		System.out.println("the size of dq1 is: " + dq1.size());
		// dq1.printDeque();
		System.out.println("\n");

		for (Integer i : dq1) {
			System.out.println(i);
			// StdOut.println(s)
		}
		

		/*
		int a = dq1.removeFirst();
		System.out.println(a);
		int b = dq1.removeFirst();
		System.out.println(b);
		System.out.println("the size of dq1 is: " + dq1.size());
		int c = dq1.removeLast();
		System.out.println(c);
		*/



	}
}