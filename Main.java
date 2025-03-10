/**
 * This is Main class runs the Backend methods
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Running Backend Application...");

        try {
            // Create the placeholder tree and backend instance
            IterableSortedCollection<Song> tree = new Tree_Placeholder();
            Backend backend = new Backend(tree);

            // Load data from the CSV file
            backend.readData("songs.csv");
            System.out.println("Songs successfully loaded into the tree!");

            // List all songs in the tree
            System.out.println("\nIterating through the tree to list songs:");
            for (Song song : tree) {
                System.out.println(song.getTitle() + " by " + song.getArtist());
            }

            // Test getRange method
            System.out.println("\nFetching songs with energy range (40, 90):");
            for (String title : backend.getRange(40, 90)) {
                System.out.println(title);
            }

            // Test filterSongs method
            System.out.println("\nApplying danceability filter (>70):");
            for (String title : backend.filterSongs(70)) {
                System.out.println(title);
            }

            // Test fiveMost method
            System.out.println("\nFetching the five most recent songs:");
            for (String title : backend.fiveMost()) {
                System.out.println(title);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

