package search;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import sort.KDT;

public class BST<Key extends Comparable<Key>, Value> implements Serializable {
	/**
	 * 
	 */
	public static void main(String[] args) {
		//BST<Integer,Integer> bst = new BST<Integer, Integer>();
		//bst.put(75, 9);
		//bst.writeToFile("bst.ser");
		BST<Integer, Integer> bst = new BST<Integer, Integer>("bst.ser");
		
		System.out.println(bst.get(75));
	}
	
	private static final long serialVersionUID = 8775155124761510511L;
	private Node root;
	
	public BST(String fn) {
		BST<Key,Value> bst = null;
		try {
	         FileInputStream fileIn = new FileInputStream(fn);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         bst = (BST<Key,Value>) in.readObject();
	         in.close();
	         fileIn.close();
	      } catch (IOException i) {
	         i.printStackTrace();
	      } catch (ClassNotFoundException c) {
	         System.out.println("Employee class not found");
	         c.printStackTrace();
	      }
		this.root = bst.root;
	}
	
	public BST() {
		root = null;
	}
	
	private class Node implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8145778479611668151L;
		private Key key;
		private Value val;
		private Node left, right;
		private int N;
		
		public Node(Key key, Value val, int N) {
			this.key = key;
			this.val = val;
			this.N = N;
		}
	}
		
	public int size() {
		return size(root);
	}
	
	private int size(Node x) {
		if (x == null) return 0;
		else return x.N;
	}
	
	public Value get(Key key) { if (key == null) return null; return get(root, key); }
	
	private Value get(Node x, Key key) {
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if (cmp < 0) return get(x.left, key);
		else if (cmp > 0) return get(x.right, key);
		else return x.val;
	}
	
	public void put(Key key, Value val) {
		root = put(root, key, val);
	}
	
	private Node put(Node x, Key key, Value val) {
		if (x == null) return new Node(key, val, 1);
		
		int cmp = key.compareTo(x.key);
		
		if (cmp < 0) x.left = put(x.left, key, val);
		else if (cmp > 0) x.right = put(x.right, key, val);
		else x.val = val;
		
		x.N = size(x.left) + size(x.right) + 1;
		return x;
	}
	
	public boolean isEmpty() {
		return root == null;
	}
	
	public Key min() {
		if (isEmpty()) throw new NoSuchElementException();
		Node x = min(root);
		return x.key;
	}
	
	private Node min(Node x) {
		if (x.left == null) return x;
		return min(x.left);
	}
	
	public Key max() {
		if (isEmpty()) throw new NoSuchElementException();
		Node x = max(root);
		return x.key;
	}
	
	private Node max(Node x) {
		if (x.right == null) return x;
		return max(x.right);
	}
	
	public Key floor(Key key) {
		Node x = floor(root, key);
		if (x == null) throw new NoSuchElementException();
		return x.key;
	}
	
	private Node floor(Node x, Key key) {
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0) return x;
		if (cmp < 0) return floor(x.left, key);
		Node t = floor(x.right, key);
		if (t != null) 	return t;
		else 			return x;
	}
	
	public Key select(int k) {
		if (k < 0 || k >= size()) throw new IllegalArgumentException();
		Node x = select(root, k);
		return x.key;
	}
	
	private Node select(Node x, int k) {
		if (x == null) return null;
		int t = size(x.left);
		if		(t > k)	return select(x.left, k);
		else if (t < k)	return select(x.right, k - t -1);
		else				return x;
	}
	
	public int rank(Key key) {
		return rank(key, root);
	}
	
	private int rank(Key key, Node x) {
		if (x == null) return 0;
		int cmp = key.compareTo(x.key);
		if		(cmp < 0) 	return rank(key, x.left);
		else if	(cmp > 0) 	return 1 + size(x.left) + rank(key, x.right);
		else					return size(x.left);
	}
	
	public void deleteMin() {
		if (isEmpty()) throw new NoSuchElementException();
		root = deleteMin(root);
	}
	
	private Node deleteMin(Node x) {
		if (x.left == null) return x.right;
		x.left = deleteMin(x.left);
		x.N = size(x.left) + size(x.right) + 1;
		return x;
	}
	
	public void delete(Key key) {
		root = delete(root, key);
	}
	
	private Node delete(Node x, Key key) {
		if (x == null) return null;
		int cmp = key.compareTo(x.key);
		if		(cmp < 0) x.left 	= delete(x.left, key);
		else if 	(cmp > 0) x.right 	= delete(x.right, key);
		else {
			if (x.right == null) return x.left;
			if (x.left == null) return x.right;
			Node t = x;
			x = min(t.right);
			x.right = deleteMin(t.right);
			x.left = t.left;
		}
		x.N  = size(x.left) + size(x.right) + 1;
		return x;
	}
	
	public Iterable<Key> keys() {
		return keys(min(), max());
	}
	
	public Iterable<Key> keys(Key lo, Key hi) {
		ArrayList<Key> al = new ArrayList<Key>();
		keys(root, al, lo, hi);
		return al;
	}
	
	private void keys(Node x, ArrayList<Key> al, Key lo, Key hi) {
		if (x == null) return;
		int cmplo = lo.compareTo(x.key);
		int cmphi = hi.compareTo(x.key);
		if (cmplo < 0) keys(x.left, al, lo, hi);
		if (cmplo <= 0 && cmphi >= 0) al.add(x.key);
		if (cmphi > 0) keys(x.right, al, lo, hi);
	}
	
	public int height() {
		return height(root);
	}
	
	private int height(Node x) {
		if (x == null) return 0;
		else return Math.max(height (x.left), height(x.right)) + 1;
	}
	
	public void writeToFile(String fn) {
		try {
	         FileOutputStream fileOut =
	        		 new FileOutputStream(fn);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(this);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in /tmp/kdtree.ser");
	      } catch (IOException i) {
	         i.printStackTrace();
	      }
	}
}