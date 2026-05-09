package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.util.List;

/**
 * Command to list all customers in the system.
 */
public class ListCustomers implements Command {
    
    /**
     * Executes the list customers command.
     * Displays all customers with their details.
     * 
     * @param flightBookingSystem the system to query
     * @throws FlightBookingSystemException if there's an error
     */
    @Override
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        List<Customer> customers = flightBookingSystem.getCustomers();
        
        if (customers.isEmpty()) {
            System.out.println("No customers in the system.");
            return;
        }
        
        System.out.println("Customers:");
        System.out.println("========================================");
        for (Customer customer : customers) {
            System.out.println(customer.getDetailsShort());
        }
        System.out.println("========================================");
        System.out.println(customers.size() + " customer(s) total");
    }
}
