package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.util.ValidationUtil;

/**
 * Command to add a new customer to the system.
 * Implements validation for email uniqueness and phone format.
 * 
 * Demonstrates: Exception Handling, Validation, Encapsulation
 * 
 * @version 9.0 (Professional Validation System)
 */
/**
 * Command class that implements the AddCustomer operation.
 * Follows the Command pattern to encapsulate a request as an object.
 *
 * @author Group O3
 * @version 1.0
 */
public class AddCustomer implements Command {

/**
 * The name field.
 */
    private final String name;
/**
 * The phone field.
 */
    private final String phone;
/**
 * The email field.
 */
    private final String email;

    /**
     * Creates an AddCustomer command with email.
     * 
     * @param name the customer's name
     * @param phone the customer's phone number (must be 10 digits)
     * @param email the customer's email address (must be unique)
     */
/**
 * Constructs a new instance of this class.
 * @param name the name to set
 * @param phone the phone to set
 * @param email the email to set
 */
    public AddCustomer(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    
    /**
     * Creates an AddCustomer command without email (for backward compatibility).
     * 
     * @param name the customer's name
     * @param phone the customer's phone number
     */
/**
 * Performs the AddCustomer operation.
 * @param name the name parameter
 * @param phone the phone parameter
 * @return the public result
 */
    public AddCustomer(String name, String phone) {
        this(name, phone, "");
    }

    /**
     * Executes the add customer command.
     * Validates all inputs before creating customer.
     * 
     * Exception Handling: Catches and wraps all exceptions appropriately.
     * 
     * @param flightBookingSystem the system to update
     * @throws FlightBookingSystemException if validation fails or error adding customer
     */
    @Override
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        try {
            // Validate name
            ValidationUtil.validateName(name);
            
            // Validate phone number - must be exactly 10 digits
            ValidationUtil.validatePhone(phone);
            
            // Clean phone (remove spaces and separators)
/**
 * Performs the replaceAll operation.
 */
            String cleanPhone = phone.replaceAll("[\\s\\-()]", "");
            
            // Validate email format and uniqueness
            ValidationUtil.validateEmail(email, flightBookingSystem, null);
            
            // Generate a new unique ID
            int maxId = 0;
            if (!flightBookingSystem.getAllCustomers().isEmpty()) {
                for (Customer customer : flightBookingSystem.getAllCustomers()) {
                    if (customer.getId() > maxId) {
                        maxId = customer.getId();
                    }
                }
            }
            int newId = maxId + 1;
            
            // Create and add the new customer with cleaned phone
            Customer customer = new Customer(newId, name, cleanPhone, email);
            flightBookingSystem.addCustomer(customer);
            
            System.out.println("Customer added successfully:");
            System.out.println(customer.getDetailsShort());
            
        } catch (FlightBookingSystemException e) {
            // Re-throw FlightBookingSystemException as-is
            throw e;
        } catch (Exception e) {
            // Wrap any unexpected exceptions
            throw new FlightBookingSystemException(
                "Unexpected error adding customer: " + e.getMessage(), e);
        }
    }
}
