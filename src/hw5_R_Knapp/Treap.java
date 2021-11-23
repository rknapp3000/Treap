package hw5_R_Knapp;
import java.util.*;

public class Treap <E extends Comparable<E>> {

	private static class Node <E extends Comparable<E>> { 
			
			public E data; // search key
			public int priority; // random heap priority
			public Node <E> left; 
			public Node <E> right; 
			
	public Node(E data, int priority) {
	    
		if(data == null) {
			throw new IllegalArgumentException("The data can't be null!"); //throws exception if data is null
	    }
	    	this.data = data;
	        this.priority = priority;
	} 
	
	//Method to perform a right rotation that returns a reference to the root of the result
	Node <E> rotateRight() { 
		if (this.left != null) { 
			Node <E> left = this.left;
			Node <E> rotatedRight = left.right; 
			
			left.right = this; 
			left.right.left = rotatedRight; 
			
			return left; 
		}
		
		return this; 
	}

	//Method to perform a right rotation that returns a reference to the root of the result
	Node <E> rotateLeft() { 
		if (this.right != null) { 
			Node <E> right = this.right; 
			Node <E> rotatedLeft = right.left; 
			
			right.left = this; 
			right.left.right = rotatedLeft; 
			
			return right; 
		}
		
		return this; 
	}
	public String toString() {
		return "(key = " + data.toString() + ", priority = " + priority + ")"; //prints the pair consisting of the key and its priority 
	}
}
	
	private Random priorityGenerator; //generates a random number to put into priority field of a tree node
	private Node <E> root; 
	
	//Constructors for Treap Class
    //Creates an empty treap using the random number generator
	public Treap() {
		priorityGenerator = new Random();
	    }
	
	//Creates a new treap and initializes the priority generator using new random seed value
	public Treap(long seed) {
		priorityGenerator = new Random(seed);
	    }
	
	// Adds a new node to the tree with a user specified key and the priority generated randomly by the priorityGenerator
	boolean add (E key) { 
		 return add(key, priorityGenerator.nextInt());
		}
	
	// Adds a new node to the tree with a user specified key and priority 
	boolean add (E key, int priority) { 
		Node <E> nodeAdded = new Node <> (key, priority); 
		if (root == null) { 
			root = nodeAdded; 
			return true; 
		}
		
		Deque <Node <E>> stack = new ArrayDeque<>(); 
		Node <E> presnode = root; 
		
		while (true) {
			if (presnode.data.compareTo(key) == 0)
				return false; 
				
				stack.push(presnode);
				
			if (presnode.data.compareTo(key) < 0) {
				if (presnode.right == null) { 
					presnode.right = nodeAdded; 
					
					break; 
				}
				presnode = presnode.right; 
				
			} else { 
				if (presnode.left == null) { 
					presnode.left = nodeAdded; 
					
					break; 
			}
				presnode = presnode.left; 
		}
	}
		
		reheap(nodeAdded, stack); //calling the reheap helper function
		
		return true;
	}
	
	// Function to help restore the heap invariant
	boolean reheap (Node<E> node, Deque <Node<E>> stack) { 
		while (!stack.isEmpty() && stack.peek().priority < node.priority) { 
			Node <E> parnode = stack.pop(); 
			
			if (!childL(parnode, node)) { 
				if (!stack.isEmpty()) { 
					boolean childL = childL(stack.peek(), parnode); 
					parnode = parnode.rotateLeft(); 
					if (childL)
						stack.peek().left = parnode; 
					else 
						stack.peek().right = parnode; 
				} else { 
					parnode = parnode.rotateLeft(); 
				}
			
			} else { 
				
				if (!stack.isEmpty()) { 
					boolean childL = childL(stack.peek(), parnode); 
					parnode = parnode.rotateRight(); 
					if (childL)
						stack.peek().left = parnode; 
					else 
						stack.peek().right = parnode;
				} else { 
					
					parnode = parnode.rotateRight(); 
				}
			}
			node = parnode; 
		}
		
		if (stack.size() == 0) { 
			root = node; 
		}
		
		return true; 
	}
	
	// helper method that determines if the child is the left or right
	public boolean childL (Node<E> parnode, Node <E> child) { 
		if (parnode == null)
			return false; 
		
		if (parnode.left == null)
			return false; 
		
		return parnode.left.data.compareTo(child.data) == 0; 
	}
	
	// Deletes the node from the given key and returns true 
	boolean delete(E key){
		
		if(this.root == null)
			return false;
		
		if (this.find(root, key) == false)
			return false; 
		
		if (key == null)  
			return false; 
			
		else {
			
			this.root = delete(this.root, key);
			
			return true;
		}
	}
	
	// helper method containing the conditions for maintaining aspects of the heap. Method to be called in main delete method. 
	public Node<E> delete(Node<E> rootNode, E key){
		
		int compNode = key.compareTo(rootNode.data);
		
		if (compNode > 0) { rootNode.right = delete(rootNode.right, key); }
		
		else if (compNode < 0) { rootNode.left = delete(rootNode.left, key); }
		
		else if (rootNode.right == null){
			
			Node<E> newrootNode = rootNode.left;
			rootNode = newrootNode;
		}
		
		else if (rootNode.left == null){
			
			Node<E> newrootNode = rootNode.right;
			rootNode = newrootNode;
			
		}
		
		else if (rootNode.right.priority > rootNode.left.priority) {
			
			rootNode = rootNode.rotateLeft();
			rootNode.left = delete(rootNode.left, key);
			
		}
		
		else if (rootNode.right.priority < rootNode.left.priority) {
			
			rootNode = rootNode.rotateRight();
			rootNode.right = delete(rootNode.right, key);
			
		} else {
			
			return null;
			
		}
		return rootNode;
	}
	
	// Method that finds a node with the given key in the treap at "root". Method returns true if it finds the key, otherwise returns false
	private boolean find (Node<E> root, E key) { 
		Node <E> presnode = root; 
		
		while (presnode != null) { 
			if (presnode.data.compareTo(key) == 0)
				return true; 
			
			if (presnode.data.compareTo(key) > 0) { 
				presnode = presnode.left; 
		} else { 
			presnode = presnode.right; 
		}
	}
		return false; 
}
	
	// Finds a node with the given key in the treap. Method returns true if it finds the key, otherwise returns false
	public boolean find (E key) { 
		return find(root, key); 
	}
	
	public String toString() { 
		
		StringBuilder s = new StringBuilder(); 
		preOrderTraversal (root, s, 0, "  "); 
		
		return s.toString(); 
	}
	
	//Helper method to carry out the preOrder traversal of the tree for the toString method to return an accurate representation of the nodes in the string
	public void preOrderTraversal(Node <E> root, StringBuilder s, int treeDepth, String p) { 
		
		int i; 
		
		if (root == null) { 
			for (i = 0; i < treeDepth; ++i)  
				s.append(p); 
				s.append("null" + "\n"); 
				
				return; 
			}
			
			for (i = 0; i < treeDepth; ++i)
				s.append(p); 
				s.append(root.toString()).append("\n"); 
				
				preOrderTraversal (root.left, s, treeDepth + 1, p); 
				
				preOrderTraversal (root.right, s, treeDepth + 1, p); 
			
		}
		
	//Testing program in main
	public static void main(String[] args) {
		 
		Treap <Integer> TreapTester = new Treap<>();
	        
			TreapTester.add(4,19);
	        TreapTester.add(2,31);
	        TreapTester.add(6,70);
	        TreapTester.add(1,84);
	        TreapTester.add(3,12);
	        TreapTester.add(5,83);
	        TreapTester.add(7,26);
	        
	        
	        //print the tree
	        System.out.println(TreapTester);
	        System.out.println();
	        System.out.println("After toString method: " + "\n" + TreapTester.toString());
	        System.out.println();
	        System.out.println("find method: " + TreapTester.find(7));
	        System.out.println();
	        System.out.println("delete method: " + TreapTester.delete(7));
	        System.out.println();
	        System.out.println("Print after deleting node 7: " + "\n" + TreapTester);
	        TreapTester.add(7,26); 
	        System.out.println("Print after re-adding the deleted node 7: "  + "\n" + TreapTester);
	        TreapTester.delete(5); 
	        System.out.println("Print after deleting node with key 5: " + "\n" + TreapTester);
	        TreapTester.add(5,83); 
	        System.out.println("Print after adding back node 5: \n" + TreapTester);
	        TreapTester.delete(3); 
	        System.out.println("Print after deleting node with key 3: \n" + TreapTester);
	        TreapTester.add(3,12); 
	        System.out.println("Print after adding back node 3: \n" + TreapTester);
	        TreapTester.delete(2); 
	        System.out.println("Print after deleting node with key 2: \n" + TreapTester);
	        TreapTester.add(2,31); 
	        System.out.println("Print after adding back node 2: \n" + TreapTester);
	        TreapTester.delete(6); 
	        System.out.println("Print after deleting node with key 6: \n" + TreapTester); 
	        TreapTester.add(6, 70); 
	        TreapTester.add(8); 
	        System.out.println("Print after adding 8 with random priority generator: \n" + TreapTester);
		}
	}

//Ryan Knapp
//Homework Assignment 5: Binary Search Trees and Heaps
//Date: 4/19/2021














