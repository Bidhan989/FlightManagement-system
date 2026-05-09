package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a flight in the booking system.
 * Each flight has a unique ID, flight number, origin, destination, 
 * departure date, capacity, and price.
 * 
 * @version 2.0 (Added capacity and price)
 */
public class Flight {
    
    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private int capacity;
    private double price;
    private boolean deleted;
    private PricingStrategy pricingStrategy;  // Dynamic pricing

    private final Set<Customer> passengers;

    /**
     * Creates a new Flight with all details including capacity and price.
     * 
     * @param id the unique identifier for the flight
     * @param flightNumber the flight number (e.g., "BA123")
     * @param origin the departure airport/city
     * @param destination the arrival airport/city
     * @param departureDate the date of departure
     * @param capacity the maximum number of passengers
     * @param price the ticket price in pounds
     */
    public Flight(int id, String flightNumber, String origin, String destination, 
                  LocalDate departureDate, int capacity, double price) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.capacity = capacity;
        this.price = price;
        this.deleted = false;  // Default to not deleted
        this.pricingStrategy = new DynamicPricingStrategy();  // Default strategy
        
        passengers = new HashSet<>();
    }
    
    /**
     * Creates a new Flight without capacity and price (for backward compatibility).
     * Sets default capacity to 100 and price to 0.0.
     * 
     * @param id the unique identifier for the flight
     * @param flightNumber the flight number
     * @param origin the departure airport/city
     * @param destination the arrival airport/city
     * @param departureDate the date of departure
     */
    public Flight(int id, String flightNumber, String origin, String destination, LocalDate departureDate) {
        this(id, flightNumber, origin, destination, departureDate, 100, 0.0);
    }

    /**
     * Gets the flight's ID.
     * 
     * @return the flight ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the flight's ID.
     * 
     * @param id the new flight ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the flight number.
     * 
     * @return the flight number
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Sets the flight number.
     * 
     * @param flightNumber the new flight number
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    /**
     * Gets the origin airport/city.
     * 
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }
    
    /**
     * Sets the origin airport/city.
     * 
     * @param origin the new origin
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Gets the destination airport/city.
     * 
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the destination airport/city.
     * 
     * @param destination the new destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Gets the departure date.
     * 
     * @return the departure date
     */
    public LocalDate getDepartureDate() {
        return departureDate;
    }

    /**
     * Sets the departure date.
     * 
     * @param departureDate the new departure date
     */
    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
    
    /**
     * Gets the maximum capacity of the flight.
     * 
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Sets the maximum capacity of the flight.
     * 
     * @param capacity the new capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
    /**
     * Gets the base price of the flight.
     * 
     * @return the price in pounds
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Sets the base price of the flight.
     * 
     * @param price the new price in pounds
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * Checks if this flight has been deleted (soft delete).
     * 
     * @return true if deleted, false otherwise
     */
    public boolean isDeleted() {
        return deleted;
    }
    
    /**
     * Sets the deleted status of this flight.
     * Soft delete - flight is hidden but data remains.
     * 
     * @param deleted true to mark as deleted, false to restore
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    /**
     * Gets the current price based on the pricing strategy and booking date.
     * Dynamic pricing implementation.
     * 
     * @param bookingDate the date the booking is being made
     * @return the dynamically calculated price
     */
    public double getCurrentPrice(LocalDate bookingDate) {
        if (pricingStrategy != null) {
            return pricingStrategy.calculatePrice(this, bookingDate);
        }
        return price;  // Fallback to base price
    }
    
    /**
     * Gets a breakdown of the pricing calculation.
     * Shows how dynamic price is calculated.
     * 
     * @param bookingDate the booking date
     * @return detailed pricing breakdown
     */
    public String getPricingBreakdown(LocalDate bookingDate) {
        if (pricingStrategy instanceof DynamicPricingStrategy) {
            return ((DynamicPricingStrategy) pricingStrategy).getPricingBreakdown(this, bookingDate);
        }
        return "Base Price: £" + String.format("%.2f", price);
    }
    
    /**
     * Sets the pricing strategy for this flight.
     * Allows different pricing algorithms.
     * 
     * @param strategy the pricing strategy to use
     */
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }
    
    /**
     * Gets the current pricing strategy.
     * 
     * @return the pricing strategy
     */
    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    /**
     * Gets the list of passengers on this flight.
     * 
     * @return a new list containing all passengers
     */
    public List<Customer> getPassengers() {
        return new ArrayList<>(passengers);
    }
	
    /**
     * Gets a short summary of the flight details.
     * 
     * @return a formatted string with basic flight info
     */
    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to " 
                + destination + " on " + departureDate.format(dtf) + " - £" + 
                String.format("%.2f", price) + " - " + passengers.size() + "/" + capacity + " seats";
    }

    /**
     * Gets a detailed summary of the flight information.
     * 
     * @return a formatted string with complete flight details
     */
    public String getDetailsLong() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder details = new StringBuilder();
        details.append("Flight #").append(id).append("\n");
        details.append("Flight Number: ").append(flightNumber).append("\n");
        details.append("Origin: ").append(origin).append("\n");
        details.append("Destination: ").append(destination).append("\n");
        details.append("Departure Date: ").append(departureDate.format(dtf)).append("\n");
        details.append("Capacity: ").append(capacity).append(" passengers\n");
        details.append("Price: £").append(String.format("%.2f", price)).append("\n");
        details.append("Current Passengers: ").append(passengers.size()).append("/").append(capacity).append("\n");
        
        if (!passengers.isEmpty()) {
            details.append("\nPassenger List:\n");
            int count = 1;
            for (Customer passenger : passengers) {
                details.append("  ").append(count++).append(". ").append(passenger.getDetailsShort()).append("\n");
            }
        }
        
        return details.toString();
    }
    
    /**
     * Adds a passenger to this flight.
     * 
     * @param passenger the customer to add as a passenger
     */
    public void addPassenger(Customer passenger) {
        passengers.add(passenger);
    }
    
    /**
     * Removes a passenger from this flight.
     * 
     * @param passenger the customer to remove
     */
    public void removePassenger(Customer passenger) {
        passengers.remove(passenger);
    }
}
