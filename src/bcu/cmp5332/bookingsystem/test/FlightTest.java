package bcu.cmp5332.bookingsystem.test;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.DynamicPricingStrategy;
import bcu.cmp5332.bookingsystem.model.PricingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Comprehensive unit tests for the Flight class using JUnit 5.
 * Tests all constructors, getters, setters, and business logic.
 */
@DisplayName("Flight Class Tests")
public class FlightTest {
    
/**
 * The flight1 field.
 */
    private Flight flight1;
/**
 * The flight2 field.
 */
    private Flight flight2;
/**
 * The customer1 field.
 */
    private Customer customer1;
/**
 * The customer2 field.
 */
    private Customer customer2;
/**
 * The departureDate field.
 */
    private LocalDate departureDate;
/**
 * The bookingDate field.
 */
    private LocalDate bookingDate;
    
    @BeforeEach
/**
 * Sets the up value.
 */
    public void setUp() {
        // Initialize test data before each test
        departureDate = LocalDate.of(2026, 3, 16);  // Monday - avoids weekend premium
        bookingDate = LocalDate.of(2026, 2, 1);
        
        // Flight with full constructor (id, flightNumber, origin, destination, departureDate, capacity, price)
        flight1 = new Flight(1, "BA123", "London", "New York", departureDate, 200, 450.00);
        
        // Flight with simple constructor (backward compatibility)
        flight2 = new Flight(2, "LH456", "Frankfurt", "Tokyo", departureDate);
        
        // Create test customers
        customer1 = new Customer(1, "John Doe", "07700900123", "john@email.com");
        customer2 = new Customer(2, "Jane Smith", "07700900456", "jane@email.com");
    }
    
    @AfterEach
/**
 * Performs the tearDown operation.
 */
    public void tearDown() {
        flight1 = null;
        flight2 = null;
        customer1 = null;
        customer2 = null;
    }
    
    // ========== Constructor Tests ==========
    
    @Test
    @DisplayName("Full constructor should initialize all fields correctly")
/**
 * Performs the testFullConstructor operation.
 */
    public void testFullConstructor() {
        assertEquals(1, flight1.getId(), "Flight ID should be 1");
        assertEquals("BA123", flight1.getFlightNumber(), "Flight number should be BA123");
        assertEquals("London", flight1.getOrigin(), "Origin should be London");
        assertEquals("New York", flight1.getDestination(), "Destination should be New York");
        assertEquals(departureDate, flight1.getDepartureDate(), "Departure date should match");
        assertEquals(200, flight1.getCapacity(), "Capacity should be 200");
        assertEquals(450.00, flight1.getPrice(), 0.01, "Price should be 450.00");
        assertFalse(flight1.isDeleted(), "Flight should not be deleted by default");
        assertNotNull(flight1.getPricingStrategy(), "Pricing strategy should be initialized");
    }
    
    @Test
    @DisplayName("Simple constructor should use default capacity and price")
/**
 * Performs the testSimpleConstructor operation.
 */
    public void testSimpleConstructor() {
        assertEquals(2, flight2.getId(), "Flight ID should be 2");
        assertEquals("LH456", flight2.getFlightNumber(), "Flight number should be LH456");
        assertEquals(100, flight2.getCapacity(), "Default capacity should be 100");
        assertEquals(0.0, flight2.getPrice(), 0.01, "Default price should be 0.0");
        assertFalse(flight2.isDeleted(), "Flight should not be deleted by default");
    }
    
    @Test
    @DisplayName("Passengers list should be initialized as empty")
/**
 * Performs the testPassengersInitiallyEmpty operation.
 */
    public void testPassengersInitiallyEmpty() {
        List<Customer> passengers = flight1.getPassengers();
        assertNotNull(passengers, "Passengers list should not be null");
        assertEquals(0, passengers.size(), "Passengers list should be empty initially");
    }
    
    // ========== Getter and Setter Tests ==========
    
    @Test
    @DisplayName("Get and set ID")
/**
 * Performs the testGetAndSetId operation.
 */
    public void testGetAndSetId() {
        flight1.setId(100);
        assertEquals(100, flight1.getId(), "ID should be updated to 100");
    }
    
    @Test
    @DisplayName("Get and set flight number")
/**
 * Performs the testGetAndSetFlightNumber operation.
 */
    public void testGetAndSetFlightNumber() {
        flight1.setFlightNumber("EK789");
        assertEquals("EK789", flight1.getFlightNumber(), "Flight number should be updated");
    }
    
    @Test
    @DisplayName("Get and set origin")
/**
 * Performs the testGetAndSetOrigin operation.
 */
    public void testGetAndSetOrigin() {
        flight1.setOrigin("Paris");
        assertEquals("Paris", flight1.getOrigin(), "Origin should be updated to Paris");
    }
    
    @Test
    @DisplayName("Get and set destination")
/**
 * Performs the testGetAndSetDestination operation.
 */
    public void testGetAndSetDestination() {
        flight1.setDestination("Dubai");
        assertEquals("Dubai", flight1.getDestination(), "Destination should be updated to Dubai");
    }
    
    @Test
    @DisplayName("Get and set departure date")
/**
 * Performs the testGetAndSetDepartureDate operation.
 */
    public void testGetAndSetDepartureDate() {
        LocalDate newDate = LocalDate.of(2026, 4, 20);
        flight1.setDepartureDate(newDate);
        assertEquals(newDate, flight1.getDepartureDate(), "Departure date should be updated");
    }
    
    @Test
    @DisplayName("Get and set capacity")
/**
 * Performs the testGetAndSetCapacity operation.
 */
    public void testGetAndSetCapacity() {
        flight1.setCapacity(300);
        assertEquals(300, flight1.getCapacity(), "Capacity should be updated to 300");
    }
    
    @Test
    @DisplayName("Get and set price")
/**
 * Performs the testGetAndSetPrice operation.
 */
    public void testGetAndSetPrice() {
        flight1.setPrice(599.99);
        assertEquals(599.99, flight1.getPrice(), 0.01, "Price should be updated to 599.99");
    }
    
    @Test
    @DisplayName("Get and set deleted status")
/**
 * Performs the testGetAndSetDeleted operation.
 */
    public void testGetAndSetDeleted() {
        assertFalse(flight1.isDeleted(), "Flight should not be deleted initially");
        flight1.setDeleted(true);
        assertTrue(flight1.isDeleted(), "Flight should be marked as deleted");
        flight1.setDeleted(false);
        assertFalse(flight1.isDeleted(), "Flight should be restored");
    }
    
    // ========== Passenger Management Tests ==========
    
    @Test
    @DisplayName("Add single passenger")
/**
 * Performs the testAddPassenger operation.
 */
    public void testAddPassenger() {
        flight1.addPassenger(customer1);
        assertEquals(1, flight1.getPassengers().size(), "Should have 1 passenger");
        assertTrue(flight1.getPassengers().contains(customer1), "Passenger list should contain customer1");
    }
    
    @Test
    @DisplayName("Add multiple passengers")
/**
 * Performs the testAddMultiplePassengers operation.
 */
    public void testAddMultiplePassengers() {
        flight1.addPassenger(customer1);
        flight1.addPassenger(customer2);
        assertEquals(2, flight1.getPassengers().size(), "Should have 2 passengers");
        assertTrue(flight1.getPassengers().contains(customer1), "Should contain customer1");
        assertTrue(flight1.getPassengers().contains(customer2), "Should contain customer2");
    }
    
    @Test
    @DisplayName("Add duplicate passenger should not duplicate in Set")
/**
 * Performs the testAddDuplicatePassenger operation.
 */
    public void testAddDuplicatePassenger() {
        flight1.addPassenger(customer1);
        flight1.addPassenger(customer1); // Adding same passenger again
        // HashSet should prevent duplicates
        assertEquals(1, flight1.getPassengers().size(), 
                     "Should still have only 1 passenger (Set prevents duplicates)");
    }
    
    @Test
    @DisplayName("Remove passenger")
/**
 * Performs the testRemovePassenger operation.
 */
    public void testRemovePassenger() {
        flight1.addPassenger(customer1);
        flight1.addPassenger(customer2);
        assertEquals(2, flight1.getPassengers().size(), "Should have 2 passengers initially");
        
        flight1.removePassenger(customer1);
        assertEquals(1, flight1.getPassengers().size(), "Should have 1 passenger after removal");
        assertFalse(flight1.getPassengers().contains(customer1), "Should not contain customer1");
        assertTrue(flight1.getPassengers().contains(customer2), "Should still contain customer2");
    }
    
    @Test
    @DisplayName("Remove non-existent passenger should not affect list")
/**
 * Performs the testRemoveNonExistentPassenger operation.
 */
    public void testRemoveNonExistentPassenger() {
        flight1.addPassenger(customer1);
/**
 * Retrieves the passengers value.
 */
        int initialSize = flight1.getPassengers().size();
        
        flight1.removePassenger(customer2); // Remove passenger not in the list
        assertEquals(initialSize, flight1.getPassengers().size(), "Size should remain unchanged");
    }
    
    @Test
    @DisplayName("GetPassengers should return new list each time")
/**
 * Performs the testGetPassengersReturnsNewList operation.
 */
    public void testGetPassengersReturnsNewList() {
        flight1.addPassenger(customer1);
        List<Customer> passengers1 = flight1.getPassengers();
        List<Customer> passengers2 = flight1.getPassengers();
        
        assertNotSame(passengers1, passengers2, "Should return new list instance each time");
        assertEquals(passengers1, passengers2, "Both lists should have same content");
    }
    
    // ========== Pricing Strategy Tests ==========
    
    @Test
    @DisplayName("Get and set pricing strategy")
/**
 * Performs the testGetAndSetPricingStrategy operation.
 */
    public void testGetAndSetPricingStrategy() {
        PricingStrategy newStrategy = new DynamicPricingStrategy();
        flight1.setPricingStrategy(newStrategy);
        assertSame(newStrategy, flight1.getPricingStrategy(), "Pricing strategy should be updated");
    }
    
    @Test
    @DisplayName("Get current price with early bird discount")
/**
 * Performs the testGetCurrentPriceWithDynamicStrategy operation.
 */
    public void testGetCurrentPriceWithDynamicStrategy() {
        // Book 42 days in advance (early bird)
        LocalDate earlyBooking = departureDate.minusDays(42);
/**
 * Retrieves the currentprice value.
 */
        double price = flight1.getCurrentPrice(earlyBooking);
        
        // Early bird discount = 0.80 * basePrice = 360.00
        double expectedPrice = 360.00;
        assertEquals(expectedPrice, price, 0.01, "Early bird price should apply 20% discount");
    }
    
    @Test
    @DisplayName("Get current price with last minute premium")
/**
 * Performs the testGetCurrentPriceLastMinute operation.
 */
    public void testGetCurrentPriceLastMinute() {
        // Book 2 days in advance (last minute)
        LocalDate lastMinuteBooking = departureDate.minusDays(2);
/**
 * Retrieves the currentprice value.
 */
        double price = flight1.getCurrentPrice(lastMinuteBooking);
        
        // Last minute premium = 1.50 * basePrice = 675.00
        double expectedPrice = 675.00;
        assertEquals(expectedPrice, price, 0.01, "Last minute price should apply 50% premium");
    }
    
    @Test
    @DisplayName("Get current price with standard pricing")
/**
 * Performs the testGetCurrentPriceStandard operation.
 */
    public void testGetCurrentPriceStandard() {
        // Book 15 days in advance (standard pricing)
        LocalDate standardBooking = departureDate.minusDays(15);
/**
 * Retrieves the currentprice value.
 */
        double price = flight1.getCurrentPrice(standardBooking);
        
        // Standard = 1.00 * basePrice
        assertEquals(450.00, price, 0.01, "Standard price should be same as base price");
    }
    
    @Test
    @DisplayName("Get current price with high demand premium")
/**
 * Performs the testGetCurrentPriceWithHighDemand operation.
 */
    public void testGetCurrentPriceWithHighDemand() {
        // Fill flight to 80%+ capacity
        for (int i = 0; i < 161; i++) { // 161/200 = 80.5%
            flight1.addPassenger(new Customer(i, "Passenger" + i, "0770090" + i, "p" + i + "@email.com"));
        }
        
        LocalDate standardBooking = departureDate.minusDays(15);
/**
 * Retrieves the currentprice value.
 */
        double price = flight1.getCurrentPrice(standardBooking);
        
        // Standard (1.00) * high demand (1.30) = 1.30 * 450.00 = 585.00
        double expectedPrice = 585.00;
        assertEquals(expectedPrice, price, 1.0, "High demand should apply 30% premium");
    }
    
    @Test
    @DisplayName("Get current price with null strategy should return base price")
/**
 * Performs the testGetCurrentPriceNullStrategy operation.
 */
    public void testGetCurrentPriceNullStrategy() {
        flight1.setPricingStrategy(null);
/**
 * Retrieves the currentprice value.
 */
        double price = flight1.getCurrentPrice(bookingDate);
        assertEquals(450.00, price, 0.01, "Should return base price when strategy is null");
    }
    
    @Test
    @DisplayName("Get pricing breakdown should return formatted string")
/**
 * Performs the testGetPricingBreakdown operation.
 */
    public void testGetPricingBreakdown() {
/**
 * Retrieves the pricingbreakdown value.
 */
        String breakdown = flight1.getPricingBreakdown(bookingDate);
        assertNotNull(breakdown, "Pricing breakdown should not be null");
        assertTrue(breakdown.contains("Base Price"), "Breakdown should contain 'Base Price'");
        assertTrue(breakdown.contains("Final Price"), "Breakdown should contain 'Final Price'");
    }
    
    // ========== Details Display Tests ==========
    
    @Test
    @DisplayName("Get details short should format correctly")
/**
 * Performs the testGetDetailsShort operation.
 */
    public void testGetDetailsShort() {
/**
 * Retrieves the detailsshort value.
 */
        String details = flight1.getDetailsShort();
        assertNotNull(details, "Details should not be null");
        assertTrue(details.contains("#1"), "Should contain flight ID");
        assertTrue(details.contains("BA123"), "Should contain flight number");
        assertTrue(details.contains("London"), "Should contain origin");
        assertTrue(details.contains("New York"), "Should contain destination");
        assertTrue(details.contains("450.00"), "Should contain price");
        assertTrue(details.contains("0/200"), "Should contain capacity info");
    }
    
    @Test
    @DisplayName("Get details short with passengers should show count")
/**
 * Performs the testGetDetailsShortWithPassengers operation.
 */
    public void testGetDetailsShortWithPassengers() {
        flight1.addPassenger(customer1);
        flight1.addPassenger(customer2);
/**
 * Retrieves the detailsshort value.
 */
        String details = flight1.getDetailsShort();
        assertTrue(details.contains("2/200"), "Should show passenger count");
    }
    
    @Test
/**
 * Performs the DisplayName operation.
 */
    @DisplayName("Get details long should include all information")
/**
 * Performs the testGetDetailsLong operation.
 */
    public void testGetDetailsLong() {
/**
 * Retrieves the detailslong value.
 */
        String details = flight1.getDetailsLong();
        assertNotNull(details, "Details should not be null");
        assertTrue(details.contains("BA123"), "Should contain flight number");
        assertTrue(details.contains("London"), "Should contain origin");
        assertTrue(details.contains("New York"), "Should contain destination");
        assertTrue(details.contains("200 passengers"), "Should contain capacity");
        assertTrue(details.contains("450.00"), "Should contain price");
    }
    
    @Test
/**
 * Performs the DisplayName operation.
 */
    @DisplayName("Get details long with passengers should list them")
/**
 * Performs the testGetDetailsLongWithPassengers operation.
 */
    public void testGetDetailsLongWithPassengers() {
        flight1.addPassenger(customer1);
        flight1.addPassenger(customer2);
/**
 * Retrieves the detailslong value.
 */
        String details = flight1.getDetailsLong();
        assertTrue(details.contains("Passenger List"), "Should contain passenger list section");
        assertTrue(details.contains("John Doe"), "Should contain customer1 name");
        assertTrue(details.contains("Jane Smith"), "Should contain customer2 name");
    }
    
    @Test
/**
 * Performs the DisplayName operation.
 */
    @DisplayName("Get details long without passengers should not show list")
/**
 * Performs the testGetDetailsLongEmptyPassengers operation.
 */
    public void testGetDetailsLongEmptyPassengers() {
/**
 * Retrieves the detailslong value.
 */
        String details = flight1.getDetailsLong();
        assertFalse(details.contains("Passenger List"), 
                    "Should not contain passenger list when empty");
    }
    
    // ========== Edge Case Tests ==========
    
    @Test
    @DisplayName("Flight with zero capacity should be allowed")
/**
 * Performs the testZeroCapacity operation.
 */
    public void testZeroCapacity() {
        Flight zeroCapFlight = new Flight(10, "ZC001", "A", "B", departureDate, 0, 100.0);
        assertEquals(0, zeroCapFlight.getCapacity(), "Capacity should be 0");
    }
    
    @Test
    @DisplayName("Flight with negative price should be allowed")
/**
 * Performs the testNegativePrice operation.
 */
    public void testNegativePrice() {
        Flight negativePriceFlight = new Flight(11, "NP001", "A", "B", departureDate, 100, -50.0);
        assertEquals(-50.0, negativePriceFlight.getPrice(), 0.01, 
                     "Should allow negative price (for testing)");
    }
    
    @Test
    @DisplayName("Flight with very large capacity")
/**
 * Performs the testVeryLargeCapacity operation.
 */
    public void testVeryLargeCapacity() {
        Flight largeFlight = new Flight(12, "LG001", "A", "B", departureDate, 999999, 100.0);
        assertEquals(999999, largeFlight.getCapacity(), "Should handle large capacity");
    }
    
    @Test
    @DisplayName("Flight with past departure date")
/**
 * Performs the testPastDepartureDate operation.
 */
    public void testPastDepartureDate() {
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        Flight pastFlight = new Flight(13, "PS001", "A", "B", pastDate, 100, 100.0);
        assertEquals(pastDate, pastFlight.getDepartureDate(), "Should allow past departure date");
    }
    
    @Test
    @DisplayName("Flight with future departure date")
/**
 * Performs the testFutureDepartureDate operation.
 */
    public void testFutureDepartureDate() {
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        Flight futureFlight = new Flight(14, "FT001", "A", "B", futureDate, 100, 100.0);
        assertEquals(futureDate, futureFlight.getDepartureDate(), "Should allow future departure date");
    }
    
    @Test
    @DisplayName("Flight with empty strings")
/**
 * Performs the testEmptyStrings operation.
 */
    public void testEmptyStrings() {
        Flight emptyFlight = new Flight(15, "", "", "", departureDate, 100, 100.0);
        assertEquals("", emptyFlight.getFlightNumber(), "Should allow empty flight number");
        assertEquals("", emptyFlight.getOrigin(), "Should allow empty origin");
        assertEquals("", emptyFlight.getDestination(), "Should allow empty destination");
    }
    
    @Test
    @DisplayName("Flight with special characters in fields")
/**
 * Performs the testSpecialCharactersInFields operation.
 */
    public void testSpecialCharactersInFields() {
        Flight specialFlight = new Flight(16, "BA@123#", "Düsseldorf", "São Paulo", 
                                          departureDate, 100, 100.0);
        assertEquals("BA@123#", specialFlight.getFlightNumber(), 
                     "Should handle special chars in flight number");
        assertEquals("Düsseldorf", specialFlight.getOrigin(), 
                     "Should handle special chars in origin");
        assertEquals("São Paulo", specialFlight.getDestination(), 
                     "Should handle special chars in destination");
    }
    
    @Test
    @DisplayName("Full capacity booking scenario")
/**
 * Performs the testFullCapacityBooking operation.
 */
    public void testFullCapacityBooking() {
        Flight smallFlight = new Flight(17, "SM001", "A", "B", departureDate, 2, 100.0);
        smallFlight.addPassenger(customer1);
        smallFlight.addPassenger(customer2);
        
        assertEquals(2, smallFlight.getPassengers().size(), 
                     "Should allow booking to full capacity");
        
        // Try to add one more (Set allows it, but business logic should prevent overbooking)
        Customer customer3 = new Customer(3, "Bob Brown", "0770090789", "bob@email.com");
        smallFlight.addPassenger(customer3);
        assertEquals(3, smallFlight.getPassengers().size(), 
                     "Set allows overbooking (business logic should prevent this)");
    }
    
    // ========== Integration Tests ==========
    
    @Test
    @DisplayName("Complete flight lifecycle")
/**
 * Performs the testCompleteFlightLifecycle operation.
 */
    public void testCompleteFlightLifecycle() {
        // Create flight
        Flight flight = new Flight(20, "LC001", "London", "Paris", 
                                   LocalDate.of(2026, 6, 1), 50, 100.0);
        
        // Add passengers
        for (int i = 0; i < 10; i++) {
            Customer c = new Customer(i, "Customer" + i, "077009" + i, "c" + i + "@email.com");
            flight.addPassenger(c);
        }
        assertEquals(10, flight.getPassengers().size(), "Should have 10 passengers");
        
        // Check pricing
        LocalDate earlyBooking = LocalDate.of(2026, 4, 1); // 61 days early
/**
 * Retrieves the currentprice value.
 */
        double price = flight.getCurrentPrice(earlyBooking);
        assertTrue(price < flight.getPrice(), "Early booking should be cheaper");
        
        // Soft delete
        flight.setDeleted(true);
        assertTrue(flight.isDeleted(), "Flight should be deleted");
        assertEquals(10, flight.getPassengers().size(), 
                     "Passengers should remain even when deleted");
        
        // Restore
        flight.setDeleted(false);
        assertFalse(flight.isDeleted(), "Flight should be restored");
    }
    
    @Test
    @DisplayName("Date formatting in details")
/**
 * Performs the testDateFormatting operation.
 */
    public void testDateFormatting() {
        LocalDate testDate = LocalDate.of(2026, 1, 5);
        Flight dateFlight = new Flight(21, "DF001", "A", "B", testDate, 100, 100.0);
/**
 * Retrieves the detailsshort value.
 */
        String details = dateFlight.getDetailsShort();
        assertTrue(details.contains("05/01/2026"), "Date should be formatted as dd/MM/yyyy");
    }
}
