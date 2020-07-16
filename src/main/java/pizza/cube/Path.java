package pizza.cube;

import java.util.Set;
import java.util.function.Consumer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This path contains static methods to manipulate deck file names based off of an existing string in the file name or cards it finds in the deck file.
 * 
 * @author THUNDERWANG
 * @since June 11th, 2020
 */
public class Path {

    /**
     * Rewrites file extensions. If name collison occurs, it will append a number to the current file.
     * 
     * @param file deck file
     * @param ext file extension (eg. ".txt")
     * @see #correctDuplicates(File)
     */
    static void toExt(File file, String ext) {

        String newFilePath = 
            !file.getName().contains(".")
            ? file.getPath() + ext 
            : file.getPath().substring(0, file.getPath().lastIndexOf(".")) + ext;
        
        if (!file.renameTo(new File(newFilePath))) {
            correctDuplicates(file, newFilePath);
        }
    }

    /**
     * Appends a number to a file name due to name collision.
     * 
     * @param file deck file
     * @see #toExt(File file)
     */
    static void correctDuplicates(File file, String newFilePath) {
        // Consider these cases: file, file.txt, file.rtf, file (1).txt
        
        String newFileName = newFilePath.substring(newFilePath.lastIndexOf("\\") + 1);
        int copyNumber = 1;
        boolean writeable = false;
        while (!writeable) {
            try {
                if (!newFileName.contains(".")) {
                    newFilePath = newFilePath + " (" + copyNumber + ")";
                } else {
                    int extPeriod = newFilePath.lastIndexOf(".");
                    newFilePath = newFilePath.substring(0, extPeriod) + " (" + copyNumber + ")" + newFilePath.substring(extPeriod);
                }
                writeable = file.renameTo(new File(newFilePath));
                copyNumber++;
            } catch (NullPointerException exception) {
                System.out.println("(NP) Exception in correctDuplicates: " + file.getPath() + " may not have been corrected.");
            }
        }
    }

    /**
     * Scans a folder and rewrites file extensions
     * 
     * @param folder folder that contains the deck files
     * @param ext    file extension (eg. ".txt")
     */
    public static void fixExtAll(File folder, String ext) {
        Consumer<File> consumer = (deckFile) -> toExt(deckFile, ext);
        Core.scanDir(folder, consumer);
    }

    /**
     * Appends a tag to a file name based on key cards.
     * <p>
     * It will first check to see if the file name has an existing string to prevent appending the same tag multiple times.
     * </p>
     * 
     * @param tag       string added to the file name
     * @param deckFile  deck file
     * @param querySet of key cards (eg. A list of P9)
     */
    static void appendFileName(File deckFile, Set<String> querySet, String tag) {
        try {
            if (!deckFile.getName().toUpperCase().contains(tag.toUpperCase())) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(deckFile));
                boolean cardFound = false;
                String cardInDeck;
                readloop: 
                while ((cardInDeck = bufferedReader.readLine()) != null) {
                    for (String queryCard : querySet) {
                        if (cardInDeck.toUpperCase().contains(queryCard.toUpperCase())) {
                            cardFound = true;
                            break readloop;
                        }
                    }
                }
                bufferedReader.close();
                if (cardFound) {
                    String newFilePath = deckFile.getPath();
                    if (!deckFile.getName().contains(".")) {
                        newFilePath = newFilePath + " " + tag;
                    } else {
                        int extPeriod = newFilePath.lastIndexOf(".");
                        newFilePath = newFilePath.substring(0, extPeriod) + " " + tag + newFilePath.substring(extPeriod);
                    }
                    if (!deckFile.renameTo(new File(newFilePath))) {
                        correctDuplicates(deckFile, newFilePath);
                    }
                }
            }
        } catch (FileNotFoundException exception) {
            System.out.println("FNF Exception in appendFileName: " + exception);
        } catch (IOException exception) {
            System.out.println("IO Exception in appendFileName: " + exception);
        }
    }

    /**
     * Scans a folder and appends tags to deck file names if the deck contains key cards.
     * 
     * @param folder folder of deck files
     * @param queryFile file of key cards
     * @param tag tag to append to file name
     */
    public static void appendAll(File folder, File queryFile, String tag) {
        Set<String> querySet = Core.fileToSet(queryFile);
        Consumer<File> consumer = (deckFile) -> appendFileName(deckFile, querySet, tag);
        Core.scanDir(folder, consumer);
    }

    /**
     * Replaces a tag in the deck file name with a replacement tag.
     * 
     * @param file        deck file
     * @param tag         existing tag
     * @param replacement tag to replace
     */
    static void replaceTag (File file, String tag, String replacement) {
        String fileName = file.getName();
        if (fileName.contains(tag)) {
            fileName = fileName.replace(tag, replacement);
            String newFilePath = file.getPath().substring(0, file.getPath().lastIndexOf("\\") + 1) + fileName;
            if (!file.renameTo(new File(newFilePath))) {
                Path.correctDuplicates(file, newFilePath);
            }
        }
    }
    
    /**
     * Scans a folder and replaces all tags in deck files with new replacement tags.
     * 
     * 
     * @param folder deck file
     * @param tag         existing tag
     * @param replacement tag to replace
     */
    public static void replaceAll (File folder, String tag, String replacement) {
        Consumer<File> consumer = (file) -> replaceTag(file,tag, replacement);
        Core.scanDir(folder, consumer);
    } 


}
