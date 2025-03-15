package models;

import java.io.Serializable;
import java.util.Scanner;
import util.AnimatedText;
import util.DatabaseManager;

public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userID;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private boolean isFrozen;

    public User(Scanner sc, AnimatedText animate) {
        animate.animateText("Creating a new User Account", 25);
        animate.animateText("Enter username: ", 25);
        this.userName = sc.nextLine();
        
        boolean validPassword;
        do {
            animate.animateText("Enter Password: ", 25);
            this.password = sc.nextLine();
            validPassword = this.password.length() >= 8 && this.password.matches(".*\\d.*");
            if (!validPassword) 
                animate.animateText("Password not strong enough. Use at least 8 characters and include a digit.", 25);
        } while (!validPassword);

        boolean validEmail;
        do {
            animate.animateText("Enter Email: ", 25);
            this.email = sc.nextLine();
            validEmail = ! new DatabaseManager().loadUsers().stream()
                            .anyMatch(user -> user.getEmail().equalsIgnoreCase(this.email));
            if (!validEmail) 
                animate.animateText("Email already in use", 25);
        } while (!validEmail);
        
        animate.animateText("Enter Phone Number: ", 25);
        this.phoneNumber = sc.nextLine();
    }

    public boolean authenticate(String username, String password) {
        return this.userName.equalsIgnoreCase(username) && this.password.equals(password);
    }

    // getters
    public String getName(){ return this.userName; }
    public String getID(){ return this.userID; }
    public String getPassword(){ return this.password; }
    public String getEmail(){ return this.email; }
    public String getPhone(){ return this.phoneNumber; }
    public boolean getStatus(){ return this.isFrozen; }

     // setters 
     public void setName(String name){ this.userName = name; }
     public void setID(String ID){ this.userID = ID; }
     public void setPassword(String password){ this.password = password; }
     public void setEmail(String email){ this.email = email; }
     public void setPhone(String phoneNumber){ this.phoneNumber = phoneNumber; }
     public void setStatus(boolean isFrozen){ this.isFrozen = isFrozen; }
}
