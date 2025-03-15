package util;

public class Rules {
    private final AnimatedText animate = new AnimatedText();
    public void RuleList(){
        String arr [] = new String[] {
            "1.Choose difficulty Easy/Medium/Hard ",
            "2.You only get 5 chances, diffculty only changes the range ",
            "3.You can paly multiple rounds, As you clear each round the next starts ",
            "4.You are assigned your score based on how many guesses you took and the number of the rounds played ",
            "5.Scoring formual : (RoundCounter * Guesses) * DifficultyMultiplier. ",
            "6.You can see the Highscore by typing 'highscore' ",
            "7.Type exit to stop the game. Or the games ends after you loose "
        };
        for(String str : arr){
            animate.animateText(str,5);
        }
    }
}