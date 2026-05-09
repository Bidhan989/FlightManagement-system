package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to display detailed pricing breakdown for a flight.
 * Shows how dynamic pricing is calculated.
 * 
 * @version 5.0 (Dynamic pricing display)
 */
/**
 * Command class that implements the ShowPricing operation.
 * Follows the Command pattern to encapsulate a request as an object.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class ShowPricing implements Command {
    
/**
 * The flightId field.
 */
    private final int flightId;
    
    /**
     * Creates a ShowPricing command.
     * 
     * @param flightId the ID of the flight to show pricing for
     */
    public ShowPricing(int flightId) {
        this.flightId = flightId;
    }

    /**
     * Executes the show pricing command.
     * Displays detailed pricing breakdown.
     * 
     * @param flightBookingSystem the system to query
     * @throws FlightBookingSystemException if the flight doesn't exist
     */
    @Override
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Flight flight = flightBookingSystem.getFlightByID(flightId);
        
        System.out.println("\n" + flight.getDetailsShort());
        System.out.println("\n" + flight.getPricingBreakdown(flightBookingSystem.getSystemDate()));
    }
}
