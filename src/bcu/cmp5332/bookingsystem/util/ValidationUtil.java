package bcu.cmp5332.bookingsystem.util;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Utility class for input validation.
 * Provides validation for email, phone numbers, and other data.
 * 
 * Demonstrates: Encapsulation(private constructors, static methods)
 * 
 * @version 9.0 (Professional Validation System)
 */
public class ValidationUtil {
    
    // Private constructor to prevent instantiation (utility class)
    private ValidationUtil() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    /**
     * Validates email format and checks for uniqueness.
     * 
     * @param email the email to validate
     * @param fbs the flight booking system (to check uniqueness)
     * @param excludeCustomerId customer ID to exclude from uniqueness check (for updates)
     * @throws FlightBookingSystemException if email is invalid or already exists
     */
    public static void validateEmail(String email, FlightBookingSystem fbs, Integer excludeCustomerId) 
            throws FlightBookingSystemException {
        
        // Check if email is empty
        if (email == null || email.trim().isEmpty()) {
            throw new FlightBookingSystemException("Email is required.");
        }
        
        // Validate email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new FlightBookingSystemException(
                "Invalid email format. Please enter a valid email address.\n" +
                "Example: user@example.com");
        }
        
        // Check for email uniqueness (Important: Email must be unique!)
        for (Customer customer : fbs.getCustomers()) {
            // Skip the customer being updated
            if (excludeCustomerId != null && customer.getId() == excludeCustomerId) {
                continue;
            }
            
            if (customer.getEmail().equalsIgnoreCase(email)) {
                throw new FlightBookingSystemException(
                    "Email already exists: " + email + "\n" +
                    "This email is already registered to customer: " + customer.getName() + "\n" +
                    "Please use a different email address.");
            }
        }
    }
    
    /**
     * Validates phone number format (must be exactly 10 digits).
     * 
     * @param phone the phone number to validate
     * @throws FlightBookingSystemException if phone is invalid
     */
    public static void validatePhone(String phone) throws FlightBookingSystemException {
        // Check if phone is empty
        if (phone == null || phone.trim().isEmpty()) {
            throw new FlightBookingSystemException("Phone number is required.");
        }
        
        // Remove spaces and common separators
        String cleanPhone = phone.replaceAll("[\\s\\-()]", "");
        
        // Check if it's exactly 10 digits
        if (!cleanPhone.matches("^\\d{10}$")) {
            throw new FlightBookingSystemException(
                "Phone number must be exactly 10 digits.\n" +
                "Current input: " + phone + " (cleaned: " + cleanPhone + ")\n" +
                "Examples: 1234567890, 123-456-7890, (123) 456-7890");
        }
    }
    
    /**
     * Validates customer name.
     * 
     * @param name the name to validate
     * @throws FlightBookingSystemException if name is invalid
     */
    public static void validateName(String name) throws FlightBookingSystemException {
        if (name == null || name.trim().isEmpty()) {
            throw new FlightBookingSystemException("Name is required.");
        }
        
        if (name.trim().length() < 2) {
            throw new FlightBookingSystemException("Name must be at least 2 characters long.");
        }
        
        // Name should contain only letters and spaces
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new FlightBookingSystemException(
                "Name should contain only letters and spaces.\n" +
                "Invalid characters found in: " + name);
        }
    }
    
    /**
     * Validates flight number format.
     * 
     * @param flightNumber the flight number to validate
     * @throws FlightBookingSystemException if flight number is invalid
     */
    public static void validateFlightNumber(String flightNumber) throws FlightBookingSystemException {
        if (flightNumber == null || flightNumber.trim().isEmpty()) {
            throw new FlightBookingSystemException("Flight number is required.");
        }
        
        // Flight number should be 2 letters followed by 3-4 digits (e.g., BA123, EZ4567)
        if (!flightNumber.matches("^[A-Z]{2}\\d{3,4}$")) {
            throw new FlightBookingSystemException(
                "Invalid flight number format.\n" +
                "Format: 2 uppercase letters + 3-4 digits\n" +
                "Examples: BA123, EZ4567\n" +
                "Your input: " + flightNumber);
        }
    }
    
    /**
     * Validates capacity value.
     * 
     * @param capacity the capacity to validate
     * @throws FlightBookingSystemException if capacity is invalid
     */
    public static void validateCapacity(int capacity) throws FlightBookingSystemException {
        if (capacity <= 0) {
            throw new FlightBookingSystemException(
                "Capacity must be greater than 0.\n" +
                "Current value: " + capacity);
        }
        
        if (capacity > 1000) {
            throw new FlightBookingSystemException(
                "Capacity cannot exceed 1000 seats.\n" +
                "Current value: " + capacity);
        }
    }
    
    /**
     * Validates price value.
     * 
     * @param price the price to validate
     * @throws FlightBookingSystemException if price is invalid
     */
    public static void validatePrice(double price) throws FlightBookingSystemException {
        if (price <= 0) {
            throw new FlightBookingSystemException(
                "Price must be greater than 0.\n" +
                "Current value: £" + price);
        }
        
        if (price > 10000) {
            throw new FlightBookingSystemException(
                "Price seems unusually high (> £10,000).\n" +
                "Current value: £" + price + "\n" +
                "Please verify this amount.");
        }
    }
    
    /**
     * Validates username format.
     * 
     * @param username the username to validate
     * @throws FlightBookingSystemException if username is invalid
     */
    public static void validateUsername(String username) throws FlightBookingSystemException {
        if (username == null || username.trim().isEmpty()) {
            throw new FlightBookingSystemException("Username is required.");
        }
        
        if (username.length() < 3) {
            throw new FlightBookingSystemException(
                "Username must be at least 3 characters long.\n" +
                "Current length: " + username.length());
        }
        
        if (username.length() > 20) {
            throw new FlightBookingSystemException(
                "Username cannot exceed 20 characters.\n" +
                "Current length: " + username.length());
        }
        
        // Username should be alphanumeric
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new FlightBookingSystemException(
                "Username should contain only letters, numbers, and underscores.\n" +
                "Your input: " + username);
        }
    }
}
