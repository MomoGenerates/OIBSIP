package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.*;

public class DatabaseManager {
    private static final String DB_FOLDER = "database/";
    private static final String STUDENT_FILE = DB_FOLDER + "student.dat";
    private static final String TEACHER_FILE = DB_FOLDER + "teacher.dat";
    private static final String ADMIN_FILE = DB_FOLDER + "admin.dat";
    private static final String QUESTIONS_FILE = DB_FOLDER + "exams.dat";
    
    static {
        new File(DB_FOLDER).mkdirs();
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        users.addAll(loadAdmins());
        users.addAll(loadStudents());
        users.addAll(loadTeachers());
        return users;
    }


    @SuppressWarnings("CallToPrintStackTrace")
    public void saveAdmins(List<Admin> admins) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ADMIN_FILE))) {
            oos.writeObject(admins);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Admin> loadAdmins() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ADMIN_FILE))) {
            return (List<Admin>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); 
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void saveStudents(List<Student> Students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(STUDENT_FILE))) {
            oos.writeObject(Students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Student> loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(STUDENT_FILE))) {
            return (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); 
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void saveTeachers(List<Teacher> Teachers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(TEACHER_FILE))) {
            oos.writeObject(Teachers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Teacher> loadTeachers() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(TEACHER_FILE))) {
            return (List<Teacher>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); 
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void saveExams(List<Exam> Exams) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(QUESTIONS_FILE))) {
            oos.writeObject(Exams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Exam> loadExams() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(QUESTIONS_FILE))) {
            return (List<Exam>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}