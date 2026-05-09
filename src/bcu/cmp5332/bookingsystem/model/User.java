package bcu.cmp5332.bookingsystem.model;

/**
 * Represents a user in the flight booking system.
 * Users can be either administrators or passengers with different permissions.
 * 
 * @version 8.0 (Authentication System)
 */
public class User {
    
    private String username;
    private String passwordHash;
    private Role role;
    private Integer customerId;  // Linked customer ID for passengers, null for admins
    
    /**
     * Creates a new User.
     * 
     * @param username the unique username
     * @param passwordHash the hashed password (not plain text)
     * @param role the user's role (ADMIN or PASSENGER)
     * @param customerId the linked customer ID (for passengers only, null for admins)
     */
    public User(String username, String passwordHash, Role role, Integer customerId) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.customerId = customerId;
    }
    
    /**
     * Gets the username.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username.
     * 
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Gets the password hash.
     * 
     * @return the hashed password
     */
    public String getPasswordHash() {
        return passwordHash;
    }
    
    /**
     * Sets the password hash.
     * 
     * @param passwordHash the new password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    /**
     * Gets the user's role.
     * 
     * @return the role (ADMIN or PASSENGER)
     */
    public Role getRole() {
        return role;
    }
    
    /**
     * Sets the user's role.
     * 
     * @param role the new role
     */
    public void setRole(Role role) {
        this.role = role;
    }
    
    /**
     * Gets the linked customer ID.
     * 
     * @return the customer ID for passengers, null for admins
     */
    public Integer getCustomerId() {
        return customerId;
    }
    
    /**
     * Sets the linked customer ID.
     * 
     * @param customerId the customer ID
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    
    /**
     * Checks if this user is an administrator.
     * 
     * @return true if admin, false otherwise
     */
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
    
    /**
     * Checks if this user is a passenger.
     * 
     * @return true if passenger, false otherwise
     */
    public boolean isPassenger() {
        return role == Role.PASSENGER;
    }
    
    @Override
    public String toString() {
        return "User{" +
               "username='" + username + '\'' +
               ", role=" + role +
               ", customerId=" + customerId +
               '}';
    }
}
