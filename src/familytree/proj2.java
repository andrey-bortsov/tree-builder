package familytree;

import java.util.LinkedList;
import java.util.Scanner;

public class proj2 {
	
	public static final String BEGIN_PREORDER_LINE = "<";
	public static final String BEGIN_POSTORDER_LINE = ">";
	public static char[] pretrav;
	public static char[] posttrav;
	FamilyTree ft;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		proj2 ftb = new proj2();
		ftb.readPreorder(sc);
		ftb.readPostorder(sc);
		ftb.ft.mainRoot = ftb.ft.buildTree(pretrav.length, 0, 0);
		

	}
	
	public proj2(){
		ft = new FamilyTree();
	}
	
	private void readPreorder(Scanner sc){
		if(sc.hasNext()){
			String s = sc.next();
			if(s.equals(BEGIN_PREORDER_LINE)){
				String preorder = sc.nextLine();
				int j = 0;
				for(int i = 0; i < preorder.length(); i++){
					char c = preorder.charAt(i);
					if(c != ',' && c != ' ' && c != '.'){
						pretrav[j] = c;
						j++;
					}
				}
			}
		}
	}
	
	private void readPostorder(Scanner sc){
		if(sc.hasNext()){
			String s = sc.next();
			if(s.equals(BEGIN_POSTORDER_LINE)){
				String postorder = sc.nextLine();
				int j = 0;
				for(int i = 0; i < postorder.length(); i++){
					char c = postorder.charAt(i);
					if(c != ',' && c != ' ' && c != '.'){
						posttrav[j] = c;
						j++;
					}
				}
			}
		}
	}
	
	public class FamilyTree{
		public int size;
		public Node mainRoot;
		
		public class Node{
			public char name;
			public Node[] offspring;
			public int nChildren;
			public Node parent;
			public int mark;
			
			public Node(char e){
				name = e;
				offspring = new Node[256]; // set to tree size - 1
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
			

		}
		
		public Node lookup(char e, Node root){
			if(root.getName() == e){
				return root;
			}else{
				Node node = null;
				Node[] children = root.getOffspring();
				for(int i = 0; i < root.nChildren; i++){
					Node root1 = children[i];
					node = lookup(e, root1);
					if(node != null){
						return node;
					}
				}
				return null;
			}
		}
		
		
		public int[] relate(Node a, Node b){
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
				relation = "'s (great)^" + (second - 2) + " grandparent";
			}else if(first == 0 && second == 0){
				relation = "...";
			}else if(first == 0 && second == 0){
				relation = "...";
			}else if(first == 0 && second == 0){
				relation = "...";
			}else if(first == 0 && second == 0){
				relation = "...";
			}else if(first == 0 && second == 0){
				relation = "...";
			}
			
			System.out.println(a.getName() + " is " + b.getName() + relation);
		}
		
		
		
		public void clearMarks(Node a){
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
			LinkedList<Node> myQueue = new LinkedList<Node>();
			myQueue.add(p);
			Node q;
			String sep = ", ";
			while(!myQueue.isEmpty()){
				q = myQueue.remove();
				Node[] off = q.getOffspring();
				for(int i = 0; i < q.nChildren; i++){
					myQueue.add(off[i]);
				}
				if(myQueue.isEmpty()){
					sep = ".";
				}
				System.out.print(q.getName() + sep);
			}
		}
		
		
		 
		
	}

}
