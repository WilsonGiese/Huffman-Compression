
public class Huffman {
	
	private static boolean checkForFlag(String[] args, String flag) {
		for(String s : args) { 
			if(s.equals(flag)) { 
				return true; 
			}
		}
		return false; 
	}
	
	private static void printUsageExample() { 
		System.out.println("Warning: Missing arguments " + "\n\n" + 
				   "FLAGS:\n-c compress or -u uncompress\n-v verbose\n-f force\n\n" + 
				   "Usage example: java Huffman (-c|-u) [-v]* [-f]* inputFile outputFile" + 
				   "\n" + "* = optional flag"); 
	}
	
	public static void main(String[] args) {
		if(args.length < 3) { 
			printUsageExample(); 
		} else {
			String iFile = args[args.length - 2]; //Input and output files
			String oFile = args[args.length - 1]; //Will always be the last two.
			if(checkForFlag(args, "-c")) {
				long start = System.currentTimeMillis(); 
				Encode e = new Encode(iFile, oFile); 
				if(!e.checkEncodedFileSize() && !checkForFlag(args, "-f")) { 
					System.out.println("Cannot compress");
				} else { 
					e.encode(); 
					if(checkForFlag(args, "-v")) { 
						e.printCodes(); 
						e.printTree(); 
					}
				}
				long end = System.currentTimeMillis(); 
				System.out.println("Encode Runtime: " + (end - start) + "ms");
			} else if(checkForFlag(args, "-u")) { 
				long start = System.currentTimeMillis(); 
				Decode d = new Decode(iFile, oFile);
				d.decode(); 
				long end = System.currentTimeMillis(); 
				System.out.println("Decode Runtime: " + (end - start) + "ms");
				if(checkForFlag(args, "-v")) 
					d.printTree(); 
			} else { 
				printUsageExample(); 
			}
		} 
	}
}