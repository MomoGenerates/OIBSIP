package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import models.*;

public class DatabaseManager {
    private static final String DB_FOLDER = "database/";
    private static final String PASSENGER_FILE = DB_FOLDER + "passenger.dat";
    private static final String TRAIN_FILE = DB_FOLDER + "train.dat";
    private static final String ADMIN_FILE = DB_FOLDER + "admin.dat";
    
    static {
        new File(DB_FOLDER).mkdirs();
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        users.addAll(loadAdmins());
        users.addAll(loadPassengers());
        return users;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void savePassengers(List<Passenger> passengers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(PASSENGER_FILE))) {
            oos.writeObject(passengers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Passenger> loadPassengers() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(PASSENGER_FILE))) {
            return (List<Passenger>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); 
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void saveTrains(List<Train> trains) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(TRAIN_FILE))) {
            oos.writeObject(trains);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Train> loadTrains() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(TRAIN_FILE))) {
            return (List<Train>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>(); 
        }
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
}