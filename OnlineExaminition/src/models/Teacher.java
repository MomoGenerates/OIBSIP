package models;

import java.util.Scanner;
import util.AnimatedText;

public class Teacher extends User {

    private static final long serialVersionUID = 1L;

    private String department;

    public Teacher(Scanner sc, AnimatedText animate){
        super(sc, animate);
        animate.animateText("Enter Department ", 25);
        this.department = sc.nextLine();
        animate.animateText("Enter Teacher ID Number ", 25);
        this.setID(this.department.substring(0, 3).toUpperCase() + String.format("%03d", sc.nextInt()));sc.nextLine();
        animate.animateText("Account created Successfully ", 25);
    }

    public void displayTeacherDetails(AnimatedText animate){
        animate.animateText("Teacher Name :"+this.getName(),25);
        animate.animateText("Teacher ID :"+this.getID(),25);
        animate.animateText("Teacher Email :"+this.getEmail(),25);
        animate.animateText("Teacher Phone Number :"+this.getPhone(),25);
        animate.animateText("Teacher Department :"+this.department,25);
    }

    // getters
    public void setTeacherDept(String department){ this.department = department; }

    // setters
    public String getTeacherDept(){ return this.department; }
}
