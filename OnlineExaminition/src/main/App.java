package main;

import java.util.*;
import java.util.stream.Collectors;
import models.*;
import util.*;

public class App {
    private static DatabaseManager databaseManager;
    private static boolean userExist = false;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AnimatedText animate = new AnimatedText();
        databaseManager = new DatabaseManager();
        
        while (true){
            List<Student> students = databaseManager.loadStudents();
            List<Teacher> teachers = databaseManager.loadTeachers();
            List<Admin> admins = databaseManager.loadAdmins();
            System.out.println("\033[H\033[2J");
            animate.animateText("Welcome to Online Examination System ", 25);
            
            // while (!userExist){
                animate.animateText("Logging in ", 25);
                animate.animateText("Are you a student or a Teacher ", 25);
                String initialInput = sc.nextLine();
                
                if (initialInput.equalsIgnoreCase("student")) {
                    animate.animateText("Enter your username or email: ", 25);
                    String tempUser = sc.nextLine();
                    
                    students.stream()
                    .filter(student -> student.getName().equalsIgnoreCase(tempUser)  || 
                        student.getEmail().equalsIgnoreCase(tempUser))
                    .findFirst()
                    .ifPresentOrElse(
                        student -> { 
                            boolean validateUser = false;
                        while (!validateUser) {
                            animate.animateText("Enter your Password: ", 25);
                            final String userPassword = sc.nextLine();

                            if (student.authenticate(tempUser, userPassword)) {
                                animate.animateText("Welcome " + student.getName(), 25);
                                validateUser = true;
                                studentMenu(sc, animate, student);
                            } else animate.animateText("Invalid Password ", 25);
                        }
                    },
                    () -> {
                        animate.animateText("Username not found. Would you like to create a new account?", 25);
                        if (sc.nextLine().equalsIgnoreCase("Yes")) {
                            Student newStudent = new Student(sc, animate);
                            students.add(newStudent);
                            databaseManager.saveStudents(students);
                            studentMenu(sc, animate, newStudent);  
                        }
                    }); 
                } else if (initialInput.equalsIgnoreCase("teacher")){
                    animate.animateText("Enter your username or email: ", 25);
                    String tempUser = sc.nextLine();
                    
                    teachers.stream()
                    .filter(teacher -> teacher.getName().equalsIgnoreCase(tempUser) || 
                        teacher.getEmail().equalsIgnoreCase(tempUser))
                    .findFirst()
                    .ifPresentOrElse(
                    teacher -> {
                        boolean validateUser = false;
                        while (!validateUser) {
                            animate.animateText("Enter your Password: ", 25);
                            final String userPassword = sc.nextLine();
                            
                            if (teacher.authenticate(tempUser, userPassword)) {
                                animate.animateText("Welcome " + teacher.getName(), 25);
                                validateUser = true;
                                teacherMenu(sc, animate, teacher);
                            } else animate.animateText("Invalid Password ", 25);
                        }
                    },
                    () -> {
                        animate.animateText("Username not found. Would you like to create a new account?", 25);
                        if (sc.nextLine().equalsIgnoreCase("Yes")) {
                            Teacher newTeacher = new Teacher(sc, animate);
                            teachers.add(newTeacher);
                            databaseManager.saveTeachers(teachers);
                            teacherMenu(sc, animate, newTeacher);
                        }
                    });
                } else if (initialInput.equalsIgnoreCase("admin")){
                    animate.animateText("Enter your username or email: ", 25);
                    String tempUser = sc.nextLine();
                    
                    admins.stream()
                    .filter(admin -> admin.getName().equalsIgnoreCase(tempUser) || 
                        admin.getEmail().equalsIgnoreCase(tempUser))
                    .findFirst()
                    .ifPresentOrElse(
                    admin -> {
                        boolean validateUser = false;
                        while (!validateUser) {
                            animate.animateText("Enter your Password: ", 25);
                            final String userPassword = sc.nextLine();
                            
                            if (admin.authenticate(tempUser, userPassword)) {
                                animate.animateText("Welcome " + admin.getName(), 25);
                                validateUser = true;
                                adminMenu(sc, animate, admin);
                            } else animate.animateText("Invalid Password ", 25);
                        }
                    },
                    () -> {
                        animate.animateText("Username not found. Would you like to create a new account?", 25);
                        if (sc.nextLine().equalsIgnoreCase("Yes")) {
                            Admin newAdmin = new Admin(sc, animate);
                            admins.add(newAdmin);
                            databaseManager.saveAdmins(admins);
                            adminMenu(sc, animate, newAdmin);
                        }
                    });

                } else if (initialInput.equalsIgnoreCase("exit") || initialInput.equalsIgnoreCase("log out")) {
                    databaseManager.saveStudents(students);
                    databaseManager.saveExams(databaseManager.loadExams());
                    System.exit(0);
                }
            // }
        }
    }

    public static void studentMenu(Scanner sc, AnimatedText animate, Student currentStudent){
        List<Exam> exams = databaseManager.loadExams();
        List<Student> students = databaseManager.loadStudents();

        animate.animateText("""
                            ===== Student Menu =====
                            \u2192 Update Profile
                            \u2192 Take Exam
                            \u2192 View Previous Results
                            \u2192 Change Password
                            \u2192 Logout""", 25);

        while(true){
            animate.animateText("Enter the command ", 25);
            String operation = sc.nextLine().toLowerCase();

            switch(operation){
                case "update profile", "update" ->{
                    currentStudent.displayStudentDetails(animate);
                    animate.animateText("""
                            === Update Profile ===
                            \u2192 Update Full Name
                            \u2192 Update Email
                            \u2192 Update Phone Number
                            \u2192 Update Department
                            \u2192 Update Semester
                            \u2192 Back to Main Menu""", 25);
                    while(true){
                        animate.animateText("Enter your choice ", 25);
                        switch(sc.nextLine()){
                            case "name" ->{
                                animate.animateText("Enter the new Name ", 25);
                                currentStudent.setName(sc.nextLine());
                                databaseManager.saveStudents(students);
                            }
                            case "email" ->{
                                animate.animateText("Enter the new Email ", 25);
                                currentStudent.setEmail(sc.nextLine());  
                                databaseManager.saveStudents(students);
                            }
                            case "phone" ->{
                                animate.animateText("Enter the new Phone Number ", 25);
                                currentStudent.setPhone(sc.nextLine());  
                                databaseManager.saveStudents(students);
                            }
                            case "dept" ->{
                                animate.animateText("Enter the new Department ", 25);
                                currentStudent.setStudentDept(sc.nextLine());  
                                databaseManager.saveStudents(students);
                            }
                            case "sem" ->{
                                animate.animateText("Enter the new Semester ", 25);
                                currentStudent.setStudentSem(sc.nextInt());sc.nextLine();
                                databaseManager.saveStudents(students);
                            }
                            case "exit", "back" ->{
                                break;
                            }
                            default -> animate.animateText("Invalid input", 25);
                        }
                    }
                }
                case "take exam", "exam" ->{
                    List<Exam> availableExams = exams.stream()
                    .filter(exam -> !exam.getAttemptTracker().contains(currentStudent.getID()) &&
                    exam.getExamSem() == currentStudent.getStudentSem())
                    .collect(Collectors.toList());

                    if (availableExams.isEmpty()) animate.animateText("No available exams", 25);
                    else {
                        animate.animateText("Available exams to take ~", 25);
                        animate.animateText(String.format("%-10s %-15s %-15s %-20s %-6s",
                            "Exam ID", "Subject", "Total Marks", "Teacher", "Time"), 25);

                        availableExams.forEach(exam -> 
                            animate.animateText(String.format("%-10s %-15s %-15s %-20s %-6s",
                                            exam.getExamID(), exam.getSubject(), exam.getTotalMarks(), 
                                            exam.getCreatedBy(), exam.getExamTime() + " min"), 25));

                        animate.animateText("Enter the ID of exam you'd like to take ", 25);
                        availableExams.stream()
                            .filter(exam -> exam.getExamID().equalsIgnoreCase(sc.nextLine()))
                            .findFirst()
                            .ifPresentOrElse(
                                exam -> exam.startExam(sc, animate, currentStudent),
                                () -> animate.animateText("Wrong Exam ID", 25)
                            );
                    }
                }
                case "results", "view results" ->{
                    currentStudent.displayResults(animate,exams);
                }
                case "change password", "password" -> {
                    boolean correctPassword = false;
                    int passwordLimit = 0;
                    while (!correctPassword && passwordLimit < 4) {
                        animate.animateText("Enter old Password", 25);
                        if (currentStudent.getPassword().equals(sc.nextLine())) {
                            correctPassword = true;
                            boolean validPassword;
                            String newPassword;
                            do {
                                animate.animateText("Enter New Password", 25);
                                newPassword = sc.nextLine();
                                validPassword = newPassword.length() >= 8 && newPassword.matches(".*\\d.*");
                                if (!validPassword) animate.animateText("Password not strong enough. Use at least 8 characters and include a digit.", 25);
                            } while (!validPassword);
                            animate.animateText("Confirm New Password", 25);
                            if (newPassword.equals(sc.nextLine())) {
                                currentStudent.setPassword(newPassword);
                                databaseManager.saveStudents(students);
                                animate.animateText("Password changed successfully", 25);
                            } else {
                                animate.animateText("Passwords do not match", 25);
                            }
                        } else {
                            animate.animateText("Wrong Password", 25);
                            passwordLimit++;
                            if (passwordLimit >= 3) animate.animateText("Too many attempts", 25);
                        }
                    }
                }
                case "log out", "exit" ->{
                    databaseManager.saveStudents(students);
                    databaseManager.saveExams(exams);
                    animate.animateText("Logging out ", 25);
                    userExist = false;
                    System.out.println("\033[H\033[2J");
                    return;
                }
                default -> animate.animateText("Invalid input", 25);
            }
        }
    }

    public static void teacherMenu(Scanner sc, AnimatedText animate, Teacher currentTeacher){
        List<Teacher> teachers = databaseManager.loadTeachers();
        List<Student> students = databaseManager.loadStudents();
        List<Exam> exams = databaseManager.loadExams();

        animate.animateText("""
                            ===== Teacher Menu =====
                             \u2192 Update Profile
                             \u2192 Create New Exam
                             \u2192 Manage Questions
                             \u2192 View Student Results
                             \u2192 Set Exam Timer
                             \u2192 Generate Result Reports
                             \u2192 Change Password
                             \u2192 Logout""", 25);
        while (true) { 
            animate.animateText("Enter the command ", 25);
            String operation = sc.nextLine().toLowerCase();

            switch(operation){
                case "update profile", "update" ->{
                    currentTeacher.displayTeacherDetails(animate);
                    animate.animateText("""
                            === Update Profile ===
                            \u2192 Update Full Name
                            \u2192 Update Email
                            \u2192 Update Phone Number
                            \u2192 Update Department
                            \u2192 Back to Main Menu""", 25);
                    while(true){
                        animate.animateText("Enter your choice ", 25);
                        switch(sc.nextLine()){
                            case "name" ->{
                                animate.animateText("Enter the new Name ", 25);
                                currentTeacher.setName(sc.nextLine());
                                databaseManager.saveTeachers(teachers);  
                            }
                            case "email" ->{
                                animate.animateText("Enter the new Email ", 25);
                                currentTeacher.setEmail(sc.nextLine());  
                                databaseManager.saveTeachers(teachers);
                            }
                            case "phone" ->{
                                animate.animateText("Enter the new Phone Number ", 25);
                                currentTeacher.setPhone(sc.nextLine());  
                                databaseManager.saveTeachers(teachers);
                            }
                            case "dept" ->{
                                animate.animateText("Enter the new Department ", 25);
                                currentTeacher.setTeacherDept(sc.nextLine());  
                                databaseManager.saveTeachers(teachers);
                            }
                            case "exit", "back" ->{
                                break;
                            }
                            default -> animate.animateText("Invalid input", 25);
                        }
                    }
                }
                case "new exam", "create exam" ->{
                    Exam newExam = new Exam(sc, animate, currentTeacher);
                    newExam.createExam(sc, animate);
                    exams.add(newExam);
                    databaseManager.saveExams(exams);
                }
                case "manage", "manage questions" ->{
                    List<Exam> filteredExams = exams.stream()
                        .filter(exam -> exam.getCreatedBy().equalsIgnoreCase(currentTeacher.getName()))
                        .collect(Collectors.toList());

                    animate.animateText("Available Exams ", 25);
                    filteredExams.forEach(exam -> animate.animateText(exam.getSubject(), 25));
                    animate.animateText("Enter the Exam to manage ", 25);
                    filteredExams.stream()
                        .filter(exam -> exam.getSubject().equalsIgnoreCase(sc.nextLine()))
                        .findFirst()
                        .ifPresentOrElse(exam -> {
                            exam.displayQuestions(animate);  
                            animate.animateText(""" 
                                        Would you like to     
                                         \u2192 Add Questions
                                         \u2192 Update Questions
                                         \u2192 Delete Questions
                                         \u2192 Exit
                                        """, 25);
                            while(true) {
                                animate.animateText("Enter your choice ", 25);
                                switch(sc.nextLine()){
                                    case "add" ->{
                                        exam.addQuestion(sc, animate);
                                        animate.animateText("Successfully added Question ", 25);
                                        databaseManager.saveExams(exams); 
                                    }
                                    case "update" ->{
                                        exam.updateQuestion(sc, animate);
                                        animate.animateText("Successfully updated Question ", 25);
                                        databaseManager.saveExams(exams);  
                                    }
                                    case "delete" ->{
                                        exam.deleteQuestion(sc, animate);
                                        animate.animateText("Successfully deleted Question ", 25);
                                        databaseManager.saveExams(exams); 
                                    }
                                    case "exit", "back" ->{
                                        break;
                                    }
                                    default -> animate.animateText("Invalid input", 25);
                                }
                                exam.displayQuestions(animate);
                            }
                        },
                        () -> animate.animateText("Wrong Exam ", 25)
                        );
                }
                case "results report", "generate reulsts report" ->{
                    List<Exam> filteredExams = exams.stream()
                        .filter(exam -> exam.getCreatedBy().equalsIgnoreCase(currentTeacher.getName()))
                        .collect(Collectors.toList());
                    if(filteredExams.isEmpty())  animate.animateText("No avaliable exams", 25);
                    else {
                        filteredExams.forEach(exam -> 
                            animate.animateText(String.format("%-10s %-20s", 
                                exam.getExamID(), 
                                exam.getSubject()), 25)
                        );
                        animate.animateText("Enter Exam ID", 25);
                        exams.stream()
                            .filter(exam -> exam.getExamID().equalsIgnoreCase(sc.nextLine()))
                            .findFirst()
                            .ifPresentOrElse(exam -> exam.resultsReport(animate),
                            () -> animate.animateText("Exam not found!", 25)
                        );
                    }
                }

                case "student result", "view student results" ->{
                    animate.animateText(String.format("%-12s  %-20s  %-12s  %-10s", "Student ID", "Name", "Department", "Semester"), 25);
                    students.forEach(student -> animate.animateText(String.format("%-12s  %-20s  %-12s  %-10s",
                            student.getID(), 
                            student.getName(), 
                            student.getStudentDept(), 
                            student.getStudentSem()), 25));
                    animate.animateText("Enter the Student ID to view results", 25);
                    students.stream()
                        .filter(student -> student.getID().equalsIgnoreCase(sc.nextLine()))
                        .findFirst()
                        .ifPresentOrElse(exam -> exam.displayResults(animate, exams),
                        () -> animate.animateText("Student not found!", 25)
                        );
                }
                case "set timer", "timer" ->{
                    List<Exam> filteredExams = exams.stream()
                        .filter(exam -> exam.getCreatedBy().equalsIgnoreCase(currentTeacher.getName()))
                        .collect(Collectors.toList());
                        if(filteredExams.isEmpty()){
                            animate.animateText("No avaliable exams", 25);
                            break;
                        }
                    animate.animateText(String.format("%-15s %-10s", "Exam Subject", "Exam time"), 25);
                    filteredExams.forEach(exam -> animate.animateText(String.format("%-15s %-10s",
                        exam.getSubject(), exam.getExamTime()), 25));
                    animate.animateText("Enter the Exam to change Time ", 25);
                    filteredExams.stream()
                        .filter(exam -> exam.getSubject().equalsIgnoreCase(sc.nextLine()))
                        .findFirst()
                        .ifPresent(exam -> {
                            animate.animateText("Enter new exam time in minutes: ", 25);
                            exam.setExamTime(sc.nextInt());sc.nextLine(); 
                            databaseManager.saveExams(exams);
                        });
                    }
                case "change password", "password" -> {
                    boolean correctPassword = false;
                    int passwordLimit = 0;
                    while (!correctPassword && passwordLimit < 4) {
                        animate.animateText("Enter old Password", 25);
                        if (currentTeacher.getPassword().equals(sc.nextLine())) {
                            correctPassword = true;
                            boolean validPassword;
                            String newPassword;
                            do {
                                animate.animateText("Enter New Password", 25);
                                newPassword = sc.nextLine();
                                validPassword = newPassword.length() >= 8 && newPassword.matches(".*\\d.*");
                                if (!validPassword) animate.animateText("Password not strong enough. Use at least 8 characters and include a digit.", 25);
                            } while (!validPassword);
                            animate.animateText("Confirm New Password", 25);
                            if (newPassword.equals(sc.nextLine())) {
                                currentTeacher.setPassword(newPassword);
                                databaseManager.saveTeachers(teachers);
                                animate.animateText("Password changed successfully", 25);
                            } else {
                                animate.animateText("Passwords do not match", 25);
                            }
                        } else {
                            animate.animateText("Wrong Password", 25);
                            passwordLimit++;
                            if (passwordLimit >= 3) animate.animateText("Too many attempts", 25);
                        }
                    }
                }
                case "log out", "exit" ->{
                    databaseManager.saveStudents(students);
                    databaseManager.saveExams(exams);
                    animate.animateText("Logging out ", 25);
                    userExist = false;
                    System.out.println("\033[H\033[2J");
                    return;
                }
                default -> animate.animateText("Invalid input", 25);
            }
        }
    }

    public static void adminMenu(Scanner sc, AnimatedText animate, Admin currentAdmin){
        List<Admin> admins = databaseManager.loadAdmins();
        List<Teacher> teachers = databaseManager.loadTeachers();
        List<Student> students = databaseManager.loadStudents();
        List<Exam> exams = databaseManager.loadExams();

        animate.animateText("""
                            ===== Admin Menu =====
                             \u2192 Update Profile
                             \u2192 Create New Teacher
                             \u2192 Create New Student
                             \u2192 View all Exams
                             \u2192 View all Students
                             \u2192 View all Teachers
                             \u2192 Manage Users
                             \u2192 Change Password
                             \u2192 Logout""", 25);
        while (true) {
            animate.animateText("Enter the command ", 25);
            String operation = sc.nextLine().toLowerCase();

            switch(operation){
                case "update profile", "update" -> {
                    currentAdmin.displayAdminDetails(animate);
                    animate.animateText("""
                            === Update Profile ===
                            \u2192 Update Full Name
                            \u2192 Update Email
                            \u2192 Update Phone Number
                            \u2192 Back to Main Menu""", 25);
                    while(true){
                        animate.animateText("Enter your choice ", 25);
                        switch(sc.nextLine()){
                            case "name" ->{
                                animate.animateText("Enter the new Name ", 25);
                                currentAdmin.setName(sc.nextLine());
                                databaseManager.saveAdmins(admins);  
                            }
                            case "email" ->{
                                animate.animateText("Enter the new Email ", 25);
                                currentAdmin.setEmail(sc.nextLine());  
                                databaseManager.saveAdmins(admins);
                            }
                            case "phone" ->{
                                animate.animateText("Enter the new Phone Number ", 25);
                                currentAdmin.setPhone(sc.nextLine());  
                                databaseManager.saveAdmins(admins);
                            }
                            case "exit", "back" ->{
                                break;
                            }
                            default -> animate.animateText("Invalid input", 25);
                        }
                    }
                }
            
                case "new student", "create student" ->{
                    Student newStudent = new Student(sc, animate);
                    students.add(newStudent);
                    databaseManager.saveStudents(students);
                }
                case "new teacher", "create teacher" ->{
                    Teacher newTeacher = new Teacher(sc, animate);
                    teachers.add(newTeacher);
                    databaseManager.saveTeachers(teachers);
                }
                case "view students", "students" -> {
                    animate.animateText(String.format("%-12s  %-20s  %-12s  %-10s", "Student ID", "Name", "Department", "Semester"), 25);
                    students.forEach(student -> animate.animateText(String.format("%-12s  %-20s  %-12s  %-10s",
                            student.getID(), 
                            student.getName(), 
                            student.getStudentDept(), 
                            student.getStudentSem()), 25));
                }
                case "view teachers", "teachers" -> {
                    animate.animateText(String.format("%-12s  %-20s  %-12s", "Student ID", "Name", "Department"), 25);
                    teachers.forEach(teacher -> animate.animateText(String.format("%-12s  %-20s  %-12s",
                            teacher.getID(), 
                            teacher.getName(), 
                            teacher.getTeacherDept()), 25));
                }
                case "view exams", "exams" -> {
                    animate.animateText(String.format("%-10s | %-15s | %-7s | %-15s | %-11s | %-8s", 
                        "Exam ID", "Subject", "Semester", "Created By", "Total Marks", "Duration(Min)"), 25);
                    exams.forEach(exam -> animate.animateText(String.format("%-10s | %-15s | %-7d | %-15s | %-11d | %-8d min",
                            exam.getExamID(), 
                            exam.getSubject(), 
                            exam.getExamSem(),
                            exam.getCreatedBy(),
                            exam.getTotalMarks(),
                            exam.getExamTime()), 25));
                }
                case "manage users", "users" -> {
                    animate.animateText("""
                            === Manage Users ===
                            \u2192 Freeze User
                            \u2192 Unfreeze User
                            \u2192 Back to Main Menu""", 25);
                    while (true) {
                        animate.animateText("Enter your choice ", 25);
                        switch (sc.nextLine()) {
                            case "freeze" -> {
                                animate.animateText("Enter the User ID to freeze ", 25);
                                String userID = sc.nextLine();
                                List<User> users = databaseManager.loadUsers(); // This would load both students and teachers
                                users.stream()
                                    .filter(user -> user.getID().equalsIgnoreCase(userID))
                                    .findFirst()
                                    .ifPresentOrElse(user -> {
                                        user.setStatus(true);
                                        if (user instanceof Student student) {
                                            students.add(student);
                                            databaseManager.saveStudents(students);
                                        } else if (user instanceof Teacher teacher) {
                                            teachers.add(teacher);
                                            databaseManager.saveTeachers(teachers);
                                        }
                                    }, () -> animate.animateText("User not found", 25));
                            }
                            case "unfreeze" -> {
                                animate.animateText("Enter the User ID to unfreeze ", 25);
                                String userID = sc.nextLine();
                                List<User> users = databaseManager.loadUsers(); // This would load both students and teachers
                                users.stream()
                                    .filter(user -> user.getID().equalsIgnoreCase(userID))
                                    .findFirst()
                                    .ifPresentOrElse(user -> {
                                        user.setStatus(false);
                                        if (user instanceof Student student) {
                                            students.add(student);
                                            databaseManager.saveStudents(students);
                                        } else if (user instanceof Teacher teacher) {
                                            teachers.add(teacher);
                                            databaseManager.saveTeachers(teachers);
                                        }
                                    }, () -> animate.animateText("User not found", 25));
                            }
                            case "exit", "back" -> {
                                break;
                            }
                            default -> animate.animateText("Invalid input", 25);
                        }
                    }
                }
                case "change password", "password" -> {
                    boolean correctPassword = false;
                    int passwordLimit = 0;
                    while (!correctPassword && passwordLimit < 4) {
                        animate.animateText("Enter old Password", 25);
                        if (currentAdmin.getPassword().equals(sc.nextLine())) {
                            correctPassword = true;
                            boolean validPassword;
                            String newPassword;
                            do {
                                animate.animateText("Enter New Password", 25);
                                newPassword = sc.nextLine();
                                validPassword = newPassword.length() >= 8 && newPassword.matches(".*\\d.*");
                                if (!validPassword) animate.animateText("Password not strong enough. Use at least 8 characters and include a digit.", 25);
                            } while (!validPassword);
                            animate.animateText("Confirm New Password", 25);
                            if (newPassword.equals(sc.nextLine())) {
                                currentAdmin.setPassword(newPassword);
                                databaseManager.saveAdmins(admins);
                                animate.animateText("Password changed successfully", 25);
                            } else {
                                animate.animateText("Passwords do not match", 25);
                            }
                        } else {
                            animate.animateText("Wrong Password", 25);
                            passwordLimit++;
                            if (passwordLimit >= 3) animate.animateText("Too many attempts", 25);
                        }
                    }
                }
                case "log out", "exit" ->{
                    databaseManager.saveAdmins(admins);
                    databaseManager.saveStudents(students);
                    databaseManager.saveTeachers(teachers);
                    databaseManager.saveExams(exams);
                    animate.animateText("Logging out ", 25);
                    userExist = false;
                    System.out.println("\033[H\033[2J");
                    return;
                }
                default -> animate.animateText("Invalid input", 25);
            }
        }
    }
}