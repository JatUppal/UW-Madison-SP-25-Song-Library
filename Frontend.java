import java.util.Scanner;
import java.util.List;
import java.io.IOException;

public class Frontend implements FrontendInterface {
    private Scanner scanner;
    private BackendInterface backend;

    public Frontend(Scanner scanner, BackendInterface backend) {
        this.scanner = scanner;
        this.backend = backend;
    }

    @Override
    public void runCommandLoop() {
        displayCommandInstructions();
        while (true) {
            System.out.print("Enter command: ");
            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("quit")) {
                System.out.println("Exiting the program. Goodbye!");
                break;
            }
            executeSingleCommand(command);
        }
    }

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

    @Override
    public void executeSingleCommand(String command) {
        String[] parts = command.split(" ");
        String action = parts[0].toLowerCase();

        try {
            switch (action) {
                case "load":
                    if (parts.length < 2) {
                        System.out.println("Error: Missing file path.");
                    } else {
                        backend.readData(parts[1]);
                        System.out.println("Data loaded successfully.");
                    }
                    break;

                case "energy":
                    if (parts.length == 2) {
                        // Set maximum energy
                        int maxEnergy = Integer.parseInt(parts[1]);
                        List<String> songs = backend.getRange(null, maxEnergy);
                        System.out.println("Songs with energy up to " + maxEnergy + ":");
                        displaySongs(songs);
                    } else if (parts.length == 4 && parts[2].equalsIgnoreCase("to")) {
                        // Set energy range
                        int minEnergy = Integer.parseInt(parts[1]);
                        int maxEnergy = Integer.parseInt(parts[3]);
                        List<String> songs = backend.getRange(minEnergy, maxEnergy);
                        System.out.println("Songs with energy between " + minEnergy + " and " + maxEnergy + ":");
                        displaySongs(songs);
                    } else {
                        System.out.println("Error: Invalid energy command syntax.");
                    }
                    break;

                case "danceability":
                    if (parts.length < 2) {
                        System.out.println("Error: Missing danceability threshold.");
                    } else {
                        int threshold = Integer.parseInt(parts[1]);
                        List<String> songs = backend.filterSongs(threshold);
                        System.out.println("Songs with danceability above " + threshold + ":");
                        displaySongs(songs);
                    }
                    break;

                case "show":
                    if (parts.length < 2) {
                        System.out.println("Error: Missing show argument.");
                    } else if (parts[1].equalsIgnoreCase("most") && parts[2].equalsIgnoreCase("recent")) {
                        // Show five most recent songs
                        List<String> songs = backend.fiveMost();
                        System.out.println("Five most recent songs:");
                        displaySongs(songs);
                    } else {
                        // Show first MAX_COUNT songs
                        int maxCount = Integer.parseInt(parts[1]);
                        List<String> songs = backend.getRange(null, null);
                        if (songs.size() > maxCount) {
                            songs = songs.subList(0, maxCount);
                        }
                        System.out.println("First " + maxCount + " songs:");
                        displaySongs(songs);
                    }
                    break;

                case "help":
                    displayCommandInstructions();
                    break;

                default:
                    System.out.println("Error: Unknown command.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: An unexpected error occurred.");
        }
    }

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
