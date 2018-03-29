/**
 * 
 */
package search;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author HaleyGlavina
 *
 */
public class RedBlackTreeTest {
	
	private GeneralCompare<Integer> b1;
	private Field<Integer, Integer[]> fld;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		b1 = (a1, a2) -> (Integer) a1 - (Integer) a2;
		fld = (a1) -> (Integer) a1[0];
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link search.RedBlackTree#root()}.
	 */
	@Test
	public void testRoot() {
		Integer[][] x = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}, {8, 8}, {9, 9}};
		RedBlackTree<Integer, Integer[]> myTree = new RedBlackTree<Integer, Integer[]>(fld, b1);
		
		// Add first 5 nodes, expected root is 2
		for(int i = 0; i < 4; i++){
			myTree.put(x[i]);
		}
		assert((Integer) myTree.root().key() == 2);
		
		// Add remaining nodes, expected root is 4
		for(int i = 5; i < x.length; i++){
			myTree.put(x[i]);
		}
		assert((Integer) myTree.root().key() == 4);
	}

	/**
	 * Test method for {@link search.RedBlackTree#put(java.lang.Object)}.
	 */
	@Test
	public void testPut() {
		Integer[][] x = {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}, {8, 8}, {9, 9}};
		RedBlackTree<Integer, Integer[]> myTree = new RedBlackTree<Integer, Integer[]>(fld, b1);
		for(int i = 0; i < x.length; i++){
			myTree.put(x[i]);
		}
		Node h = myTree.root(); 
		
		// Check if right-most branch of tree matches what is expected
		Integer[] expect = {4, 6, 7, 8, 9};
		for (int i = 0 ; i < expect.length ; i++) {
			assert(expect[i] == h.key());
			h = h.right();
		}
	}

	/**
	 * Test method for {@link search.RedBlackTree#rotateLeft(search.Node)}.
	 */
	@Test
	public void testRotateLeft() {
		RedBlackTree<Integer, Integer[]> myTree = new RedBlackTree<Integer, Integer[]>(fld, b1);
		Integer[][] x = {{1, 1}, {2, 2}, {3, 3}, {4, 4}};
		myTree.put(x[0]);
		myTree.put(x[1]);
		assert((Integer) myTree.root().key() == 1);
		
		// Check if left rotation changes the root of the tree as expected
		myTree.put(x[2]);
		myTree.put(x[3]);
		assert((Integer) myTree.root().key() == 2);
		
	}

	/**
	 * Test method for {@link search.RedBlackTree#rotateRight(search.Node)}.
	 */
	@Test
	public void testRotateRight() {
		RedBlackTree<Integer, Integer[]> myTree = new RedBlackTree<Integer, Integer[]>(fld, b1);
		Integer[][] x = {{1, 1}, {2, 2}, {3, 3}, {4, 4}};
		myTree.put(x[3]);
		myTree.put(x[2]);
		assert((Integer) myTree.root().key() == 4);
		
		// Check if right rotation changes the root of the tree as expected
		myTree.put(x[1]);
		myTree.put(x[0]);
		assert((Integer) myTree.root().key() == 3);
	}

}