public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {

	/**
	 * Performs the rotation operation on the provided nodes within this tree. When
	 * the provided child is a left child of the provided parent, this method will
	 * perform a right rotation. When the provided child is a right child of the
	 * provided parent, this method will perform a left rotation. When the provided
	 * nodes are not related in one of these ways, this method will either throw a
	 * NullPointerException: when either reference is null, or otherwise will throw
	 * an IllegalArgumentException.
	 *
	 * @param child  is the node being rotated from child to parent position
	 * @param parent is the node being rotated from parent to child position
	 * @throws NullPointerException     when either passed argument is null
	 * @throws IllegalArgumentException when the provided child and parent nodes are
	 *                                  not initially (pre-rotation) related that
	 *                                  way
	 */
	protected void rotate(BinaryTreeNode<T> child, BinaryTreeNode<T> parent)
			throws NullPointerException, IllegalArgumentException {
		if (child == null || parent == null) {
			throw new NullPointerException(); // Ensure valid input
		} else {
			if (parent.left == child) {
				rightRotation(child, parent); // Perform right rotation
			} else if (parent.right == child) {
				leftRotation(child, parent); // Perform left rotation
			} else {// Invalid relationship
				throw new IllegalArgumentException("Child is not a direct child of parent.");
			}
		}
	}

	/**
	 * Private helper method, leftRotation, performs a left rotation on the BST
	 * 
	 * @param child  is the node being rotated from child to parent position
	 * @param parent is the node being rotated from parent to child position
	 */
	private void leftRotation(BinaryTreeNode<T> child, BinaryTreeNode<T> parent) {
		BinaryTreeNode<T> temp = child.left;
		if (parent.up == null) {
			root = child;
			child.up = null;
		} else {
			// Update parent's parent reference if needed
			if (parent.up.left == parent) { // check if parent is on left or right
				parent.up.setChildLeft(child);
			} else {
				parent.up.setChildRight(child);
			}
			child.setParent(parent.up); // set new parent for child
		}
		parent.setChildRight(null);
		if (temp != null) {
			temp.setParent(parent);
			parent.setChildRight(temp);
		}
		child.setChildLeft(parent);
		parent.up = child; // Update parent reference
	}

	/**
	 * Private helper method, rightRotation, performs a right rotation on the BST
	 * 
	 * @param child  is the node being rotated from child to parent position
	 * @param parent is the node being rotated from parent to child position
	 */
	private void rightRotation(BinaryTreeNode<T> child, BinaryTreeNode<T> parent) {
		BinaryTreeNode<T> temp = child.right;
		if (parent.up == null) {
			root = child;
			child.up = null;
		} else {
			// Update parent's parent reference if parent is not root
			if (parent.up.right == parent) { // check if parent is on left or right
				parent.up.setChildRight(child);
			} else {
				parent.up.setChildLeft(child);
			}
			child.setParent(parent.up); // set new parent for child
		}
		parent.setChildLeft(null); // Disconnect old left child
		if (temp != null) {
			temp.setParent(parent);
			parent.setChildLeft(temp); // Attach temp as new left child
		}
		child.setChildRight(parent);
		parent.up = child; // Update parent reference
	}

	/**
	 * Tester, test1, method tests the behavior of the rotate function to ensure it
	 * is correctly performing both left and right rotations.
	 * 
	 * @return true if correctly performed both left and right rotations
	 */
	public boolean test1() {
		BSTRotation<Integer> bstR = new BSTRotation<>();

		BinaryTreeNode<Integer> node1 = new BinaryTreeNode<>(5); // Node 1
		BinaryTreeNode<Integer> node2 = new BinaryTreeNode<>(10); // Node 2
		BinaryTreeNode<Integer> node3 = new BinaryTreeNode<>(2); // Node 3
		BinaryTreeNode<Integer> node4 = new BinaryTreeNode<>(15); // Node 4
		BinaryTreeNode<Integer> node5 = new BinaryTreeNode<>(13); // Node 5
		BinaryTreeNode<Integer> node6 = new BinaryTreeNode<>(8); // Node 6

		// Manually link nodes to form the BST
		bstR.root = node1;
		node1.setChildRight(node2);
		node1.setChildLeft(node3);
		node2.setChildRight(node4);
		node2.setChildLeft(node6);
		node4.setChildLeft(node5);

		node3.setParent(node1);
		node2.setParent(node1);
		node6.setParent(node2);
		node4.setParent(node2);
		node5.setParent(node4);

		// Test left rotation with right child and parent node
		bstR.rotate(node4, node2);
		// rotate(node 4, node 2)

		// Verify root
		if ((int) bstR.root.getData() != 5)
			return false;
		// Verify node 2
		if ((int) bstR.root.childLeft().getData() != 2)
			return false;
		// Verify node 3
		if ((int) bstR.root.childRight().getData() != 15)
			return false;
		// Verify node 4
		if ((int) bstR.root.childRight().childLeft().getData() != 10)
			return false;
		// Verify node 5
		if ((int) bstR.root.childRight().childLeft().childRight().getData() != 13)
			return false;
		// Verify node 6
		if ((int) bstR.root.childRight().childLeft().childLeft().getData() != 8)
			return false;

		// Test right rotation with left child and parent node
		bstR.rotate(bstR.root.childLeft(), bstR.root);
		// rotate(node 3, node 1)

		// Verify root
		if ((int) bstR.root.getData() != 2)
			return false;
		// Verify node 2
		if ((int) bstR.root.childRight().getData() != 5)
			return false;
		// Verify node 3
		if ((int) bstR.root.childRight().childRight().getData() != 15)
			return false;
		// Verify node 4
		if ((int) bstR.root.childRight().childRight().childLeft().getData() != 10)
			return false;
		// Verify node 5
		if ((int) bstR.root.childRight().childRight().childLeft().childLeft().getData() != 8)
			return false;
		// Verify node 6
		if ((int) bstR.root.childRight().childRight().childLeft().childRight().getData() != 13)
			return false;

		return true;
	}

	/**
	 * Tester, test2, method tests the behavior of the rotate function to ensure it
	 * is correctly performing rotations that include the root node, and some that
	 * do not.
	 * 
	 * @return true if correctly rotations that include the root node, and some that
	 *         do not.
	 */
	public boolean test2() {
		BSTRotation<String> bstR = new BSTRotation<>();

		BinaryTreeNode<String> node1 = new BinaryTreeNode<>("Paul");
		BinaryTreeNode<String> node2 = new BinaryTreeNode<>("Jack");
		BinaryTreeNode<String> node3 = new BinaryTreeNode<>("Rocket");
		BinaryTreeNode<String> node4 = new BinaryTreeNode<>("Diaper");
		BinaryTreeNode<String> node5 = new BinaryTreeNode<>("Super");
		BinaryTreeNode<String> node6 = new BinaryTreeNode<>("Crazy");

		// Manually link nodes to form the BST
		bstR.root = node1;
		node1.setChildLeft(node2);
		node1.setChildRight(node3);
		node2.setChildLeft(node4);
		node4.setChildLeft(node6);
		node3.setChildRight(node5);

		node2.setParent(node1);
		node3.setParent(node1);
		node4.setParent(node2);
		node6.setParent(node4);
		node5.setParent(node3);

		// First Rotation: Left Rotation on Root
		bstR.rotate(node3, node1);

		// Verify tree structure after first rotation
		if (!bstR.root.getData().equals("Rocket"))
			return false;
		if (!bstR.root.childLeft().getData().equals("Paul"))
			return false;
		if (!bstR.root.childRight().getData().equals("Super"))
			return false;
		if (!bstR.root.childLeft().childLeft().getData().equals("Jack"))
			return false;
		if (!bstR.root.childLeft().childLeft().childLeft().getData().equals("Diaper"))
			return false;
		if (!bstR.root.childLeft().childLeft().childLeft().childLeft().getData().equals("Crazy"))
			return false;

		// Second Rotation: Right Rotation at the Bottom of the Tree
		bstR.rotate(bstR.root.childLeft().childLeft().childLeft().childLeft(),
				bstR.root.childLeft().childLeft().childLeft());

		// Verify tree structure after second rotation
		if (!bstR.root.getData().equals("Rocket"))
			return false;
		if (!bstR.root.childLeft().getData().equals("Paul"))
			return false;
		if (!bstR.root.childRight().getData().equals("Super"))
			return false;
		if (!bstR.root.childLeft().childLeft().getData().equals("Jack"))
			return false;
		if (!bstR.root.childLeft().childLeft().childLeft().getData().equals("Crazy"))
			return false;
		if (!bstR.root.childLeft().childLeft().childLeft().childRight().getData().equals("Diaper"))
			return false;

		return true;

	}

	/**
	 * Tester, test3, method tests the behavior of the rotate function to ensure it
	 * is correctly performing rotations on parent-child pairs of nodes that have
	 * between them 0, 1, 2, and 3 shared children (that do not include the child
	 * being rotated).
	 * 
	 * @return true if correctly rotations on parent-child pairs of nodes that have
	 *         between them 0, 1, 2, and 3 shared children
	 */
	public boolean test3() {
		BSTRotation<Double> bstR = new BSTRotation<>();
		
		BinaryTreeNode<Double> node1 = new BinaryTreeNode<>(10.5);
		BinaryTreeNode<Double> node2 = new BinaryTreeNode<>(5.5);
		BinaryTreeNode<Double> node3 = new BinaryTreeNode<>(15.5);
		BinaryTreeNode<Double> node4 = new BinaryTreeNode<>(3.3);
		BinaryTreeNode<Double> node5 = new BinaryTreeNode<>(7.7);
		BinaryTreeNode<Double> node6 = new BinaryTreeNode<>(13.3);
		BinaryTreeNode<Double> node7 = new BinaryTreeNode<>(17.7);
		BinaryTreeNode<Double> node8 = new BinaryTreeNode<>(6.6);
		BinaryTreeNode<Double> node9 = new BinaryTreeNode<>(9.9);

		// Manually link nodes to form the BST
		bstR.root = node1;
		node1.setChildLeft(node2);
		node1.setChildRight(node3);
		node2.setChildLeft(node4);
		node2.setChildRight(node5);
		node3.setChildLeft(node6);
		node3.setChildRight(node7);
		node5.setChildLeft(node8);
		node5.setChildRight(node9);

		node2.setParent(node1);
		node3.setParent(node1);
		node4.setParent(node2);
		node5.setParent(node2);
		node6.setParent(node3);
		node7.setParent(node3);
		node8.setParent(node5);
		node9.setParent(node5);

		// First Rotation: (node 3, node 1)
		bstR.rotate(bstR.root.childRight(), bstR.root);

		// Verify tree structure after first rotation
		if (bstR.root.getData() != 15.5)
			return false;
		if (bstR.root.childLeft().getData() != 10.5)
			return false;
		if (bstR.root.childRight().getData() != 17.7)
			return false;
		if (bstR.root.childLeft().childLeft().getData() != 5.5)
			return false;
		if (bstR.root.childLeft().childRight().getData() != 13.3)
			return false;
		if (bstR.root.childLeft().childLeft().childLeft().getData() != 3.3)
			return false;
		if (bstR.root.childLeft().childLeft().childRight().getData() != 7.7)
			return false;
		if (bstR.root.childLeft().childLeft().childRight().childLeft().getData() != 6.6)
			return false;
		if (bstR.root.childLeft().childLeft().childRight().childRight().getData() != 9.9)
			return false;

		// Second Rotation: (node 5, node 2)
		bstR.rotate(bstR.root.childLeft().childLeft().childRight(), bstR.root.childLeft().childLeft());

		// Verify tree structure after second rotation
		if (bstR.root.getData() != 15.5)
			return false;
		if (bstR.root.childLeft().getData() != 10.5)
			return false;
		if (bstR.root.childRight().getData() != 17.7)
			return false;
		if (bstR.root.childLeft().childLeft().getData() != 7.7)
			return false;
		if (bstR.root.childLeft().childRight().getData() != 13.3)
			return false;
		if (bstR.root.childLeft().childLeft().childLeft().getData() != 5.5)
			return false;
		if (bstR.root.childLeft().childLeft().childRight().getData() != 9.9)
			return false;
		if (bstR.root.childLeft().childLeft().childLeft().childLeft().getData() != 3.3)
			return false;
		if (bstR.root.childLeft().childLeft().childLeft().childRight().getData() != 6.6)
			return false;

		return true;
	}

	public static void main(String args[]) {
		BSTRotation<Integer> bstR = new BSTRotation<>();
		System.out.println("Test 1: " + (bstR.test1() ? "PASS" : "FAIL"));
		System.out.println("Test 2: " + (bstR.test2() ? "PASS" : "FAIL"));
		System.out.println("Test 3: " + (bstR.test3() ? "PASS" : "FAIL"));
	}

}
