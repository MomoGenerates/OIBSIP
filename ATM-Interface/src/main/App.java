package main;

import java.util.List;
import java.util.Scanner;
import util.*;

public class App {
    private static String tempUser;
    private static User currentUser;
    private static UserManager userManager;
    //Validation Variables
    static boolean UserExist = false;
    static boolean validateUser = false;

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        Scanner sc = new Scanner(System.in);
        userManager = new UserManager();
        AnimatedText animate = new AnimatedText();
        System.out.println("\033[H\033[2J");
        animate.animateText("Welcome to ATM ", 25);

        while (true){
            List<User> users = userManager.getUsers(); 
            animate.animateText("Logging in ", 25);

            while(!UserExist){   //User Name input loop
                animate.animateText("Enter your Name", 25);
                final String userName = tempUser = sc.nextLine();
                if (userName.equalsIgnoreCase("exit")) { System.exit(0); }

                if (users.stream().noneMatch(user -> user.getName().equalsIgnoreCase(userName))) {
                    animate.animateText("User not found in System \nWould you like to make a account ", 25);
                    if(sc.nextLine().equalsIgnoreCase("yes")) {
                        User newUser = new User();
                        newUser.createAcc(sc);
                        users.add(newUser);
                        userManager.saveUsers(users);
                    }
                    if(sc.nextLine().equalsIgnoreCase("admin acc")) {
                        User newUser = new User(true);
                        newUser.adminCreateAcc(sc);
                        users.add(newUser);
                        userManager.saveUsers(users);
                    }
                } else UserExist = true;
            }
            
            users = userManager.getUsers();
            while(!validateUser){   //User Pin input loop
                animate.animateText("Enter your Pin ", 25);
                final String userPin = sc.nextLine();

                if (users.stream().anyMatch(user -> { 
                    if(user.getName().equalsIgnoreCase(tempUser) && user.getPin().equals(userPin)){
                        if(user.getStatus()) {
                            animate.animateText("Account is Frozen", 25);
                            return false;
                        }
                        currentUser = user; 
                        return true;
                    } 
                    return false;
                })) {
                    animate.animateText("Welcome "+tempUser, 25);
                    validateUser = true;
                } else animate.animateText("Invalid Pin ", 25);
            }

            if (currentUser.isAdmin()) {
                adminMenu(users, sc, animate);
            } else {
                userMenu(users, sc, animate);
            }
        }
    }

    private static void userMenu(List<User> users, Scanner sc, AnimatedText animate) {
        
        animate.animateText("""
                            You can do following operations 
                            \u2192 Deposit 
                            \u2192 Withdraw 
                            \u2192 Check Balance 
                            \u2192 Transfer money 
                            \u2192 Transaction History
                            \u2192 Delete Account """, 25);
        while (true) { 
            animate.animateText("Enter the command ", 25);
            String operation = sc.nextLine().toLowerCase();
            switch(operation){
                case "deposit" -> {
                    animate.animateText("Enter the amount to deposit ", 25);
                    currentUser.deposit(sc.nextDouble());
                    sc.nextLine();
                }
                case "withdraw" -> {
                    animate.animateText("Enter the amount to withdraw ", 25);
                    currentUser.withdraw(sc.nextDouble());
                    sc.nextLine();
                }
                case "transfer" -> {
                    boolean trasnferUserNotFound;
                    int invalidInput = 0;
                    do{
                        if(invalidInput == 3) {
                            animate.animateText("Too many invalid inputs", 25);
                            break;
                        }
                        animate.animateText("Enter the user to transfer to ", 25);
                        String targetUser = sc.nextLine();
                        User transferUser = users.stream()
                            .filter(user -> user.getName().equalsIgnoreCase(targetUser)).findFirst().orElse(null);

                        if(transferUser == null) {
                            invalidInput++;
                            animate.animateText("User not found", 25);  
                            trasnferUserNotFound = true;
                        } else {
                            animate.animateText("Enter the amount to transfer ", 25);
                            double transferAmount = sc.nextDouble();
                            sc.nextLine();
                            currentUser.transferFrom(transferAmount, transferUser);
                            transferUser.transferTo(transferAmount, currentUser.getName());
                            trasnferUserNotFound = false;
                        }
                    }while(trasnferUserNotFound);
                }
                case "check balance", "balance" -> {
                    currentUser.checkBalance();
                }
                case "transaction history", "history" -> {
                    currentUser.printTransactionHistory();
                }
                case "delete account", "delete" -> {
                    animate.animateText("Are you sure you want to delete your account ", 25);
                    if(sc.nextLine().equalsIgnoreCase("yes")) {
                        if (currentUser.getBalance() > 0) {
                            animate.animateText("Withdraw your balance first or Transfer it", 25);
                            if(sc.nextLine().equalsIgnoreCase("withdraw")) { 
                                currentUser.withdraw(currentUser.getBalance()); 
                            }
                            if(sc.nextLine().equalsIgnoreCase("transfer")) {
                                animate.animateText("Enter the user to transfer to ", 25);
                                String targetUser = sc.nextLine();
                                User transferUser = users.stream()
                                    .filter(user -> user.getName().equalsIgnoreCase(targetUser)).findFirst().orElse(null);
                                if(transferUser != null) {
                                    currentUser.transferFrom(currentUser.getBalance(), transferUser);
                                    transferUser.transferTo(currentUser.getBalance(), currentUser.getName());
                                } else animate.animateText("User not found", 25);
                            }
                        }
                        users.remove(currentUser);
                        userManager.saveUsers(users);
                        animate.animateText("Account deleted", 25);
                        System.exit(0);
                    }
                }
                case "exit", "Log out" -> {
                    animate.animateText("Logging out ", 25);
                    UserExist = false;
                    validateUser = false;
                    return;
                }
                default -> animate.animateText("Invalid input", 25);
            }
        }
    }

    private static void adminMenu(List<User> users, Scanner sc, AnimatedText animate) {
        List<AdminLogs> adminLogsList = AdminLogs.getLogs();
        adminLogsList.add(new AdminLogs(currentUser.getUserID(), currentUser.getName(), "Logged in"));
        animate.animateText("""
                            Admin Menu
                            \u2192 View All Users
                            \u2192 View All Admin
                            \u2192 View Users details
                            \u2192 View Admin Logs
                            \u2192 Delete User
                            \u2192 Freeze User
                            \u2192 Unfreeze User
                            \u2192 Exit""", 25);
        
        while (true) {
            animate.animateText("Enter the command ", 25);
            String operation = sc.nextLine().toLowerCase();

            switch(operation) {
                case "view all users", "view all" ->{
                    animate.animateText("Regular Users:", 25);
                    users.stream()
                        .filter(user -> !user.isAdmin())
                        .forEach(user -> animate.animateText(user.getUserID() + "\t" + user.getName(), 25));
                        adminLogsList.add(new AdminLogs(currentUser.getUserID(), currentUser.getName(), "Viewed all users"));
                        AdminLogs.saveLogs(adminLogsList);
                    }
                    case "view all admin", "view admin" ->{
                        animate.animateText("Admin Users:", 25);
                        users.stream()
                        .filter(User::isAdmin)
                        .forEach(user -> animate.animateText(user.getUserID() + "\t" + user.getName(), 25));
                        adminLogsList.add(new AdminLogs(currentUser.getUserID(), currentUser.getName(), "Viewed all Admins"));
                        AdminLogs.saveLogs(adminLogsList);
                }
                case "view user details", "view user" -> {
                    animate.animateText("Enter user name to view details: ", 25);
                    String userName = sc.nextLine();
                    users.stream()
                        .filter(user -> user.getName().equalsIgnoreCase(userName))
                        .forEach(user -> animate.animateText(user.toString(), 25));
                    adminLogsList.add(new AdminLogs(currentUser.getUserID(), currentUser.getName(), "Viewed user details of " + userName));
                    AdminLogs.saveLogs(adminLogsList);
                }
                case "delete user", "delete" -> {
                    animate.animateText("Enter user name to delete: ", 25);
                    String userName = sc.nextLine();
                    if(users.removeIf(user -> user.getName().equalsIgnoreCase(userName))) {
                        animate.animateText("User deleted", 25);
                        userManager.saveUsers(users);
                        adminLogsList.add(new AdminLogs(currentUser.getUserID(), currentUser.getName(), "Deleted user " + userName));
                        AdminLogs.saveLogs(adminLogsList);
                    } else
                        animate.animateText("User not found ", 25);
                }
                case "freeze user", "freeze" -> {
                    users.stream()
                        .filter(user -> !user.isAdmin())
                        .filter(user -> !user.getStatus())
                        .forEach(user -> animate.animateText(user.getUserID() + "\t" + user.getName(), 25));
                    animate.animateText("Enter user ID to freeze: ", 25);
                    String userID = sc.nextLine();
                    users.stream()
                        .filter(user -> user.getUserID().equalsIgnoreCase(userID) && !user.isAdmin())
                        .findFirst()
                        .ifPresentOrElse(user -> {
                            user.setStatus(true);
                            userManager.saveUsers(users);
                            animate.animateText("User account frozen", 25);
                            adminLogsList.add(new AdminLogs(currentUser.getUserID(), currentUser.getName(), "Froze user account: " + user.getUserID()));
                            AdminLogs.saveLogs(adminLogsList);
                        }, () -> animate.animateText("User not found or is admin", 25));
                }
                case "unfreeze user", "unfreeze" -> {
                    users.stream()
                        .filter(user -> !user.isAdmin())
                        .filter(user -> user.getStatus())
                        .forEach(user -> animate.animateText(user.getUserID() + "\t" + user.getName(), 25));
                    animate.animateText("Enter user ID to unfreeze: ", 25);
                    String userID = sc.nextLine();
                    users.stream()
                        .filter(user -> user.getUserID().equalsIgnoreCase(userID) && !user.isAdmin())
                        .findFirst()
                        .ifPresentOrElse(user -> {
                            user.setStatus(false);
                            userManager.saveUsers(users);
                            animate.animateText("User account unfrozen", 25);
                            adminLogsList.add(new AdminLogs(currentUser.getUserID(), currentUser.getName(), "Unfroze user account: " + user.getUserID()));
                            AdminLogs.saveLogs(adminLogsList);
                        }, () -> animate.animateText("User not found or is admin", 25));
                }
                case "admin logs", "logs" -> {
                    adminLogsList.add(new AdminLogs(currentUser.getUserID(), currentUser.getName(), "Viewed Admin Logs"));
                    AdminLogs.saveLogs(adminLogsList);
                    AdminLogs.printAdminLogs(animate, adminLogsList);
                }
                case "exit" -> {
                    animate.animateText("Logging out ", 25);
                    UserExist = false;
                    validateUser = false;
                    return;
                }
                default -> animate.animateText("Invalid input", 25);
            }
        }
    }

}