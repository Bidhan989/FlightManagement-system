package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the flight booking system.
 * Each customer has a unique ID, name, phone number, email, and a list of bookings.
 * 
 * @version 2.0 (Added email)
 */
public class Customer {
    
    private int id;
    private String name;
    private String phone;
    private String email;
    private boolean deleted;  // Soft delete flag
    private final List<Booking> bookings = new ArrayList<>();
    
    /**
     * Creates a new Customer with all details including email.
     * 
     * @param id the unique identifier for the customer
     * @param name the customer's full name
     * @param phone the customer's phone number
     * @param email the customer's email address
     */
    public Customer(int id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.deleted = false;  // Default to not deleted
    }
    
    /**
     * Creates a new Customer without email (for backward compatibility).
     * 
     * @param id the unique identifier for the customer
     * @param name the customer's full name
     * @param phone the customer's phone number
     */
    public Customer(int id, String name, String phone) {
        this(id, name, phone, "");
    }
    
    /**
     * Gets the customer's ID.
     * 
     * @return the customer ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Sets the customer's ID.
     * 
     * @param id the new customer ID
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Gets the customer's name.
     * 
     * @return the customer name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the customer's name.
     * 
     * @param name the new customer name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the customer's phone number.
     * 
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }
    
    /**
     * Sets the customer's phone number.
     * 
     * @param phone the new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    /**
     * Gets the customer's email address.
     * 
     * @return the email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the customer's email address.
     * 
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Checks if this customer has been deleted (soft delete).
     * 
     * @return true if deleted, false otherwise
     */
    public boolean isDeleted() {
        return deleted;
    }
    
    /**
     * Sets the deleted status of this customer.
     * Soft delete - customer is hidden but data remains.
     * 
     * @param deleted true to mark as deleted, false to restore
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    /**
     * Gets a copy of the customer's bookings list.
     * 
     * @return an unmodifiable list of bookings
     */
    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }
    
    /**
     * Adds a booking to this customer's booking list.
     * 
     * @param booking the booking to add
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
    
    /**
     * Removes a booking from this customer's booking list.
     * 
     * @param booking the booking to remove
     */
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
    }
    
    /**
     * Gets a short summary of the customer's details.
     * 
     * @return a formatted string with basic customer info
     */
    public String getDetailsShort() {
        return "Customer #" + id + " - " + name + " - " + phone + 
               (email != null && !email.isEmpty() ? " - " + email : "");
    }
    
    /**
     * Gets a detailed summary of the customer's information.
     * 
     * @return a formatted string with complete customer details
     */
    public String getDetailsLong() {
        StringBuilder details = new StringBuilder();
        details.append("Customer #").append(id).append("\n");
        details.append("Name: ").append(name).append("\n");
        details.append("Phone: ").append(phone).append("\n");
        details.append("Email: ").append(email != null && !email.isEmpty() ? email : "Not provided").append("\n");
        details.append("Number of Bookings: ").append(bookings.size()).append("\n");
        
        if (!bookings.isEmpty()) {
            details.append("\nBookings:\n");
            int count = 1;
            for (Booking booking : bookings) {
                details.append("  ").append(count++).append(". ").append(booking.getDetailsShort()).append("\n");
            }
        }
        
        return details.toString();
    }
}
