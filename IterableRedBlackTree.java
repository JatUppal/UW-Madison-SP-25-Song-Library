import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * This class extends RedBlackTree into a tree that supports iterating over the values it
 * stores in sorted, ascending order.
 *
 * @param <T> The type of data stored in the tree, must be comparable
 */
public class IterableRedBlackTree<T extends Comparable<T>>
        extends RedBlackTree<T> implements IterableSortedCollection<T> {

    // Fields to store the minimum and maximum values for the iterator
    private T min = null;
    private T max = null;

    /**
     * Allows setting the start (minimum) value of the iterator. When this method is called,
     * every iterator created after it will use the minimum set by this method until this method
     * is called again to set a new minimum value.
     *
     * @param min the minimum for iterators created for this tree, or null for no minimum
     */
    @Override
    public void setIteratorMin(Comparable<T> min) {
        this.min = (T) min;  // Explicit cast to T
    }

    /**
     * Allows setting the stop (maximum) value of the iterator. When this method is called,
     * every iterator created after it will use the maximum set by this method until this method
     * is called again to set a new maximum value.
     *
     * @param max the maximum for iterators created for this tree, or null for no maximum
     */
    @Override
    public void setIteratorMax(Comparable<T> max) {
        this.max = (T) max;  // Explicit cast to T
    }

    /**
     * Returns an iterator over the values stored in this tree. The iterator uses the
     * start (minimum) value set by a previous call to setIteratorMin, and the stop (maximum)
     * value set by a previous call to setIteratorMax.
     *
     * If setIteratorMin has not been called before, or if it was called with a null argument,
     * the iterator uses no minimum value and starts with the lowest value in the tree.
     * If setIteratorMax has not been called before, or if it was called with a null argument,
     * the iterator uses no maximum value and finishes with the highest value in the tree.
     */
    @Override
    public Iterator<T> iterator() {
        return new RBTIterator<>((RBTNode<T>) this.root, min, max);
    }


    /**
     * Nested class for Iterator objects created for this tree and returned by the iterator method.
     * This iterator follows an in-order traversal of the tree and returns the values in sorted,
     * ascending order.
     */
    protected static class RBTIterator<R extends Comparable<R>> implements Iterator<R> {

        private final R min; // Minimum boundary
        private final R max; // Maximum boundary
        private final Stack<RBTNode<R>> stack = new Stack<>();

        /**
         * Constructor for a new iterator of the tree.
         *
         * @param root the root node of the tree to traverse
         * @param min  the minimum value that the iterator will return
         * @param max  the maximum value that the iterator will return
         */
        public RBTIterator(RBTNode<R> root, R min, R max) {
            this.min = min;
            this.max = max;
            buildStackHelper(root);
        }

        /**
         * Helper method to initialize the stack with an in-order traversal.
         * Pushes leftmost nodes onto the stack first.
         *
         * @param node the root of the tree or subtree being processed
         */
        private void buildStackHelper(RBTNode<R> node) {
            while (node != null) {
                if (min == null || node.getData().compareTo(min) >= 0) {
                    if (max == null || node.getData().compareTo(max) <= 0) {
                        stack.push(node); // Push only if within range
                    }
                    node = node.childLeft(); // Navigate left
                } else {
                    node = node.childRight(); // Skip left if below min
                }
            }
        }

        /**
         * Checks if there are more values to return from the iterator.
         *
         * @return true if there are more values, false otherwise
         */
        @Override
        public boolean hasNext() {
            while (!stack.isEmpty() && max != null && stack.peek().getData().compareTo(max) > 0) {
                stack.pop(); // Remove values that exceed max
            }
            return !stack.isEmpty();
        }


        /**
         * Returns the next value of the iterator.
         *
         * @return the next value in sorted order
         * @throws NoSuchElementException if there are no more values
         */
        @Override
        public R next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the iterator");
            }

            RBTNode<R> node = stack.pop();
            R nextValue = node.getData();

            // Push right subtree nodes (leftmost path) onto the stack
            if (node.childRight() != null) {
                buildStackHelper(node.childRight());
            }

            // Ensure returned value is within max boundary
            if (max != null && nextValue.compareTo(max) > 0) {
                throw new NoSuchElementException("Exceeded max limit of iterator");
            }

            return nextValue;
        }
    }

    // Test Cases

    /**
     * Test 1: Iterates over an integer tree with duplicates.
     * - Inserts integers (including duplicates).
     * - Ensures iteration returns values in sorted order.
     */
    @Test
    public void testIntegerIterationWithDuplicates() {
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();

        // Insert values (with duplicates)
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(5);  // Duplicate
        tree.insert(10); // Duplicate
        tree.insert(20);

        // Create iterator with no min/max constraints
        Iterator<Integer> iterator = tree.iterator();

        // Expected sorted order: [5, 5, 10, 10, 15, 20]
        int[] expectedOrder = {5, 5, 10, 10, 15, 20};
        int index = 0;

        while (iterator.hasNext()) {
            assertEquals(expectedOrder[index++], iterator.next());
        }
    }

    /**
     * Test 2: Iterates over a string tree with no duplicates.
     * - Inserts words into the tree.
     * - Ensures iteration returns values in lexicographical order.
     */
    @Test
    public void testStringIteration() {
        IterableRedBlackTree<String> tree = new IterableRedBlackTree<>();

        // Insert words (no duplicates)
        tree.insert("banana");
        tree.insert("apple");
        tree.insert("cherry");
        tree.insert("date");
        tree.insert("elderberry");

        // Create iterator with no min/max constraints
        Iterator<String> iterator = tree.iterator();

        // Expected sorted order: ["apple", "banana", "cherry", "date", "elderberry"]
        String[] expectedOrder = {"apple", "banana", "cherry", "date", "elderberry"};
        int index = 0;

        while (iterator.hasNext()) {
            assertEquals(expectedOrder[index++], iterator.next());
        }
    }

    /**
     * Test 3: Iterates over an integer tree with both min and max bounds.
     * - Inserts numbers into the tree.
     * - Sets a min/max range to limit values returned by the iterator.
     */
    @Test
    public void testIterationWithMinMax() {
        IterableRedBlackTree<Integer> tree = new IterableRedBlackTree<>();

        // Insert values
        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(2);
        tree.insert(7);
        tree.insert(12);
        tree.insert(20);

        // Set min and max values
        tree.setIteratorMin(5);
        tree.setIteratorMax(15);

        // Create iterator with min/max constraints
        Iterator<Integer> iterator = tree.iterator();

        // Expected sorted order within range [5, 15]: [5, 7, 10, 12, 15]
        int[] expectedOrder = {5, 7, 10, 12, 15};
        int index = 0;

        while (iterator.hasNext()) {
            assertEquals(expectedOrder[index++], iterator.next());
        }
    }

}

