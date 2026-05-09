package bcu.cmp5332.bookingsystem.services;

import bcu.cmp5332.bookingsystem.data.UserDataManager;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Role;
import bcu.cmp5332.bookingsystem.model.Session;
import bcu.cmp5332.bookingsystem.model.User;
import bcu.cmp5332.bookingsystem.util.PasswordUtil;

import java.io.IOException;
import java.util.List;

/**
 * Service for handling user authentication.
 * Manages login, logout, and user validation.
 * 
 * @version 8.0 (Authentication System)
 */
public class AuthenticationService {
    
    private List<User> users;
    private UserDataManager userDataManager;
    
    /**
     * Creates a new AuthenticationService.
     */
    public AuthenticationService() {
        this.userDataManager = new UserDataManager();
        try {
            this.users = userDataManager.loadUsers();
        } catch (IOException e) {
            this.users = new java.util.ArrayList<>();
            System.err.println("Warning: Could not load users: " + e.getMessage());
        }
    }
    
    /**
     * Attempts to log in a user with the given credentials.
     * 
     * @param username the username
     * @param password the plain text password
     * @param expectedRole the expected role (ADMIN or PASSENGER)
     * @return the authenticated user
     * @throws FlightBookingSystemException if authentication fails
     */
    public User login(String username, String password, Role expectedRole) 
            throws FlightBookingSystemException {
        
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            throw new FlightBookingSystemException("Username is required.");
        }
        
        if (password == null || password.isEmpty()) {
            throw new FlightBookingSystemException("Password is required.");
        }
        
        // Find user
        User user = findUserByUsername(username);
        if (user == null) {
            throw new FlightBookingSystemException("Invalid username or password.");
        }
        
        // Verify password
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            throw new FlightBookingSystemException("Invalid username or password.");
        }
        
        // Check role
        if (user.getRole() != expectedRole) {
            if (expectedRole == Role.ADMIN) {
                throw new FlightBookingSystemException(
                    "This account is not an administrator account.\n" +
                    "Please use the Passenger login instead.");
            } else {
                throw new FlightBookingSystemException(
                    "This is an administrator account.\n" +
                    "Please use the Admin login instead.");
            }
        }
        
        // Login successful - create session
        Session.getInstance().login(user);
        
        return user;
    }
    
    /**
     * Registers a new passenger user.
     * 
     * @param username the desired username
     * @param password the password
     * @param customerId the linked customer ID
     * @return the newly created user
     * @throws FlightBookingSystemException if registration fails
     */
    public User registerPassenger(String username, String password, Integer customerId) 
            throws FlightBookingSystemException {
        
        // Validate username
        if (username == null || username.trim().isEmpty()) {
            throw new FlightBookingSystemException("Username is required.");
        }
        
        if (username.length() < 3) {
            throw new FlightBookingSystemException("Username must be at least 3 characters.");
        }
        
        // Check if username already exists
        if (findUserByUsername(username) != null) {
            throw new FlightBookingSystemException("Username already exists. Please choose another.");
        }
        
        // Validate password
        if (!PasswordUtil.isValidPassword(password)) {
            throw new FlightBookingSystemException(PasswordUtil.getPasswordRequirements());
        }
        
        // Hash password
        String passwordHash = PasswordUtil.hashPassword(password);
        
        // Create user
        User newUser = new User(username, passwordHash, Role.PASSENGER, customerId);
        users.add(newUser);
        
        // Save to file
        try {
            userDataManager.saveUsers(users);
        } catch (IOException e) {
            throw new FlightBookingSystemException("Error saving user: " + e.getMessage());
        }
        
        return newUser;
    }
    
    /**
     * Logs out the current user.
     */
    public void logout() {
        Session.getInstance().logout();
    }
    
    /**
     * Finds a user by username.
     * 
     * @param username the username to search for
     * @return the user, or null if not found
     */
    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Gets all users (for admin purposes).
     * 
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return new java.util.ArrayList<>(users);
    }

	public static boolean authenticate(FlightBookingSystem fbs, String username, String password, Role passenger) {
		try {
	        AuthenticationService authService = new AuthenticationService();
	        authService.login(username, password, passenger);
	        return true; // login successful
	    } catch (FlightBookingSystemException e) {
	        System.out.println("Authentication failed: " + e.getMessage());
	        return false;		
	}
	}
}
