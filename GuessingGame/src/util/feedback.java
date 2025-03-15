package util;

import java.util.Random;

public class feedback {
    Random random = new Random();

    String winQuotes [] = new String[] {
        "Congratulations, you guessed it correctly!",
        "Well done! You've found the right number!",
        "Great job! You nailed it!",
        "Fantastic! You guessed the number!",
        "Bravo! You've got it right!",
        "Awesome! You guessed it correctly!",
        "Impressive! You found the number!",
        "Excellent! You guessed the number!",
        "Hooray! You got it right!",
        "You did it! You guessed the, number!"
    };
    String loseQuotes [] = new String[] {
        "Better luck next time!",
        "Don't worry, you'll get it next time!",
        "Keep trying, you'll get it!",
        "Almost there, try again!",
        "Don't give up, you'll guess it next time!",
        "Nice try, but not quite!",
        "You'll get it next time, keep going!",
        "So close, but not this time!",
        "Good effort, but not the right number!",
        "Keep practicing, you'll get it next time!"
    };

    public String win(){
        return winQuotes[random.nextInt(winQuotes.length)];
    }

    public String lose(){
        return loseQuotes[random.nextInt(loseQuotes.length)];
    }
}
