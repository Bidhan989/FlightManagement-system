package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.util.List;

/**
 * Command to show all bookings for a specific customer.
 */
public class ShowBooking implements Command {
    
    private final int customerId;
    
    /**
     * Creates a ShowBooking command.
     * 
     * @param customerId the ID of the customer whose bookings to show
     */
    public ShowBooking(int customerId) {
        this.customerId = customerId;
    }
    
    /**
     * Executes the show booking command.
     * Displays all bookings for the specified customer.
     * 
     * @param flightBookingSystem the system to query
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
        List<Booking> bookings = customer.getBookings();
        
        System.out.println("Bookings for Customer: " + customer.getName());
        System.out.println("========================================");
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found for this customer.");
        } else {
            for (int i = 0; i < bookings.size(); i++) {
                Booking booking = bookings.get(i);
                System.out.println((i + 1) + ". " + booking.getDetailsShort());
            }
        }
        
        System.out.println("========================================");
        System.out.println(bookings.size() + " booking(s) total");
    }
}
