package reservation;

public interface CircularQueueInterface<T> {
	
	public void enqueue(T element);
	public T dequeue() throws QueueUnderflowException;
	public boolean isEmpty();

}
