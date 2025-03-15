package main;

import java.util.List;
import java.util.Scanner;
import models.*;
import util.*;

public class App {
    private static DatabaseManager db; 

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AnimatedText animate = new AnimatedText();
        db = new DatabaseManager();
        
        while (true) { 
            System.out.println("\033[H\033[2J");
            animate.animateText("Welcome to Online Reservation System ", 25);

            List<User> users = db.loadUsers();
            List<Passenger> passengers = db.loadPassengers();   
            List<Admin> admins = db.loadAdmins();

            // while (!userExist){
                animate.animateText("Logging in ", 25);
                animate.animateText("Enter your username or email: ", 25);
                String tempUser = sc.nextLine();
                
            if (tempUser.equalsIgnoreCase("exit") || tempUser.equalsIgnoreCase("log out")) {
                db.savePassengers(passengers);
                db.saveAdmins(admins);
                System.exit(0);
            }
                
                users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(tempUser)  || 
                    user.getEmail().equalsIgnoreCase(tempUser))
                .findFirst()
                .ifPresentOrElse(
                    user -> { 
                        boolean validateUser = false;
                    while (!validateUser) {
                        animate.animateText("Enter your Password: ", 25);
                        final String userPassword = sc.nextLine();
                        
                        if (user.authenticate(tempUser, userPassword)) {
                            animate.animateText("Welcome " + user.getName(), 25);
                            validateUser = true;
                            if (user instanceof Passenger passenger) userMenu(sc, animate, passenger);
                            else if (user instanceof Admin admin) adminMenu(sc, animate, admin);
                        } else animate.animateText("Invalid Password ", 25);
                    }
                },
                () -> {
                    animate.animateText("Username not found. Would you like to create a new account?", 25);
                    String userChoice = sc.nextLine();
                    if (userChoice.equalsIgnoreCase("yes")) {
                        animate.animateText("Creating new Acc", 25);
                        Passenger newUser = new Passenger(sc, animate);
                        passengers.add(newUser);
                        db.savePassengers(passengers);
                        userMenu(sc, animate, (Passenger) newUser);
                    } else if (userChoice.equalsIgnoreCase("yes admin")) {
                        animate.animateText("Creating new Admin acc", 25);
                        Admin newUser = new Admin(sc, animate);
                        admins.add(newUser);
                        db.saveAdmins(admins);
                        adminMenu(sc, animate, newUser);
                    }
                }); 
            // }
        }
    }

    public static void userMenu(Scanner sc, AnimatedText animate, Passenger currentUser) {
        List<Passenger> passengers = db.loadPassengers();
        List<Train> trains = db.loadTrains();

        while(true){
        animate.animateText("""
                            ===== Passenger Menu =====
                            \u2192 Update Profile
                            \u2192 View Available Trains
                            \u2192 Search Trains
                            \u2192 Make Reservation
                            \u2192 View My Reservations
                            \u2192 Cancel Reservation
                            \u2192 Change Password
                            \u2192 Logout""", 20);

            animate.animateText("Enter the command ", 25);
            String operation = sc.nextLine().toLowerCase();

            switch(operation){
                case "update profile", "update" ->{
                    currentUser.displayUserDetails(animate);
                    animate.animateText("""
                            === Update Profile ===
                            \u2192 Update Username
                            \u2192 Update Full Name
                            \u2192 Update Email
                            \u2192 Update Phone Number
                            \u2192 Display Details 
                            \u2192 Back to Main Menu""", 20);
                    boolean updating = true;
                    while(updating){
                        animate.animateText("Enter your choice ", 25);
                        switch(sc.nextLine()){
                            case "name", "full name" ->{
                                animate.animateText("Enter the new Name ", 25);
                                currentUser.setName(sc.nextLine());
                                db.savePassengers(passengers);
                            }
                            case "username" ->{
                                animate.animateText("Enter the new Username ", 25);
                                currentUser.setUsername(sc.nextLine());
                                db.savePassengers(passengers);
                            }
                            case "email" ->{
                                animate.animateText("Enter the new Email ", 25);
                                currentUser.setEmail(sc.nextLine());  
                                db.savePassengers(passengers);
                            }
                            case "phone" ->{
                                animate.animateText("Enter the new Phone Number ", 25);
                                currentUser.setPhone(sc.nextLine());  
                                db.savePassengers(passengers);
                            }
                            case "exit", "back" -> updating = false;
                            case "display details", "display" -> currentUser.displayUserDetails(animate);
                            default -> animate.animateText("Invalid input", 25);
                        }
                    }
                }
                case "train", "available trains" -> {
                    if (trains.isEmpty()) animate.animateText("No trains available.\n", 25);
                    else {
                        animate.animateText("=== Available Trains ===\n", 25);
                        trains.forEach(train -> train.displayTrainDetails(animate));
                    }
                }
                case "search", "search train" -> {
                    animate.animateText("Enter the source station: ", 25);
                    String source = sc.nextLine();
                    animate.animateText("Enter the destination station: ", 25);
                    String destination = sc.nextLine();
                    
                    List<Train> filteredTrains = trains.stream()
                        .filter(train -> train.getSource().equalsIgnoreCase(source))
                        .toList();
                    
                    List<Train>  availableTrains = filteredTrains.stream()
                        .filter(train -> train.getDestination().equalsIgnoreCase(destination) || 
                            train.getStoppages().stream()
                                .anyMatch(stop -> stop.getStationName().equalsIgnoreCase(destination)))
                        .toList();

                    if (availableTrains.isEmpty()) animate.animateText("No trains available.\n", 25);
                    else {
                        animate.animateText("=== Available Trains ===\n", 25);
                        availableTrains.forEach(train -> train.displayTrainDetails(animate));
                    }
                }
                case "reservation", "make reservation" -> {
                    animate.animateText("Enter the train number: ", 25);
                    String trainNumber = sc.nextLine();
                    trains.stream()
                        .filter(train -> train.getTrainNumber().equalsIgnoreCase(trainNumber))
                        .findFirst()
                        .ifPresentOrElse(
                            train -> {
                                animate.animateText("Enter the number of seats: ", 25);
                                int numSeats = Integer.parseInt(sc.nextLine());
                                if (numSeats > train.getAvailableSeats()) {
                                    animate.animateText("Not enough seats available.\n", 25);
                                    return;
                                }
                                Reservation newReservation = new Reservation(sc, animate, train, currentUser, numSeats);
                                currentUser.addReservation(newReservation);
                                train.bookSeats(numSeats);
                                db.savePassengers(passengers);
                                db.saveTrains(trains);
                                animate.animateText("Reservation successful. Your PNR is " + newReservation.getPNR() + ".\n", 25);
                            }, 
                        () -> animate.animateText("Invalid train number.\n", 25)); 
                }
                case "view reservation", "view my reservations" -> {
                    if (currentUser.getReservations().isEmpty()) animate.animateText("No Active reservations found.\n", 25);
                    else {
                        animate.animateText("=== My Reservations ===\n", 25);
                        currentUser.getReservations().forEach(reservation -> reservation.displayReservationDetails(animate));
                    }

                    if (currentUser.getPastReservations().isEmpty()) animate.animateText("No Previous reservations found.\n", 25);
                    else {
                        animate.animateText("=== Previous Reservations ===\n", 25);
                        currentUser.getPastReservations().forEach(reservation -> reservation.displayReservationDetails(animate));
                    }
                }
                case "cancel reservation", "cancel" -> {
                    if (currentUser.getReservations().isEmpty()) animate.animateText("No reservations to cancel.\n", 25);
                    else {
                        animate.animateText("Enter the PNR of the reservation to cancel: ", 25);
                        String pnr = sc.nextLine();
                        currentUser.getReservations().stream()
                            .filter(reservation -> reservation.getPNR().equalsIgnoreCase(pnr))
                            .findFirst()
                            .ifPresentOrElse(
                                reservation -> {
                                    reservation.cancelReservation();
                                    currentUser.removeReservation(reservation);
                                    currentUser.addPastReservations(reservation);
                                    db.savePassengers(passengers);
                                    db.saveTrains(trains);
                                    animate.animateText("Reservation cancelled successfully.\n", 25);
                                },
                                () -> animate.animateText("Invalid PNR.\n", 25));
                    }
                }
                case "change password", "password" -> {
                    boolean correctPassword = false;
                    int passwordLimit = 0;
                    while (!correctPassword && passwordLimit < 4) {
                        animate.animateText("Enter old Password", 25);
                        if (currentUser.getPassword().equals(sc.nextLine())) {
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
                                currentUser.setPassword(newPassword);
                                db.savePassengers(passengers);
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
                    db.savePassengers(passengers);
                    db.saveTrains(trains);
                    animate.animateText("Logging out ", 25);
                    System.out.println("\033[H\033[2J");
                    return;
                }
            }
        }
    }

    public static void adminMenu(Scanner sc, AnimatedText animate, Admin currentUser){
        List<Passenger> passengers = db.loadPassengers();
        List<Train> trains = db.loadTrains();
        List<Admin> adminss = db.loadAdmins();

        while(true){
        animate.animateText("""
                            ===== Admin Menu =====
                            \u2192 Update Profile
                            \u2192 View All Trains
                            \u2192 Add New Train
                            \u2192 Remove Train
                            \u2192 Update Train Details
                            \u2192 View All Reservations
                            \u2192 Search Reservations by PNR
                            \u2192 Search Traveler by Username
                            \u2192 View System Statistics
                            \u2192 Change Password
                            \u2192 Logout""", 20);

            animate.animateText("Enter the command ", 25);
            String operation = sc.nextLine().toLowerCase();

            switch(operation){
                case "update profile", "update" ->{
                    currentUser.displayUserDetails(animate);
                    animate.animateText("""
                            === Update Profile ===
                            \u2192 Update Username
                            \u2192 Update Full Name
                            \u2192 Update Email
                            \u2192 Update Phone Number
                            \u2192 Back to Main Menu""", 20);
                    while(true){
                        animate.animateText("Enter your choice ", 25);
                        switch(sc.nextLine()){
                            case "name", "full name" ->{
                                animate.animateText("Enter the new Name ", 25);
                                currentUser.setName(sc.nextLine());
                                db.saveAdmins(adminss);
                            }
                            case "username" ->{
                                animate.animateText("Enter the new Username ", 25);
                                currentUser.setUsername(sc.nextLine());
                                db.saveAdmins(adminss);
                            }
                            case "email" ->{
                                animate.animateText("Enter the new Email ", 25);
                                currentUser.setEmail(sc.nextLine());  
                                db.saveAdmins(adminss);
                            }
                            case "phone" ->{
                                animate.animateText("Enter the new Phone Number ", 25);
                                currentUser.setPhone(sc.nextLine());  
                                db.saveAdmins(adminss);
                            }
                            case "exit", "back" ->{
                                break;
                            }
                            default -> animate.animateText("Invalid input", 25);
                        }
                    }
                }
                case "train", "all trains" -> {
                    if (trains.isEmpty()) animate.animateText("No trains available.\n", 25);
                    else {
                        animate.animateText("=== Available Trains ===\n", 25);
                        trains.forEach(train -> train.displayTrainDetails(animate));
                    }
                }
                case "add train", "new train" -> {
                    Train newTrain = new Train(sc, animate);
                    trains.add(newTrain);
                    db.saveTrains(trains);
                    animate.animateText("Train added successfully.\n", 25);
                }
                case "remove train", "delete train" -> {
                    if (trains.isEmpty()) animate.animateText("No trains available.\n", 25);
                    else {
                        animate.animateText("Enter the train number to remove: ", 25);
                        String trainNumber = sc.nextLine();
                        trains.removeIf(train -> train.getTrainNumber().equalsIgnoreCase(trainNumber));
                        db.saveTrains(trains);
                        animate.animateText("Train removed successfully.\n", 25);
                    } 
                }
                case "update train", "edit train" -> {
                    if (trains.isEmpty()) animate.animateText("No trains available.\n", 25);
                    else {
                        animate.animateText("Enter the train number to update: ", 25);
                        String trainNumber = sc.nextLine();
                        trains.stream()
                            .filter(train -> train.getTrainNumber().equalsIgnoreCase(trainNumber))
                            .findFirst()
                            .ifPresentOrElse(
                                train -> {
                                    train.updateTrainDetails(sc, animate);
                                    db.saveTrains(trains);
                                    animate.animateText("Train updated successfully.\n", 25);
                                },
                                () -> animate.animateText("Invalid train number.\n", 25));
                    }
                }
                case "view reservation", "view all reservations" -> {
                    boolean anyReservationsExist = passengers.stream()
                        .anyMatch(passenger -> !passenger.getReservations().isEmpty());
                        
                    if (passengers.isEmpty() || !anyReservationsExist) {
                        animate.animateText("No reservations found.\n", 25);
                    } else {
                        animate.animateText("=== All Reservations ===\n", 25);
                        
                        for (Passenger passenger : passengers) {
                            if (!passenger.getReservations().isEmpty()) {
                                animate.animateText("Passenger: " + passenger.getName() + "\n", 25);
                                
                                for (Reservation reservation : passenger.getReservations()) {
                                    reservation.displayReservationDetails(animate);
                                    animate.animateText("\n", 25);
                                }
                            }
                        }
                    }
                }
                case "search reservation", "search by pnr" -> {
                    if (passengers.isEmpty()) animate.animateText("No reservations found.\n", 25);
                    else {
                        animate.animateText("Enter the PNR to search: ", 25);
                        String pnr = sc.nextLine();
                        passengers.stream()
                            .filter(passenger -> passenger.getReservations().stream()
                                .anyMatch(reservation -> reservation.getPNR().equalsIgnoreCase(pnr)))
                            .findFirst()
                            .ifPresentOrElse(
                                passenger -> {
                                    animate.animateText("Passenger: " + passenger.getName(), 25);
                                    passenger.getReservations().stream()
                                        .filter(reservation -> reservation.getPNR().equalsIgnoreCase(pnr))
                                        .findFirst()
                                        .ifPresentOrElse(
                                            reservation -> reservation.displayReservationDetails(animate),
                                            () -> animate.animateText("Invalid PNR.\n", 25));
                                },
                                () -> animate.animateText("Invalid PNR.\n", 25));
                    }
                }
                case "search traveler", "search by username" -> {
                    animate.animateText("Enter the username to search: ", 25);
                    String username = sc.nextLine();
                    passengers.stream()
                        .filter(passenger -> passenger.getName().equalsIgnoreCase(username))
                        .findFirst()
                        .ifPresentOrElse(
                            passenger -> {
                                animate.animateText("Passenger: " + passenger.getName() + "\n", 25);
                                if (passenger.getReservations().isEmpty()) {
                                    animate.animateText("This passenger has no reservations.\n", 25);
                                } else {
                                    animate.animateText("Reservations:\n", 25);
                                    passenger.getReservations().forEach(reservation -> {
                                        reservation.displayReservationDetails(animate);
                                        animate.animateText("\n", 25);
                                    });
                                }
                            },
                            () -> animate.animateText("Invalid username.\n", 25));
                }
                case "statistics", "system statistics" -> {
                    int totalTrains = trains.size();
                    int totalPassengers = passengers.size();
                    int totalReservations = passengers.stream()
                        .mapToInt(passenger -> passenger.getReservations().size())
                        .sum();
                    
                    animate.animateText("=== System Statistics ===\n", 25);
                    animate.animateText("Total Trains: " + totalTrains, 25);
                    animate.animateText("Total Passengers: " + totalPassengers, 25);
                    animate.animateText("Total Reservations: " + totalReservations, 25);
                }
                case "change password", "password" -> {
                    boolean correctPassword = false;
                    int passwordLimit = 0;
                    while (!correctPassword && passwordLimit < 4) {
                        animate.animateText("Enter old Password", 25);
                        if (currentUser.getPassword().equals(sc.nextLine())) {
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
                                currentUser.setPassword(newPassword);
                                db.savePassengers(passengers);
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
                    db.savePassengers(passengers);
                    db.saveTrains(trains);
                    animate.animateText("Logging out ", 25);
                    System.out.println("\033[H\033[2J");
                    return;
                }

            }
        }
    }
}