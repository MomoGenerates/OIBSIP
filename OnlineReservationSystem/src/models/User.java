package models;
import java.io.Serializable;
import java.util.Scanner;
import util.AnimatedText;
import util.DatabaseManager;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private String fullName;
    private String email;
    private String phone;

    public User(Scanner sc, AnimatedText animate) {
        animate.animateText("Enter your userName", 25);
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
        animate.animateText("Enter your Full Name", 25);
        this.fullName = sc.nextLine();
        animate.animateText("Enter your Phone Nnumber", 25);
        this.phone = sc.nextLine();
    }

    public boolean authenticate(String username, String password) {
        return this.userName.equalsIgnoreCase(username) && this.password.equals(password);
    }

    public void displayUserDetails(AnimatedText animate){
        animate.animateText("User username :"+this.getUsername(),25);
        animate.animateText("User Full Name :"+this.getName(),25);
        animate.animateText("User Email :"+this.getEmail(),25);
        animate.animateText("User Phone Number :"+this.getPhone(),25);
    }

    // getters
    public String getUsername() { return this.userName; }
    public String getPassword() { return this.password; }
    public String getName() { return this.fullName; }
    public String getEmail() { return this.email; }
    public String getPhone() { return this.phone; }

    // setters
    public void setUsername(String username) { this.userName = username; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }  
    
}