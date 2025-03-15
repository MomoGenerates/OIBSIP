package main;

import java.util.Scanner;
import modes.*;
import util.*;

public class App {
    public static boolean state = true;
    public static int RoundCounter = 1;
    
    public static void main(String[] args) throws Exception { 
        AnimatedText animate = new AnimatedText();
        Scanner sc = new Scanner(System.in);
        
            new MusicPlayer("assets/bgMusic.wav").start();
            System.out.println("\033[H\033[2J");
            animate.animateText("Welcome to the Number Guessing Game", 25 );
            while(state){
                animate.animateText("Enter the command : ", 25);
                String difficulty = sc.nextLine().toLowerCase();
    
                switch(difficulty){ 
                    case "easy", "medium", "hard" ->{
                        switch(difficulty) {
                            case "easy" -> new Easy(sc).play();
                            case "medium" -> new Medium(sc).play();
                            case "hard" -> new Hard(sc).play();
                        }
                    }
                    case "rules" -> new Rules().RuleList();
                    case "highscore" -> animate.animateText("Highscore : " + Highscore.readHighScoreName() +" - " + Highscore.readHighScore(), 25);
                    case "exit" -> {
                        sc.close();
                        System.exit(0);
                }
                default -> animate.animateText("Invalid input", 25);
            } 
        }
        Score.displayScore();
    }
}