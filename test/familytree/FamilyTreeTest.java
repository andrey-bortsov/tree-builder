package familytree;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import familytree.FamilyTreeBuilder.FamilyTree;
import familytree.FamilyTreeBuilder.FamilyTree.Node;
//import familytree.FamilyTreeBuilder.FamilyTree.Node;

public class FamilyTreeTest {
	
	FamilyTreeBuilder ftb;

	char[] pre = {'D', 'H', 'B', 'G', 'M', 'W', 'F', 'T', 'X', 'Z', 'C', 'R', 'P', 'Q', 'N'};
	char[] post = {'G', 'M', 'W', 'F', 'B', 'X', 'Z', 'T', 'R', 'P', 'C', 'H', 'N', 'Q', 'D'};
	
	//char[] pre = {'B', 'G', 'M', 'W', 'F'};
	//char[] post = {'G', 'M', 'W', 'F', 'B'};
	
	Node n = null;

	
	
	@Before
	public void setUp() throws Exception {
		ftb = new FamilyTreeBuilder();
		ftb.pretrav = pre;
		ftb.posttrav = post;
		ftb.ft.mainRoot = ftb.ft.buildTree(15, 0, 0);
	}

	@Test
	public void testBuildTree() {
		
		
		
		assertEquals(ftb.ft.mainRoot.getName(), 'D');
		assertEquals(ftb.ft.mainRoot.nChildren, 2);
		assertEquals(ftb.ft.mainRoot.offspring[0].offspring[0].offspring[2].getName(), 'W');
		assertEquals(ftb.ft.mainRoot.offspring[0].offspring[0].offspring[2].getParent().getParent().getName(), 'H');
	}
	
	@Test
	public void testLookup(){
		n = ftb.ft.lookup('Z', ftb.ft.mainRoot);
		assertEquals(n.nChildren, 0);
		
		n = ftb.ft.lookup('B', ftb.ft.mainRoot);
		assertEquals(n.nChildren, 4);
		
		n = ftb.ft.lookup('H', ftb.ft.mainRoot);
		assertEquals(n.nChildren, 3);
		
		n = ftb.ft.lookup('Q', ftb.ft.mainRoot);
		assertEquals(n.nChildren, 1);
	}
	
	@Test
	public void testRelate(){
		Node a = ftb.ft.lookup('D', ftb.ft.mainRoot);
		Node b = ftb.ft.lookup('B', ftb.ft.mainRoot);
		int[] pair = ftb.ft.relate(a, b);
		assertEquals(pair[0], 0);
		assertEquals(pair[1], 2);
	}
	
	@Test
	public void testTraverseLevelOrder(){
		ftb.ft.traverseLevelOrder(ftb.ft.mainRoot);
		assertTrue(true);
	}

}
