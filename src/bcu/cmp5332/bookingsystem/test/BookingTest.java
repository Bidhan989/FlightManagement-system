package bcu.cmp5332.bookingsystem.test;

import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Booking.BookingStatus;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;


@DisplayName("Booking Class Tests")
/**
 * JUnit test class for testing Booking functionality.
 * Contains unit tests to verify correct behavior of the class.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class BookingTest {
/**
 * The customer field.
 */
    private Customer customer;
/**
 * The flight field.
 */
    private Flight flight;
/**
 * The departureDate field.
 */
    private LocalDate bookingDate, departureDate;
/**
 * The booking3 field.
 */
    private Booking booking1, booking2, booking3;
    
    @BeforeEach
/**
 * Sets the up value.
 */
    public void setUp() {
        departureDate = LocalDate.of(2026, 3, 15);
        bookingDate = LocalDate.of(2026, 2, 1);
        customer = new Customer(1, "John Doe", "07700900123", "john@email.com");
        flight = new Flight(1, "BA123", "London", "New York", departureDate, 200, 450.00);
        booking1 = new Booking(customer, flight, bookingDate);
        booking2 = new Booking(customer, flight, bookingDate, 450.00, 50.00);
        booking3 = new Booking(customer, flight, bookingDate, 450.00, 75.00, BookingStatus.CANCELLED);
    }
    
    @AfterEach
/**
 * Performs the tearDown operation.
 */
    public void tearDown() {
        customer = null;
        flight = null;
        booking1 = booking2 = booking3 = null;
    }
    
    @Test
    @DisplayName("Simple constructor initializes correctly")
/**
 * Performs the testSimpleConstructor operation.
 */
    public void testSimpleConstructor() {
        assertNotNull(booking1);
        assertEquals(customer, booking1.getCustomer());
        assertEquals(flight, booking1.getFlight());
        assertEquals(bookingDate, booking1.getBookingDate());
        assertEquals(0.0, booking1.getCancellationFee(), 0.01);
        assertEquals(BookingStatus.BOOKED, booking1.getStatus());
    }
    
    @Test
    @DisplayName("Constructor with price and fee")
/**
 * Performs the testConstructorWithPriceAndFee operation.
 */
    public void testConstructorWithPriceAndFee() {
        assertEquals(450.00, booking2.getBookingPrice(), 0.01);
        assertEquals(50.00, booking2.getCancellationFee(), 0.01);
        assertEquals(BookingStatus.REBOOKED, booking2.getStatus());
    }
    
    @Test
    @DisplayName("Full constructor with status")
/**
 * Performs the testFullConstructor operation.
 */
    public void testFullConstructor() {
        assertEquals(BookingStatus.CANCELLED, booking3.getStatus());
        assertEquals(75.00, booking3.getCancellationFee(), 0.01);
    }
    
    @Test
    @DisplayName("Get and set customer")
/**
 * Performs the testGetAndSetCustomer operation.
 */
    public void testGetAndSetCustomer() {
        Customer newCustomer = new Customer(2, "Jane", "0770090", "jane@email.com");
        booking1.setCustomer(newCustomer);
        assertEquals(newCustomer, booking1.getCustomer());
    }
    
    @Test
    @DisplayName("Get and set flight")
/**
 * Performs the testGetAndSetFlight operation.
 */
    public void testGetAndSetFlight() {
        Flight newFlight = new Flight(2, "LH456", "Frankfurt", "Tokyo", departureDate, 180, 550.00);
        booking1.setFlight(newFlight);
        assertEquals(newFlight, booking1.getFlight());
    }
    
    @Test
    @DisplayName("Get and set booking date")
/**
 * Performs the testGetAndSetBookingDate operation.
 */
    public void testGetAndSetBookingDate() {
        LocalDate newDate = LocalDate.of(2026, 2, 15);
        booking1.setBookingDate(newDate);
        assertEquals(newDate, booking1.getBookingDate());
    }
    
    @Test
    @DisplayName("Get and set booking price")
/**
 * Performs the testGetAndSetBookingPrice operation.
 */
    public void testGetAndSetBookingPrice() {
        booking1.setBookingPrice(599.99);
        assertEquals(599.99, booking1.getBookingPrice(), 0.01);
    }
    
    @Test
    @DisplayName("Get and set cancellation fee")
/**
 * Performs the testGetAndSetCancellationFee operation.
 */
    public void testGetAndSetCancellationFee() {
        booking1.setCancellationFee(100.00);
        assertEquals(100.00, booking1.getCancellationFee(), 0.01);
    }
    
    @Test
    @DisplayName("Get and set status")
/**
 * Performs the testGetAndSetStatus operation.
 */
    public void testGetAndSetStatus() {
        booking1.setStatus(BookingStatus.CANCELLED);
        assertEquals(BookingStatus.CANCELLED, booking1.getStatus());
    }
    
    @Test
    @DisplayName("BookingStatus display names")
/**
 * Performs the testBookingStatusDisplayNames operation.
 */
    public void testBookingStatusDisplayNames() {
        assertEquals("Booked", BookingStatus.BOOKED.getDisplayName());
        assertEquals("Rebooked", BookingStatus.REBOOKED.getDisplayName());
        assertEquals("Cancelled", BookingStatus.CANCELLED.getDisplayName());
    }
    
    @Test
    @DisplayName("Get details short format")
/**
 * Performs the testGetDetailsShort operation.
 */
    public void testGetDetailsShort() {
/**
 * Retrieves the detailsshort value.
 */
        String details = booking1.getDetailsShort();
        assertNotNull(details);
        assertTrue(details.contains("John Doe"));
        assertTrue(details.contains("BA123"));
    }
    
    @Test
    @DisplayName("Refund calculation")
/**
 * Performs the testRefundCalculation operation.
 */
    public void testRefundCalculation() {
        booking1.setBookingPrice(450.00);
        booking1.setCancellationFee(50.00);
/**
 * Retrieves the bookingprice value.
 */
        double refund = booking1.getBookingPrice() - booking1.getCancellationFee();
        assertEquals(400.00, refund, 0.01);
    }
}
