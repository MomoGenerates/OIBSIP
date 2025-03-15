package util;

import java.io.*;
import java.util.Scanner;

public class Highscore {
    private static final String NAME_FILE = "data/highscoreName.txt";
    private static final String SCORE_FILE = "data/highscoreValue.txt";
    private final static AnimatedText animate = new AnimatedText(); 

    public static void saveHighScore(int newScore) {
        int currentHighScore = readHighScore();

        if (newScore > currentHighScore) {
            new MusicPlayer("assets/highscore.wav", false).start();
            animate.animateText("You have made a new High-Score !\nEnter your Name ", 25);
            try (Scanner sc = new Scanner(System.in)) {
                String playerName = sc.nextLine();
                
                try {
                    new File("data").mkdirs();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(NAME_FILE))) {
                        writer.write(playerName);
                    }
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
                        writer.write(String.valueOf(newScore));
                    }
                } catch (IOException e) { animate.animateText("Error saving high score.", 25); }
                sc.close();
            }
        } 
    }

    public static String readHighScoreName() {
        try (BufferedReader reader = new BufferedReader(new FileReader(NAME_FILE))) {
            return reader.readLine();
        } catch (IOException e) {
            return "No Player";
        }
    }

    public static int readHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            return Integer.parseInt(reader.readLine().trim());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }
}