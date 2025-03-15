package models;

import java.io.Serializable;
import java.util.Scanner;
import util.AnimatedText;

public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    private String questionText;
    private final String[] options = new String[4];
    private String correctAnswer;
    private int marks;

    public Question(Scanner sc, AnimatedText animate){
        animate.animateText("Enter the Question ", 25);
        this.questionText = sc.nextLine();
        animate.animateText("Enter options ", 25);
        for (int i = 0; i < options.length; i++) {
            animate.animateText("Option " + (char)('a'+ i) + ": ", 25);
            options[i] = sc.nextLine();
        }
        animate.animateText("Enter the correct answer ", 25);
        this.correctAnswer = sc.nextLine();
        animate.animateText("Enter the marks ", 25);
        this.marks = sc.nextInt();sc.nextLine();
    }

    public boolean validateAnswer(String answer) {
        return this.correctAnswer.equalsIgnoreCase(answer);
    }

    public void displayQuestion(AnimatedText animate) {
        animate.animateText(this.questionText + "\t" + this.marks + "M", 25);
        animate.animateText(String.format("a. %-20s b. %-20s", options[0], options[1]), 25);
        animate.animateText(String.format("c. %-20s d. %-20s", options[2], options[3]), 25);
        animate.animateText(("Enter your answer (a/b/c/d): "), 25);
    }

    // getters
    public int getMarks(){ return this.marks; }

    // setters
    public void setQuestion(String question){ this.questionText = question; }
    public void setAnswer(String answer){ this.correctAnswer = answer; }
    public void setMarks(int marks){ this.marks = marks; }
    public void setOption(String option, int num){
        switch(num){
            case 1 -> this.options[0] = option;
            case 2 -> this.options[1] = option;
            case 3 -> this.options[2] = option;
            case 4 -> this.options[3] = option;
        }
    }
}