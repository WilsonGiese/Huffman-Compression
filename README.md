Huffman-Compression
===================

Author: Wilson Giese - giese.wilson@gmail.com

###About: 
This program has two functions: Compressing a file, and decompressing a 
previously compressed file. It will take in two files(any file type), 
compressing the first file and outputting the second file, or, it will take 
in a compressed file, and create a decompressed file. 


### Usage info: 
	
	-c Compress
	-u Uncompress
	-v* Verbose; Prints a table with frequency and codes for each ascii in the 
	    file(-c only), and a visual representation of the Huffman Tree. 
	-f* Force; Forces compression even if the encoded file will be larger than 
	    the non-encoded file. 

	* optional flag

### Usage examples: 	

	java -cp HCompress.jar Huffman -c -v -f path/To/InputFile   path/To//EncodedFile
	java -cp HCompress.jar Huffman -u -v    path/To/EncodedFile path/To//DecodedFile
	

### Unbuffered/Buffered time comparisons: 

Test System: 2.66 GHz Intel Core i7 with 8GB 1067 MHz DDR3

	Unbuffered Test on 4940645b file: 
	COMPRESS:           DECOMPRESS:  
	[1] 5008ms          [1] 2693ms
	[2] 5133ms          [2] 2694ms
	[3] 5536ms          [3] 2787ms
	[4] 5168ms          [4] 2719ms
	[5] 5335ms          [5] 2777ms

	Buffered Test on 4940645b file: BUFFER_SIZE = 4096b
	[1] 472ms           [1] 332ms
	[2] 462ms           [2] 293ms
	[3] 459ms           [3] 304ms
	[4] 462ms           [4] 327ms	
	[5] 457ms           [5] 302ms

	Buffered Test on 4940645b file: BUFFER_SIZE = 2048b
	[1] 466ms           [1] 315ms
	[2] 463ms           [2] 364ms
	[3] 465ms           [3] 355ms	
	[4] 470ms           [4] 351ms
	[5] 466ms           [5] 332ms
