import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Decode {
	private BinaryFile iFile; 
	private BufferedWriter oFile; 
	private HuffmanNode head; 
	
	public Decode(String encodedFile, String targetFile) {
		this.iFile = new BinaryFile(encodedFile, 'r');
		File delete = new File(targetFile); 
		delete.delete(); //Removing output file if it already exists. 
		try {
			oFile = new BufferedWriter(new FileWriter(targetFile));
		} catch (IOException e) {
			System.out.println(targetFile + "(no such file or diretory)");
			System.exit(0); 
		} 
	}
	
	public void decode() {
		//First check if this file contains the magic number(Is compressed with this algorithm)
		try { 
			if(iFile.readChar() == 'H' && iFile.readChar() == 'C') {
				boolean bit;
				head = new HuffmanNode();
				HuffmanTree.buildHuffmanTree(iFile, head); //Rebuild serialized tree
				
				if(head.isLeaf()) { //Single char tree(no parent nodes)
					while(!iFile.EndOfFile()) {
						iFile.readBit(); 
						oFile.write(head.getCharacter()); //writeChar(head.getCharacter()); 
					}
				} else {
					HuffmanNode cur = head; 
					
					while(!iFile.EndOfFile()) { 
						bit = iFile.readBit(); 
						if(bit) {
							cur = cur.getRight(); 
						} else { 
							cur = cur.getLeft(); 
						}
						
						if(cur.isLeaf()) { 
							oFile.write(cur.getCharacter()); 
							cur = head; 
						}
					}
				}
				iFile.close(); 
				oFile.close(); 
			} else {
				System.out.println("Error could not decompress: This file has not been compressed with this format. ");
			}
		} catch (IOException e) { 
			System.out.println("Problem writing to the decoded file. Shutting down");
		}
	}
	
	//Print HuffmanTree
	public void printTree() {
		System.out.println("Printed Tree:\n");
		HuffmanTree.printTree(head, 0); 
	}
}