package util;

public class ConsolePrinter {
    static AnimatedText animate = new AnimatedText();

    public static synchronized void print(String text) {
        animate.animateText(text, 25);
    }
    
    public static synchronized void println(String text) {
        animate.animateTextNoln(text, 25);
    }

    public static synchronized void printTimer(String text) {
        System.out.print("\r" + text);
    }
}