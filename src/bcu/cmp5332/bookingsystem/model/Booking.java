package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a booking in the flight booking system.
 * A booking links a customer to a flight on a specific date.
 */
public class Booking {
    
    /**
     * Enum representing the status of a booking
     */
    public enum BookingStatus {
        BOOKED("Booked"),
        REBOOKED("Rebooked"),
        CANCELLED("Cancelled");
        
        private final String displayName;
        
        BookingStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    private Customer customer;
    private Flight flight;
    private LocalDate bookingDate;
    private double bookingPrice;        // Price at time of booking
    private double cancellationFee;     // Fee if cancelled/rebooked
    private BookingStatus status;       // Status of the booking

    /**
     * Creates a new Booking with the specified details.
     * 
     * @param customer the customer making the booking
     * @param flight the flight being booked
     * @param bookingDate the date when the booking was made
     */
    public Booking(Customer customer, Flight flight, LocalDate bookingDate) {
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.bookingPrice = flight.getCurrentPrice(bookingDate);  // Store price at booking time
        this.cancellationFee = 0.0;  // No fee until cancelled/rebooked
        this.status = BookingStatus.BOOKED;  // Default status
    }
    
    /**
     * Creates a Booking with explicit price and fee values.
     * Used when loading from file.
     * 
     * @param customer the customer
     * @param flight the flight
     * @param bookingDate the booking date
     * @param bookingPrice the price at time of booking
     * @param cancellationFee the cancellation/rebook fee
     */
    public Booking(Customer customer, Flight flight, LocalDate bookingDate, 
                   double bookingPrice, double cancellationFee) {
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.bookingPrice = bookingPrice;
        this.cancellationFee = cancellationFee;
        this.status = cancellationFee > 0 ? BookingStatus.REBOOKED : BookingStatus.BOOKED;
    }
    
    /**
     * Creates a Booking with all fields including status.
     * Used when loading from file.
     * 
     * @param customer the customer
     * @param flight the flight
     * @param bookingDate the booking date
     * @param bookingPrice the price at time of booking
     * @param cancellationFee the cancellation/rebook fee
     * @param status the booking status
     */
    public Booking(Customer customer, Flight flight, LocalDate bookingDate, 
                   double bookingPrice, double cancellationFee, BookingStatus status) {
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.bookingPrice = bookingPrice;
        this.cancellationFee = cancellationFee;
        this.status = status;
    }
    
    /**
     * Gets the customer associated with this booking.
     * 
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }
    
    /**
     * Sets the customer for this booking.
     * 
     * @param customer the new customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    /**
     * Gets the flight associated with this booking.
     * 
     * @return the flight
     */
    public Flight getFlight() {
        return flight;
    }
    
    /**
     * Sets the flight for this booking.
     * 
     * @param flight the new flight
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
    }
    
    /**
     * Gets the date when this booking was made.
     * 
     * @return the booking date
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }
    
    /**
     * Sets the booking date.
     * 
     * @param bookingDate the new booking date
     */
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    /**
     * Gets the price paid for this booking at time of booking.
     * Stores historical price.
     * 
     * @return the booking price
     */
    public double getBookingPrice() {
        return bookingPrice;
    }
    
    /**
     * Sets the booking price.
     * 
     * @param bookingPrice the new booking price
     */
    public void setBookingPrice(double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }
    
    /**
     * Gets the cancellation fee for this booking.
     * Fee charged if booking is cancelled or rebooked.
     * 
     * @return the cancellation fee (0.0 if not cancelled)
     */
    public double getCancellationFee() {
        return cancellationFee;
    }
    
    /**
     * Sets the cancellation fee.
     * Called when booking is cancelled or rebooked.
     * 
     * @param cancellationFee the cancellation/rebook fee
     */
    public void setCancellationFee(double cancellationFee) {
        this.cancellationFee = cancellationFee;
    }
    
    /**
     * Gets the status of this booking.
     * 
     * @return the booking status
     */
    public BookingStatus getStatus() {
        return status;
    }
    
    /**
     * Sets the status of this booking.
     * 
     * @param status the new booking status
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
    /**
     * Gets a short summary of the booking details.
     * 
     * @return a formatted string with booking information
     */
    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return customer.getName() + " on Flight " + flight.getFlightNumber() + 
               " on " + bookingDate.format(dtf) + " - £" + String.format("%.2f", bookingPrice);
    }
    
    /**
     * Gets a detailed summary of the booking.
     * 
     * @return a formatted string with complete booking details
     */
    public String getDetailsLong() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder details = new StringBuilder();
        details.append("Booking Details:\n");
        details.append("Customer: ").append(customer.getName()).append(" (ID: ")
               .append(customer.getId()).append(")\n");
        details.append("Flight: ").append(flight.getFlightNumber()).append(" - ")
               .append(flight.getOrigin()).append(" to ").append(flight.getDestination()).append("\n");
        details.append("Departure Date: ").append(flight.getDepartureDate().format(dtf)).append("\n");
        details.append("Booking Date: ").append(bookingDate.format(dtf)).append("\n");
        details.append("Booking Price: £").append(String.format("%.2f", bookingPrice)).append("\n");
        
        if (cancellationFee > 0) {
            details.append("Cancellation Fee: £").append(String.format("%.2f", cancellationFee)).append("\n");
            details.append("Refund Amount: £").append(String.format("%.2f", bookingPrice - cancellationFee)).append("\n");
        }
        
        return details.toString();
    }
}
