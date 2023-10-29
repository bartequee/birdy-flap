package flapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Class responsible for parsing the file containing the high scores.
 */
public class fileParse {
    private Dictionary<String, Integer> dictionary;

    /**
     * Constructor for FileParse.
     */
    public fileParse() {
        try {
            dictionary = redFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new score to the file.
     * 
     * @param name  name of the player
     * @param score of the player
     */

    public void add(String name, int score) {

        Enumeration<String> keys = dictionary.keys();
        int[] scores = new int[10];
        int n = 0;
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            scores[n] = dictionary.get(key);
            n++;
        }
        // print scores
        for (int i = 0; i < 10; i += 1) {
            System.out.println(scores[i]);
        }

        int smallest = 100;
        for (int i = 0; i < 10; i += 1) {
            if (scores[i] < smallest) {
                smallest = scores[i];
            }
        }

        if (score <= smallest) {
            return;
        }
        keys = dictionary.keys();
        String smallestKey = "";
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (dictionary.get(key) == smallest) {
                smallestKey = key;
            }
        }

        if (dictionary.get(name) == null) {
            dictionary.remove(smallestKey);
        }

        dictionary.put(name, score);
    }

    public Dictionary<String, Integer> getDict() {
        return dictionary;
    }

    private Dictionary<String, Integer> redFile() throws IOException {
        Dictionary<String, Integer> dict = new Hashtable<>();

        BufferedReader bf = new BufferedReader(
                new FileReader("src/flapper/resources/score.txt"));

        String line = bf.readLine();

        for (int i = 0; i < 10; i += 1) {
            dict.put(line, Integer.parseInt(bf.readLine()));
            line = bf.readLine();
        }

        bf.close();
        return dict;
    }

    /**
     * Writes the dictionary to the file.
     */

    public void write() {
        try {
            writeToFile(dictionary);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(Dictionary<String, Integer> dict) throws IOException {
        FileWriter myWriter = new FileWriter("src/flapper/resources/score.txt");
        Enumeration<String> keys = dict.keys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            myWriter.write(key + "\n");
            myWriter.write(dict.get(key) + "\n");
        }
        myWriter.close();
    }
}