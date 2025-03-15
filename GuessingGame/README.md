# Guessing Number Game 🎲

## Overview
Welcome to the **Guessing Number Game**! This is a fun and interactive game where you can test your luck and skills by guessing the correct number. The game offers three difficulty levels: Easy, Medium, and Hard.

## Features
- **Easy Mode**: Guess a number between 1 and 25.
- **Medium Mode**: Guess a number between 1 and 50.
- **Hard Mode**: Guess a number between 1 and 100.
- **Rules**: Display the game rules.
- **High Score**: Show the current high score.
- **Exit**: Quit the game.

## Project Structure
The project consists of the following main components:
- `Game.java`: The main class that handles the game logic and user interactions.
- `HighScoreManager.java`: A utility class for managing high scores.
- `AnimatedText.java`: A utility class for displaying animated text in the console.

## How to Play
Enter a command in the input stream to perform an action:
- **easy** – Start the game in Easy mode.
- **medium** – Start the game in Medium mode.
- **hard** – Start the game in Hard mode.
- **rules** – Display the game rules.
- **highscore** – Show the current high score.
- **exit** – Quit the game.

💡 **Tip:** Start by typing `rules` to learn how to play! 🎯

Good luck and have fun! 🚀

## How to Run
1. **Clone the repository**:
    ```sh
    git clone https://github.com/your-repo/GuessingGame.git
    ```
2. **Navigate to the project directory**:
    ```sh
    cd GuessingGame
    ```
3. **Compile the project**:
    ```sh
    javac -d bin src/main/*.java src/util/*.java
    ```
4. **Run the application**:
    ```sh
    java -cp bin main.Game
    ```

## Dependencies
- Java Development Kit (JDK) 8 or higher

## Author
- Rohit