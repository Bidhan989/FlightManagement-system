package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manages the persistence of flight data.
 * Handles loading flights from and storing flights to text files.
 * 
 * @version 2.0 (Added capacity and price support)
 */
/**
 * Data manager class for persisting and loading Flight data.
 * Handles file I/O operations for data persistence.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class FlightDataManager implements DataManager {
    
/**
 * The RESOURCE field.
 */
    private final String RESOURCE = "./resources/data/flights.txt";
    
    /**
     * Loads flight data from the text file into the system.
     * Supports both old format (without capacity/price) and new format (with capacity/price).
     * 
     * @param fbs the flight booking system to load data into
     * @throws IOException if there's an error reading the file
     * @throws FlightBookingSystemException if there's an error parsing flight data
     */
    @Override
/**
 * Performs the loadData operation.
 * @param fbs the fbs parameter
 * @param IOException the IOException parameter
 * @throws IOException if an error occurs
 * @throws FlightBookingSystemException if an error occurs
 */
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
/**
 * Performs the nextLine operation.
 */
                String line = sc.nextLine();
                String[] properties = line.split(SEPARATOR, -1);
                try {
/**
 * Performs the parseInt operation.
 */
                    int id = Integer.parseInt(properties[0]);
                    String flightNumber = properties[1];
                    String origin = properties[2];
                    String destination = properties[3];
                    LocalDate departureDate = LocalDate.parse(properties[4]);
                    
                    Flight flight;
                    // Check if capacity and price are present (new format)
                    if (properties.length >= 7 && !properties[5].isEmpty() && !properties[6].isEmpty()) {
/**
 * Performs the parseInt operation.
 */
                        int capacity = Integer.parseInt(properties[5]);
/**
 * Performs the parseDouble operation.
 */
                        double price = Double.parseDouble(properties[6]);
                        flight = new Flight(id, flightNumber, origin, destination, departureDate, capacity, price);
                        
                        // Check if deleted flag is present
                        if (properties.length >= 8 && !properties[7].isEmpty()) {
/**
 * Performs the parseBoolean operation.
 */
                            boolean deleted = Boolean.parseBoolean(properties[7]);
                            flight.setDeleted(deleted);
                        }
                    } else {
                        // Old format - use defaults
                        flight = new Flight(id, flightNumber, origin, destination, departureDate, 100, 0.0);
                    }
                    
                    fbs.addFlight(flight);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse flight id " + properties[0] + 
                        " on line " + line_idx + "\nError: " + ex);
                }
                line_idx++;
            }
        }
    }
    
    /**
     * Stores flight data from the system to the text file.
     * Saves in new format with capacity, price, and deleted flag.
     * Stores ALL flights including deleted ones.
     * 
     * @param fbs the flight booking system to save data from
     * @throws IOException if there's an error writing to the file
     */
    @Override
/**
 * Performs the storeData operation.
 * @param fbs the fbs parameter
 * @throws IOException if an error occurs
 */
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            // Get ALL flights including deleted ones for complete persistence
            for (Flight flight : fbs.getAllFlights()) {
                out.print(flight.getId() + SEPARATOR);
                out.print(flight.getFlightNumber() + SEPARATOR);
                out.print(flight.getOrigin() + SEPARATOR);
                out.print(flight.getDestination() + SEPARATOR);
                out.print(flight.getDepartureDate() + SEPARATOR);
                out.print(flight.getCapacity() + SEPARATOR);
                out.print(flight.getPrice() + SEPARATOR);
                out.print(flight.isDeleted() + SEPARATOR);  // Save deleted flag
                out.println();
            }
        }
    }
}
