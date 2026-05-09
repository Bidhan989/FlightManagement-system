package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to delete (hide) a customer from the system using soft delete.
 * The customer is marked as deleted but data is retained.
 * 
 * @version 4.0 (Soft delete implementation)
 */
/**
 * Command class that implements the DeleteCustomer operation.
 * Follows the Command pattern to encapsulate a request as an object.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class DeleteCustomer implements Command {
    
/**
 * The customerId field.
 */
    private final int customerId;

    /**
     * Creates a DeleteCustomer command.
     * 
     * @param customerId the ID of the customer to delete
     */
    public DeleteCustomer(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Executes the delete customer command.
     * Marks the customer as deleted (soft delete).
     * 
     * @param flightBookingSystem the system to update
     * @throws FlightBookingSystemException if the customer doesn't exist
     */
    @Override
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        
        // Check if already deleted
        if (customer.isDeleted()) {
            throw new FlightBookingSystemException(
                "Customer #" + customerId + " is already deleted.");
        }
        
        // Soft delete - mark as deleted but retain data
        customer.setDeleted(true);
        
        System.out.println("Customer #" + customerId + " (" + customer.getName() + ") " +
                         "has been deleted.");
        System.out.println("Note: This is a soft delete. Data is retained but customer is hidden.");
    }
}
