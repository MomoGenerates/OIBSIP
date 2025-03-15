package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.*;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userID;
    private String name;
    private String pin;
    private double balance ;
    private final boolean isAdmin;
    private boolean isAccFrozen;
    private List<Transaction> transactionHistory;

    public User() {
        this.userID = "";
        this.name = "";
        this.pin = "";
        this.balance = 0.0;
        this.isAdmin = false;
        this.isAccFrozen = false;
        this.transactionHistory = new ArrayList<>();
    }
    public User(boolean isAdmin) {// admin user
        this.userID = "AD000";
        this.name = "";
        this.pin = "";
        this.isAdmin = true;
    }

    private transient final AnimatedText animate = new AnimatedText();

    // Custom deserialization for the transient field
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        java.lang.reflect.Field field;
        try {
            field = User.class.getDeclaredField("animate");
            field.setAccessible(true);
            field.set(this, new AnimatedText());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void createAcc(Scanner sc){
        this.userID = IDinitialize();
        animate.animateText("Creating a new Account ", 25);
        animate.animateText("Enter the Name ", 25);
        this.name = sc.nextLine();

        do {    // Validate the pin
        animate.animateText("Enter the Pin ", 25);
        this.pin = sc.nextLine();
        } while (this.pin.length() != 4);

        do {    // Validate the initial deposit
        animate.animateText("Enter the Initial Deposit ", 25);
        this.balance = sc.nextDouble();
        sc.nextLine();
        } while (this.balance < 0);

        this.transactionHistory = new ArrayList<>();
        transactionHistory.add(new Transaction("Deposit", this.balance, "Initial Deposit"));
    }
    // public void adminCreateAcc(Scanner sc){//admin user
    //     animate.animateText("Creating a new admin ", 25);
    //     animate.animateText("Enter the Name ", 25);
    //     this.name = sc.nextLine();

    //     do {    // Validate the pin
    //     animate.animateText("Enter the Pin ", 25);
    //     this.pin = sc.nextLine();
    //     } while (this.pin.length() != 6);
    //     animate.animateText("Admin created successfully ", 25);
    // }

    public void deposit(double amount) {
        animate.animateText("Successfully deposited "+amount, 25);
        this.balance += amount;
        transactionHistory.add(new Transaction("Deposit", amount, "Deposit by user"));
    }

    public void withdraw(double amount) {
        if (this.balance - amount < 0) {
            animate.animateText("Insufficient funds.", 25);
        } else {
            animate.animateText("Successfully withdrew "+amount, 25);
            this.balance -= amount;
            transactionHistory.add(new Transaction("Withdraw", amount, "Withdraw by user"));
        }
    }

    public boolean transferFrom(double amount, User account) {
        if (this.balance - amount < 0) {
            animate.animateText("Insufficient funds.", 25);
            return false;
        } 
            this.balance -= amount;
            animate.animateText("Successfully transfered "+amount, 25);
            transactionHistory.add(new Transaction("Transfer", amount, "Transfered to " + account.getName()));
            return true;
    }

    public void transferTo(double amount, String account) {
        this.balance += amount;
        transactionHistory.add(new Transaction("Transfer", amount, "Transfered from " + account));
    }

    public void checkBalance() {
        animate.animateText("Your balance is: $" + this.balance, 25);
    }

    public void printTransactionHistory() {
        animate.animateText("User ID: "+ this.userID +"\tName: " + this.name, 25);
        animate.animateText(String.format("%-20s %-15s %-12s %s", "Time", "Transaction", "Amount", "Details"), 25);
    
        for (Transaction t : transactionHistory) {
            animate.animateText(String.format("%-20s %-15s $%-11.2f %s",
                t.getFormattedTimestamp(),
                t.getType(),
                t.getAmount(),
                t.getDetails() + "."),
                25);
        }
    }

    public String IDinitialize() {
        List<User> users = new UserManager().getUsers();
        int id = users.size() + 1;
        return "U00" + id;
    }

    public boolean validateUserName(String name) { return this.name.equals(name); }
    public boolean validateUser(String name, String pin) { return (this.pin.equals(pin) && this.name.equals(name)); }

    // getters
    public String getName() { return this.name; }
    public String getPin() { return this.pin; }
    public String getUserID() { return this.userID; }
    public double getBalance() { return this.balance; }
    public boolean getStatus() { return this.isAccFrozen; }
    public boolean isAdmin() { return this.isAdmin; }
    // setters 
    public void setStatus(boolean status) { this.isAccFrozen = status; }
}