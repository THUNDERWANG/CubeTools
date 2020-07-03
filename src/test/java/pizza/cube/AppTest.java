package pizza.cube;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.function.Consumer;

/**
 * Unit test for simple App.
 */
class AppTest {

    public static void main(String[] args) {

        File chickenDinner = new File("Tagger\\src\\main\\resources\\Chicken Dinner UPDATED 6-16-20");
        File queryFile = new File("Tagger\\src\\main\\resources\\Subset Lists\\Power.txt");
        File sampleFolder = new File("Tagger\\src\\main\\resources\\Sample");
        File exDeck = new File("Tagger\\src\\main\\resources\\Sample\\Dimir Reanimator MTGO.txt");

        Path.replaceAll(sampleFolder, "Power", "");
        


    }


    
}
