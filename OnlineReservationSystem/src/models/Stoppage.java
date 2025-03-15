package models;

import java.io.Serializable;
import java.util.Scanner;
import util.AnimatedText;

public class Stoppage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String stationName;
    private String arrivalTime;
    private String departureTime;
    
    Stoppage(Scanner sc, AnimatedText animate){
        animate.animateText("Enter station name: ", 25);
        stationName = sc.nextLine();
        animate.animateText("Enter arrival time: ", 25);
        arrivalTime = sc.nextLine();
        animate.animateText("Enter departure time: ", 25);
        departureTime = sc.nextLine();
    }

    // getters
    public String getStationName() { return stationName; }
    public String getArrivalTime() { return arrivalTime; } 
    public String getDepartureTime() { return departureTime; }

    // setters 
    public void setStationName(String stationName) { this.stationName = stationName; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
}
