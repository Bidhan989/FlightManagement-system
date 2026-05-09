package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Role;
import bcu.cmp5332.bookingsystem.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages persistence of user data.
 * Handles loading and storing user information to/from files.
 * 
 * @version 8.0 (Authentication System)
 */
public class UserDataManager {
    
    private static final String USERS_FILE = "./resources/data/users.txt";
    private static final String SEPARATOR = "::";
    
    /**
     * Loads all users from the data file.
     * 
     * @return list of users
     * @throws IOException if file cannot be read
     */
    public List<User> loadUsers() throws IOException {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        
        // Create file if it doesn't exist
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            // Create default admin user
            createDefaultAdmin(users);
            saveUsers(users);
            return users;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    User user = parseUser(line);
                    users.add(user);
                } catch (Exception e) {
                    System.err.println("Error parsing user line: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }
        
        // Ensure default admin exists
        if (users.stream().noneMatch(u -> u.getRole() == Role.ADMIN)) {
            createDefaultAdmin(users);
            saveUsers(users);
        }
        
        return users;
    }
    
    /**
     * Saves all users to the data file.
     * 
     * @param users the list of users to save
     * @throws IOException if file cannot be written
     */
    public void saveUsers(List<User> users) throws IOException {
        File file = new File(USERS_FILE);
        file.getParentFile().mkdirs();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (User user : users) {
                String line = formatUser(user);
                writer.println(line);
            }
        }
    }
    public static void storeUsers(FlightBookingSystem fbs) throws IOException {
        UserDataManager udm = new UserDataManager();
        udm.saveUsers(fbs.getUsers());
    }

    
    /**
     * Parses a user from a data line.
     * Format: username::passwordHash::role::customerId::
     * 
     * @param line the data line
     * @return the parsed user
     */
    private User parseUser(String line) {
        String[] parts = line.split(SEPARATOR, -1);
        
        String username = parts[0];
        String passwordHash = parts[1];
        Role role = Role.valueOf(parts[2]);
        Integer customerId = parts.length > 3 && !parts[3].isEmpty() 
                           ? Integer.parseInt(parts[3]) 
                           : null;
        
        return new User(username, passwordHash, role, customerId);
    }
    
    /**
     * Formats a user for storage.
     * Format: username::passwordHash::role::customerId::
     * 
     * @param user the user to format
     * @return the formatted string
     */
    private String formatUser(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(user.getUsername()).append(SEPARATOR);
        sb.append(user.getPasswordHash()).append(SEPARATOR);
        sb.append(user.getRole().name()).append(SEPARATOR);
        sb.append(user.getCustomerId() != null ? user.getCustomerId() : "").append(SEPARATOR);
        return sb.toString();
    }
    
    /**
     * Creates the default admin user.
     * Username: admin
     * Password: admin123
     * 
     * @param users the list to add the admin to
     */
    private void createDefaultAdmin(List<User> users) {
        // Hash of "admin123"
        String adminPasswordHash = "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9";
        User admin = new User("admin", adminPasswordHash, Role.ADMIN, null);
        users.add(admin);
        System.out.println("Default admin user created:");
        System.out.println("  Username: admin");
        System.out.println("  Password: admin123");
    }
}
