import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Encode {
	private int[] frequencyList;
	private String[] charMapping; 
	private HuffmanNode head; 
	private String inputFileName; 
	private BufferedInputStream iFile;
	private BinaryFile oFile; 
	
	public Encode(String inputFileName, String outputFileName) { 
		try {
			this.iFile = new BufferedInputStream(new FileInputStream(inputFileName));
		} catch (FileNotFoundException e) {
			System.out.println(inputFileName + "(no such file or diretory)");
		}
		
		File delete = new File(outputFileName); 
		delete.delete(); //Removing output file if it already exists. 
		this.oFile = new BinaryFile(outputFileName, 'w');
		this.inputFileName = inputFileName; 
		
		build(); 
	}
	
	/* Build huffman tree from the char frequency list */ 
	private void build() { 		
		//Create mappings and tree
		charMapping = new String[256];
		buildFrequencyMap();
		head = HuffmanTree.buildHuffmanTree(frequencyList);
		
		if(head.isLeaf()) { //Single character tree - Has no code designated by the tree; set bit value to 0.
			charMapping[(int)head.getCharacter()] = "0";  
		} else {
			buildCodeMap(head, "");
		}
		
	}
	
	private void buildFrequencyMap() {
		frequencyList = new int[256];
		int asciiValue;
		
		try {
			while((asciiValue = iFile.read()) != -1) { 
				frequencyList[asciiValue] = frequencyList[asciiValue] + 1; 
			}
			iFile =  new BufferedInputStream(new FileInputStream(inputFileName)); //Resetting the reader
		} catch (IOException e) {
			System.out.println("Error while reading the file.");
		}
	}
	
	/* Recursively build the character to code map */ 
	private void buildCodeMap(HuffmanNode root, String code) { 
		if(!root.isLeaf()) {
			buildCodeMap(root.getLeft(), (code + "0")); 
			buildCodeMap(root.getRight(), (code + "1")); 
		} else {
			charMapping[(int)root.getCharacter()] = code; 
		}
	}
	
	public void serializeTree(HuffmanNode root) {
		if(root.isLeaf()) {
			oFile.writeBit(true); //1 for leaf
			oFile.writeChar(root.getCharacter()); 
		} else {
			oFile.writeBit(false); //0 for parent
			serializeTree(root.getLeft()); 
			serializeTree(root.getRight()); 
		}
	}
	
	public void encode() {
		//Magic Number
		oFile.writeChar('H');
		oFile.writeChar('C'); 
		
		serializeTree(head);
		
		int asciiValue; 
		try {
			/* Get character codes and write to file */ 
			while((asciiValue = iFile.read()) != -1) { 
				writeBitsFromString(charMapping[asciiValue]);
			}
			iFile.close(); 
		} catch (IOException e) {
			System.out.println("Error while reading the file.");
		} 
		oFile.close(); 
	}
	
	/* Writes bits using the character code string */ 
	private void writeBitsFromString(String code) {
		for(int i = 0; i < code.length(); i++) {
			if(code.charAt(i) == '0') { 
				oFile.writeBit(false); 
			} else {
				oFile.writeBit(true); 
			}
		}
	}
	
	/* Checks encoded file size before compressing to avoid
	 * compressing the file if it will be larger. This can happens
	 * if the tree is very balanced. 
	 */
	public boolean checkEncodedFileSize() {
		int encodedFileSize = 48; //16 bits for HC, and 32 for header. 
		int inputFileSize = 0; 
		int leafCount = 0; 
		
		for(int i = 0; i < frequencyList.length; i++) { 
			if(frequencyList[i] != 0) { 
				encodedFileSize += frequencyList[i] * charMapping[i].length(); 
				inputFileSize += frequencyList[i] * 8; 
				leafCount++; 
			}
		}
		encodedFileSize += ((leafCount* 9) + (leafCount - 1));
		encodedFileSize += 8 - (encodedFileSize % 8); 
		if(encodedFileSize < inputFileSize) { 
			return true; 
		}
		return false; 
	}
	
	/* Prints a code for frequency table for each char present in the file. */ 
	public void printCodes() { 
		System.out.println("+-----+---------+-------------------------+");
		System.out.format("|%5s|%9s|%25s|", "Ascii", "Freq.", "Bit Codes");
		System.out.println("\n+-----+---------+-------------------------+");
		
		for(int i = 0; i < charMapping.length; i++) {
			if(charMapping[i] != null) {
				System.out.format("|%5d|%9d|%25s|", i, frequencyList[i], charMapping[i]);
				System.out.println("\n+-----+---------+-------------------------+");
			}
		}
	}
	
	public void printTree() {
		System.out.println("\nPrinted Tree:\n");
		HuffmanTree.printTree(head, 0); 
	}
}