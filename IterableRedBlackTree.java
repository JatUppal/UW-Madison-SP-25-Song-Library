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
    public void setIteratorMin(T min) {
        this.min = min;
    }

    /**
     * Allows setting the stop (maximum) value of the iterator. When this method is called,
     * every iterator created after it will use the maximum set by this method until this method
     * is called again to set a new maximum value.
     *
     * @param max the maximum for iterators created for this tree, or null for no maximum
     */
    public void setIteratorMax(T max) {
        this.max = max;
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
        return new RBTIterator<>(this.root, min, max);
    }

    /**
     * Nested class for Iterator objects created for this tree and returned by the iterator method.
     * This iterator follows an in-order traversal of the tree and returns the values in sorted,
     * ascending order.
     */
    protected static class RBTIterator<R extends Comparable<R>> implements Iterator<R> {

        // Stores the start point (minimum) for the iterator
        private final R min;
        // Stores the stop point (maximum) for the iterator
        private final R max;
        // Stack for tracking nodes during in-order traversal
        private final Stack<BinaryTreeNode<R>> stack;

        /**
         * Constructor for a new iterator of the tree.
         *
         * @param root the root node of the tree to traverse
         * @param min  the minimum value that the iterator will return
         * @param max  the maximum value that the iterator will return
         */
        public RBTIterator(BinaryTreeNode<R> root, R min, R max) {
            this.min = min;
            this.max = max;
            this.stack = new Stack<>();
            buildStackHelper(root);
        }

        /**
         * Helper method for initializing and updating the stack.
         * This method:
         * - Finds the next data value stored in the tree that is between min and max.
         * - Builds up the stack of ancestor nodes for future traversal.
         *
         * @param node the root node of the subtree to process
         */
        private void buildStackHelper(BinaryTreeNode<R> node) {
            if (node == null) return;
            
            if (min == null || node.data.compareTo(min) >= 0) {
                buildStackHelper(node.left);
                if (max == null || node.data.compareTo(max) <= 0) {
                    stack.push(node);
                }
            }
            buildStackHelper(node.right);
        }

        /**
         * Returns true if the iterator has another value to return, and false otherwise.
         *
         * @return true if there are more elements, false otherwise
         */
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * Returns the next value of the iterator.
         *
         * @return the next value in sorted order
         * @throws NoSuchElementException if the iterator has no more values to return
         */
        @Override
        public R next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the iterator");
            }

            BinaryTreeNode<R> node = stack.pop();
            buildStackHelper(node.right);
            return node.data;
        }
    }
}

