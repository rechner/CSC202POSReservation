package reservation;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularLinkedQueue<T> implements CircularQueueInterface<T>, Iterable<T> {
	
	private int count;
	private Node<T> first;
	private Node<T> last;
	
	@SuppressWarnings("hiding")
	class Node<T> {
		private T item;
		private Node<T> next;
	}
	
	public CircularLinkedQueue() {
		first = null;
		last = null;
		count = 0;
	}

	public T dequeue() throws QueueUnderflowException {
		
		return null;
	}

	public void enqueue(T element) {
		Node<T> oldlast = last;                                             
        last = new Node<T>();                                               
        last.item = element;                                                      
        last.next = null;                                                      
        if (isEmpty()) {
        	first = last;                                           
        } else {
        	oldlast.next = last;                                    
        }                                                  
		this.count++;
	}

	public boolean isEmpty() {
		return (first == null);
	}
	
	public int size() {
		return count;
	}
	
	public Iterator<T> iterator()  {
        return new ListIterator<T>(null);  
    }
	
	// an iterator, doesn't implement remove() since it's optional
    @SuppressWarnings("hiding")
	private class ListIterator<T> implements Iterator<T> {
        private Node<T> current;

        public ListIterator(Node<T> first) {
            current = first;
        }

        public boolean hasNext()  { 
        	return current != null;                     
        }
        
        public void remove() { 
        	throw new UnsupportedOperationException();  
        }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T item = current.item;
            current = current.next; 
            return item;
        }
    }

	public void remove(String id) {
		
		
	}



}
