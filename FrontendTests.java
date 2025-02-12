import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import java.io.ByteArrayInputStream;

public class FrontendTests {

    /**
     * Test loading data using the "load" command.
     * Expected output: "Data loaded successfully"
     */
    @Test
    public void frontendTest1() {
        String input = "load data.txt\nquit\n"; //Simulated user input
        TextUITester tester = new TextUITester(input); //Create a new TextUITester object

        //Create a new Backend_Placeholder object and a new Frontend object to simulate a test environment
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(new Scanner(new ByteArrayInputStream(input.getBytes())), backend);

        frontend.runCommandLoop(); //Run the command loop

        String output = tester.checkOutput(); //Check the output printed to System.out
        assertTrue(output.contains("Data loaded successfully")); //Verify expected output
    }

    /**
     * Test setting an energy range using the "energy MIN to MAX" command.
     * Expected output includes: "Songs with energy between 50 and 100"
     */
    @Test
    public void frontendTest2() {
        String input = "energy 50 to 100\nquit\n"; //Simulated user input
        TextUITester tester = new TextUITester(input); //Create a new TextUITester object

        //Create a new Backend_Placeholder object and a new Frontend object to simulate a test environment
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(new Scanner(new ByteArrayInputStream(input.getBytes())), backend);

        frontend.runCommandLoop(); //Run the command loop

        String output = tester.checkOutput(); //Check the output printed to System.out
        assertTrue(output.contains("Songs with energy between 50 and 100")); //Verify expected output
    }

    /**
     * Test setting a danceability threshold using the "danceability MIN" command.
     * Expected output includes: "Songs with danceability above 70"
     */
    @Test
    public void frontendTest3() {
        String input = "danceability 70\nquit\n"; //Simulated user input
        TextUITester tester = new TextUITester(input); //Create a new TextUITester object

        //Create a new Backend_Placeholder object and a new Frontend object to simulate a test environment
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(new Scanner(new ByteArrayInputStream(input.getBytes())), backend);

        frontend.runCommandLoop(); //Run the command loop

        String output = tester.checkOutput(); //Check the output printed to System.out
        assertTrue(output.contains("Songs with danceability above 70")); //Verify expected output
    }

    /**
     * Test displaying the five most recent songs using the "show most recent" command.
     * // Expected output includes: "Five most recent songs"
     */
    @Test
    public void frontendTest4() {
        String input = "show most recent\nquit\n"; //Simulated user input
        TextUITester tester = new TextUITester(input); //Create a new TextUITester object

        //Create a new Backend_Placeholder object and a new Frontend object to simulate a test environment
        Tree_Placeholder tree = new Tree_Placeholder(); 
        Backend_Placeholder backend = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(new Scanner(new ByteArrayInputStream(input.getBytes())), backend);

        frontend.runCommandLoop(); //Run the command loop

        String output = tester.checkOutput(); //Check the output printed to System.out
        assertTrue(output.contains("Five most recent songs")); //Verify expected output
    }

    /**
     * Test displaying the first N songs using the "show MAX_COUNT" command.
     * Expected output includes: "First 3 songs"
     */
    @Test
    public void frontendTest5() {
        String input = "show 3\nquit\n"; //Simulated user input
        TextUITester tester = new TextUITester(input); //Create a new TextUITester object

        //Create a new Backend_Placeholder object and a new Frontend object to simulate a test environment
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(new Scanner(new ByteArrayInputStream(input.getBytes())), backend);

        frontend.runCommandLoop(); //Run the command loop

        String output = tester.checkOutput(); //Check the output printed to System.out
        assertTrue(output.contains("First 3 songs")); //Verify expected output
    }

    /**
     * Test handling an invalid command.
     * // Expected output: "Error: Unknown command"
     */
    @Test
    public void frontendTest6() {
        String input = "invalid command\nquit\n"; //Simulated user input
        TextUITester tester = new TextUITester(input); //Create a new TextUITester object

        //Create a new Backend_Placeholder object and a new Frontend object to simulate a test environment
        Tree_Placeholder tree = new Tree_Placeholder();
        Backend_Placeholder backend = new Backend_Placeholder(tree);
        Frontend frontend = new Frontend(new Scanner(new ByteArrayInputStream(input.getBytes())), backend);

        frontend.runCommandLoop(); //Run the command loop

        String output = tester.checkOutput(); //Check the output printed to System.out
        assertTrue(output.contains("Error: Unknown command")); //Verify expected output
    }
}


