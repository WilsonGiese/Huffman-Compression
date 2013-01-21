import java.io.*;

/* NOTE: The majority of this class was provided by 
 * Professor David Galles from the University of San Francisco. 
 * However, the code was modified to create a buffered bit 
 * writer and reader. */ 

public class BinaryFile {
	private boolean inputFile;
	private RandomAccessFile file;
	private byte buffer;
	private int buf_length;
	private int total_bits;
	private int bitsleft;
	private int bitsread;
	private int buffer_index;  
	private char[] char_buffer; //Used for buffered writing
	private byte[] byte_buffer; //Used for buffered reading
	
	private final int BUFFER_SIZE;
	private static final int DEFAULT_BUFFER_SIZE = 4096; /* 4KB */ 

	/**
	 * Binary File constructor. Open a file for reading, or create a file for
	 * writing. If we create a file, and a file already exists with that name,
	 * the old file will be removed.
	 * 
	 * @param filename
	 *            The name of the file to read from or write to
	 * @param readOrWrite
	 *            'w' or 'W' for an output file (open for writing), and 'r' or
	 *            'R' for an input file (open for reading)
	 */
	public BinaryFile(String filename, char readOrWrite) {
		buffer = (byte) 0;
		buf_length = 0;
		total_bits = 0;
		bitsleft = 0;
		bitsread = 0;
		buffer_index = 0; 
		BUFFER_SIZE = DEFAULT_BUFFER_SIZE; 

		try {
			if (readOrWrite == 'w' || readOrWrite == 'W') {
				inputFile = false;
				file = new RandomAccessFile(filename, "rw");
				file.writeInt(0); /* header -- # of bits in the file */
				char_buffer = new char[BUFFER_SIZE]; 
			} else if (readOrWrite == 'r' || readOrWrite == 'R') {
				inputFile = true;
				file = new RandomAccessFile(filename, "r");
				total_bits = file.readInt();
				byte_buffer = new byte[BUFFER_SIZE]; 
			}
		} catch (Exception e) {
			System.out.println("In BinaryFile(): " + e.getMessage());
			System.exit(0);
		}
	}
	
	public BinaryFile(String filename, char readOrWrite, int BUFFER_SIZE) {
		buffer = (byte) 0;
		buf_length = 0;
		total_bits = 0;
		bitsleft = 0;
		bitsread = 0;
		buffer_index = 0; 
		if(BUFFER_SIZE > 0) { 
			this.BUFFER_SIZE = BUFFER_SIZE; 
		} else { 
			this.BUFFER_SIZE = DEFAULT_BUFFER_SIZE; 
		}

		
		char_buffer = new char[BUFFER_SIZE]; 
 
		
		try {
			if (readOrWrite == 'w' || readOrWrite == 'W') {
				inputFile = false;
				file = new RandomAccessFile(filename, "rw");
				file.writeInt(0); /* header -- # of bits in the file */
			} else if (readOrWrite == 'r' || readOrWrite == 'R') {
				inputFile = true;
				file = new RandomAccessFile(filename, "r");
				total_bits = file.readInt();
			}
		} catch (Exception e) {
			System.out.println("In BinaryFile(): " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Checks to see if we are at the end of a file. This method is only valid
	 * for input files, calling EndOfFile on an output fill will cause the
	 * program to halt execution. 
	 * 
	 * @return True if we are at the end of an input file, and false otherwise
	 */
	public boolean EndOfFile() {
		Assert.notFalse(inputFile, "EndOfFile only relevant for input files");
		return bitsread == total_bits;
	}

	/**
	 * Read in the next 8 bits to the input file, and interpret them as a
	 * character. This method is only valid for input files, and will halt
	 * exception of called on an output file.
	 * 
	 * @return The next character from an input file
	 */
	public char readChar() {
		int charbuf = 0;
		int revcharbuf = 0;
		int i;

		Assert.notFalse(inputFile, "Can only read from input files");
		for (i = 0; i < 8; i++) {
			charbuf = charbuf << 1;
			if (readBit()) {
				charbuf += 1;
			}
		}
		for (i = 0; i < 8; i++) {
			revcharbuf = revcharbuf << 1;
			revcharbuf += charbuf % 2;
			charbuf = charbuf >> 1;
		}
		
		return (char) revcharbuf;
	}

	/**
	 * Write a character to an output file. The 8 bits representing the
	 * character are written one at a time to the file. This method is only
	 * valid for output files, and will halt execution if called on an input
	 * file.
	 * 
	 * @param c
	 *            The character to write to the output file.
	 */
	public void writeChar(char c) {
		Assert.notFalse(!inputFile, "Can only write to output files");

		int i;
		int charbuf = (int) c;
		for (i = 0; i < 8; i++) {
			writeBit(charbuf % 2 > 0);
			charbuf = charbuf >> 1;
		}
	}

	/**
	 * Write a bit to an output file This method is only valid for output files,
	 * and will halt execution if called on an input file. 
	 * 
	 * @param bit
	 *            The bit to write. false writes a 0 and true writes a 1.
	 */
	public void writeBit(boolean bit) {
		byte bit_;
		Assert.notFalse(!inputFile, "Can't write to an input file");
		total_bits++;

		if (bit)
			bit_ = 1;
		else
			bit_ = 0;
		buffer |= (bit_ << (7 - buf_length++));
		try {
			if (buf_length == 8) {
				char_buffer[buffer_index++] = (char)buffer; 
				//file.writeByte(buffer);
				buf_length = 0;
				buffer = 0;
				
				/* If the buffer is full, write it! */ 
				if(buffer_index == BUFFER_SIZE) { 
					file.writeBytes(new String(char_buffer)); 
					
					buffer_index = 0; 
				}
			}
		} catch (Exception e) {
			System.out.println("In writeBit(): " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Read a bit from an input file. This method is only valid for input files,
	 * and will halt execution if called on an output file. 
	 * 
	 * @return The next bit in the input file -- false for 0 and true for 1.
	 */
	public boolean readBit() {
		Assert.notFalse(inputFile, "Can't read from an output file");
		Assert.notFalse(bitsread < total_bits, "Read past end of file");
		try {
			if (bitsleft == 0) {
				if(buffer_index == 0 || buffer_index == BUFFER_SIZE) { 
					file.read(byte_buffer); /* Fill Buffer */ 
					buffer_index = 0;
				}
				buffer = byte_buffer[buffer_index++]; 
				bitsleft = 8;
			}
		} catch (Exception e) {
			System.out.println("In readBit(): " + e.getMessage());
			System.exit(0);
		}
		bitsread++; 
		return (((buffer >> --bitsleft) & 0x01) > 0);
	}

	/**
	 * Close the file (works for input and output files). Output files will not
	 * be properly written to disk if this method is not called.
	 */
	public void close() {
		try {
			if (!inputFile) {
				if (buf_length != 0) {
					while (buf_length < 8) {
						buffer |= (0 << (7 - buf_length++));
					}
					/* Add final char to buffer */ 
					char_buffer[buffer_index] = (char)buffer; 
					//file.writeByte(buffer);
				} 
				/* Write final buffer */ 
				file.writeBytes(new String(char_buffer)); 
				file.seek(0);
				file.writeInt(total_bits);
			}
			file.close();
		} catch (Exception e) {
			System.out.println("In close(): " + e.getMessage());
			System.exit(0);
		}
	}
}