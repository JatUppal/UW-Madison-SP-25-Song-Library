import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Scanner;

public class FrontendTests {

    /**
     * Test loading data using the "load" command.
     */
    @Test
    public void frontendTest1() {
        // Simulate user input: "load data.txt" followed by "quit"
        String input = "load data.txt\nquit\n";
        TextUITester tester = new TextUITester(input);

        // Create a backend placeholder and frontend instance
        Backend_Placeholder backend = new Backend_Placeholder(null);
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        // Run the command loop
        frontend.runCommandLoop();

        // Verify the output contains the success message
        String output = tester.checkOutput();
        assertTrue(output.contains("Data loaded successfully"));
    }

    /**
     * Test setting an energy range using the "energy MIN to MAX" command.
     */
    @Test
    public void frontendTest2() {
        // Simulate user input: "energy 50 to 100" followed by "quit"
        String input = "energy 50 to 100\nquit\n";
        TextUITester tester = new TextUITester(input);

        // Create a backend placeholder and frontend instance
        Backend_Placeholder backend = new Backend_Placeholder(null);
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        // Run the command loop
        frontend.runCommandLoop();

        // Verify the output contains the energy range message
        String output = tester.checkOutput();
        assertTrue(output.contains("Songs with energy between 50 and 100"));
    }

    /**
     * Test setting a danceability threshold using the "danceability MIN" command.
     */
    @Test
    public void frontendTest3() {
        // Simulate user input: "danceability 70" followed by "quit"
        String input = "danceability 70\nquit\n";
        TextUITester tester = new TextUITester(input);

        // Create a backend placeholder and frontend instance
        Backend_Placeholder backend = new Backend_Placeholder(null);
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        // Run the command loop
        frontend.runCommandLoop();

        // Verify the output contains the danceability threshold message
        String output = tester.checkOutput();
        assertTrue(output.contains("Songs with danceability above 70"));
    }

    /**
     * Test displaying the five most recent songs using the "show most recent" command.
     */
    @Test
    public void frontendTest4() {
        // Simulate user input: "show most recent" followed by "quit"
        String input = "show most recent\nquit\n";
        TextUITester tester = new TextUITester(input);

        // Create a backend placeholder and frontend instance
        Backend_Placeholder backend = new Backend_Placeholder(null);
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        // Run the command loop
        frontend.runCommandLoop();

        // Verify the output contains the five most recent songs message
        String output = tester.checkOutput();
        assertTrue(output.contains("Five most recent songs"));
    }

    /**
     * Test displaying the first N songs using the "show MAX_COUNT" command.
     */
    @Test
    public void frontendTest5() {
        // Simulate user input: "show 3" followed by "quit"
        String input = "show 3\nquit\n";
        TextUITester tester = new TextUITester(input);

        // Create a backend placeholder and frontend instance
        Backend_Placeholder backend = new Backend_Placeholder(null);
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        // Run the command loop
        frontend.runCommandLoop();

        // Verify the output contains the first N songs message
        String output = tester.checkOutput();
        assertTrue(output.contains("First 3 songs"));
    }

    /**
     * Test handling an invalid command.
     */
    @Test
    public void frontendTest6() {
        // Simulate user input: "invalid command" followed by "quit"
        String input = "invalid command\nquit\n";
        TextUITester tester = new TextUITester(input);

        // Create a backend placeholder and frontend instance
        Backend_Placeholder backend = new Backend_Placeholder(null);
        Frontend frontend = new Frontend(new Scanner(System.in), backend);

        // Run the command loop
        frontend.runCommandLoop();

        // Verify the output contains the error message
        String output = tester.checkOutput();
        assertTrue(output.contains("Error: Unknown command"));
    }
}
