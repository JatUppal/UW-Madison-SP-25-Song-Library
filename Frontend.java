import java.util.Scanner;
import java.util.List;
import java.io.IOException;

public class Frontend implements FrontendInterface {
    private Scanner scanner;
    private BackendInterface backend;

    /*
     * Constructor for the Frontend class.
     * 
     * @param scanner - Scanner object to read user input
     * @param backend - BackendInterface object to interact with the backend
     */
    public Frontend(Scanner scanner, BackendInterface backend) {
        this.scanner = scanner;
        this.backend = backend;
    }

    /*
     * Runs the command loop, allowing the user to enter commands until they type "quit".
     */
    @Override
    public void runCommandLoop() {
        displayCommandInstructions(); //Display the command instructions at the start
        while (true) {
            System.out.print("Enter command: "); //Prompt the user for input
            String command = scanner.nextLine().trim(); //Read the user's input
            if (command.equalsIgnoreCase("quit")) { //Check if the user wants to quit
                System.out.println("Exiting the program.");
                break;
            }
            executeSingleCommand(command); //Process the user's command
        }
    }

    /*
     * Displays instructions for the syntax of user commands.
     */
    @Override
    public void displayCommandInstructions() {
        System.out.println("Available commands:");
        System.out.println("load FILEPATH - Load data from the specified file.");
        System.out.println("energy MAX - Set the maximum energy level for songs.");
        System.out.println("energy MIN to MAX - Set the energy range for songs.");
        System.out.println("danceability MIN - Set the minimum danceability threshold.");
        System.out.println("show MAX_COUNT - Show the first MAX_COUNT songs.");
        System.out.println("show most recent - Show the five most recent songs.");
        System.out.println("help - Display these instructions again.");
        System.out.println("quit - Exit the program.");
    }

    /*
     * Executes a single command entered by the user.
     * 
     * @param command - The command entered by the user
     */
    @Override
    public void executeSingleCommand(String command) {
        String[] parts = command.split(" "); //Split the command into parts, using space as the delimiter
        String action = parts[0].toLowerCase(); //Convert the command action to lowercase for easier comparison

        try {
            switch (action) {
                case "load": //Load data from a file
                    if (parts.length < 2) {
                        System.out.println("Error: Missing file path.");
                    } else {
                        backend.readData(parts[1]); //Call to the backend to read data from the specified file
                        System.out.println("Data loaded successfully.");
                    }
                    break;

                case "energy": //Set an energy filter for retrieving songs
                    if (parts.length == 2) {
                        // Set maximum energy if only one value is provided
                        int maxEnergy = Integer.parseInt(parts[1]);
                        List<String> songs = backend.getRange(null, maxEnergy); //Get songs with energy up to the specified value from the backend
                        System.out.println("Songs with energy up to " + maxEnergy + ":");
                        displaySongs(songs);
                    } else if (parts.length == 4 && parts[2].equalsIgnoreCase("to")) {
                        // Set energy range if two values are provided and the middle part is "to"
                        int minEnergy = Integer.parseInt(parts[1]);
                        int maxEnergy = Integer.parseInt(parts[3]);
                        List<String> songs = backend.getRange(minEnergy, maxEnergy); //Get songs within the specified energy range from the backend
                        System.out.println("Songs with energy between " + minEnergy + " and " + maxEnergy + ":");
                        displaySongs(songs);
                    } else {
                        System.out.println("Error: Invalid energy command syntax.");
                    }
                    break;

                case "danceability": //Filter songs based on danceability threshold
                    if (parts.length < 2) { //Check if the danceability threshold is provided
                        System.out.println("Error: Missing danceability threshold.");
                    } else {
                        int threshold = Integer.parseInt(parts[1]);
                        List<String> songs = backend.filterSongs(threshold); //Get songs with danceability above the specified threshold from the backend
                        System.out.println("Songs with danceability above " + threshold + ":");
                        displaySongs(songs);
                    }
                    break;

                case "show": //Display songs based on user input
                    if (parts.length < 2) {
                        System.out.println("Error: Missing show argument.");
                    } else if (parts.length >= 3 && parts[1].equalsIgnoreCase("most") && parts[2].equalsIgnoreCase("recent")) { 
                        // Ensure index check prevents ArrayIndexOutOfBoundsException
                        List<String> songs = backend.fiveMost(); //Get the five most recent songs from the backend
                        System.out.println("Five most recent songs:");
                        displaySongs(songs);
                    } else {
                        try { //Display a specific number of songs
                            int maxCount = Integer.parseInt(parts[1]);
                            List<String> songs = backend.getRange(null, null);
                            if (songs.size() > maxCount) {
                                songs = songs.subList(0, maxCount); //Limit the number of songs displayed to the specified count
                            }
                            System.out.println("First " + maxCount + " songs:");
                            displaySongs(songs);
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Invalid number format for show command.");
                        }
                    }
                    break;

                case "help":
                    displayCommandInstructions(); //Display the command instructions again
                    break;

                default:
                    System.out.println("Error: Unknown command.");
            }
        } catch (NumberFormatException e) { //Catch exceptions where a number format is expected but not provided.
            System.out.println("Error: Invalid number format.");
        } catch (IOException e) { //File reading errors from the backend
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) { //Other unexpected errors elsewhere in the program.
            System.out.println("Error: An unexpected error occurred.");
        }
    }

    /*
     * Helper method to display a list of songs.
     * 
     * @param songs - List of songs titles to display
     */
    private void displaySongs(List<String> songs) {
        if (songs.isEmpty()) {
            System.out.println("No songs found.");
        } else {
            for (String song : songs) {
                System.out.println(song);
            }
        }
    }
}
