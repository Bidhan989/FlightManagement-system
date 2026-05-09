package bcu.cmp5332.bookingsystem.model;

/**
 * Represents the current user session in the system.
 * Tracks who is logged in and their permissions.
 * 
 * @version 8.0 (Authentication System)
 */
public class Session {
    
    private static Session instance;
    private User currentUser;
    
    /**
     * Private constructor for singleton pattern.
     */
    private Session() {
    }
    
    /**
     * Gets the singleton instance of the session.
     * 
     * @return the session instance
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
    
    /**
     * Logs in a user and starts their session.
     * 
     * @param user the user to log in
     */
    public void login(User user) {
        this.currentUser = user;
    }
    
    /**
     * Logs out the current user and ends the session.
     */
    public void logout() {
        this.currentUser = null;
    }
    
    /**
     * Gets the currently logged-in user.
     * 
     * @return the current user, or null if not logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if a user is currently logged in.
     * 
     * @return true if someone is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Checks if the current user is an admin.
     * 
     * @return true if admin, false otherwise
     */
    public boolean isAdmin() {
        return isLoggedIn() && currentUser.isAdmin();
    }
    
    /**
     * Checks if the current user is a passenger.
     * 
     * @return true if passenger, false otherwise
     */
    public boolean isPassenger() {
        return isLoggedIn() && currentUser.isPassenger();
    }
    
    /**
     * Gets the username of the current user.
     * 
     * @return the username, or null if not logged in
     */
    public String getUsername() {
        return isLoggedIn() ? currentUser.getUsername() : null;
    }
    
    /**
     * Gets the customer ID of the current passenger.
     * 
     * @return the customer ID, or null if not a passenger or not logged in
     */
    public Integer getCustomerId() {
        return isLoggedIn() ? currentUser.getCustomerId() : null;
    }
    
    /**
     * Gets the role of the current user.
     * 
     * @return the role, or null if not logged in
     */
    public Role getRole() {
        return isLoggedIn() ? currentUser.getRole() : null;
    }
}
