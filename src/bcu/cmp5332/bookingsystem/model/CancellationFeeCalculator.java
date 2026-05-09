package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Calculates cancellation and rebooking fees based on various factors.
 * Fee structure:
 * - More than 14 days before departure: £25 flat fee
 * - 7-14 days before departure: 30% of flight price
 * - 3-6 days before departure: 50% of flight price  
 * - Less than 3 days before departure: 80% of flight price
 * 
 * @version 5.1 (Cancellation fees)
 */
/**
 * CancellationFeeCalculator class for the Flight Booking System.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class CancellationFeeCalculator {
    
/**
 * The EARLY_CANCELLATION_FEE field.
 */
    private static final double EARLY_CANCELLATION_FEE = 25.00;
/**
 * The MODERATE_FEE_PERCENTAGE field.
 */
    private static final double MODERATE_FEE_PERCENTAGE = 0.30;
/**
 * The HIGH_FEE_PERCENTAGE field.
 */
    private static final double HIGH_FEE_PERCENTAGE = 0.50;
/**
 * The VERY_HIGH_FEE_PERCENTAGE field.
 */
    private static final double VERY_HIGH_FEE_PERCENTAGE = 0.80;
    
    /**
     * Calculates the cancellation fee based on when the cancellation occurs.
     * 
     * @param flight the flight being cancelled
     * @param cancellationDate the date of cancellation
     * @param flightPrice the price paid for the flight
     * @return the cancellation fee amount
     */
/**
 * Performs the calculateCancellationFee operation.
 * @param flight the flight parameter
 * @param cancellationDate the cancellationDate parameter
 * @param flightPrice the flightPrice parameter
 * @return the double result
 */
    public static double calculateCancellationFee(Flight flight, LocalDate cancellationDate, double flightPrice) {
/**
 * Performs the between operation.
 */
        long daysBeforeDeparture = ChronoUnit.DAYS.between(cancellationDate, flight.getDepartureDate());
        
        if (daysBeforeDeparture > 14) {
            // Early cancellation - flat fee
            return EARLY_CANCELLATION_FEE;
        } else if (daysBeforeDeparture >= 7) {
            // Moderate notice - 30% of price
            return Math.round(flightPrice * MODERATE_FEE_PERCENTAGE * 100.0) / 100.0;
        } else if (daysBeforeDeparture >= 3) {
            // Short notice - 50% of price
            return Math.round(flightPrice * HIGH_FEE_PERCENTAGE * 100.0) / 100.0;
        } else {
            // Very short notice - 80% of price
            return Math.round(flightPrice * VERY_HIGH_FEE_PERCENTAGE * 100.0) / 100.0;
        }
    }
    
    /**
     * Calculates the rebooking fee (typically same as cancellation).
     * 
     * @param flight the flight being rebooked
     * @param rebookDate the date of rebooking
     * @param flightPrice the price paid for the original flight
     * @return the rebooking fee amount
     */
/**
 * Performs the calculateRebookingFee operation.
 * @param flight the flight parameter
 * @param rebookDate the rebookDate parameter
 * @param flightPrice the flightPrice parameter
 * @return the double result
 */
    public static double calculateRebookingFee(Flight flight, LocalDate rebookDate, double flightPrice) {
        // For now, rebooking fee is same as cancellation fee
        return calculateCancellationFee(flight, rebookDate, flightPrice);
    }
    
    /**
     * Gets a detailed breakdown of the cancellation fee calculation.
     * 
     * @param flight the flight
     * @param cancellationDate the cancellation date
     * @param flightPrice the flight price
     * @return detailed fee breakdown string
     */
/**
 * Retrieves the feebreakdown value.
 * @param flight the flight parameter
 * @param cancellationDate the cancellationDate parameter
 * @param flightPrice the flightPrice parameter
 * @return the String result
 */
    public static String getFeeBreakdown(Flight flight, LocalDate cancellationDate, double flightPrice) {
/**
 * Performs the between operation.
 */
        long daysBeforeDeparture = ChronoUnit.DAYS.between(cancellationDate, flight.getDepartureDate());
/**
 * Performs the calculateCancellationFee operation.
 */
        double fee = calculateCancellationFee(flight, cancellationDate, flightPrice);
        
        StringBuilder breakdown = new StringBuilder();
        breakdown.append("Cancellation Fee Breakdown:\n");
        breakdown.append("═══════════════════════════════════\n");
        breakdown.append(String.format("Flight Price: £%.2f\n", flightPrice));
        breakdown.append(String.format("Days before departure: %d\n", daysBeforeDeparture));
        breakdown.append("\n");
        
        if (daysBeforeDeparture > 14) {
            breakdown.append("Policy: Early cancellation (>14 days)\n");
            breakdown.append("Fee: £25.00 flat fee\n");
        } else if (daysBeforeDeparture >= 7) {
            breakdown.append("Policy: Moderate notice (7-14 days)\n");
            breakdown.append("Fee: 30% of flight price\n");
        } else if (daysBeforeDeparture >= 3) {
            breakdown.append("Policy: Short notice (3-6 days)\n");
            breakdown.append("Fee: 50% of flight price\n");
        } else {
            breakdown.append("Policy: Very short notice (<3 days)\n");
            breakdown.append("Fee: 80% of flight price\n");
        }
        
        breakdown.append("═══════════════════════════════════\n");
        breakdown.append(String.format("Total Cancellation Fee: £%.2f\n", fee));
        breakdown.append(String.format("Refund Amount: £%.2f\n", flightPrice - fee));
        
        return breakdown.toString();
    }
}
