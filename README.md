HUFFMAN ENCODING & ZIPPING
This repository contains a Java implementation of Huffman Encoding and Zipping. The provided code demonstrates how to use Huffman Encoding to compress a text file and then create a ZIP archive of the encoded data.

Overview
Huffman Encoding: A lossless data compression algorithm that assigns variable-length codes to input characters based on their frequencies. More frequent characters receive shorter codes.
Zipping: Compresses the encoded data into a ZIP file.

Files
HuffmanZipper.java: The main Java class that performs Huffman encoding and creates a ZIP file.
BitOutputStream.java: A utility class for writing bits to an OutputStream.

How It Works
Build the Huffman Tree: Analyzes the input file to build the Huffman Tree based on character frequencies.
Generate Huffman Codes: Creates binary codes for each character based on the Huffman Tree.
Encode the File: Converts the input file into encoded binary data using the Huffman codes.
Create a ZIP File: Compresses the encoded data into a ZIP file.
