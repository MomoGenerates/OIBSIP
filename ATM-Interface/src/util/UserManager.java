package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.User;

public class UserManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = "database/users.ser";

    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            return users;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            users = (List<User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
        
        return users;
    }

    public void saveUsers(List<User> users) {
        if (users == null) {
            throw new IllegalArgumentException("Users list cannot be null");
        }

        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}