import java.io.File;
import pizza.cube.*;

public class Entry {

    public static void main(String[] args) {

        File chickenDinner = new File("src\\main\\resources\\Chicken Dinner UPDATED 7-16-20");
        File powerFile = new File("src\\main\\resources\\Subset Lists\\Power.txt");
        File sampleFolder = new File("src\\main\\resources\\Sample");
        File exDeck = new File("src\\main\\resources\\Sample\\Dimir Reanimator MTGO.txt");
    
        // Primary Functions

        // Change file Extensions
        // Path.fixExtAll(sampleFolder, ".txt");

        // Change Encoding to UTF-8
        // Scan.encodeAll(sampleFolder);

        // Append a tag based on a list of cards
        // Path.appendAll(sampleFolder, queryFile, "MTGO");

        // Find tags and write to "Results" folder
        // Scan.findTagsAll(chickenDinner, "Power");

        // Find unlabeled Power lists (No longer needed)
        // Scan.findUnlabelAll(sampleFolder, powerFile);
        
        // Commonly Used Functions
        // Path.replaceAll(chickenDinner, "(MTGO Vintage)", "[WOTC Vintage]");
        // Path.replaceAll(chickenDinner, "[MTGO Vintage]", "[WOTC Vintage]");
        // Path.fixExtAll(chickenDinner, ".txt");
       

    }
}
