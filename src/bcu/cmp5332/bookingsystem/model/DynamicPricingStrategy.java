package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Dynamic pricing strategy that adjusts prices based on:
 * 1. Days until departure (early bird discount / last minute premium)
 * 2. Current capacity utilization (demand-based pricing)
 * 3. Day of week (weekend premium)
 * 
 * @version 5.0 (Dynamic pricing)
 */
/**
 * Defines the strategy pattern for flight pricing calculations.
 * Allows different pricing algorithms to be implemented and swapped.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class DynamicPricingStrategy implements PricingStrategy {
    
/**
 * The EARLY_BIRD_DISCOUNT field.
 */
    private static final double EARLY_BIRD_DISCOUNT = 0.80;  // 20% off
/**
 * The STANDARD_MULTIPLIER field.
 */
    private static final double STANDARD_MULTIPLIER = 1.00;  // No change
/**
 * The LAST_MINUTE_PREMIUM field.
 */
    private static final double LAST_MINUTE_PREMIUM = 1.50;  // 50% premium
/**
 * The HIGH_DEMAND_MULTIPLIER field.
 */
    private static final double HIGH_DEMAND_MULTIPLIER = 1.30;  // 30% premium
/**
 * The WEEKEND_PREMIUM field.
 */
    private static final double WEEKEND_PREMIUM = 1.10;  // 10% premium
    
    /**
     * Calculates dynamic price based on multiple factors.
     * 
     * @param flight the flight to price
     * @param bookingDate the date the booking is being made
     * @return the dynamically calculated price
     */
    @Override
/**
 * Performs the calculatePrice operation.
 * @param flight the flight parameter
 * @param bookingDate the bookingDate parameter
 * @return the double result
 */
    public double calculatePrice(Flight flight, LocalDate bookingDate) {
/**
 * Retrieves the price value.
 */
        double basePrice = flight.getPrice();
        double multiplier = 1.0;
        
        // Factor 1: Days until departure
/**
 * Performs the between operation.
 */
        long daysUntilDeparture = ChronoUnit.DAYS.between(bookingDate, flight.getDepartureDate());
        
        if (daysUntilDeparture >= 30) {
            // Early bird discount (30+ days in advance)
            multiplier *= EARLY_BIRD_DISCOUNT;
        } else if (daysUntilDeparture <= 3) {
            // Last minute premium (3 days or less)
            multiplier *= LAST_MINUTE_PREMIUM;
        } else {
            // Standard pricing (4-29 days)
            multiplier *= STANDARD_MULTIPLIER;
        }
        
        // Factor 2: Capacity utilization (demand-based)
/**
 * Retrieves the passengers value.
 */
        double capacityUtilization = (double) flight.getPassengers().size() / flight.getCapacity();
        
        if (capacityUtilization >= 0.80) {
            // High demand (80%+ full)
            multiplier *= HIGH_DEMAND_MULTIPLIER;
        }
        
        // Factor 3: Weekend premium
/**
 * Retrieves the departuredate value.
 */
        int dayOfWeek = flight.getDepartureDate().getDayOfWeek().getValue();
        if (dayOfWeek == 6 || dayOfWeek == 7) {  // Saturday or Sunday
            multiplier *= WEEKEND_PREMIUM;
        }
        
        // Calculate final price and round to 2 decimal places
        double finalPrice = basePrice * multiplier;
        return Math.round(finalPrice * 100.0) / 100.0;
    }
    
    /**
     * Gets the name of this pricing strategy.
     * 
     * @return "Dynamic Pricing"
     */
    @Override
/**
 * Retrieves the strategyname value.
 * @return the String result
 */
    public String getStrategyName() {
        return "Dynamic Pricing";
    }
    
    /**
     * Gets a breakdown of pricing factors for display purposes.
     * 
     * @param flight the flight
     * @param bookingDate the booking date
     * @return detailed pricing breakdown
     */
/**
 * Retrieves the pricingbreakdown value.
 * @param flight the flight parameter
 * @param bookingDate the bookingDate parameter
 * @return the String result
 */
    public String getPricingBreakdown(Flight flight, LocalDate bookingDate) {
/**
 * Retrieves the price value.
 */
        double basePrice = flight.getPrice();
        StringBuilder breakdown = new StringBuilder();
        
        breakdown.append("Pricing Breakdown:\n");
        breakdown.append("─────────────────\n");
        breakdown.append(String.format("Base Price: £%.2f\n", basePrice));
        
        // Days until departure
/**
 * Performs the between operation.
 */
        long daysUntilDeparture = ChronoUnit.DAYS.between(bookingDate, flight.getDepartureDate());
        breakdown.append(String.format("Days until departure: %d\n", daysUntilDeparture));
        
        if (daysUntilDeparture >= 30) {
            breakdown.append("  → Early Bird Discount: -20%\n");
        } else if (daysUntilDeparture <= 3) {
            breakdown.append("  → Last Minute Premium: +50%\n");
        } else {
            breakdown.append("  → Standard Pricing: 0%\n");
        }
        
        // Capacity utilization
/**
 * Retrieves the passengers value.
 */
        double capacityUtilization = (double) flight.getPassengers().size() / flight.getCapacity();
        breakdown.append(String.format("Capacity: %d/%d (%.0f%% full)\n", 
            flight.getPassengers().size(), flight.getCapacity(), capacityUtilization * 100));
        
        if (capacityUtilization >= 0.80) {
            breakdown.append("  → High Demand Premium: +30%\n");
        }
        
        // Weekend check
/**
 * Retrieves the departuredate value.
 */
        int dayOfWeek = flight.getDepartureDate().getDayOfWeek().getValue();
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            breakdown.append("Weekend Departure: Yes\n");
            breakdown.append("  → Weekend Premium: +10%\n");
        }
        
/**
 * Performs the calculatePrice operation.
 */
        double finalPrice = calculatePrice(flight, bookingDate);
        breakdown.append("─────────────────\n");
        breakdown.append(String.format("Final Price: £%.2f\n", finalPrice));
        
        return breakdown.toString();
    }
}
