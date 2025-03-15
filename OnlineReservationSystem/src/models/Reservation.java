package models;

import java.io.Serializable;
import java.time.LocalDate;
import util.PNRGenerator;
import util.AnimatedText;
import java.util.Scanner;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final PNRGenerator pnrGenerator = new PNRGenerator();
    
    private final String pnr;
    private final User user;
    private final Train train;
    private LocalDate journeyDate;
    private final int numberOfPassengers;
    private final double totalFare;
    private boolean cancelled;
    private final String classType;
    private final String bookingDate;
    private String status;
    
    public Reservation(Scanner sc, AnimatedText animate, Train train, Passenger passenger, int numSeats) {
        this.pnr = pnrGenerator.generatePNR();
        this.train = train;
        this.user = passenger;
        animate.animateText("Enter the date of journey (yyyy-mm-dd): ", 25);
        this.journeyDate = LocalDate.parse(sc.nextLine());
        this.numberOfPassengers = numSeats;
        
        String[] classTypes = train.getClassTypes();
        animate.animateText("\nAvailable Class Types:", 25);
        for (int i = 0; i < classTypes.length; i++) {
            double classFare = train.calculateFare(i);
            animate.animateText(String.format("%d. %s - ₹%.2f", (i + 1), classTypes[i], classFare), 25);
        }
        
        int classChoice;
        while (true) {
            animate.animateText("\nEnter the class number (1-" + classTypes.length + "): ", 25);
            try {
                classChoice = Integer.parseInt(sc.nextLine()) - 1;
                if (classChoice >= 0 && classChoice < classTypes.length) {
                    break;
                }
                animate.animateText("Invalid choice. Please try again.", 25);
            } catch (NumberFormatException e) {
                animate.animateText("Please enter a valid number.", 25);
            }
        }
        
        this.classType = classTypes[classChoice];
        this.totalFare = train.calculateFare(classChoice) * numSeats;
        
        animate.animateText(String.format("\nSelected Class: %s", this.classType), 25);
        animate.animateText(String.format("Total Fare: ₹%.2f", this.totalFare), 25);
        
        this.cancelled = false;
        this.bookingDate = LocalDate.now().toString();
        this.status = "Confirmed";
    }

    public void cancelReservation() {
        this.cancelled = true;
        this.status = "Cancelled";
        this.train.cancelSeats(numberOfPassengers);
    }

    public void displayReservationDetails(AnimatedText animate) {
        animate.animateText("PNR: " + pnr, 25);
        animate.animateText("Train: " + train.getTrainName(), 25);
        animate.animateText("Journey Date: " + journeyDate, 25);
        animate.animateText("Number of Passengers: " + numberOfPassengers, 25);
        animate.animateText("Total Fare: ₹" + totalFare, 25);
        animate.animateText("Class Type: " + classType, 25);
        animate.animateText("Booking Date: " + bookingDate, 25);
        animate.animateText("Status: " + status, 25);
    }

    // getters 
    public String getPNR() { return pnr; }
    public Train getTrain() { return train; }
    public LocalDate getJourneyDate() { return journeyDate; }
    public int getNumberOfPassengers() { return numberOfPassengers; }
    public double getTotalFare() { return totalFare; }
    public String getClassType() { return classType; }
    public User getUser() { return user; }
    public boolean isCancelled() { return cancelled; }
    public String getStatus() { return status; }
    // public List<Passenger> getPassengers() { return passengers; }
    public String getBookingDate() { return bookingDate; }
    
    // setters
    public void setJourneyDate(LocalDate journeyDate) { this.journeyDate = journeyDate; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    // public void addPassenger(Passenger passenger) { this.passengers.add(passenger); }
    public void setStatus(String status) { this.status = status; }
    


}
