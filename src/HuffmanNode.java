
public class HuffmanNode implements Comparable<HuffmanNode> {
	private int sum; 
	private char character; 
	private boolean isLeaf; 
	private HuffmanNode left; 
	private HuffmanNode right; 
	
	
	/* Constructors */ 
	public HuffmanNode(int sum, char character) {
		this.sum = sum; 
		this.character = character;
		this.isLeaf = true; 
	}
	
	public HuffmanNode(HuffmanNode left, HuffmanNode right) {
		this.left = left; 
		this.right = right; 
		this.isLeaf = false; 
		sum = left.getSum() + right.getSum();
	}
	
	public HuffmanNode() { 
		isLeaf = false; 
	}
	
	/* Comparable functions */ 
	@Override
	public int compareTo(HuffmanNode arg0) {
		return this.sum - arg0.getSum(); 
	}
	
	/* Getters and Setters */  
	public HuffmanNode getLeft() {
		return left;
	}

	public void setLeft(HuffmanNode left) {
		this.left = left;
	}

	public HuffmanNode getRight() {
		return right;
	}

	public void setRight(HuffmanNode right) {
		this.right = right;
	}
	
	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}
	
	public boolean isLeaf() {
		return isLeaf; 
	}
	
	public void setIsLeaf(boolean b) {
		this.isLeaf = b; 
	}
}
