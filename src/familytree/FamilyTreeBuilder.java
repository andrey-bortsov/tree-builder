package familytree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * This program builds a general tree using preorder and postorder 
 * traversals, and determines the relationship between any two nodes.
 * @author bortsov
 */
public class FamilyTreeBuilder {
	/** Character denoting beginning of the preorder traversal sequence */
	public static final char PREORDER_MARK = '<';
	/** Character denoting beginning of the postorder traversal sequence */
	public static final char POSTORDER_MARK = '>';
	/** Character denoting beginning of the query */
	public static final char QUERY_MARK = '?';
	/** Preorder traversal */
	private static char[] pretrav;
	/** Postorder traversal */
	private static char[] posttrav;
	/**  Tree object */
	private static FamilyTree ft;
	

	/**
	 * Main method
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		FamilyTreeBuilder ftb = new FamilyTreeBuilder();
		readPreorder(reader);
		readPostorder(reader);
		ft.setRoot(ft.buildTree(pretrav.length, 0, 0));
		String queryString = "";
		while(queryString != null){
			queryString = reader.readLine();
			char[] pair = readQuery(queryString);
			if(pair != null){
			ft.printRelationship(ft.lookup(pair[0], ft.root()), 
					                 ft.lookup(pair[1], ft.root()));
			}
		}
		ft.traverseLevelOrder(ft.root());
	}
	
	/**
	 * Constructor for proj2 class
	 */
	public FamilyTreeBuilder(){
		ft = new FamilyTree();
	}
	
	/**
	 * Read the preorder sequence into an array.
	 * Correct format begins with '<', names are single characters comma-separated,
	 * and end with a period. May span more than one line.
	 * @param br BufferedReader input
	 * @throws IOException
	 */
	public static void readPreorder(BufferedReader br) throws IOException{
		if(br.ready()){
			char c = (char) br.read();
			while(c != PREORDER_MARK){
				c = (char) br.read();
			}
			char[] temp = new char[256];
			int j = 0;
			while(c != '.'){
				if(c != ',' && c != ' ' && c != '\t' && c != '<' && c != '>' && c != '\n' && c != '\r'){
					temp[j] = c;
					j++;
				}
				c = (char) br.read();
		    }
			pretrav = new char[j];
			for(int i = 0; i < j; i++){
				pretrav[i] = temp[i];
			}
		}
	}

			
	/**
	 * Read the preorder sequence into an array.
	 * Correct format begins with '>', names are single characters comma-separated,
	 * and end with a period. May span more than one line.
	 * @param br BufferedReader input
	 * @throws IOException
	 */
	public static void readPostorder(BufferedReader br) throws IOException{
		if(br.ready()){
			char c = (char) br.read();
			while(c != POSTORDER_MARK){
				c = (char) br.read();
			}
			char[] temp = new char[256];
			int j = 0;
			while(c != '.'){
				if(c != ',' && c != ' ' && c != '\t' && c != '<' && c != '>' && c != '\n' && c != '\r'){
					temp[j] = c;
					j++;
				}
				c = (char) br.read();
		    }
			posttrav = new char[j];
			for(int i = 0; i < j; i++){
				posttrav[i] = temp[i];
			}
		}
	}
	
	/**
	 * Read the preorder sequence into an array.
	 * Correct format begins with '?', followed by two names single characters comma-separated,
	 * and end with a period.
	 * @param s String input
	 * @return a character array of two names
	 */
	public static char[] readQuery(String s){
		if(s != null && !s.isEmpty()){
				char[] pair = new char[2];
				int j = 0;
				for(int i = 0; i < s.length(); i++){
					char c = s.charAt(i);
					if(c != ',' && c != ' ' && c != '\t' && c != '?' && c != '.'){
						pair[j] = c;
						j += 1;
					}
				}
				
				return pair;
			}
		return null;
	}
	
	/**
	 * Tree implementation
	 */
	private class FamilyTree{
		public int size;
		public Node mainRoot;
		
		/**
		 * Tree node.
		 */
		public class Node{
			/**Character name*/
			private char name;
			/** Array for node's children */
			private Node[] offspring;
			/** Number of node's children */
			private int nChildren;
			/** Reference to parent */
			private Node parent;
			/** Mark used to determine relationship */
			private int mark;
			
			/**
			 * Node constructor, takes character name as a parameter
			 * @param e Character name
			 */
			public Node(char e){
				name = e;
				offspring = new Node[pretrav.length - 1];
				nChildren = 0;
				mark = -1;
			}
			
			/**
			 * Add a child to the array of offspring of the node,
			 * increment the child counter, and set the back reference from the child
			 * to this node as parent
			 * @param c Node child
			 */
			public void addChild(Node c){
				offspring[nChildren] = c;
				nChildren++;
				c.setParent(this);
			}
			
			/**
			 * Return reference to parent
			 * @return Parent node
			 */
			public Node getParent(){
				return parent;
			}
			
			/**
			 * Set reference to parent
			 * @param p Node parent
			 */
			public void setParent(Node p){
				parent = p;
			}
			
			/**
			 * Return single character name 
			 * @return name
			 */
			public char getName(){
				return name;
			}
			
			/**
			 * Return array of children
			 * @return Array of children
			 */
			public Node[] getOffspring(){
				return offspring;
			}
			
			/**
			 * Set mark value as checked, 0 or more
			 * @param i Checked value
			 */
			public void checkMark(int i){
				mark = i;
			}
			
			/**
			 * Uncheck mark, reset to -1
			 */
			public void uncheckMark(){
				mark = -1;
			}
			
			/**
			 * Return mark value
			 * @return int mark value
			 */
			public int getMark(){
				return mark;
			}
			
			/**
			 * Return the number of children
			 * @return Integer number of children
			 */
			public int getNchildren(){
				return nChildren;
			}

		}
		
		/**
		 * Find and return a node by given name and the root of the tree
		 * @param e Value of the node to find
		 * @param root Root of the tree
		 * @return Reference to the found node
		 */
		public Node lookup(char e, Node root){
			if(root.getName() == e){
				return root;
			}else{
				Node node = null;
				Node[] children = root.getOffspring();
				for(int i = 0; i < root.getNchildren(); i++){
					Node rootSubtree = children[i];
					node = lookup(e, rootSubtree);
					if(node != null){
						return node;
					}
				}
				return null;
			}
		}
		
		/**
		 * Return the root of the tree
		 * @return Node root
		 */
		private Node root(){
			return mainRoot;
		}
		
		/**
		 * Set the root of the tree
		 * @param r Node for root
		 */
		private void setRoot(Node r){
			mainRoot = r;
		}
		
		/**
		 * Return the distance to the common ancestor from two nodes
		 * @param a First node to relate
		 * @param b Second node to relate
		 * @return Integer array of size 2
		 */
		private int[] relate(Node a, Node b){
			if(a.getName() == b.getName()){
				return new int[] {0, 0};
			}
			int indexA = 0;
			a.checkMark(indexA);
			while(a.getParent() != null){
				a = a.getParent();
				indexA++;
				a.checkMark(indexA);		
			}
			int indexB = 0;			
			if(b.getMark() != -1){
				return new int[] {b.getMark(), 0};
			}else{
				while(b.getParent().getMark() == -1){
					b = b.getParent();
					indexB++;
				}
			}
			indexA = b.getParent().getMark();
			return new int[] {indexA, indexB + 1};
		}
		
		/**
		 * Find the relationship between two nodes using relate() as
		 * helper function, and output the string describing relationship
		 * @param a First node to relate
		 * @param b Second node to relate
		 */
		public void printRelationship(Node a, Node b){
			int[] pair = relate(a, b);
			String relation = null;
			int first = pair[0];
			int second = pair[1];
			if      (first == 0 && second == 0){
				relation = "";
			}else if(first == 0 && second == 1){
				relation = "'s parent";
			}else if(first == 0 && second == 2){
				relation = "'s grandparent";
			}else if(first == 0 && second == 3){
				relation = "'s great grandparent";
			}else if(first == 0 && second > 3 ){
				relation = "'s (great)^" + (second - 2) + "-grandparent";
			}else if(first == 1 && second == 0){
				relation = "'s child";
			}else if(first == 2 && second == 0){
				relation = "'s grandchild";
			}else if(first >  2 && second == 0){
				relation = "'s (great)^" + (first - 2) + "-grandchild";
			}else if(first == 1 && second == 1){
				relation = "'s sibling";
			}else if(first == 1 && second == 2){
				relation = "'s aunt/uncle";
			}else if(first == 1 && second >  2){
				relation = "'s (great)^" + (second - 2) + "-aunt/uncle";
			}else if(first == 2 && second == 1){
				relation = "'s niece/nephew";
			}else if(first >  2 && second == 1){
				relation = "'s (great)^" + (first - 2) + "-niece/nephew";
			}else if(first >  1 && second >  1){
				relation = "'s " + (Math.min(first, second) - 1) + 
						"th cousin " + Math.abs(first - second) + " times removed";
			}
			
			System.out.println(a.getName() + " is " + b.getName() + relation);
			clearMarks(a);
			clearMarks(b);
		}
		
		
		/**
		 * Uncheck node marks once relationship is determined
		 * @param a Node for whose parents marks should be unchecked
		 */
		private void clearMarks(Node a){
			while(a.getParent() != null){
				a.uncheckMark();
				a = a.getParent();		
			}
		}
		
		/**
		 * Recursively build a tree using the pretraversal and posttraversal arrays
		 * of node names
		 * @param size Size of the tree
		 * @param prestart Starting position in the pretraversal array
		 * @param poststart Starting position in the posttraversal array
		 * @return Reference to the root of the tree
		 */
		public Node buildTree(int size, int prestart, int poststart){
			Node node = new Node(pretrav[prestart]);
			if(size == 1){
				return node; // base case
			}else{
				int totalSubtreesSize = 0;
				int subtreeSize = 0;
				prestart++; // go to the first child of the node
				while(totalSubtreesSize < size - 1){ 
				    subtreeSize = 1; // initialize subtree size
				    while(posttrav[poststart + subtreeSize - 1] != pretrav[prestart]){
						subtreeSize++; // determine subtree size for the child
					}					
					node.addChild(buildTree(subtreeSize, prestart, poststart)); // recursive call
					prestart += subtreeSize;
					poststart += subtreeSize;
					totalSubtreesSize += subtreeSize;
				}
				return node;
			}
		}
		
		/**
		 * Traverse and print the tree in level order
		 * @param p Reference to the root of the tree
		 */
		public void traverseLevelOrder(Node p){
			LinkedList<Node> helperQueue = new LinkedList<Node>();
			helperQueue.add(p);
			Node q;
			String sep = ", ";
			while(!helperQueue.isEmpty()){
				q = helperQueue.remove();
				Node[] off = q.getOffspring();
				for(int i = 0; i < q.getNchildren(); i++){
					helperQueue.add(off[i]);
				}
				if(helperQueue.isEmpty()){
					sep = ".";
				}
				System.out.print(q.getName() + sep);
			}
		}		 
		
	}

}
