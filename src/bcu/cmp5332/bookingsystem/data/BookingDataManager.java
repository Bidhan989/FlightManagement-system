package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Manages the persistence of booking data.
 * Handles loading bookings from and storing bookings to text files.
 * Creates the relationships between customers and flights.
 */
public class BookingDataManager implements DataManager {
    
/**
 * The RESOURCE field.
 */
    private final String RESOURCE = "./resources/data/bookings.txt";
    
    /**
     * Loads booking data from the text file and establishes relationships
     * between customers and flights.
     * Now loads booking price and cancellation fee.
     * Backward compatible with old format.
     * 
     * @param fbs the flight booking system to load data into
     * @throws IOException if there's an error reading the file
     * @throws FlightBookingSystemException if there's an error parsing booking data
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
                    int customerId = Integer.parseInt(properties[0]);
/**
 * Performs the parseInt operation.
 */
                    int flightId = Integer.parseInt(properties[1]);
                    LocalDate bookingDate = LocalDate.parse(properties[2]);
                    
                    // Get the customer and flight from the system
                    Customer customer = fbs.getCustomerByID(customerId);
                    Flight flight = fbs.getFlightByID(flightId);
                    
                    Booking booking;
                    
                    // Check if new format with price, fee, and status
                    if (properties.length >= 6 && !properties[3].isEmpty() && !properties[4].isEmpty() && !properties[5].isEmpty()) {
/**
 * Performs the parseDouble operation.
 */
                        double bookingPrice = Double.parseDouble(properties[3]);
/**
 * Performs the parseDouble operation.
 */
                        double cancellationFee = Double.parseDouble(properties[4]);
                        Booking.BookingStatus status = Booking.BookingStatus.valueOf(properties[5].toUpperCase());
                        booking = new Booking(customer, flight, bookingDate, bookingPrice, cancellationFee, status);
                    } else if (properties.length >= 5 && !properties[3].isEmpty() && !properties[4].isEmpty()) {
                        // Format with price and fee but no status (backward compatibility)
/**
 * Performs the parseDouble operation.
 */
                        double bookingPrice = Double.parseDouble(properties[3]);
/**
 * Performs the parseDouble operation.
 */
                        double cancellationFee = Double.parseDouble(properties[4]);
                        booking = new Booking(customer, flight, bookingDate, bookingPrice, cancellationFee);
                    } else {
                        // Old format - create with default price calculation
                        booking = new Booking(customer, flight, bookingDate);
                    }
                    
                    // Establish relationships
                    customer.addBooking(booking);
                    
                    // Only add customer to flight passengers if booking is active (not cancelled)
                    if (booking.getStatus() != Booking.BookingStatus.CANCELLED) {
                        flight.addPassenger(customer);
                    }
                    
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse booking on line " + 
                        line_idx + "\nError: " + ex);
                }
                line_idx++;
            }
        }
    }
    
    /**
     * Stores all booking data from the system to the text file.
     * Now saves booking price and cancellation fee.
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
            for (Customer customer : fbs.getAllCustomers()) {  // Use getAllCustomers to include deleted
                for (Booking booking : customer.getBookings()) {
                    out.print(customer.getId() + SEPARATOR);
                    out.print(booking.getFlight().getId() + SEPARATOR);
                    out.print(booking.getBookingDate() + SEPARATOR);
                    out.print(booking.getBookingPrice() + SEPARATOR);  
                    out.print(booking.getCancellationFee() + SEPARATOR);  
                    out.print(booking.getStatus() + SEPARATOR);
                    out.println();
                }
            }
        }
    }
}
