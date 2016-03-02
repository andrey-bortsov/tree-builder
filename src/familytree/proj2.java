package familytree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * This program builds a general tree using preorder and postorder 
 * traversals, and determines the relationship between any two nodes.
 * @author bortsov
 */
public class proj2 {
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
	private FamilyTree ft;

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("E:\\WORKFOLDERS\\CSC_316\\Projects\\P2\\temp\\medium-input.txt"));
		proj2 ftb = new proj2();
		ftb.readPreorder(reader);
		ftb.readPostorder(reader);
		ftb.ft.mainRoot = ftb.ft.buildTree(pretrav.length, 0, 0);
		String queryString = "";
		while(queryString != null){
			queryString = reader.readLine();
			char[] pair = ftb.readQuery(queryString);
			if(pair != null){
			ftb.ft.printRelationship(ftb.ft.lookup(pair[0], ftb.ft.mainRoot), 
					                 ftb.ft.lookup(pair[1], ftb.ft.mainRoot));
			}
		}
		ftb.ft.traverseLevelOrder(ftb.ft.mainRoot);
	}
	
	/**
	 * Constructor for proj2 class
	 */
	public proj2(){
		ft = new FamilyTree();
	}
	
	/**
	 * Read the preorder sequence into an array.
	 * Correct format begins with '<', names are single characters comma-separated,
	 * and end with a period. May span more than one line.
	 * @param br BufferedReader input
	 * @throws IOException
	 */
	public void readPreorder(BufferedReader br) throws IOException{
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
	public void readPostorder(BufferedReader br) throws IOException{
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
	public char[] readQuery(String s){
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
	 * Inner class for the tree object
	 */
	private class FamilyTree{
		public int size;
		public Node mainRoot;
		
		/**
		 * Inner class for the tree node.
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
			 * Node constructor
			 * @param e Character name
			 */
			public Node(char e){
				name = e;
				offspring = new Node[pretrav.length - 1];
				nChildren = 0;
				mark = -1;
			}
			
			public void addChild(Node c){
				offspring[nChildren] = c;
				nChildren++;
				c.setParent(this);
			}
			
			public Node getParent(){
				return parent;
			}
			
			public void setParent(Node p){
				parent = p;
			}
			
			public char getName(){
				return name;
			}
			
			public Node[] getOffspring(){
				return offspring;
			}
			
			public void checkMark(int i){
				mark = i;
			}
			
			public void uncheckMark(){
				mark = -1;
			}
			
			public int getMark(){
				return mark;
			}
			
			public int getNchildren(){
				return nChildren;
			}

		}
		
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
		
		
		
		private void clearMarks(Node a){
			while(a.getParent() != null){
				a.uncheckMark();
				a = a.getParent();		
			}
		}
		
		
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
