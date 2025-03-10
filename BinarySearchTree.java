public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {
	protected BinaryTreeNode<T> root;

	/**
	 * Inserts a new data value into the sorted collection.
	 * 
	 * @param data the new value being inserted
	 * @throws NullPointerException if data argument is null, we do not allow null
	 *                              values to be stored within a SortedCollection
	 */
	@Override
	public void insert(T data) throws NullPointerException {
		if (data == null) {
			throw new NullPointerException("Data cannot be null."); // Check for null input
		}
		BinaryTreeNode<T> newNode = new BinaryTreeNode<>(data); // Create new node
		if (root == null) {
			root = newNode; // Assign as root
		} else {
			insertHelper(newNode, root); // Use helper for recursion
		}
	}

	/**
	 * Performs the naive binary search tree insert algorithm to recursively insert
	 * the provided newNode (which has already been initialized with a data value)
	 * into the provided tree/subtree. When the provided subtree is null, this
	 * method does nothing.
	 */
	protected void insertHelper(BinaryTreeNode<T> newNode, BinaryTreeNode<T> subtree) {
		if (subtree == null) {
			return; // Base case reached
		}
		if (newNode.getData().compareTo(subtree.getData()) > 0) {
			if (subtree.childRight() != null) {
				insertHelper(newNode, subtree.childRight()); // Recur right subtree
				return;
			}
			subtree.setChildRight(newNode); // Set right child
			newNode.setParent(subtree); // Link parent
		} else {
			if (subtree.childLeft() != null) {
				insertHelper(newNode, subtree.childLeft()); // Recur left subtree
				return;
			}
			subtree.setChildLeft(newNode); // Set left child
			newNode.setParent(subtree); // Link parent
		}
	}

	/**
	 * Check whether data is stored in the tree.
	 * 
	 * @param data the value to check for in the collection
	 * @return true if the collection contains data one or more times, and false
	 *         otherwise
	 */
	@Override
	public boolean contains(Comparable<T> data) {
		if (data == null) {
			return false; // Null not allowed
		}
		return containsHelper(data, root); // Call helper method
	}

	private boolean containsHelper(Comparable<T> data, BinaryTreeNode<T> root2) {
		if (root2 == null) {
			return false; // Data not found
		}
		if (data.compareTo(root2.getData()) > 0) {
			return containsHelper(data, root2.childRight()); // Recur right subtree
		}
		if (data.compareTo(root2.getData()) < 0) {
			return containsHelper(data, root2.childLeft()); // Recur left subtree
		}
		return data.compareTo(root2.getData()) == 0; // Found matching data
	}

	/**
	 * Counts the number of values in the collection, with each duplicate value
	 * being counted separately within the value returned.
	 * 
	 * @return the number of values in the collection, including duplicates
	 */
	@Override
	public int size() {
		int size = 0;
		size = sizeHelper(root); // Start recursive count
		return size;
	}

	private int sizeHelper(BinaryTreeNode<T> tempRoot) {
		if (tempRoot == null) {
			return 0; // Leaf reached
		}
		return 1 + sizeHelper(tempRoot.childLeft()) + sizeHelper(tempRoot.childRight()); // Count nodes
	}

	/**
	 * Checks if the collection is empty.
	 * 
	 * @return true if the collection contains 0 values, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return root == null; // Root determines emptiness
	}

	/**
	 * Removes all values and duplicates from the collection.
	 */
	@Override
	public void clear() {
		root = null; // Remove root reference
	}

	/**
	 * Tester test1 method that focuses on testing BinarySearchTree insert method on
	 * inserting multiple values as both left and right children in different orders
	 * to create differently shaped trees.
	 * 
	 * @return true if insert method correctly inserts a node in the tree
	 */
	public boolean test1() {
		BinarySearchTree<Integer> tree = new BinarySearchTree<>();

		tree.insert(5); // Node 1
		tree.insert(10); // Node 2
		tree.insert(2); // Node 3
		tree.insert(15); // Node 4
		tree.insert(13); // Node 5
		tree.insert(8); // Node 6
		try {
			tree.insert(null); // Test null insertion
			return false;
		} catch (NullPointerException e) { // Expected exception
		} catch (Exception e) {
			return false; // Unexpected exception
		}

		BinaryTreeNode<Integer> root = tree.root;

		// Verify root
		if (root.getData() != 5)
			return false;

		// Verify left subtree
		if (root.childLeft().getData() != 2)
			return false;

		// Verify right subtree
		BinaryTreeNode<Integer> rightChild = root.childRight();

		if (rightChild.getData() != 10)
			return false;
		if (rightChild.childLeft().getData() != 8)
			return false;
		if (rightChild.childRight().getData() != 15)
			return false;
		if (rightChild.childRight().childLeft().getData() != 13)
			return false;

		return true; // All checks pass
	}

	/**
	 * Tester test2 method focuses on testing the contains method in the BST class.
	 * Finding values that are both left and right leaves as well as values stored
	 * in the interior of a tree (including at the root position).
	 * 
	 * @return true if contains method properly evaluates if the node is the BST
	 */
	public boolean test2() {
		BinarySearchTree<String> tree = new BinarySearchTree<>();
		tree.insert("Hellen"); // Root
		tree.insert("Jerry"); // Right subtree
		tree.insert("Jack"); // Left of Jerry
		tree.insert("Alexander"); // Leftmost
		tree.insert("Karen"); // Right of Jerry
		tree.insert("Richard"); // Rightmost
		tree.insert("Patrick"); // Left of Richard
		tree.insert("Stephen"); // Right of Patrick

		boolean subtest1 = tree.contains("Paul"); // Not in tree
		boolean subtest2 = tree.contains("Jack"); // Exists in tree
		boolean subtest3 = tree.contains("karen"); // Case mismatch
		boolean subtest4 = tree.contains("Hellen"); // Root check
		boolean subtest5 = tree.contains("Alexander"); // Leftmost check
		boolean subtest6 = tree.contains("Patrick"); // Nested node check
		boolean subtest7 = tree.contains("Karen"); // Proper case check

		return ((!subtest1) && subtest2 && (!subtest3) && subtest4 && subtest5 && subtest6 && subtest7); // Verify
																											// results
	}

	/**
	 * Tester test3 method focuses on ensuring that the size and clear methods are
	 * working through the building and clearing of a few different trees worth of
	 * data.
	 * 
	 * @return true if the trees are properly measured in size and cleared
	 */
	public boolean test3() {
		BinarySearchTree<Double> treeDB = new BinarySearchTree<>();
		treeDB.insert(2.3); // Insert value
		treeDB.insert(3.1); // Insert value
		treeDB.insert(4.5); // Insert value

		if (treeDB.size() != 3)
			return false;
		treeDB.insert(1.39); // Insert value
		treeDB.insert(200.3); // Insert value

		if (treeDB.size() != 5)
			return false;

		treeDB.clear(); // Clear tree
		if (treeDB.size() != 0)
			return false;

		BinarySearchTree<String> treeST = new BinarySearchTree<>();
		treeST.insert("Basketball"); // Insert string
		treeST.insert("Baseball"); // Insert string
		treeST.insert("Volleyball"); // Insert string
		treeST.clear(); // Clear tree
		treeST.insert("Soccer"); // Insert new string
		treeST.insert("Swimming"); // Insert new string

		if (treeST.size() != 2)
			return false;

		return true; // All tests pass
	}

	public static void main(String[] args) {
		BinarySearchTree<String> bst = new BinarySearchTree<>();
		System.out.println("Test 1: " + (bst.test1() ? "PASS" : "FAIL"));
		System.out.println("Test 2: " + (bst.test2() ? "PASS" : "FAIL"));
		System.out.println("Test 3: " + (bst.test3() ? "PASS" : "FAIL"));
	}
}
