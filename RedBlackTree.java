import org.junit.jupiter.api.Test;                                                                                                          
import static org.junit.jupiter.api.Assertions.*;                               
import java.io.IOException;                                                     
import java.util.List; 

public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {


    /**
     * Repairs a red property violation in the Red-Black Tree caused by the given red node
     * having a red parent. Uses rotations (via {@code rotate} from {@code BSTRotation})
     * and recoloring as needed to restore Red-Black Tree properties.
     *
     * Ensures O(log N) runtime for {@code insert} and {@code contains} operations by
     * recursively handling violations caused by repairs, following the lecture algorithm.
     *
     * @param redNode The red node with a red parent causing the violation. Must not be null.
     * @throws IllegalArgumentException if the node is null.
     */
    protected void ensureRedProperty(RBTNode<T> newNode) {
        if (newNode == null) {
            throw new IllegalArgumentException("Node cannot be null.");
        }

        // Base case: Stop if the node is root or its parent is black
        if (newNode.parent() == null || !newNode.parent().isRed()) {
            return;
        }

        RBTNode<T> parent = newNode.parent();
        RBTNode<T> grandparent = parent.parent();

        // Get the uncle of the node
        RBTNode<T> uncle = (grandparent.childLeft() == parent)
                ? grandparent.childRight()
                : grandparent.childLeft();

        // Case 1: Uncle is red (recoloring)
        if (uncle != null && uncle.isRed()) {
            case1(newNode); // Call Case 1 logic
            ensureRedProperty(grandparent); // Recur on grandparent
        }
        // Case 2: Uncle is black and node forms a line
        else if ((!newNode.isRightChild() && parent == grandparent.childLeft())
                || (newNode.isRightChild() && parent == grandparent.childRight())) {
            case2(newNode); // Call Case 2 logic
        }
        // Case 3: Uncle is black and node forms a zigzag
        else {
            case3(newNode); // Call Case 3 logic
            //now we have a line, so we can call case2
            case2(parent); // Call Case 2 logic on parent
        }
    }

    // Helper methods for ensureRedProperty
    /**
     * Case 1: Red Sibling
     * @param newNode a newly inserted red node, or a node turned red by previous repair
     */
    private void case1(RBTNode<T> newNode) {
        // Flip nodes
        newNode.parent().parent().childLeft().flipColor();
        newNode.parent().parent().childRight().flipColor();
        newNode.parent().parent().flipColor();

    }

    /**
     * Case 2: Black Sibling & Line
     * @param newNode a newly inserted red node, or a node turned red by previous repair
     */
    private void case2(RBTNode<T> newNode) {
        RBTNode<T> grandparent = newNode.parent().parent();
        RBTNode<T> parent = newNode.parent();
        // Rotate
        rotate(parent, grandparent);

        // Flip nodes
        parent.flipColor();
        grandparent.flipColor();
    }

    /**
     * Case 3: Black Sibling & Zig
     * @param newNode a newly inserted red node, or a node turned red by previous repair
     */
    private void case3(RBTNode<T> newNode) {
        // Rotate
        rotate(newNode, newNode.parent());
    }

    /**
     * Inserts a new data value into the RedBlackTree. The new value is always
     * inserted as a red node, and the red-black properties are then enforced.
     * @param data the new value being inserted
     * @throws NullPointerException if data argument is null, we do not allow
     * null values to be stored within a RedBlackTree
     */
    @Override
    public void insert(T data) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException("Data cannot be null."); // Check for null input
        }
        RBTNode<T> newNode = new RBTNode<>(data); // Create new node (red by default)

        // If root is null, assign as root and make it black
        if (root == null) {
            this.root = newNode; // Assign as root
            ((RBTNode<T>)this.root).flipColor(); // Root is always black
        } else {
            // Otherwise, insert and ensure red property
            insertHelper(newNode, root);
            ensureRedProperty(newNode); // Ensure red property
            ((RBTNode<T>) this.root).isRed = false; // Ensure the root is black after all repairs
        }
    }

    // JUnit 5 Test cases

    /**
     * Tests the insertion of the letter H into a Red-Black Tree based on the provided quiz example.
     * Triggers case 3 and verifies that all Red-Black Tree properties are maintained and the level-order
     * traversal of the resulting tree is correct after insertion and repairs.
     */
    @Test
    public void testRBT1() {
        RedBlackTree<String> tree = new RedBlackTree<>();

        // Build the initial tree structure from the quiz example
        tree.insert("L"); // Root
        tree.insert("F");
        tree.insert("P");
        tree.insert("J");
        tree.insert("N");
        tree.insert("S");

        ((RBTNode<T>)tree.root.childRight()).flipColor(); // Flip the color of P to red
        ((RBTNode<T>)tree.root.childRight().childLeft()).flipColor();// Flip the color of N to black
        ((RBTNode<T>)tree.root.childRight().childRight()).flipColor();// Flip the color of S to black

        // Insert "H" into the tree
        tree.insert("H");

        // Verify the tree structure after insertion and repairs
        // Perform a level-order traversal and check the order
        String expectedLevelOrder = "[ L(b), H(b), P(r), F(r), J(r), N(b), S(b) ]";
        String actualLevelOrder = tree.root.toLevelOrderString();

        // Assert that the level-order traversal matches the expected structure
        assertEquals(expectedLevelOrder, actualLevelOrder);

        // Verify Red-Black Tree properties
        assertFalse(((RBTNode<String>) tree.root).isRed()); // Root must be black
        assertTrue(((RBTNode<String>) tree.root.childRight()).isRed()); // P is red
        assertFalse(((RBTNode<String>) tree.root.childLeft()).isRed()); // H is black
    }

    /**
     * Tests a Red-Black Tree insertion where the new node triggers Case 1 (Red Sibling Recoloring).
     * - Inserts nodes such that the newly inserted node's uncle is red.
     * - Ensures the `ensureRedProperty` method correctly recolors without performing rotations.
     */
    @Test
    public void testRBT2(){
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // Insert nodes to create a red sibling recoloring scenario
        tree.insert(10);
        tree.insert(5);
        tree.insert(15); // Causes Case 1 (Both children of root are red)
        tree.insert(1);

        // Expected level-order traversal after recoloring
        String expectedLevelOrder = "[ 10(b), 5(b), 15(b), 1(r) ]"; // Both children turn black, root stays black
        String actualLevelOrder = tree.root.toLevelOrderString();

        // Assertions
        assertEquals(expectedLevelOrder, actualLevelOrder);
        assertFalse(((RBTNode<Integer>) tree.root).isRed()); // Root must be black
        assertFalse(((RBTNode<Integer>) tree.root.childLeft()).isRed()); // Left child (5) is black
        assertFalse(((RBTNode<Integer>) tree.root.childRight()).isRed()); // Right child (15) is black
        assertTrue(((RBTNode<Integer>) tree.root.childLeft().childLeft()).isRed()); // Leftmost child (1) remains red
    }

    /**
     * Tests a Red-Black Tree insertion where the new node triggers Case 2 (Black Sibling & Line Rotation).
     * - Inserts nodes such that the newly inserted node's uncle is black, and the tree forms a *traight line.
     * - Ensures the `ensureRedProperty` method correctly applies a single rotation.
     */
    @Test
    public void testRBT3(){
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // Insert nodes to create a left-heavy unbalanced tree
        tree.insert(10); // Root (Black)
        tree.insert(5);  // Left child (Red)
        tree.insert(1);  // Left-Left case (Causes a right rotation at 10)

        // Expected level-order traversal after a right rotation
        String expectedLevelOrder = "[ 5(b), 1(r), 10(r) ]"; // 5 becomes root, 1 and 10 are red
        String actualLevelOrder = tree.root.toLevelOrderString();

        // Assertions
        assertEquals(expectedLevelOrder, actualLevelOrder);
        assertFalse(((RBTNode<Integer>) tree.root).isRed()); // Root must be black
        assertTrue(((RBTNode<Integer>) tree.root.childLeft()).isRed()); // Left child (1) is red
        assertTrue(((RBTNode<Integer>) tree.root.childRight()).isRed()); // Right child (10) is red
    }
}

