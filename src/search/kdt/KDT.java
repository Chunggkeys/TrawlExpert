package search.kdt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.hamcrest.core.IsInstanceOf;

import sort.GeneralCompare;
import sort.GeneralRange;
import sort.MergeSort;
import sort.QuickSelect;
import utils.Stopwatch;

/**
 * A class for constructing KD-trees with range-searching abilities. Written using
 * pseudocode from https://en.wikipedia.org/wiki/K-d_tree for building the tree
 * and referencing several resources: 
 * - http://www.cs.utah.edu/~lifeifei/cs6931/kdtree.pdf
 * - https://www.youtube.com/watch?v=Z4dNLvno-EY
 * - http://www.cs.uu.nl/docs/vakken/ga/slides5a.pdf
 * - https://www.datasciencecentral.com/profiles/blogs/implementing-kd-tree-for-fast-range-search-nearest-neighbor
 * All code is original and was written by Christopher W. Schankula.
 * @author Christopher W. Schankula
 *
 * @param <KeyVal> The type of key-value objects to insert into the tree.
 */
public class KDT<KeyVal extends Comparable<KeyVal>> implements Serializable {
	/**
	 * The serialization version id
	 */
	private static final long serialVersionUID = -5494252458136566820L;
	/**
	 * The root of the kd-tree.
	 */
	private KDNode<KeyVal> root;
	/**
	 * The GeneralCompare functions determining the splitting axes of the data.
	 */
	private ArrayList<GeneralCompare<KeyVal>> axes;
	
	/**
	 * Load a kd-tree from a serialized file.
	 * @param fn The filname of the serialized data.
	 */
	public KDT(String fn) {
		KDT<KeyVal> kdt = null;
		try {
	         FileInputStream fileIn = new FileInputStream(fn);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         kdt = (KDT<KeyVal>) in.readObject();
	         in.close();
	         fileIn.close();
	      } catch (IOException i) {
	         i.printStackTrace();
	      } catch (ClassNotFoundException c) {
	         System.out.println("Employee class not found");
	         c.printStackTrace();
	      }
		//https://stackoverflow.com/questions/26327956/set-this-in-a-class
		this.root = kdt.root;
		this.axes = kdt.axes;
	}
	
	/**
	 * Construct a new kd-tree from an array of nodes.
	 * @param axes A GeneralCompare instance for each dimension of the kd-tree. This
	 * GeneralCompare must input an object of type KeyVal and output an ordering correspoding
	 * to that axis.
	 * @param keyvals An array of Key-Value pairs to insert into the tree.
	 */
	public KDT(ArrayList<GeneralCompare<KeyVal>> axes, KeyVal[] keyvals) {
		this.axes = axes;
		root = buildTree(keyvals, 0, keyvals.length - 1, 0);
	}
	
	/**
	 * Builds a balanced tree recursively. Builds the tree by using the (i % k)th comparison 
	 * function on each level i of the tree.
	 * 
	 * @param keyvals The array of key-value objects
	 * @param lo The lower bound of the part to build
	 * @param hi The upper bound of the part to build
	 * @param depth The depth of the new node to be created
	 * @return The new node
	 */
	private KDNode<KeyVal> buildTree(KeyVal[] keyvals, int lo, int hi, int depth) {
		if (lo > hi) return null;
		int axis = depth % getK();
		
		int mid = (lo + hi) / 2;
		
		//find the median of this portion of the array using quickselect
		QuickSelect.median(keyvals, lo, hi, axes.get(axis));
		KeyVal median = (KeyVal) keyvals[mid];
		
		//add median to this node
		KDNode<KeyVal> newNode = new KDNode<KeyVal>(median, 0);
		
		//recursively construct the two subtrees
		newNode.left = buildTree(keyvals, lo, mid - 1, depth + 1);
		newNode.right = buildTree(keyvals, mid + 1, hi, depth + 1);
		
		//set this node's number of subtree nodes
		newNode.n = size(newNode.left) + size(newNode.right) + 1;
		return newNode;
	}
	
	/**
	 * Perform a range search for nodes in the tree. Assumes number of axes in the range
	 * variable is equal to the current tree's number of axes.
	 * @param range	An ArrayList of GeneralRange instances. This must take a value of type
	 * KeyVal and return whether that value is in the range for the given axis. Must provide
	 * an ArrayList with a size equal to the current tree's number of axes, k.
	 * 
	 * The search works by traversing the tree as in a BST but only comparing the (i % k)th
	 * axis on each level i of the tree.
	 * @return An Iterable of nodes found to be matching the range search.
	 */
	public Iterable<KeyVal> rangeSearch(ArrayList<GeneralRange<KeyVal>> range){
		ArrayList<KeyVal> result = new ArrayList<KeyVal>();
		rangeSearch(root, range, result, 0);
		return result;
	}
	
	/**
	 * Recursively build a balanced kd-tree from an array of objects.
	 * @param x The current node to build under.
	 * @param range The ranges to search on
	 * @param result The ArrayList pointer to build the result list.
	 * @param depth The current depth of the search.
	 */
	private void rangeSearch(KDNode<KeyVal> x, ArrayList<GeneralRange<KeyVal>> range, ArrayList<KeyVal> result, int depth) {
		if (x == null) return;
		int axis = depth % getK();
		GeneralRange<KeyVal> rg = range.get(axis);
		
		//System.out.println("Try: " + x.keyval);
		
		int bounds = rg.isInBounds((KeyVal) x.keyval);
		//if it's in the bounds, must search both subtrees. Also a candidate to be included in the results
		if (bounds == 0) {
			//if the point is inside the axis range, check if it's in the other ranges too
			if (pointInside(x.keyval, range)) {
				result.add(x.keyval);
			}
			//range search both subtrees
			rangeSearch(x.left, range, result, depth + 1);
			rangeSearch(x.right, range, result, depth + 1);
		} else if (bounds > 0) { //if it's bigger than the current axis, search the left subtree
			rangeSearch(x.left, range, result, depth + 1);
		} else if (bounds < 0)	//if it's smaller than the current axis, search the right subtree
			rangeSearch(x.right, range, result, depth + 1);
		
		return;
	}
	
	/**
	 * See if a point is inside the range given for all axes
	 * @param pt The point to test
	 * @param range The range search for all axes
	 * @return true if inside all ranges, false otherwise
	 */
	private boolean pointInside(KeyVal pt, ArrayList<GeneralRange<KeyVal>> range) {
		for (int i = 0; i < axes.size(); i++)
			if (range.get(i).isInBounds(pt) != 0) return false;
		return true;
	}
	
	/**
	 * Returns the number of nodes in the tree.
	 * @return the number of nodes present in the kd-tree.
	 */
	public int size() {
		return size(root);
	}
	
	/**
	 * The maximum depth for any node in the kd-tree.
	 * @return the depth of the maximum-depth node in the kd-tree.
	 */
	public int height() {
		return height(root);
	}
	
	/**
	 * Recursively determine the height of the kd-tree.
	 * @param x The current node being examined
	 * @return The height of this part of the tree
	 */
	private int height(KDNode<KeyVal> x) {
		if (x == null) return 0;
		return 1 + Math.max(height(x.left), height(x.right));
	}
	
	/**
	 * Determine the number of nodes in the kd-tree.
	 * @param x The node to examine.
	 * @return The nubmer of nodes in the node x's subtree.
	 */
	private int size(KDNode<KeyVal> x) {
		if (x == null) return 0;
		else return x.n;
	}
	
	/**
	 * Return the number of axes in the current kd-tree.
	 * @return the nubmer of axes in the current kd-tree.
	 */
	public int getK() {
		return axes.size();
	}
	
	/**
	 * Return a string representation of the kd-tree.
	 */
	public String toString() {
		return toString(root, "");
	}
	
	/**
	 * Write the current kd-tree to the filesystem.
	 * @param fn The filename to which to write the kd-tree.
	 */
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
	
	/**
	 * Private recursive function to build kd-tree string
	 * @param x The current node being examined / added to the string
	 * @param depth The depth of the tree
	 * @return The string representing this part of the tree (returned recursively).
	 */
	private String toString(KDNode<KeyVal> x, String depth) {
		if (x == null) return depth + "null\n";
		String result = "";
		result += depth + x.keyval.toString() + "\n";
		result += toString(x.left, depth + "    ");
		result += toString(x.right, depth + "    ");
		return result;
	}
}
