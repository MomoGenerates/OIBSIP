package modes;

import java.util.Random;
import java.util.Scanner;
import main.App;
import util.*;

public class Hard {
    private final AnimatedText animate = new AnimatedText();
    private final Scanner sc;
    private int correct;

    public Hard(Scanner sc) {
        this.sc = sc;
    }
    
    public int check(int guess) {
        return Integer.compare(guess, correct);
    }
    
    public void play() {
        animate.animateText("You have selected HARD difficulty", 25);
        animate.animateText("Number range is from 0 to 100", 25);
        animate.animateText("You have 5 guesses", 25);
        while(App.state){
            
            this.correct = new Random().nextInt(0,100);
            animate.animateText("Round "+App.RoundCounter, 25);
            
            int flag = 0;
            for (int i = 5; i > 0; i--) {
                animate.animateText(i + " guesses left :", 25);
                flag = check(sc.nextInt());
                if (flag == 0){
                    Score.scoreCounterHard(i);
                    break;
                }
                else if (flag > 0) 
                    animate.animateText("Guess lower ", 25);
                else 
                    animate.animateText("Guess higher ", 25);
            }
            
            if (flag == 0){
                new MusicPlayer("assets/winMusic.wav", false).start();
                animate.animateText(new feedback().win(), 25); 
            } else { 
                new MusicPlayer("assets/loseMusic.wav", false).start();
                animate.animateText(new feedback().lose(), 25);
                App.state = false;
            }
            sc.nextLine();
            App.RoundCounter++;
            
        }   
    }
}