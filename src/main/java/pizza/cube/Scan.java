package pizza.cube;

import java.util.Set;
import java.util.function.Consumer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;

/**
 * This class contains methods to scan deck file names for certain tags or
 * checks whether a deck is tagged properly.
 * 
 * @author THUNDERWANG
 * @since June 11th, 2020
 */

public class Scan {

    /**
     * //TODO: Consider another option to count
     * Count how many tagged files found. Resets in {@link #findTagsAll(File, String)}.
     */
    private static int fileTagCount = 0;

    /**
     * Searches for tags within a deck file name.
     * 
     * @param file           deck file
     * @param tag            tag to search for
     * @param bufferedWriter writer of results file
     */
    static void searchFileTag(File file, String tag, BufferedWriter bufferedWriter) {
        try {
            if (file.getName().toUpperCase().contains(tag.toUpperCase())) {
                Scan.fileTagCount++;
                bufferedWriter.write(file.getPath() + "\n\n");
                bufferedWriter.flush();
            }
        } catch (IOException exception) {
            System.out.println("(IO) Exception in searchFileTag:\n" + exception);
        }
    }

    /**
     * Scans through directories for tags in the file names and writes the
     * results to the file "Results\\Found Tags.txt".
     * 
     * @param file directory of files
     * @param tag  tag of interest (eg. "Reanimator", "MTGO", "Power")
     * @see #searchFileTag(File, String, BufferedWriter)
     * @see Core#scanDir(File, Consumer)
     */
    public static void findTagsAll(File file, String tag) {
        try {
            new File("src\\main\\resources\\Results").mkdir();
            Core.replaceFile("src\\main\\resources\\Results\\Tags Found.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(
            "src\\main\\resources\\Results\\Tags Found.txt"), true));
            Consumer<File> consumer = (deckFile) -> searchFileTag(deckFile, tag, bufferedWriter);
            Core.scanDir(file, consumer);
            bufferedWriter.close();
            System.out.println(tag + " tag found: " + Scan.fileTagCount);
            Scan.fileTagCount = 0;
        } catch (IOException exception) {
            System.out.println("(IO) Exception in findTagsAll:\n" + exception);
        }
    }

    /**
     * Converts a file to UTF-8 encoding and adds in the missing apostrophe from the
     * ANSI encoded files.
     * 
     * @param item deck file to scan
     */
    static void encodeFile(File item) {
        try {
            String deckList = Core.fileToString(item);
            deckList = deckList.replace("�", "'");
            FileWriter fileWriter = new FileWriter(item);
            fileWriter.write(deckList);
            fileWriter.close();
        } catch (IOException exception) {
            System.out.println("(IO) Exception in encodeFile:\n" + exception);
        }
    }

    /**
     * Scans a directory and converts all files to UTF-8 and adds in the
     * missing apostrophe from the ANSI encoded files.
     * 
     * @param folder directory of files
     * @see #encodeFile(File)
     * @see Core#scanDir(File, Consumer)
     */
    public static void encodeAll(File folder) {
        Consumer<File> consumer = (deckFile) -> encodeFile(deckFile);
        Core.scanDir(folder, consumer);
    }

    /**
     * Count how many unlabel files found. Resets in
     * {@link #findUnlabelAll(File, String)}.
     */
    private static int unlabelCount = 0;

    /**
     * Scans a deck file name to see if it is unlabeled.
     * <p>
     * A deck is unlabeled if both of the following conditions are true:
     * 1. There is power in the deck.
     * 2. The deck file name is missing <b>both</b> "MTGO" and "Power".
     * </p>
     *
     * @param deckFile  deck folder or file
     * @param querySet set of key cards
     */
    static void checkUnLabelFile(File file, Set<String> powerSet, BufferedWriter bufferedWriter, StringBuilder stringBuilder) {
        try {
            if (!file.getName().toUpperCase().contains("[")) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String cardInDeck;
                boolean cardFound = false;
                while ((cardInDeck = bufferedReader.readLine()) != null) {
                    for (String queryCard : powerSet) {
                        if (cardInDeck.toUpperCase().contains(queryCard.toUpperCase())) {
                            cardFound = true;
                            stringBuilder.append(queryCard + " \n");
                        }
                    }
                }
                bufferedReader.close();
                if (cardFound) {
                    Scan.unlabelCount++;
                    bufferedWriter
                            .write("Flagged Deck: " + file.getPath() + "\n" + stringBuilder.toString().trim() + "\n\n");
                    bufferedWriter.flush();
                }
            }
        } catch (IOException exception) {
            System.out.println("(IO) Exception in encodeFile:\n" + exception);
        }
    }
    
    /**
     * <p>Scans directories to see if deck files are unlabeled.</p>
     *
     * A deck is unlabeled if both of the following conditions are true:
     * <p>1. There is power in the deck.</p>
     * <p>2. The deck file name is missing <b>both</b> "MTGO" and "Power".</p>
     *
     * @param folder    folder directory of files
     * @param powerFile file of power 9
     * @see #checkUnLabelFile(File, Set, BufferedWriter, StringBuilder)
     * @see Core#scanDir(File, Consumer)
     */
    //TODO: Update?
    public static void findUnlabelAll(File folder, File powerFile) {
        try {
            new File("src\\main\\resources\\Results").mkdir();
            Core.replaceFile("src\\main\\resources\\Results\\Unlabeled File Paths.txt");
            Set<String> powerSet = Core.fileToSet(powerFile);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src\\main\\resources\\Results\\Unlabeled File Paths.txt", true));
            StringBuilder stringBuilder = new StringBuilder();
            Consumer<File> consumer = (currentFile) -> checkUnLabelFile(currentFile, powerSet, bufferedWriter, stringBuilder);
            Core.scanDir(folder, consumer);
            bufferedWriter.close();
            System.out.println("Unlabel Count: " + Scan.unlabelCount);
            Scan.unlabelCount = 0;
        } catch (IOException exception) {
            System.out.println("(IO) exception in findUnlabelAll:\n" + exception);
        }
    }
}