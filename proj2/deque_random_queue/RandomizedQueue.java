import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
// public class RandomizedQueue<Item> {
	private Item[] items;
	private int numOfItems;
	private int queueFront;
	private int queueTail; // insert items at the tail
	private double R; // usage ratio
	private int RFACTOR = 2;

	//////////////////////////////////////////////////
	////////////////// Helper methods/////////////////
	//////////////////////////////////////////////////

	private void updateUsageRatio() {
		R = (double) numOfItems / (double) items.length;
	}

	private boolean isQueueFull() {
		return items.length == numOfItems;
	}

	private boolean isLowUsage() {
		return (items.length >= 4) && (R < 0.25);
	}

	// resize the queue if necessary.
	private void updateQueueSize(){
		if (isQueueFull()) {
			// double the 
			Item[] newArray = (Item[]) new Object[RFACTOR*items.length];
			for (int i = 0; i < numOfItems; i += 1){
				newArray[i] = items[i];
			}
			items = newArray;
			// previously queueTail = queueFront = 0 for a full queue;
			queueTail = numOfItems;
			updateUsageRatio();
		} else if (isLowUsage()) {
			Item[] newArray = (Item[]) new Object[items.length / RFACTOR];
			for (int i = 0; i < numOfItems; i += 1){
				newArray[i] = items[i];
			}
			items = newArray;
			updateUsageRatio();
		}
		// else do nothing 
	}

	private int plusOne(int n) {
		/*
		if (n < 0 || n >= items.length) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		*/
		return (n+1) % items.length;  
	}

	private int minusOne(int n) {
		/*
		if (n < 0 || n >= items.length) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		*/
		int r = (n-1) % items.length;
		if (r < 0) r += items.length;
		return r;
	}


	//////////////////////////////////////////////////
	////////////////// Public methods/////////////////
	//////////////////////////////////////////////////

	// construct an empty randomized queue;
	public RandomizedQueue(){
		items = (Item[]) new Object[2];
		numOfItems = 0 ;
		queueFront = 0;
		queueTail = 0;
		updateUsageRatio(); 
	}

	// is the queue empty? 
	public boolean isEmpty(){
		return numOfItems == 0;
	}

	// return the number of items on the queue
	public int size(){
		return numOfItems;
	}

	// add the item
	public void enqueue (Item item){
		if (item == null) {
			throw new java.lang.NullPointerException();
		}
		updateQueueSize(); // resize queue if necessary;
		items[queueTail] = item;
		queueTail = plusOne(queueTail);
		numOfItems += 1;
		updateUsageRatio();
	}

	// remove the return a random item
	public Item dequeue(){
		if (isEmpty()) {
			throw new java.util.NoSuchElementException();
		}
		// choose a random item
		int randomNumber = StdRandom.uniform(numOfItems);
		Item i = items[randomNumber];
		if (randomNumber != minusOne(queueTail)) {
			/*
			System.out.println("numOfItems is: " + numOfItems);
			System.out.println("randomNumber is: " + randomNumber);
			System.out.println("size of array is: " + items.length);
			System.out.println("queueuTail is: " + queueTail);
			System.out.println("minusOne(queueTail) is: " + minusOne(queueTail));
			*/
			items[randomNumber] = items[minusOne(queueTail)];
		}
		items[minusOne(queueTail)] = null;
		queueTail = minusOne(queueTail);
		numOfItems -= 1;
		updateQueueSize();
		updateUsageRatio();
		return i;

	}

	// return (but do not remove a random item)
	public Item sample(){
		if (isEmpty()) {
			throw new java.util.NoSuchElementException();
		}
		return items[StdRandom.uniform(numOfItems)];
	}

	// return an indepedent iterator over items in random order
	public Iterator<Item> iterator() {
		return new rqIterator();
	}

	private class rqIterator implements Iterator<Item> {
		int[] randomSequence;
		int i; 

		// constructor of rqIterator
		public rqIterator() {
			randomSequence = StdRandom.permutation(numOfItems);
			i = 0;
		}

		public boolean hasNext(){
			return i < numOfItems;
		}

		public Item next() {
			if (!hasNext()) {
				throw new java.util.NoSuchElementException();
			}
			Item theItem = items[randomSequence[i]];
			i += 1;
			return theItem;
		} 

		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	} // end rqIterator

	/*
	public void displayQueue() {
		for (int i = 0; i < numOfItems; i += 1) {
			System.out.println(items[i]);
		}
	}
	*/

	// unit testing (optional)
	public static void main(String[] args){
		RandomizedQueue<Integer> rq = new RandomizedQueue<>();
		/*
		for (int i = 0; i < 20; i += 1) {
			rq.enqueue(i);
		}
		// rq.displayQueue();
		
		System.out.println("random queue is: ");
		for (Integer i : rq){
			System.out.println(i);
		}

		for (int i = 0; i< 20; i += 1) {
			System.out.println("Dequeue: " + rq.dequeue());
		}

		System.out.println("The left-over queue is: ");
		// rq.displayQueue();
		System.out.println(rq.size());
		rq.dequeue();
		*/ 

		 rq.enqueue(9);
         rq.enqueue(220);
         rq.isEmpty();    
         rq.enqueue(380);
         rq.enqueue(470);
         rq.enqueue(300);
         rq.enqueue(60);
         rq.dequeue();    
         rq.enqueue(335);
         rq.enqueue(418);
         rq.enqueue(472);
         rq.dequeue();

			
	}
}