import java.io.*;
import java.util.*;
import java.util.zip.*;

class HuffmanNode {
    int frequency;
    char character;
    HuffmanNode left, right;

    HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        left = right = null;
    }
}

public class HuffmanZipper {

    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        String sourceFile = "C:\\Users\\Gnana chandrika\\Desktop\\experimet.txt";
        String zipFile = "C:\\Users\\Gnana chandrika\\Desktop\\demo.zip";

        try {
            Map<Character, String> huffmanCodes = buildHuffmanTreeAndGetCodes(sourceFile);
            zipFileWithHuffmanEncoding(sourceFile, zipFile, huffmanCodes);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static Map<Character, String> buildHuffmanTreeAndGetCodes(String filePath) throws IOException {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int character;
            while ((character = reader.read()) != -1) {
                frequencyMap.put((char) character, frequencyMap.getOrDefault((char) character, 0) + 1);
            }
        }

        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.frequency));
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            HuffmanNode newNode = new HuffmanNode('\0', left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;
            priorityQueue.add(newNode);
        }

        HuffmanNode root = priorityQueue.peek();
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateHuffmanCodes(root, "", huffmanCodes);

        return huffmanCodes;
    }

    private static void generateHuffmanCodes(HuffmanNode root, String code, Map<Character, String> huffmanCodes) {
        if (root == null) {
            return;
        }
        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.character, code);
        }
        generateHuffmanCodes(root.left, code + '0', huffmanCodes);
        generateHuffmanCodes(root.right, code + '1', huffmanCodes);
    }

    private static void zipFileWithHuffmanEncoding(String sourceFile, String zipFile, Map<Character, String> huffmanCodes) throws IOException {
        try (
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile), BUFFER_SIZE);
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            ZipEntry zipEntry = new ZipEntry(new File(sourceFile).getName());
            zos.putNextEntry(zipEntry);

            BitOutputStream bitOutputStream = new BitOutputStream(zos);
            int character;
            while ((character = bis.read()) != -1) {
                String code = huffmanCodes.get((char) character);
                for (char bit : code.toCharArray()) {
                    bitOutputStream.writeBit(bit == '1' ? 1 : 0);
                }
            }
            bitOutputStream.flush();
            zos.closeEntry();
        }
    }
}

// Utility class to write bits to an output stream
class BitOutputStream extends OutputStream {
    private final OutputStream out;
    private int currentByte;
    private int numBitsFilled;

    BitOutputStream(OutputStream out) {
        this.out = out;
        currentByte = 0;
        numBitsFilled = 0;
    }

    @Override
    public void write(int bit) throws IOException {
        if (bit != 0 && bit != 1) {
            throw new IllegalArgumentException("Only 0 and 1 bits are allowed");
        }
        writeBit(bit);
    }

    void writeBit(int bit) throws IOException {
        currentByte = (currentByte << 1) | bit;
        numBitsFilled++;
        if (numBitsFilled == 8) {
            flush();
        }
    }

    @Override
    public void flush() throws IOException {
        if (numBitsFilled > 0) {
            out.write(currentByte << (8 - numBitsFilled));
            numBitsFilled = 0;
            currentByte = 0;
        }
        out.flush();
    }

    @Override
    public void close() throws IOException {
        flush();
        out.close();
    }
}
