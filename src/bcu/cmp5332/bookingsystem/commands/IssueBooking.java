package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

/**
 * Command to issue (create) a new booking for a customer on a flight.
 */
public class IssueBooking implements Command {
    
    private final int customerId;
/**
 * The flightId field.
 */
    private final int flightId;
    
    /**
     * Creates an IssueBooking command.
     * 
     * @param customerId the ID of the customer making the booking
     * @param flightId the ID of the flight to book
     */
/**
 * Constructs a new instance of this class.
 * @param customerId the customerId to set
 * @param flightId the flightId to set
 */
    public IssueBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }
    
    /**
     * Executes the issue booking command.
     * Creates a booking linking the customer to the flight.
     * Enforces business rules: capacity limits and duplicate prevention.
     * 
     * @param flightBookingSystem the system to update
     * @throws FlightBookingSystemException if the customer or flight doesn't exist,
     *         or if business rules are violated
     */
    @Override
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Get the customer and flight from the system
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight flight = flightBookingSystem.getFlightByID(flightId);
        
        // Check if flight is at capacity
        if (flight.getPassengers().size() >= flight.getCapacity()) {
            throw new FlightBookingSystemException(
                "Cannot book flight " + flight.getFlightNumber() + ". " +
                "Flight is at full capacity (" + flight.getCapacity() + " passengers).");
        }
        
        // Check if customer already has a booking for this flight
        for (Booking existingBooking : customer.getBookings()) {
            if (existingBooking.getFlight().getId() == flightId) {
                throw new FlightBookingSystemException(
                    "Customer " + customer.getName() + " already has a booking for this flight.");
            }
        }
        
        // Create the booking with the current system date
        LocalDate bookingDate = flightBookingSystem.getSystemDate();
        Booking booking = new Booking(customer, flight, bookingDate);
        
        // Establish the two-way relationship
        customer.addBooking(booking);
        flight.addPassenger(customer);
        
        // Calculate dynamic price 
/**
 * Retrieves the currentprice value.
 */
        double finalPrice = flight.getCurrentPrice(bookingDate);
        
        System.out.println("Booking successful!");
        System.out.println("Customer: " + customer.getName());
        System.out.println("Flight: " + flight.getFlightNumber() + " - " + 
                         flight.getOrigin() + " to " + flight.getDestination());
        System.out.println("Departure: " + flight.getDepartureDate());
        System.out.println("Price: £" + String.format("%.2f", finalPrice));
        System.out.println("Seats remaining: " + (flight.getCapacity() - flight.getPassengers().size()) + 
                         "/" + flight.getCapacity());
    }
}
