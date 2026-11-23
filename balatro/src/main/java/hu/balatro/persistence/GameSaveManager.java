package hu.balatro.persistence;

import java.io.*;

public class GameSaveManager {
    private static final String SAVE_FILE = "balatro_highscore.txt";

    public void saveHighScore(int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            System.err.println("Hiba a mentés során: " + e.getMessage());
        }
    }

    public int loadHighScore() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return 0;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return line != null ? Integer.parseInt(line.trim()) : 0;
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }
}