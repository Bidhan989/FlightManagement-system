package bcu.cmp5332.bookingsystem.test;

import bcu.cmp5332.bookingsystem.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;


@DisplayName("DynamicPricingStrategy Tests")
/**
 * JUnit test class for testing DynamicPricingStrategy functionality.
 * Contains unit tests to verify correct behavior of the class.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class DynamicPricingStrategyTest {
/**
 * The strategy field.
 */
    private DynamicPricingStrategy strategy;
/**
 * The flight field.
 */
    private Flight flight;
/**
 * The departureDate field.
 */
    private LocalDate departureDate;
/**
 * The basePrice field.
 */
    private double basePrice;
    
    @BeforeEach
/**
 * Sets the up value.
 */
    public void setUp() {
        strategy = new DynamicPricingStrategy();
        departureDate = LocalDate.of(2026, 3, 16); // Monday - avoids weekend premium
        basePrice = 450.00;
        flight = new Flight(1, "BA123", "London", "New York", departureDate, 200, basePrice);
    }
    
    @AfterEach
/**
 * Performs the tearDown operation.
 */
    public void tearDown() {
        strategy = null;
        flight = null;
    }
    
    @Test
    @DisplayName("Strategy name is 'Dynamic Pricing'")
/**
 * Performs the testGetStrategyName operation.
 */
    public void testGetStrategyName() {
        assertEquals("Dynamic Pricing", strategy.getStrategyName());
    }
    
    @Test
    @DisplayName("Early bird discount - 30 days advance")
/**
 * Performs the testEarlyBirdDiscount30Days operation.
 */
    public void testEarlyBirdDiscount30Days() {
        LocalDate bookingDate = departureDate.minusDays(30);
/**
 * Performs the calculatePrice operation.
 */
        double price = strategy.calculatePrice(flight, bookingDate);
        // 450.00 * 0.80 = 360.00
        assertEquals(360.00, price, 0.01);
    }
    
    @Test
    @DisplayName("Early bird discount - 45 days advance")
/**
 * Performs the testEarlyBirdDiscount45Days operation.
 */
    public void testEarlyBirdDiscount45Days() {
        LocalDate bookingDate = departureDate.minusDays(45);
/**
 * Performs the calculatePrice operation.
 */
        double price = strategy.calculatePrice(flight, bookingDate);
        // 450.00 * 0.80 = 360.00
        assertEquals(360.00, price, 0.01);
    }
    
    @Test
    @DisplayName("Standard pricing - 15 days advance")
/**
 * Performs the testStandardPricing15Days operation.
 */
    public void testStandardPricing15Days() {
        LocalDate bookingDate = departureDate.minusDays(15);
/**
 * Performs the calculatePrice operation.
 */
        double price = strategy.calculatePrice(flight, bookingDate);
        // 450.00 * 1.00 = 450.00
        assertEquals(450.00, price, 0.01);
    }
    
    @Test
    @DisplayName("Last minute premium - same day")
/**
 * Performs the testLastMinutePremium0Days operation.
 */
    public void testLastMinutePremium0Days() {
        LocalDate bookingDate = departureDate;
/**
 * Performs the calculatePrice operation.
 */
        double price = strategy.calculatePrice(flight, bookingDate);
        // 450.00 * 1.50 = 675.00
        assertEquals(675.00, price, 0.01);
    }
    
    @Test
    @DisplayName("Last minute premium - 2 days before")
/**
 * Performs the testLastMinutePremium2Days operation.
 */
    public void testLastMinutePremium2Days() {
        LocalDate bookingDate = departureDate.minusDays(2);
/**
 * Performs the calculatePrice operation.
 */
        double price = strategy.calculatePrice(flight, bookingDate);
        // 450.00 * 1.50 = 675.00
        assertEquals(675.00, price, 0.01);
    }
    
    @Test
    @DisplayName("High demand premium at 80% capacity")
/**
 * Performs the testHighDemandPremium80Percent operation.
 */
    public void testHighDemandPremium80Percent() {
        for (int i = 0; i < 160; i++) {
            flight.addPassenger(new Customer(i, "P" + i, "0770090", "p" + i + "@email.com"));
        }
        LocalDate bookingDate = departureDate.minusDays(15);
/**
 * Performs the calculatePrice operation.
 */
        double price = strategy.calculatePrice(flight, bookingDate);
        // Standard (1.00) * high demand (1.30) = 1.30 * 450.00 = 585.00
        assertEquals(585.00, price, 0.01);
    }
    
    @Test
    @DisplayName("No high demand below 80% capacity")
/**
 * Performs the testNoHighDemandBelow80Percent operation.
 */
    public void testNoHighDemandBelow80Percent() {
        for (int i = 0; i < 158; i++) {
            flight.addPassenger(new Customer(i, "P" + i, "0770090", "p" + i + "@email.com"));
        }
        LocalDate bookingDate = departureDate.minusDays(15);
/**
 * Performs the calculatePrice operation.
 */
        double price = strategy.calculatePrice(flight, bookingDate);
        // Standard pricing only (no high demand) = 450.00
        assertEquals(450.00, price, 0.01);
    }
    
    @Test
    @DisplayName("Weekend premium - Sunday")
/**
 * Performs the testWeekendPremiumSunday operation.
 */
    public void testWeekendPremiumSunday() {
        LocalDate sunday = LocalDate.of(2026, 3, 15);
        Flight sundayFlight = new Flight(2, "SU001", "A", "B", sunday, 100, basePrice);
        LocalDate bookingDate = sunday.minusDays(15);
/**
 * Performs the calculatePrice operation.
 */
        double price = strategy.calculatePrice(sundayFlight, bookingDate);
        assertEquals(basePrice * 1.10, price, 0.01);
    }
    
    @Test
    @DisplayName("No weekend premium - Monday")
/**
 * Performs the testNoWeekendPremiumMonday operation.
 */
    public void testNoWeekendPremiumMonday() {
        LocalDate monday = LocalDate.of(2026, 3, 16);
        Flight mondayFlight = new Flight(3, "MO001", "A", "B", monday, 100, basePrice);
        LocalDate bookingDate = monday.minusDays(15);
/**
 * Performs the calculatePrice operation.
 */
        double price = strategy.calculatePrice(mondayFlight, bookingDate);
        assertEquals(basePrice, price, 0.01);
    }
    
    @Test
    @DisplayName("Combined: early bird + weekend")
/**
 * Performs the testEarlyBirdAndWeekend operation.
 */
    public void testEarlyBirdAndWeekend() {
        LocalDate sunday = LocalDate.of(2026, 3, 15);
        Flight sundayFlight = new Flight(4, "EW001", "A", "B", sunday, 100, basePrice);
        LocalDate bookingDate = sunday.minusDays(45);
/**
 * Performs the calculatePrice operation.
 */
        double price = strategy.calculatePrice(sundayFlight, bookingDate);
        assertEquals(basePrice * 0.80 * 1.10, price, 0.01);
    }
    
    @Test
    @DisplayName("Get pricing breakdown")
/**
 * Performs the testGetPricingBreakdown operation.
 */
    public void testGetPricingBreakdown() {
        LocalDate bookingDate = departureDate.minusDays(45);
/**
 * Retrieves the pricingbreakdown value.
 */
        String breakdown = strategy.getPricingBreakdown(flight, bookingDate);
        assertNotNull(breakdown);
        assertTrue(breakdown.contains("Base Price"));
        assertTrue(breakdown.contains("Final Price"));
    }
}
