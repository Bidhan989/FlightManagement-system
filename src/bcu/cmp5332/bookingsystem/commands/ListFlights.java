package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Command to list all flights in the system.
 * Only shows flights that have not yet departed (future flights).
 * 
 * @version 6.0 (Filters past flights using systemDate)
 */
/**
 * Command class that implements the ListFlights operation.
 * Follows the Command pattern to encapsulate a request as an object.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class ListFlights implements Command {

    @Override
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        List<Flight> flights = flightBookingSystem.getFlights();
        LocalDate today = flightBookingSystem.getSystemDate();
        
        // Filter to show only future flights (not departed)
        List<Flight> futureFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (!flight.getDepartureDate().isBefore(today)) {
                futureFlights.add(flight);
            }
        }
        
        if (futureFlights.isEmpty()) {
            System.out.println("No upcoming flights found.");
            return;
        }
        
        System.out.println("UPCOMING FLIGHTS (not yet departed):");
        System.out.println("═══════════════════════════════════════════════════════");
        
        for (Flight flight : futureFlights) {
            System.out.println(flight.getDetailsShort());
            // Show current dynamic price based on systemDate
/**
 * Retrieves the currentprice value.
 */
            double currentPrice = flight.getCurrentPrice(today);
            System.out.println("  Current Price: £" + String.format("%.2f", currentPrice));
            System.out.println("───────────────────────────────────────────────────────");
        }
        
        System.out.println("\nTotal upcoming flights: " + futureFlights.size());
    }
}
