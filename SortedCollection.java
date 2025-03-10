/**
 * This interface defines an ADT for data structures that support storing a 
 * collection of comparable values in their natural ordering.
 */
public interface SortedCollection<T extends Comparable<T>> {

    /**
     * Inserts a new data value into the sorted collection.
     * @param data the new value being inserted
     * @throws NullPointerException if data argument is null, we do not allow
     * null values to be stored within a SortedCollection
     */
    public void insert(T data) throws NullPointerException;

    public boolean contains(Comparable<T> data);
    public int size();
    public boolean isEmpty();
    public void clear();
}

