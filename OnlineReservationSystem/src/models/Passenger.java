package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import util.AnimatedText;

public class Passenger extends User{
    private static final long serialVersionUID = 1L;

    private String address;
    private final List<Reservation> reservations;
    private final List<Reservation> pastReservations;

    public Passenger(Scanner sc, AnimatedText animate) {
        super(sc, animate);
        animate.animateText("Enter your Address ", 25);
        this.address = sc.nextLine();
        this.reservations = new ArrayList<>();
        this.pastReservations = new ArrayList<>();
        animate.animateText("Accont sucessfully created", 25);
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
    
    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

    public void addPastReservations(Reservation reservation) {
        pastReservations.add(reservation);
    }

    // getters 
    public String getAddress() { return this.address; }
    public List<Reservation> getReservations() { return this.reservations; }
    public List<Reservation> getPastReservations() { return this.pastReservations; }

    // setters 
    public void setAddress(String address) { this.address = address; }

}
