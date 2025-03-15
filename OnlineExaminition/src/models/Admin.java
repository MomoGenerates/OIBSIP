package models;

import java.util.List;
import java.util.Scanner;
import util.AnimatedText;
import util.DatabaseManager;

public class Admin extends User{
    
    private static final long serialVersionUID = 1L;

     private final String AdminID;

    public Admin(Scanner sc, AnimatedText animate){
        super(sc, animate);
        this.AdminID = initializeID();
        animate.animateText("Sucessfully Creaated Admin Acc", 25);
    }

    public void displayAdminDetails(AnimatedText animate){
        animate.animateText("Admin ID :"+this.getAdminID(),25);
        animate.animateText("Admin Name :"+this.getName(),25);
        animate.animateText("Admin Email :"+this.getEmail(),25);
        animate.animateText("Admin Phone Number :"+this.getPhone(),25);
    }

     private String initializeID() {
        List<Admin> admins = new DatabaseManager().loadAdmins();
        int id = admins.size() + 1;
        return "AD" + String.format("%03d", id);
    }

    // getters
    public String getAdminID(){ return this.AdminID; }
}