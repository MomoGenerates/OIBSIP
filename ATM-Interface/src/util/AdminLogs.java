package util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminLogs implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String adminID;
    private final String adminName;
    private final String action;
    private final LocalDateTime timestamp;

    private static final String FILE_PATH = "database/adminlog.ser";
    
    public AdminLogs(String adminID,String adminName, String action) {
        this.adminID = adminID;
        this.adminName = adminName;
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }

    @SuppressWarnings("unchecked")
    public static List<AdminLogs> getLogs() {
        List<AdminLogs> logs = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            return logs;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            logs = (List<AdminLogs>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading Logs: " + e.getMessage());
        }
        
        return logs;
    }

    public static void saveLogs(List<AdminLogs> logs) {
        if (logs == null) {
            throw new IllegalArgumentException("Log list cannot be null");
        }

        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(logs);
        } catch (IOException e) {
            System.out.println("Error saving Logs: " + e.getMessage());
        }
    }

    public static void printAdminLogs(AnimatedText animate, List<AdminLogs> adminLogsList) {
        animate.animateText(String.format("%-20s %-15s %-15s %-20s", 
            "Time", 
            "Admin ID", 
            "Admin Name", 
            "Admin Action"), 
            25);
        
        for (AdminLogs al : adminLogsList) {
            animate.animateText(String.format("%-20s %-15s %-15s %-20s",
                al.getFormattedTimestamp(),
                al.getAdminName(),
                al.getAdminID(),
                al.getAction()),
                25);
        }
    }

    // Getters
    public String getAdminName() { return adminName; }
    public String getAction() { return action; }
    public String getAdminID() { return adminID; }
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-yyyy  HH:mm:ss");
        return timestamp.format(formatter);
    }
}