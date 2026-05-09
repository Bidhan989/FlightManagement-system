package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.CancellationFeeCalculator;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

/**
 * Command to update (rebook) a customer's booking to a different flight.
 * Charges a rebooking fee and creates new booking.
 * 
 * @version 5.1 (Rebooking with fees)
 */
/**
 * Command class that implements the UpdateBooking operation.
 * Follows the Command pattern to encapsulate a request as an object.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class UpdateBooking implements Command {
    
/**
 * The customerId field.
 */
    private final int customerId;
/**
 * The oldFlightId field.
 */
    private final int oldFlightId;
/**
 * The newFlightId field.
 */
    private final int newFlightId;

    /**
     * Creates an UpdateBooking command.
     * 
     * @param customerId the ID of the customer
     * @param oldFlightId the ID of the current flight
     * @param newFlightId the ID of the new flight
     */
/**
 * Constructs a new instance of this class.
 * @param customerId the customerId to set
 * @param oldFlightId the oldFlightId to set
 * @param newFlightId the newFlightId to set
 */
    public UpdateBooking(int customerId, int oldFlightId, int newFlightId) {
        this.customerId = customerId;
        this.oldFlightId = oldFlightId;
        this.newFlightId = newFlightId;
    }

    /**
     * Executes the update booking command.
     * Cancels old booking (with fee), creates new booking.
     * 
     * @param flightBookingSystem the system to update
     * @throws FlightBookingSystemException if validation fails
     */
    @Override
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        Flight oldFlight = flightBookingSystem.getFlightByID(oldFlightId);
        Flight newFlight = flightBookingSystem.getFlightByID(newFlightId);
        
        // Validate new flight has capacity
        if (newFlight.getPassengers().size() >= newFlight.getCapacity()) {
            throw new FlightBookingSystemException(
                "Cannot rebook to flight " + newFlight.getFlightNumber() + ". " +
                "Flight is at full capacity (" + newFlight.getCapacity() + " passengers).");
        }
        
        // Check customer doesn't already have booking on new flight
        for (Booking existingBooking : customer.getBookings()) {
            if (existingBooking.getFlight().getId() == newFlightId) {
                throw new FlightBookingSystemException(
                    "Customer already has a booking for flight " + newFlight.getFlightNumber());
            }
        }
        
        // Find the old booking
        Booking oldBooking = null;
        for (Booking booking : customer.getBookings()) {
            if (booking.getFlight().getId() == oldFlightId) {
                oldBooking = booking;
                break;
            }
        }
        
        if (oldBooking == null) {
            throw new FlightBookingSystemException(
                "No booking found for customer " + customer.getName() + 
                " on flight " + oldFlight.getFlightNumber());
        }
        
        // Calculate rebooking fee (same as cancellation fee)
        LocalDate today = flightBookingSystem.getSystemDate();
/**
 * Retrieves the bookingprice value.
 */
        double oldBookingPrice = oldBooking.getBookingPrice();
        double rebookingFee = CancellationFeeCalculator.calculateRebookingFee(
            oldFlight, today, oldBookingPrice);
        
        // Calculate new flight price
/**
 * Retrieves the currentprice value.
 */
        double newFlightPrice = newFlight.getCurrentPrice(today);
        
        // Total cost = rebooking fee + new flight price - old booking price (credit)
        double totalCost = rebookingFee + newFlightPrice - oldBookingPrice;
        
        // Remove old booking
        customer.removeBooking(oldBooking);
        oldFlight.removePassenger(customer);
        
        // Create new booking
        Booking newBooking = new Booking(customer, newFlight, today);
        newBooking.setCancellationFee(rebookingFee);  // Record the fee paid
        newBooking.setStatus(Booking.BookingStatus.REBOOKED);  // Mark as rebooked
        customer.addBooking(newBooking);
        newFlight.addPassenger(customer);
        
        // Display results
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              BOOKING UPDATE SUCCESSFUL                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\nCustomer: " + customer.getName());
        System.out.println("\nOLD BOOKING:");
        System.out.println("  Flight: " + oldFlight.getFlightNumber() + " - " + 
                         oldFlight.getOrigin() + " → " + oldFlight.getDestination());
        System.out.println("  Departure: " + oldFlight.getDepartureDate());
        System.out.println("  Original Price: £" + String.format("%.2f", oldBookingPrice));
        
        System.out.println("\nNEW BOOKING:");
        System.out.println("  Flight: " + newFlight.getFlightNumber() + " - " + 
                         newFlight.getOrigin() + " → " + newFlight.getDestination());
        System.out.println("  Departure: " + newFlight.getDepartureDate());
        System.out.println("  New Price: £" + String.format("%.2f", newFlightPrice));
        
        System.out.println("\n" + "─".repeat(60));
        System.out.println("FINANCIAL SUMMARY:");
        System.out.println("  Rebooking Fee:        £" + String.format("%.2f", rebookingFee));
        System.out.println("  New Flight Price:     £" + String.format("%.2f", newFlightPrice));
        System.out.println("  Credit (old booking): -£" + String.format("%.2f", oldBookingPrice));
        System.out.println("  " + "─".repeat(58));
        
        if (totalCost >= 0) {
            System.out.println("  Amount to Pay:        £" + String.format("%.2f", totalCost));
        } else {
            System.out.println("  Refund Amount:        £" + String.format("%.2f", Math.abs(totalCost)));
        }
        
        System.out.println("\nSeats remaining on new flight: " + 
                         (newFlight.getCapacity() - newFlight.getPassengers().size()) + 
                         "/" + newFlight.getCapacity());
    }
}
