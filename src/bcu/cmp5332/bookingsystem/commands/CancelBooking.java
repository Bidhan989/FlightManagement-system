package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.CancellationFeeCalculator;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

/**
 * Command to cancel a booking.
 * Removes the booking from the customer's list and removes the customer
 * from the flight's passenger list.
 * 
 * @version 4.0 (Cancel booking implementation)
 */
/**
 * Command class that implements the CancelBooking operation.
 * Follows the Command pattern to encapsulate a request as an object.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class CancelBooking implements Command {
    
/**
 * The customerId field.
 */
    private final int customerId;
/**
 * The flightId field.
 */
    private final int flightId;

    /**
     * Creates a CancelBooking command.
     * 
     * @param customerId the ID of the customer
     * @param flightId the ID of the flight
     */
/**
 * Constructs a new instance of this class.
 * @param customerId the customerId to set
 * @param flightId the flightId to set
 */
    public CancelBooking(int customerId, int flightId) {
        this.customerId = customerId;
        this.flightId = flightId;
    }

    /**
     * Executes the cancel booking command.
     * Removes the booking and updates both customer and flight.
     * Calculates and displays cancellation fee.
     * 
     * @param flightBookingSystem the system to update
     * @throws FlightBookingSystemException if customer/flight doesn't exist or booking not found
     */
    @Override
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight flight = flightBookingSystem.getFlightByID(flightId);
        
        // Find the booking
        Booking bookingToCancel = null;
        for (Booking booking : customer.getBookings()) {
            if (booking.getFlight().getId() == flightId) {
                bookingToCancel = booking;
                break;
            }
        }
        
        if (bookingToCancel == null) {
            throw new FlightBookingSystemException(
                "No booking found for customer " + customer.getName() + 
                " on flight " + flight.getFlightNumber());
        }
        
        // Calculate cancellation fee 
        LocalDate today = flightBookingSystem.getSystemDate();
/**
 * Retrieves the bookingprice value.
 */
        double bookingPrice = bookingToCancel.getBookingPrice();
        double cancellationFee = CancellationFeeCalculator.calculateCancellationFee(
            flight, today, bookingPrice);
        double refundAmount = bookingPrice - cancellationFee;
        
        // Set the cancellation fee in the booking (for record keeping)
        bookingToCancel.setCancellationFee(cancellationFee);
        bookingToCancel.setStatus(Booking.BookingStatus.CANCELLED);  // Mark as cancelled
        
        // Show fee breakdown
        System.out.println("\n" + CancellationFeeCalculator.getFeeBreakdown(flight, today, bookingPrice));
        
        // DON'T remove booking from customer - keep it with CANCELLED status
        // customer.removeBooking(bookingToCancel);  // REMOVED: We want to keep cancelled bookings
        
        // Remove customer from flight passengers
        flight.removePassenger(customer);
        
        System.out.println("Booking cancelled successfully!");
        System.out.println("Customer: " + customer.getName());
        System.out.println("Flight: " + flight.getFlightNumber() + " - " + 
                         flight.getOrigin() + " to " + flight.getDestination());
        System.out.println("\nFinancial Summary:");
        System.out.println("  Original Booking Price: £" + String.format("%.2f", bookingPrice));
        System.out.println("  Cancellation Fee:       £" + String.format("%.2f", cancellationFee));
        System.out.println("  Refund Amount:          £" + String.format("%.2f", refundAmount));
        System.out.println("\nSeats now available: " + (flight.getCapacity() - flight.getPassengers().size()) + 
                         "/" + flight.getCapacity());
    }
}
