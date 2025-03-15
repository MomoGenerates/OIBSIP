# ATM Interface

## Overview
This is a mini-project that simulates an ATM (Automated Teller Machine) interface. The project allows users to perform various banking operations such as creating an account, depositing money, withdrawing money, transferring money, checking balance, and viewing transaction history.

## Features
- **Create Account**: Users can create a new account by providing their name and PIN.
- **Deposit Money**: Users can deposit money into their account.
- **Withdraw Money**: Users can withdraw money from their account.
- **Transfer Money**: Users can transfer money to another user's account.
- **Check Balance**: Users can check their account balance.
- **Transaction History**: Users can view their transaction history.
- **Delete Account**: Users can delete their Account from the system.

## Project Structure
The project consists of the following main components:
- `App.java` → The main class that handles user interactions and operations.
- `UserManager.java` → A utility class for managing user data, including saving and retrieving users from a serialized file.
- `User.java` → A class representing a user with attributes such as name, PIN, balance, and transaction history.
- `Transaction.java` → A class for managing and recording transaction details including type, amount, and timestamp.
- `AnimatedText.java` → A utility class for displaying animated text in the console.

## Usage
1. **Start the application**: Run the `App` class to start the ATM interface.
2. **Follow the prompts**: The application will prompt you to enter your name and PIN. If you are a new user, you can create a new account.
3. **Perform operations**: Once logged in, you can perform various operations such as deposit, withdraw, transfer, check balance, and view transaction history.

## How to Run
1. **Clone the repository**:
    ```sh
    git clone https://github.com/your-repo/ATM-Interface.git    
    ```
2. **Navigate to the project directory**:
    ```sh
    cd ATM-Interface
    ```
3. **Compile the project**:
    ```sh
    javac -d bin src/main/*.java src/util/*.java
    ```
4. **Run the application**:
    ```sh
    java -cp bin main.App
    ```

## Dependencies
- Java Development Kit (JDK) 8 or higher

## Author
- Rohit