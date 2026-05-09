package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command to delete (hide) a flight from the system using soft delete.
 * The flight is marked as deleted but data is retained.
 * 
 * @version 4.0 (Soft delete implementation)
 */
/**
 * Command class that implements the DeleteFlight operation.
 * Follows the Command pattern to encapsulate a request as an object.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class DeleteFlight implements Command {
    
/**
 * The flightId field.
 */
    private final int flightId;

    /**
     * Creates a DeleteFlight command.
     * 
     * @param flightId the ID of the flight to delete
     */
    public DeleteFlight(int flightId) {
        this.flightId = flightId;
    }

    /**
     * Executes the delete flight command.
     * Marks the flight as deleted (soft delete).
     * 
     * @param flightBookingSystem the system to update
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
        
        // Check if already deleted
        if (flight.isDeleted()) {
            throw new FlightBookingSystemException(
                "Flight #" + flightId + " is already deleted.");
        }
        
        // Soft delete - mark as deleted but retain data
        flight.setDeleted(true);
        
        System.out.println("Flight #" + flightId + " (" + flight.getFlightNumber() + ") " +
                         "has been deleted.");
        System.out.println("Note: This is a soft delete. Data is retained but flight is hidden.");
    }
}
