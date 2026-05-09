package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to search and filter flights based on various criteria.
 * Supports filtering by destination, date range, and availability.
 * 
 * @version 5.0 (Advanced search)
 */
/**
 * Command class that implements the SearchFlights operation.
 * Follows the Command pattern to encapsulate a request as an object.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class SearchFlights implements Command {
    
/**
 * The destination field.
 */
    private final String destination;
/**
 * The startDate field.
 */
    private final LocalDate startDate;
/**
 * The endDate field.
 */
    private final LocalDate endDate;
/**
 * The onlyAvailable field.
 */
    private final boolean onlyAvailable;
    
    /**
     * Creates a SearchFlights command with all criteria.
     * 
     * @param destination the destination to search for (null for all)
     * @param startDate the earliest departure date (null for no limit)
     * @param endDate the latest departure date (null for no limit)
     * @param onlyAvailable if true, only show flights with available seats
     */
/**
 * Constructs a new instance of this class.
 * @param destination the destination to set
 * @param startDate the startDate to set
 * @param endDate the endDate to set
 * @param onlyAvailable the onlyAvailable to set
 */
    public SearchFlights(String destination, LocalDate startDate, LocalDate endDate, boolean onlyAvailable) {
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.onlyAvailable = onlyAvailable;
    }
    
    /**
     * Creates a simple search by destination only.
     * 
     * @param destination the destination to search for
     */
    public SearchFlights(String destination) {
        this(destination, null, null, false);
    }

    /**
     * Executes the search command.
     * Filters flights based on the specified criteria.
     * 
     * @param flightBookingSystem the system to search
     * @throws FlightBookingSystemException if there's an error
     */
    @Override
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        List<Flight> results = new ArrayList<>();
        
        for (Flight flight : flightBookingSystem.getFlights()) {
            // Filter by destination
            if (destination != null && !destination.isEmpty()) {
                if (!flight.getDestination().toLowerCase().contains(destination.toLowerCase())) {
                    continue;
                }
            }
            
            // Filter by date range
            if (startDate != null && flight.getDepartureDate().isBefore(startDate)) {
                continue;
            }
            if (endDate != null && flight.getDepartureDate().isAfter(endDate)) {
                continue;
            }
            
            // Filter by availability
            if (onlyAvailable && flight.getPassengers().size() >= flight.getCapacity()) {
                continue;
            }
            
            results.add(flight);
        }
        
        // Display results
        if (results.isEmpty()) {
            System.out.println("No flights found matching your criteria.");
        } else {
            System.out.println("\nSearch Results (" + results.size() + " flight(s) found):");
            System.out.println("═══════════════════════════════════════════════════════════");
            for (Flight flight : results) {
                System.out.println(flight.getDetailsShort());
                System.out.println("Current Price: £" + 
                    String.format("%.2f", flight.getCurrentPrice(flightBookingSystem.getSystemDate())));
                System.out.println("───────────────────────────────────────────────────────────");
            }
        }
    }
}
