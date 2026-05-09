package bcu.cmp5332.bookingsystem.main;

/**
 * FlightBookingSystemException extends {@link Exception} class and is a custom exception
 * that is used to notify the user about errors or invalid commands.
 * 
 * Supports exception chaining to preserve stack traces.
 * 
 * @version 9.0 (Added exception chaining support)
 */
/**
 * FlightBookingSystemException class for the Flight Booking System.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class FlightBookingSystemException extends Exception {

    /**
     * Creates a new FlightBookingSystemException with a message.
     * 
     * @param message the error message
     */
    public FlightBookingSystemException(String message) {
        super(message);
    }
    
    /**
     * Creates a new FlightBookingSystemException with a message and cause.
     * This enables exception chaining to preserve the original stack trace.
     * 
     * @param message the error message
     * @param cause the underlying exception that caused this exception
     */
/**
 * Constructs a new instance of this class.
 * @param message the message to set
 * @param cause the cause to set
 */
    public FlightBookingSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
