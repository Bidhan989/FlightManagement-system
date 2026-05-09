package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

/**
 * Command to add a new flight to the system.
 * 
 * @version 2.0 (Added capacity and price support)
 */
public class AddFlight implements Command {

/**
 * The flightNumber field.
 */
    private final String flightNumber;
/**
 * The origin field.
 */
    private final String origin;
/**
 * The destination field.
 */
    private final String destination;
/**
 * The departureDate field.
 */
    private final LocalDate departureDate;
/**
 * The capacity field.
 */
    private final int capacity;
/**
 * The price field.
 */
    private final double price;

    /**
     * Creates an AddFlight command with all details.
     * 
     * @param flightNumber the flight number
     * @param origin the origin city/airport
     * @param destination the destination city/airport
     * @param departureDate the departure date
     * @param capacity the maximum number of passengers
     * @param price the ticket price
     */
/**
 * Constructs a new instance of this class.
 * @param flightNumber the flightNumber to set
 * @param origin the origin to set
 * @param destination the destination to set
 */
    public AddFlight(String flightNumber, String origin, String destination, 
                     LocalDate departureDate, int capacity, double price) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = capacity;
        this.price = price;
    }
    
    /**
     * Creates an AddFlight command with default capacity and price.
     * 
     * @param flightNumber the flight number
     * @param origin the origin city/airport
     * @param destination the destination city/airport
     * @param departureDate the departure date
     */
/**
 * Performs the AddFlight operation.
 * @param flightNumber the flightNumber parameter
 * @param origin the origin parameter
 * @param destination the destination parameter
 * @param departureDate the departureDate parameter
 * @return the public result
 */
    public AddFlight(String flightNumber, String origin, String destination, LocalDate departureDate) {
        this(flightNumber, origin, destination, departureDate, 100, 99.99);
    }
    
    /**
     * Executes the add flight command.
     * Creates a new flight with an auto-generated ID.
     * Validates that departure date is not in the past.
     * 
     * @param flightBookingSystem the system to update
     * @throws FlightBookingSystemException if there's an error adding the flight
     */
    @Override
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Validate departure date is not in the past
        LocalDate today = flightBookingSystem.getSystemDate();
        if (departureDate.isBefore(today)) {
            throw new FlightBookingSystemException(
                "Cannot add flight with departure date in the past.\n" +
                "Departure date: " + departureDate + "\n" +
                "System date: " + today + "\n" +
                "Please enter a departure date that is today or in the future.");
        }
        
        int maxId = 0;
        if (!flightBookingSystem.getAllFlights().isEmpty()) {
            for (Flight flight : flightBookingSystem.getAllFlights()) {
                if (flight.getId() > maxId) {
                    maxId = flight.getId();
                }
            }
        }
        
        Flight flight = new Flight(++maxId, flightNumber, origin, destination, 
                                   departureDate, capacity, price);
        flightBookingSystem.addFlight(flight);
        System.out.println("Flight #" + flight.getId() + " added successfully.");
        System.out.println(flight.getDetailsShort());
    }
}
