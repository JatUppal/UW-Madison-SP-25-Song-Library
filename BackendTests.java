import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.List;

public class BackendTests {

    /**
     * Tests the readData method to ensure songs are correctly loaded from the CSV file
     * and inserted into the tree with the expected data.
     */
    @Test
    public void backendTest1() throws IOException {
        // Create the placeholder tree
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        // Load data from the file
        backend.readData("songs.csv");

        // Test the public methods of Backend
        // Example: Get range of songs by energy (unbounded range)
        assertEquals(4, backend.getRange(null, null).size(),
                "The tree should contain 4 songs (as per Tree_Placeholder behavior).");

        // Check the first song's title in the range
        assertTrue(backend.getRange(null, null).contains("A L I E N S"),
                "The tree should include the predefined song 'A L I E N S'.");
        assertTrue(backend.getRange(null, null).contains("BO$$"),
                "The tree should include the predefined song 'BO$$'.");
        assertTrue(backend.getRange(null, null).contains("Cake By The Ocean"),
                "The tree should include the predefined song 'Cake By The Ocean'.");
    }
    /**
     * Tests the filterSongs method to ensure songs are correctly filtered by danceability
     * and returned in the expected order.
     */
    @Test
    public void backendTest2() throws IOException {
        // Create the placeholder tree
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        // Load data from the file
        backend.readData("songs.csv");

        // Ensure the tree contains the expected number of songs
        assertEquals(4, backend.getRange(null, null).size(),
                "The tree should contain 4 songs (as per Tree_Placeholder behavior).");

        // Apply a danceability filter (e.g., threshold = 50)
        List<String> filteredSongs = backend.filterSongs(50);

        // Test the filtered list size
        assertEquals(3, filteredSongs.size(),
                "The tree should return 2 songs with danceability greater than 50 (Tree_Placeholder behavior).");
        // Test specific song titles in the filtered list
        assertTrue(filteredSongs.contains("BO$$"),
                "Filtered list should include the song 'BO$$'.");
        assertTrue(filteredSongs.contains("Cake By The Ocean"),
                "Filtered list should include the song 'Cake By The Ocean'.");
        assertFalse(filteredSongs.contains("A L I E N S"),
                "Filtered list should not include the song 'A L I E N S' because its danceability is below the threshold.");
        assertTrue(filteredSongs.contains("Kills You Slowly"),
                "Filtered list should include the song 'Kills You Slowly'.");
    }
    /**
     * Tests the getFiveMost method to ensure the five most recent songs are correctly
     * returned in the expected order.
     */
    @Test
    public void backendTest3() throws IOException {
        // Create the placeholder tree
        IterableSortedCollection<Song> tree = new Tree_Placeholder();
        Backend backend = new Backend(tree);

        // Load data from the file
        backend.readData("songs.csv");

        // Set the energy range
        backend.getRange(40, 100);

        // Apply a danceability filter
        backend.filterSongs(50);

        // Test the fiveMost method
        List<String> fiveMostSongs = backend.fiveMost();

        // Validate the number of returned songs
        assertEquals(3, fiveMostSongs.size(),
                "The tree should return 3 songs (Tree_Placeholder behavior with the given filters).");

        // Validate specific song titles in the five most recent songs
        assertTrue(fiveMostSongs.contains("Cake By The Ocean"),
                "The list of five most songs should include 'Cake By The Ocean'.");
        assertTrue(fiveMostSongs.contains("BO$$"),
                "The list of five most songs should include 'BO$$'.");
        assertFalse(fiveMostSongs.contains("A L I E N S"),
                "The list of five most songs should not include 'A L I E N S' due to danceability or year.");

        // Validate that the songs are ordered by year descending
        assertEquals("Kills You Slowly", fiveMostSongs.get(0),
                "The first song should be 'Kills You Slowly' as it is the most recent.");
        assertEquals("Cake By The Ocean", fiveMostSongs.get(1),
                "The second song should be 'Cake By The Ocean' as it is the next most recent.");


        // Test case where no energy range is set

        backend.getRange(null, null);

        // Apply a danceability filter
        backend.filterSongs(40);

        // Test the fiveMost method
        fiveMostSongs = backend.fiveMost();

        // Validate the number of returned songs
        assertEquals(4, fiveMostSongs.size(),
                "The tree should return 4 songs (Tree_Placeholder behavior with the given filters).");

        // Validate specific song titles in the five most recent songs
        assertTrue(fiveMostSongs.contains("Cake By The Ocean"),
                "The list of five most songs should include 'Cake By The Ocean'.");
        assertTrue(fiveMostSongs.contains("BO$$"),
                "The list of five most songs should include 'BO$$'.");
        assertTrue(fiveMostSongs.contains("A L I E N S"),
                "The list of five most songs should not include 'A L I E N S' due to danceability or year.");

        // Validate that the songs are ordered by year descending
        assertEquals("Kills You Slowly", fiveMostSongs.get(0),
                "The first song should be 'Kills You Slowly' as it is the most recent.");
        assertEquals("A L I E N S", fiveMostSongs.get(1),
                "The second song should be 'A L I E N S' as it is the next most recent.");

        // Test case where fiveMost does NOT meet requirements
        backend.getRange(100, 120);

        // Apply a danceability filter
        backend.filterSongs(50);

        // Test the fiveMost method
        fiveMostSongs = backend.fiveMost();

        // Validate the number of returned songs
        assertEquals(0, fiveMostSongs.size(),
                "The tree should return be empty.");
    }

}


