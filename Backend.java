import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Backend implements BackendInterface {
    private IterableSortedCollection<Song> tree;
    private Integer lowEnergy = null;
    private Integer highEnergy = null;
    private Integer danceabilityThreshold = null;

    public Backend(IterableSortedCollection<Song> tree) {
        this.tree = tree;
    }

    /**
     * Loads data from the .csv file referenced by filename.  You can rely
     * on the exact headers found in the provided songs.csv, but you should
     * not rely on them always being presented in this order or on there
     * not being additional columns describing other song qualities.
     * After reading songs from the file, the songs are inserted into
     * the tree passed to this backend' constructor.  Don't forget to
     * create a Comparator to pass to the constructor for each Song object that
     * you create.  This will be used to store these songs in order within your
     * tree, and to retrieve them by energy range in the getRange method.
     *
     * @param filename is the name of the csv file to load data from
     * @throws IOException when there is trouble finding/reading file
     */
    @Override
    public void readData(String filename) throws IOException {
        BufferedReader reader = null;
        String line = "";

        try {
            // Initialize the BufferedReader to read the file
            reader = new BufferedReader(new FileReader(filename));

            // Read the header line to understand the column order
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("Empty CSV file or missing header line.");
            }
            String[] headers = header.split(",");

            // Identify column indices for required fields
            int titleIndex = -1, artistIndex = -1, genreIndex = -1, yearIndex = -1, bpmIndex = -1,
                    danceabilityIndex = -1, livenessIndex = -1, loudnessIndex = -1, energyIndex = -1;
            for (int i = 0; i < headers.length; i++) {
                switch (headers[i].trim().toLowerCase()) {
                    case "title":
                        titleIndex = i;
                        break;
                    case "artist":
                        artistIndex = i;
                        break;
                    case "top genre":
                        genreIndex = i;
                        break;
                    case "year":
                        yearIndex = i;
                        break;
                    case "bpm":
                        bpmIndex = i;
                        break;
                    case "nrgy":
                        energyIndex = i;
                        break;
                    case "dnce":
                        danceabilityIndex = i;
                        break;
                    case "db":
                        loudnessIndex = i;
                        break;
                    case "live":
                        livenessIndex = i;
                        break;
                }
            }

            // Validate that all required indices are found
            if (titleIndex == -1 || artistIndex == -1 || genreIndex == -1 || yearIndex == -1 || bpmIndex == -1 || danceabilityIndex == -1 || livenessIndex == -1 || loudnessIndex == -1|| energyIndex == -1) {
                throw new IOException("Missing required columns in the CSV header.");
            }

            // Read each line of the file
            while ((line = reader.readLine()) != null) {
                String[] cols = parseCSVLine(line);

                // Extract values using the identified indices
                String title = cols[titleIndex];
                String artist = cols[artistIndex];
                String genre = cols[genreIndex];
                int year = Integer.parseInt(cols[yearIndex]);
                int energy = Integer.parseInt(cols[energyIndex]);
                int bpm = Integer.parseInt(cols[bpmIndex]);
                int danceability = Integer.parseInt(cols[danceabilityIndex]);
                int loudness = Integer.parseInt(cols[loudnessIndex]);
                int liveness = Integer.parseInt(cols[livenessIndex]);


                // Create a comparator for sorting songs (e.g., by energy)
                Comparator<Song> songComparator = Comparator.comparingInt(Song::getEnergy);

                // Create a Song object
                Song song = new Song(title, artist, genre, year, bpm, energy, danceability, loudness, liveness, songComparator);

                // Insert the song into the tree
                tree.insert(song);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Parses a single line of a CSV file into an array of strings.
     * This method handles fields enclosed in double quotes and fields
     * containing commas.
     *
     * @param line the CSV line to parse
     * @return an array of strings representing the fields in the CSV line
     */
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        // Set the initial state of the quotes to be false
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes; // Toggle quotes
            } else if (c == ',' && !inQuotes) {
                // Split on comma if not inside quotes
                result.add(currentField.toString().trim());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        // Add the last field
        result.add(currentField.toString().trim());

        return result.toArray(new String[0]);
    }


    /**
     * Retrieves a list of song titles from the tree passed to the contructor.
     * The songs should be ordered by the songs' energy, and fall within
     * the specified range of energy values.  This energy range will
     * also be used by future calls to filterSongs and getFiveMost.
     * <p>
     * If a danceability filter has been set using the filterSongs method
     * below, then only songs that pass that filter should be included in the
     * list of titles returned by this method.
     * <p>
     * When null is passed as either the low or high argument to this method,
     * that end of the range is understood to be unbounded.  For example, a
     * argument for the height parameter means that there is no maximum
     * energy to include in the returned list.
     *
     * @param low  is the minimum energy of songs in the returned list
     * @param high is the maximum energy of songs in the returned list
     * @return List of titles for all songs from low to high that pass any
     * set filter, or an empty list when no such songs can be found
     */
    @Override
    public List<String> getRange(Integer low, Integer high) {
        lowEnergy = low;;
        highEnergy = high;

        List<String> result = new ArrayList<>();

        // Traverse the tree using the iterator
        for (Song song : tree) {
            int energy = song.getEnergy();

            // Check energy range
            boolean withinLow = (low == null || energy >= low);
            boolean withinHigh = (high == null || energy <= high);

            if (withinLow && withinHigh) {
                // Check if the danceability filter is set
                if (danceabilityThreshold == null || song.getDanceability() > danceabilityThreshold) {
                    result.add(song.getTitle());
                }
            }
        }

        return result;
    }

    /**
     * Retrieves a list of song titles that have a danceability that is
     * larger than the specified threshold.  Similar to the getRange
     * method: this list of song titles should be ordered by the songs'
     * energy, and should only include songs that fall within the specified
     * range of energy values that was established by the most recent call
     * to getRange.  If getRange has not previously been called, then no low
     * or high energy bound should be used.  The filter set by this method
     * will be used by future calls to the getRange and fiveMost methods.
     * <p>
     * When null is passed as the threshold to this method, then no
     * danceability threshold should be used.  This clears the filter.
     *
     * @param threshold filters returned song titles to only include songs that
     *                  have a danceability that is larger than this threshold.
     * @return List of titles for songs that meet this filter requirement and
     * are within any previously set energy range, or an empty list
     * when no such songs can be found
     */
    @Override
    public List<String> filterSongs(Integer threshold) {
        List<String> result = new ArrayList<>();
        // If the threshold is null, clear the filter and return an empty list
        if (threshold == null) {
            danceabilityThreshold = null;
            return result;
        }
        // Update the danceability threshold
        danceabilityThreshold = threshold;

        // Traverse the tree and apply the filters
        for (Song song : tree) {
            int energy = song.getEnergy();
            int danceability = song.getDanceability();

            // Check energy range
            boolean withinLow = (lowEnergy == null || energy >= lowEnergy);
            boolean withinHigh = (highEnergy == null || energy <= highEnergy);

            // Check danceability if meets threshold
            boolean meetsThreshold = danceability > danceabilityThreshold;

            // Add the song title if it meets all criteria
            if (withinLow && withinHigh && meetsThreshold) {
                result.add(song.getTitle());
            }
        }

        // Return string list of song titles
        return result;
    }

    /**
     * This method returns a list of song titles representing the five
     * most recent songs that both fall within any attribute range specified
     * by the most recent call to getRange, and conform to any filter set by
     * the most recent call to filteredSongs.  The order of the song titles
     * in this returned list is up to you.
     * <p>
     * If fewer than five such songs exist, return all of them.  And return an
     * empty list when there are no such songs.
     *
     * @return List of five most recent song titles
     */
    @Override
    public List<String> fiveMost() {
        List<Song> validSongs = new ArrayList<>();
        List<String> result = new ArrayList<>();

        // Traverse the tree to collect songs within the energy range and danceability filter
        for (Song song : tree) {
            int energy = song.getEnergy();
            int danceability = song.getDanceability();

            // Check energy range from getRange
            boolean withinLow = (lowEnergy == null || energy >= lowEnergy);
            boolean withinHigh = (highEnergy == null || energy <= highEnergy);

            // Check danceability filter from filterSongs
            boolean meetsDanceability = (danceabilityThreshold == null || danceability > danceabilityThreshold);

            // Add song to the valid list if it meets all criteria
            if (withinLow && withinHigh && meetsDanceability) {
                validSongs.add(song);
            }
        }

        // Sort the valid songs by year in descending order
        validSongs.sort((song1, song2) -> Integer.compare(song2.getYear(), song1.getYear()));

        // Add the 5 most valid song titles to the result list, if less than 5, add all
        for (int i = 0; i < Math.min(5, validSongs.size()); i++) {
            result.add(validSongs.get(i).getTitle());
        }

        // Return string list of song titles
        return result;
    }

}


