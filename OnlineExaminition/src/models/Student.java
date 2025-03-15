package models;

import java.util.*;
import util.AnimatedText;

public class Student extends User {
    
    private static final long serialVersionUID = 1L;

    // private String studentId;  
    private String department;
    private int sem;
    private final HashMap<String, Integer> studentMarks; // exam ID and marks
    
    public Student(Scanner sc, AnimatedText animate){
        super(sc, animate);
        animate.animateText("Enter Department ", 25);
        this.department = sc.nextLine();
        animate.animateText("Enter Student ID Number ", 25);
        this.setID(this.department.substring(0, 3).toUpperCase() + String.format("%03d", sc.nextInt()));sc.nextLine();
        animate.animateText("Enter Semester ", 25);
        this.sem = sc.nextInt();sc.nextLine();
        this.studentMarks = new HashMap<>();
        animate.animateText("Account created Successfully ", 25);
    }
   
    public void displayResults(AnimatedText animate, List<Exam> exams) {
        if (studentMarks.isEmpty()) {
            animate.animateText("Student has not attended any exams", 25);
            return;
        }
        animate.animateText(this.getID() + "\t" + this.getName() + "\t" + this.department + "\t" + this.sem + " sem" + "\n", 25);
        animate.animateText(String.format("%-15s %-15s %-15s %-10s", "Subject", "Obtained Marks", "Total Marks", "Result"), 25);
        for (String examID : studentMarks.keySet()) {
            Exam filteredExam = exams.stream()
                .filter(exam -> exam.getExamID().equalsIgnoreCase(examID))
                .findFirst().orElse(null);
            animate.animateText(String.format("%-15s %-15d %-15d %-10s", 
            filteredExam.getSubject(), 
            studentMarks.get(examID),
            filteredExam.getTotalMarks(),
            studentMarks.get(examID) > 0.35 * filteredExam.getTotalMarks() ? "PASS" : "FAIL"), 25);
        }
    }

    public void displayStudentDetails(AnimatedText animate){
        animate.animateText("Student Name :"+this.getName(),25);
        animate.animateText("Student ID :"+this.getID(),25);
        animate.animateText("Student Email :"+this.getEmail(),25);
        animate.animateText("Student Phone Number :"+this.getPhone(),25);
        animate.animateText("Student Department :"+this.department,25);
        animate.animateText("Student Semester :"+this.sem,25);
    }

    // setters 
    public void setStudentDept(String department){ this.department = department; }
    public void setStudentSem(int sem){ this.sem = sem; }
    public void setMarks(String examID, int marks){ this.studentMarks.put(examID, marks); }

    // getters
    public String getStudentDept(){ return this.department; }
    public int getStudentSem(){ return this.sem; }
    public Integer getMarks(String examId) { return this.studentMarks.getOrDefault(examId, 0); }
}