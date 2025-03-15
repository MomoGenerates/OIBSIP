package models;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.AnimatedText;
import util.DatabaseManager;

public class Train implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    private String trainNumber;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private int availableSeats;
    private double fare;
    private final List<Stoppage> stoppages;
    private String departureTime;
    private String arrivalTime;
    private final String[] classTypes = {"Sleeper", "AC 3 Tier", "AC 2 Tier", "AC 1 Tier"};
    private final double[] classFares = {1.0, 1.5, 2.0, 3.0}; // Multipliers for base fare
    
    public Train(Scanner sc, AnimatedText animate) {
            
        animate.animateText("Enter Train Number", 25);
        this.trainNumber = sc.nextLine();
        animate.animateText("Enter Train Name", 25);
        this.trainName = sc.nextLine();
        animate.animateText("Enter Train Source Location", 25);
        this.source = sc.nextLine();
        animate.animateText("Enter Train Destination Location", 25);
        this.destination = sc.nextLine();
        animate.animateText("Enter Total seats in Train ", 25);
        this.totalSeats = this.availableSeats = sc.nextInt(); sc.nextLine();
        animate.animateText("Enter base Fare", 25);
        this.fare = sc.nextDouble(); sc.nextLine();
        animate.animateText("Enter Train Departure Time", 25);
        this.departureTime = sc.nextLine();
        animate.animateText("Enter Train Arrival Time", 25);
        this.arrivalTime = sc.nextLine();
        this.stoppages = new ArrayList<>();
        animate.animateText("Enter the number of intermediate stops", 25);
        int numStops = sc.nextInt(); sc.nextLine();
        for (int i = 1; i <= numStops; i++ ){
            animate.animateText("Station " + i , 25);
            Stoppage newStoppage = new Stoppage(sc, animate);
            stoppages.add(newStoppage);
        }
    }

    public void displayTrainDetails(AnimatedText animate) {
        animate.animateText(String.format("%-6s | %-25s | %-15s | %-15s | %-13s | %-10s | %-17s",
            trainNumber, trainName, source, destination,
            availableSeats + "/" + totalSeats, fare, 
            departureTime + " - " + arrivalTime), 25);
    }

    public void displayStoppages(AnimatedText animate) {
        if (stoppages == null || stoppages.isEmpty()) {
            animate.animateText("This train has no intermediate stops.", 25);
            return;
        }
        
        animate.animateText("\nStoppages for " + trainNumber + " - " + trainName + ":", 25);
        animate.animateText("-----------------------------------------------", 10);
        animate.animateText(String.format("%-4s | %-25s | %-10s | %-10s", "Stop", "Station", "Arrival", "Departure"), 25);
        animate.animateText("-----------------------------------------------", 10);
        
        animate.animateText(String.format("%-4s | %-25s | %-10s | %-10s", "1", source, "--:--", departureTime), 25);
        
        int stopNumber = 2;
        for (Stoppage stop : stoppages) {
            animate.animateText(String.format("%-4d | %-25s | %-10s | %-10s", 
                stopNumber++, stop.getStationName(), stop.getArrivalTime(), stop.getDepartureTime()), 25);
        }
        
        animate.animateText(String.format("%-4d | %-25s | %-10s | %-10s", stopNumber, destination, arrivalTime, "--:--"), 25);
    }

    public void addStop(AnimatedText animate, int position, Stoppage newStop) {
        if (position < 0 || position > stoppages.size()) {
            animate.animateText("Invalid position!", 25);
            return;
        }
        
        int delayMinutes = 0;
        if (position > 0) {
            delayMinutes = calculateDelay(position - 1, newStop.getDepartureTime());
        }
        
        stoppages.add(position, newStop);
        
        if (delayMinutes != 0) {
            updateTimes(position + 1, delayMinutes);
        }
    }

    private int calculateDelay(int prevStopIndex, String newStopDeparture) {
        LocalTime prevDeparture = LocalTime.parse(stoppages.get(prevStopIndex).getDepartureTime(), TIME_FORMATTER);
        LocalTime newDeparture = LocalTime.parse(newStopDeparture, TIME_FORMATTER);
        
        long minutes = java.time.Duration.between(prevDeparture, newDeparture).toMinutes();
        return minutes < 0 ? (int)(minutes + 24 * 60) : (int)minutes;
    }

    private void updateTimes(int startIndex, int delayMinutes) {
        for (int i = startIndex; i < stoppages.size(); i++) {
            Stoppage stop = stoppages.get(i);
            
            LocalTime arrival = LocalTime.parse(stop.getArrivalTime(), TIME_FORMATTER);
            LocalTime departure = LocalTime.parse(stop.getDepartureTime(), TIME_FORMATTER);
            
            arrival = arrival.plusMinutes(delayMinutes);
            departure = departure.plusMinutes(delayMinutes);
            
            stop.setArrivalTime(arrival.format(TIME_FORMATTER));
            stop.setDepartureTime(departure.format(TIME_FORMATTER));
        }
    }

    public boolean bookSeats(int numberOfSeats) {
        if (availableSeats >= numberOfSeats) {
            availableSeats -= numberOfSeats;
            return true;
        }
        return false;
    }
    
    public void cancelSeats(int numberOfSeats) {
        availableSeats += numberOfSeats;
        if (availableSeats > totalSeats) {
            availableSeats = totalSeats;
        }
    }

    public double calculateFare(int classIndex) {
        if (classIndex >= 0 && classIndex < classFares.length) {
            return fare * classFares[classIndex];
        }
        return this.fare;
    }
    
    public void updateTrainDetails(Scanner sc, AnimatedText animate) { 
        boolean changed = false;

        while (true) {
            animate.animateText("""
                === Update Train Details ===
                → Train Name
                → Source
                → Destination
                → Total Seats
                → Base Fare
                → Departure Time
                → Arrival Time
                → Manage Stoppages
                → Back to Previous Menu
                
                Enter your choice: """, 25);
            
            String choice = sc.nextLine().toLowerCase();
            
            switch (choice) {
                case "train name", "name" -> {
                    animate.animateText("Enter new Train Name: ", 25);
                    setTrainName(sc.nextLine());
                    changed = true;
                }
                case "source" -> {
                    animate.animateText("Enter new Source: ", 25);
                    setSource(sc.nextLine());
                    changed = true;
                }
                case "destination" -> {
                    animate.animateText("Enter new Destination: ", 25);
                    setDestination(sc.nextLine());
                    changed = true;
                }
                case "total seats" -> {
                    animate.animateText("Enter new Total Seats: ", 25);
                    try {
                        int newSeats = Integer.parseInt(sc.nextLine());
                        if (newSeats < totalSeats - availableSeats) {
                            animate.animateText("Error: New total seats cannot be less than currently occupied seats\n", 25);
                        } else {
                            setTotalSeats(newSeats);
                            setAvailableSeats(newSeats - (totalSeats - availableSeats));
                            changed = true;
                        }
                    } catch (NumberFormatException e) {
                        animate.animateText("Please enter a valid number\n", 25);
                    }
                }
                case "base fare" -> {
                    animate.animateText("Enter new Base Fare: ", 25);
                    try {
                        setFare(Double.parseDouble(sc.nextLine()));
                        changed = true;
                    } catch (NumberFormatException e) {
                        animate.animateText("Please enter a valid fare amount\n", 25);
                    }
                }
                case "departure time" -> {
                    animate.animateText("Enter new Departure Time (HH:mm): ", 25);
                    setDepartureTime(sc.nextLine());
                    changed = true;
                }
                case "arrival time" -> {
                    animate.animateText("Enter new Arrival Time (HH:mm): ", 25);
                    setArrivalTime(sc.nextLine());
                    changed = true;
                }
                case "manage stoppages", "stoppage" -> {
                    manageStoppages(sc, animate);
                    changed = true;
                }
                case "exit", "back" -> {
                    if (changed) {
                        DatabaseManager db = new DatabaseManager();
                        List<Train> trains = db.loadTrains();
                        trains.removeIf(t -> t.getTrainNumber().equals(this.trainNumber));
                        trains.add(this);
                        db.saveTrains(trains);
                        animate.animateText("Train details updated successfully!\n", 25);
                    }
                    animate.animateText("Returning to previous menu...\n", 25);
                    return;
                }
                default -> animate.animateText("Invalid choice! Please try again.\n", 25);
            }
        }
    }

    private void manageStoppages(Scanner sc, AnimatedText animate) {
        while (true) {
            animate.animateText("""
                === Manage Stoppages ===
                → View Stops
                → Add Stop
                → Remove Stop
                → Change Stop Details
                → Back to Train Details
                
                Enter your choice: """, 25);
            
            String stopChoice = sc.nextLine().toLowerCase();
            
            switch (stopChoice) {
                case "view stops", "view" -> displayStoppages(animate);
                case "add stop", "add" -> {
                    animate.animateText("Enter position to add stoppage: ", 25);
                    try {
                        int position = Integer.parseInt(sc.nextLine()) - 2;
                        Stoppage newStop = new Stoppage(sc, animate);
                        addStop(animate, position, newStop);
                    } catch (NumberFormatException e) {
                        animate.animateText("Please enter a valid position number\n", 25);
                    }
                }
                case "remove stop", "remove" -> {
                    if (stoppages.isEmpty()) {
                        animate.animateText("No stoppages to remove\n", 25);
                        continue;
                    }
                    displayStoppages(animate);
                    animate.animateText("Enter position of stop to remove (2 to " + (stoppages.size() + 1) + "): ", 25);
                    try {
                        int position = Integer.parseInt(sc.nextLine()) - 2;
                        if (position >= 0 && position < stoppages.size()) {
                            stoppages.remove(position);
                            animate.animateText("Stop removed successfully\n", 25);
                        } else {
                            animate.animateText("Invalid position!\n", 25);
                        }
                    } catch (NumberFormatException e) {
                        animate.animateText("Please enter a valid position number\n", 25);
                    }
                }
                case "back", "exit" -> {
                    animate.animateText("Returning to Train Details menu...\n", 25);
                    return;
                }
                default -> animate.animateText("Invalid choice! Please try again.\n", 25);
            }
        }
    }
    
    // getters 
    public double getFare() { return fare; }
    public String getSource() { return source; }
    public int getTotalSeats() { return totalSeats; }
    public String getTrainName() { return trainName; }
    public String getArrivalTime() { return arrivalTime; }
    public String getTrainNumber() { return trainNumber; }
    public String getDestination() { return destination; }
    public String[] getClassTypes() { return classTypes; }
    public int getAvailableSeats() { return availableSeats; }
    public String getDepartureTime() { return departureTime; }
    public List<Stoppage> getStoppages() { return stoppages; }
    
    //  setters
    public void setSource(String source) { this.source = source; }     
    public void setFare(double fare) { this.fare = fare; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public void setTrainNumber(String trainNumber) { this.trainNumber = trainNumber; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
     
}
