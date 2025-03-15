package main;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String type; 
    private final double amount;
    private final LocalDateTime timestamp;
    private final String details; 

    public Transaction(String type, double amount, String details) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now(); 
        this.details = details;
    }
    
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDetails() { return details; }
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd-yyyy  HH:mm");
        return timestamp.format(formatter);
    }
}