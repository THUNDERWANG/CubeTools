package pizza.cube;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.HashSet;

/**
 * This class contains the core methods used by every other class.
 * 
 * @author THUNDERWANG
 * @since June 11th, 2020
 */
public class Core {

    /**
     * Scans through directories and calls a key method on every file.
     * 
     * @param item      folder or file
     * @param consumer method that will be called for every file
     */
    static void scanDir(File item, Consumer<File> consumer) {
        if (item.isFile()) {
            consumer.accept(item);
        } else if (item.isDirectory()) {
            File[] itemsInDirc = item.listFiles();
            for (File items : itemsInDirc) {
                scanDir(items, consumer);
            }
        }
    }
    
    /**
     * Creates a file or replaces an existing one.
     * 
     * @param fileString file name
     */
    static boolean replaceFile(String fileString) {
        boolean success = false;
        try {
            File file = new File(fileString);
            if (!(success = file.createNewFile())) {
                file.delete();
                success = file.createNewFile();
            }
        } catch (IOException exception) {
            System.out.println("(IO) Exception in replaceFile:\n" + exception);
        }
        return success;
    }

    /**
     * Loads a deck file into a List&lt;String&gt;
     * 
     * @param file deck file
     * @return the cards in a List&lt;String&gt;
     */
    static List<String> fileToArray(File file) {
        List<String> fileArray = new ArrayList<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String cardInFile;
            while ((cardInFile = bufferedReader.readLine()) != null) {
                if (!cardInFile.isBlank()) {
                    fileArray.add(cardInFile.trim());
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException exception) {
            System.out.println("(FNF) Exception in fileToArray method:\n" + exception);
        } catch (IOException exception) {
            System.out.println("(IO) Exception in fileToArray method:\n" + exception);
        }
        return fileArray;
    }

    /**
     * Converts any deck file to a string separated by a new line
     * 
     * @param file deck file
     * @return deck file as a String
     */
    static String fileToString(File file) {
        StringBuilder deckString = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String cardInFile;
            while ((cardInFile = bufferedReader.readLine()) != null) {
                if (!cardInFile.isBlank()) {
                    deckString.append(cardInFile.trim() + "\n");
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException exception) {
            System.out.println("(FNF) Exception in fileToString method:\n" + exception);
        } catch (IOException exception) {
            System.out.println("(IO) Exception in fileToString method:\n" + exception);
        }
        return deckString.toString();
    }

    /**
     * Converts any deck file to a string with cards separated by a new line
     * 
     * @param file deck file
     * @return deck file as a Set
     */
    static Set<String> fileToSet(File file) {
        Set<String> fileSet = new HashSet<String>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String cardInFile;
            while ((cardInFile = bufferedReader.readLine()) != null) {
                if (!cardInFile.isBlank()) {
                    fileSet.add(cardInFile.trim());
                }
            }
            bufferedReader.close();
        } catch (IOException exception) {
            System.out.println("(IO) Exception in fileToSet method:\n" + exception);
        }
        return fileSet;
    }
}