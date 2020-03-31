This file contains instructions to compile and run the code.

Step 1  - FTP to the server and transfer the zipped folder. Unzip the folder and change the working directory to this folder.

Step 2 - Compile the programs by typing 
			java *.java
	
Step 3 - To run Tokenizer, type
 			java Tokenize <path_to_Cranfield_collection>
			
	Here, path_to_Cranfield_collection = /people/cs/s/sanda/cs6322/Cranfield
	
Step 4 - To run stemmer, type

	java Stemmer
	
The output files generated are:
	token_counts.txt : contains individual tokens and their frequencies in the Cranfield Collection
	
	stems.txt : contains individual stems and their frequencies
	
Here, Stemmer is run against the entire corpus not the vocabulary.