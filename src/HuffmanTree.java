import java.util.ArrayList;

public class HuffmanTree {
	
	/* Build a Huffman tree from a serialized tree */
	public static void buildHuffmanTree(BinaryFile f, HuffmanNode root) {
		if(f.readBit()) {
			root.setCharacter(f.readChar()); 
			root.setIsLeaf(true); 
		} else { 
			root.setLeft(new HuffmanNode()); 
			root.setRight(new HuffmanNode()); 
			buildHuffmanTree(f, root.getLeft()); 
			buildHuffmanTree(f, root.getRight()); 
		}
	}
	
	/* Build tree from an array, and return the head */ 
	public static HuffmanNode buildHuffmanTree(int[] frequencyArray) {
		ArrayList<HuffmanNode> tree = new ArrayList<HuffmanNode>(); 
		
		//Adding all leaf nodes to the list
		int frequency; 
		for(int i = 0; i < frequencyArray.length; i++) {
			frequency = frequencyArray[i]; 
			if(frequency != 0) { 
				addToList(tree, new HuffmanNode(frequency, (char)i)); 
			}
		}
		
		while(tree.size() > 1) { 
			//Adding the new parent node
			addToList(tree, new HuffmanNode(tree.get(0), tree.get(1)));
			tree.remove(1); 
			tree.remove(0); 
		}
		return tree.get(0); 
	}
	
	/* This method adds a new element in the correct position based on natural ordering. */ 
	private static boolean addToList(ArrayList<HuffmanNode> list, HuffmanNode node) {
		for(int i = 0; i < list.size(); i++) {
			if(node.compareTo(list.get(i)) <= 0) { 
				list.add(i, node); 
				return true; 
			}
		} 
		list.add(node); 
		return true; 
	}
	
	public static void printTree(HuffmanNode root, int tabs) { 
		if(root != null) { 
			for(int i = 0; i < tabs; i++) { 
				System.out.print("  ");
			}
			if(root.isLeaf()) { 
				System.out.println((int)root.getCharacter());
			} else { 
				System.out.println("."); //Represents internal node. 
			}
			tabs++; 
			printTree(root.getLeft(), tabs); 
			printTree(root.getRight(), tabs); 
		} 
	}
}