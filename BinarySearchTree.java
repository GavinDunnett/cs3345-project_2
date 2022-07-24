import java.util.*;

import javax.naming.OperationNotSupportedException;

// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x
// boolean contains( x )  --> Return true if x is present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate
//
// Implements an unbalanced binary search tree.
//  * Note that all "matching" is based on the compareTo method.
//  * @author of the aforementioned operations: Mark Allen Weiss
//
// ******************************ADDITIONAL METHODS****************************
// int nodeCount()  Returns the count of nodes. 
// boolean isFull()  Returns true if the tree is full binary tree.
// boolean compareStructure(x)  Returns true if the tree structure matches x.
// boolean equals(x)  Returns true if the tree is identical x.
// BinarySearchTree copy()  Creates a new copy of the tree.
// BinarySearchTree mirror(x)  Creates and returns a mirror of the original tree.
// boolean isMirror  Returns true if the tree is a mirror of the passed tree.
// rotateRight(x)  Performs a single rotation on the node with value of x.
// rotateLeft(x)  As above but left.
// printLevels()  Performs a level-by-level printing of the tree.
// ******************ERRORS****************************************************
// Throws NoSuchElementException
//
// 20FA CS3345 Data Structure & Introduction Alogrithmic Analysis.
// Project #2
// @author Gavin John Dunnett
public class BinarySearchTree<AnyType extends Comparable<? super AnyType>> {
    /**
     * Construct the tree.
     */
    public BinarySearchTree() {
        root = null;
    }

    /**
     * Count the number of nodes.
     */
    public int nodeCount() {
        return nodeCount(root);
    }

    /**
     * Internal method to count the number of nodes
     */
    private int nodeCount(BinaryNode<AnyType> r) {
        if (r == null)
            return 0;
        else
            return 1 + nodeCount(r.left) + nodeCount(r.right);
    }

    /**
     * Returns true if the tree is full.
     */
    public boolean isFull() {
        if (isEmpty())
            throw new UnderflowException();
        return isFull(root);
    }

    /**
     * Internal method to determine if the tree is full.
     */
    private boolean isFull(BinaryNode<AnyType> r) {
        if (r == null) // test first since subsequent methods need t!=null
            return true;
        if (r.left == null && r.right != null)
            return false; // not full, end calls
        if (r.left != null && r.right == null)
            return false; // not full, end calls
        return isFull(r.left) && isFull(r.right); // keep checking
    }

    /**
     * Determines if this tree's structure matches a specified tree's structure.
     */
    public boolean compareStructure(BinarySearchTree<AnyType> t) {
        return compareStructure(root, t.root);
    }

    /**
     * Internal method to determine if this tree's structure matches another tree's.
     */
    private boolean compareStructure(BinaryNode<AnyType> r1, BinaryNode<AnyType> r2) {
        if (r1 == null && r2 == null) // test first since subsequent methods need both != null
            return true;
        if (r1 == null && r2 != null)
            return false;
        if (r1 != null && r2 == null)
            return false;
        return compareStructure(r1.left, r2.left) && compareStructure(r1.right, r2.right);
    }

    /**
     * Compares the current tree to another tree and returns true if they are identical.
     */
    public boolean equals(BinarySearchTree<AnyType> x) {
        return equals(x.root, root);
    }

    /**
     * Internal method that compares this tree to another and returns true if they are identical.
     */
    private boolean equals(BinaryNode<AnyType> r1, BinaryNode<AnyType> r2) {
        if (r1 == null && r2 == null) // test first since subsequent methods need both != null
            return true;
        if (r1 != null && r2 != null)
            return r1.element.equals(r2.element) && equals(r1.left, r2.left) && equals(r1.right, r2.right);
        return false;
    }

    /**
     * Creates and returns a new tree that is a copy of the original tree.
     */
    public BinarySearchTree<AnyType> copy() {
        return copy(root, new BinarySearchTree<AnyType>());
    }

    /**
     * Internal method to create a new tree that is a copy of the original tree.
     */
    private BinarySearchTree<AnyType> copy(BinaryNode<AnyType> r, BinarySearchTree<AnyType> x) {
        if (r == null) // test first since subsequent methods need r != null
            return x;
        x.insert(r.element);
        if (r.left != null)
            copy(r.left, x);
        if (r.right != null)
            copy(r.right, x);
        return x;
    }

    /**
     * Creates and returns a new tree that is a mirror image of this tree.
     */
    public BinarySearchTree<AnyType> mirror() {
        BinarySearchTree<AnyType> mirror = copy();
        mirror.interchangeChildren(mirror.root);
        return mirror;
    }

    /**
     * Internal method that recursively interchanges every nodes left and right children.
     */
    private void interchangeChildren(BinaryNode<AnyType> r) {
        if (r == null)
            return;
        if (r.left != null)
            interchangeChildren(r.left);
        if (root.right != null)
            interchangeChildren(r.right);
        BinaryNode<AnyType> lt = r.left;
        BinaryNode<AnyType> rt = r.right;
        r.right = lt;
        r.left = rt;
    }

    /**
     * Returns true if the tree is a mirror of the specified tree.
     */
    public boolean isMirror(BinarySearchTree<AnyType> x) {
        BinarySearchTree<AnyType> mirrorOfThis = mirror();
        return mirrorOfThis.compareStructure(x) && mirrorOfThis.equals(x);
    }

    /**
     * Perform a single right rotation on the node with value of x.
     * @throws OperationNotSupportedException
     */
    public void rotateRight(AnyType element) throws OperationNotSupportedException {
        if (isEmpty())
            throw new UnderflowException();
        BinaryNode<AnyType> parent = findParent(root, element);
        if (parent == root && root.left == null)
            throw new OperationNotSupportedException("Only tree roots with left children can be rotated right.");
        BinaryNode<AnyType> grandparent = findParent(root, parent.element);
        BinaryNode<AnyType> pivot = parent.left;
        BinaryNode<AnyType> rightchild = pivot.right;
        if (parent.left != pivot)
            throw new OperationNotSupportedException("Only left children can be rotated right.");
        if (parent == root) { // special case: pivot will become the tree's new root
            if (root.left == null) // root has no left child, nothing changes on this roate
                return;
            root.left = pivot.right;
            pivot.right = root;
            root = pivot;
            return;
        }
        parent.left = rightchild;
        pivot.right = parent;
        if (parent == grandparent.left)
            grandparent.left = pivot;
        if (parent == grandparent.right)
            grandparent.right = pivot;
    }

    /**
     * Perform a single left rotation on the node with value of x.
     * @throws OperationNotSupportedException
     */
    public void rotateLeft(AnyType element) throws OperationNotSupportedException {
        if (isEmpty())
            throw new UnderflowException();
        BinaryNode<AnyType> parent = findParent(root, element);
        if (parent == root && root.right == null)
            throw new OperationNotSupportedException("Only tree roots with right children can be rotated left.");
        BinaryNode<AnyType> grandparent = findParent(root, parent.element);
        BinaryNode<AnyType> pivot = parent.right;
        BinaryNode<AnyType> leftchild = pivot.left;
        if (parent.right != pivot)
            throw new OperationNotSupportedException("Only right children can be rotated left.");
        if (parent == root) { // special case: pivot will become the tree's new root
            if (root.right == null) // root has no right child, nothing changes on this roate
                return;
            root.right = pivot.left;
            pivot.left = root;
            root = pivot;
            return;
        }
        parent.right = leftchild;
        pivot.left = parent;
        if (parent == grandparent.left)
            grandparent.left = pivot;
        if (parent == grandparent.right)
            grandparent.right = pivot;
    }

    /**
     * Internal method to find the parent of the node with the specified value.
     * @param t The node where to begin the search.
     * @param x The target nodes' value.
     * @return x's Parent node.
     */
    private BinaryNode<AnyType> findParent(BinaryNode<AnyType> t, AnyType x) {
        if (t == null)
            throw new NoSuchElementException("This tree does not contain " + x);
        int compareResult = x.compareTo(t.element);
        if (t.left != null && t.left.element.equals(x))
            return t;
        if (t.right != null && t.right.element.equals(x))
            return t;
        if (compareResult < 0)
            t = findParent(t.left, x);
        else if (compareResult > 0)
            t = findParent(t.right, x);
        return t;
    }

    /**
     * Printout the tree level-by-level.
     */
    public void printLevels() {
        if (isEmpty())
            throw new UnderflowException();
        Queue<BinaryNode<AnyType>> qu = new LinkedList<>();
        BinaryNode<AnyType> x;
        qu.add(root);
        qu.add(null);
        while (true) {
            x = qu.remove();
            if (x != null) {
                System.out.print(x.element + " ");
                if (x.left != null)
                    qu.add(x.left);
                if (x.right != null)
                    qu.add(x.right);
            } else {
                System.out.println();
                if (!qu.isEmpty())
                    qu.add(null);
                else
                    break;
            }
        }
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * 
     * @param x the item to insert.
     */
    public void insert(AnyType x) {
        root = insert(x, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     * 
     * @param x the item to remove.
     */
    public void remove(AnyType x) {
        root = remove(x, root);
    }

    /**
     * Find the smallest item in the tree.
     * 
     * @return smallest item or null if empty.
     */
    public AnyType findMin() {
        if (isEmpty())
            throw new UnderflowException();
        return findMin(root).element;
    }

    /**
     * Find the largest item in the tree.
     * 
     * @return the largest item of null if empty.
     */
    public AnyType findMax() {
        if (isEmpty())
            throw new UnderflowException();
        return findMax(root).element;
    }

    /**
     * Find an item in the tree.
     * 
     * @param x the item to search for.
     * @return true if not found.
     */
    public boolean contains(AnyType x) {
        return contains(x, root);
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * 
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree() {
        if (isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root);
    }

    /**
     * Internal method to insert into a subtree.
     * 
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<AnyType> insert(AnyType x, BinaryNode<AnyType> t) {
        if (t == null)
            return new BinaryNode<>(x, null, null);

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            t.left = insert(x, t.left);
        else if (compareResult > 0)
            t.right = insert(x, t.right);
        else
            ; // Duplicate; do nothing
        return t;
    }

    /**
     * Internal method to remove from a subtree.
     * 
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryNode<AnyType> remove(AnyType x, BinaryNode<AnyType> t) {
        if (t == null)
            return t; // Item not found; do nothing

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            t.left = remove(x, t.left);
        else if (compareResult > 0)
            t.right = remove(x, t.right);
        else if (t.left != null && t.right != null) // Two children
        {
            t.element = findMin(t.right).element;
            t.right = remove(t.element, t.right);
        } else
            t = (t.left != null) ? t.left : t.right;
        return t;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * 
     * @param t the node that roots the subtree.
     * @return node containing the smallest item.
     */
    private BinaryNode<AnyType> findMin(BinaryNode<AnyType> t) {
        if (t == null)
            return null;
        else if (t.left == null)
            return t;
        return findMin(t.left);
    }

    /**
     * Internal method to find the largest item in a subtree.
     * 
     * @param t the node that roots the subtree.
     * @return node containing the largest item.
     */
    private BinaryNode<AnyType> findMax(BinaryNode<AnyType> t) {
        if (t != null)
            while (t.right != null)
                t = t.right;
        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * 
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     * @return node containing the matched item.
     */
    private boolean contains(AnyType x, BinaryNode<AnyType> t) {
        if (t == null)
            return false;

        int compareResult = x.compareTo(t.element);

        if (compareResult < 0)
            return contains(x, t.left);
        else if (compareResult > 0)
            return contains(x, t.right);
        else
            return true; // Match
    }

    /**
     * Internal method to print a subtree in sorted order.
     * 
     * @param t the node that roots the subtree.
     */
    private void printTree(BinaryNode<AnyType> t) {
        if (t != null) {
            printTree(t.left);
            System.out.print(t.element + " ");
            printTree(t.right);
        }
    }

    /**
     * Internal method to compute height of a subtree.
     * 
     * @param t the node that roots the subtree.
     */
    private int height(BinaryNode<AnyType> t) {
        if (t == null)
            return -1;
        else
            return 1 + Math.max(height(t.left), height(t.right));
    }

    // Basic node stored in unbalanced binary search trees
    private static class BinaryNode<AnyType> {
        // Constructors
        BinaryNode(AnyType theElement) {
            this(theElement, null, null);
        }

        BinaryNode(AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }

        AnyType element; // The data in the node
        BinaryNode<AnyType> left; // Left child
        BinaryNode<AnyType> right; // Right child
    }

    /**
     * Exception class for access in empty containers
     * such as stacks, queues, and priority queues.
     * @author Mark Allen Weiss
     */
    private static class UnderflowException extends RuntimeException {
    }

    /** The tree root. */
    private BinaryNode<AnyType> root;

    // Test program
    public static void main(String[] args) throws OperationNotSupportedException {
        List<Integer> tree1 = Arrays.asList(20, 10, 30, 8, 12, 28, 32, 7, 9, 11, 13, 27, 29, 31, 33);
        List<Integer> tree2 = Arrays.asList(10, 15, 5, 4, 6, 14, 16, 3, 7, 13, 17);
        BinarySearchTree<Integer> a = new BinarySearchTree<>();
        BinarySearchTree<Integer> b = new BinarySearchTree<>();

        for (int num : tree1)
            a.insert(num);
        for (int num : tree2)
            b.insert(num);

        System.out.println();
        System.out.println("Tree (a)");
        a.printLevels();

        System.out.println();
        System.out.println("Tree (b)");
        b.printLevels();

        System.out.println();
        System.out.println("a) nodeCount\n\tRecursively traverses the tree and returns the count of nodes.");
        System.out.println("Node count of (a)");
        System.out.println(a.nodeCount());
        System.out.println("Node count of (b)");
        System.out.println(b.nodeCount());

        System.out.println();
        System.out.println(
                "b) isFull\n\tReturns true if the tree is full.  A full tree has every node as either a leaf or a parent with two children.");
        System.out.println("Is tree (a) full?");
        System.out.println(a.isFull());
        System.out.println("Is tree (b) full?");
        System.out.println(b.isFull());

        System.out.println();
        System.out.println(
                "c) compareStructure\n\tCompares the structure of current tree to another tree and returns true if they match.");
        System.out.println("tree (a) compared to tree (a)?");
        System.out.println(a.compareStructure(a));
        System.out.println("tree (a) compared to tree (b)?");
        System.out.println(a.compareStructure(b));

        System.out.println();
        System.out.println(
                "d) equals\n\tCompares the current tree to another tree and returns true if they are identical.");
        System.out.println("(a) equals (a)?");
        System.out.println(a.equals(a));
        System.out.println("(a) equals (b)?");
        System.out.println(a.equals(b));

        System.out.println();
        System.out.println("e) copy\n\tCreates and returns a new tree that is a copy of the original tree.");
        System.out.println("copy of tree (a)");
        a.copy().printLevels();
        System.out.println("copy of tree (b)");
        b.copy().printLevels();

        System.out.println();
        System.out.println("f) mirror\n\tCreates and returns a new tree that is a mirror image of the original tree.");
        System.out.println("mirror of tree (a)");
        BinarySearchTree<Integer> mirrorOfa = a.mirror();
        mirrorOfa.printLevels();
        System.out.println("mirror of tree (b)");
        BinarySearchTree<Integer> mirrorOfb = b.mirror();
        mirrorOfb.printLevels();

        System.out.println();
        System.out.println("g) isMirror\n\tReturns true if the tree is a mirror of the passed tree.");
        System.out.println("Is (a) a mirror of (mirrorOfa)?");
        System.out.println(a.isMirror(mirrorOfa));
        System.out.println("Is (a) a mirror of (a)?");
        System.out.println(a.isMirror(a));
        System.out.println("Is (b) a mirror of (mirrorOfb)?");
        System.out.println(b.isMirror(mirrorOfb));
        System.out.println("Is (b) a mirror of (b)?");
        System.out.println(b.isMirror(b));

        System.out.println();
        System.out.println("h) rotateRight\n\tPerforms a single rotation on the node having the passed value.");
        System.out.println("Rotate 20 right");
        a.rotateRight(20);
        a.printLevels();

        System.out.println();
        System.out.println("i) rotateLeft\n\tPerforms a single rotation on the node having the passed value.");
        System.out.println("Rotate 10 left");
        a.rotateLeft(10);
        a.printLevels();
    }
}
