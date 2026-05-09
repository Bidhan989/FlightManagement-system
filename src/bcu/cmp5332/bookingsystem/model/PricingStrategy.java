package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

/**
 * Interface for different pricing strategies.
 * Allows flexible pricing algorithms for flights.
 * 
 * @version 5.0 (Dynamic pricing)
 */
/**
 * Interface defining the contract for implementation.
 * Provides method signatures that implementing classes must define.
 */
public interface PricingStrategy {
    
    /**
     * Calculates the price for a flight based on various factors.
     * 
     * @param flight the flight to price
     * @param bookingDate the date the booking is being made
     * @return the calculated price
     */
/**
 * Performs the calculatePrice operation.
 * @param flight the flight parameter
 * @param bookingDate the bookingDate parameter
 * @return the double result
 */
    double calculatePrice(Flight flight, LocalDate bookingDate);
    
    /**
     * Gets the name of this pricing strategy.
     * 
     * @return the strategy name
     */
    String getStrategyName();
}
