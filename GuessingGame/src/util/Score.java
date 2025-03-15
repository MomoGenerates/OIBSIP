package util;

import java.util.ArrayList;
import java.util.List;

import main.App;

public class Score {
    private static int finalScore = 0;
    private static final List<Integer> scoreList = new ArrayList<>();
    private final static AnimatedText animate = new AnimatedText();
    
        public static void scoreCounterEasy(int guessNum){
            scoreList.add(App.RoundCounter * guessNum);
        }
        public static void scoreCounterMedium(int guessNum){
            scoreList.add(App.RoundCounter * (guessNum * 2));
        }
        public static void scoreCounterHard(int guessNum){
            scoreList.add(App.RoundCounter * (guessNum * 3));
        }
    
        public static void displayScore(){
            int round = 1;
            for(int score : scoreList){
                animate.animateText("Round "+round+++" Score : "+score, 25);
                finalScore += score;
            }
            animate.animateText("Final Score :"+finalScore, 25);
            Highscore.saveHighScore(finalScore);
            System.exit(0);
    }
}