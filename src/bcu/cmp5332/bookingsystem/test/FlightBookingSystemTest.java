package bcu.cmp5332.bookingsystem.test;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;

@DisplayName("FlightBookingSystem Class Tests")
/**
 * JUnit test class for testing FlightBookingSystem functionality.
 * Contains unit tests to verify correct behavior of the class.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class FlightBookingSystemTest {
/**
 * The fbs field.
 */
    private FlightBookingSystem fbs;
/**
 * The flight2 field.
 */
    private Flight flight1, flight2;
/**
 * The customer2 field.
 */
    private Customer customer1, customer2;
/**
 * The testDate field.
 */
    private LocalDate testDate;
    
    @BeforeEach
/**
 * Sets the up value.
 */
    public void setUp() {
        fbs = new FlightBookingSystem();
        testDate = LocalDate.of(2026, 3, 15);
        flight1 = new Flight(1, "BA123", "London", "New York", testDate, 200, 450.00);
        flight2 = new Flight(2, "LH456", "Frankfurt", "Tokyo", testDate.plusDays(5), 180, 550.00);
        customer1 = new Customer(1, "John Doe", "0770090", "john@email.com");
        customer2 = new Customer(2, "Jane Smith", "0770090", "jane@email.com");
    }
    
    @AfterEach
/**
 * Performs the tearDown operation.
 */
    public void tearDown() {
        fbs = null;
    }
    
    @Test
    @DisplayName("Get system date")
/**
 * Performs the testGetSystemDate operation.
 */
    public void testGetSystemDate() {
        LocalDate systemDate = fbs.getSystemDate();
        assertNotNull(systemDate);
        assertEquals(LocalDate.now(), systemDate);
    }
    
    @Test
    @DisplayName("Add flight successfully")
/**
 * Performs the testAddFlight operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testAddFlight() throws FlightBookingSystemException {
        fbs.addFlight(flight1);
        assertEquals(1, fbs.getFlights().size());
        assertTrue(fbs.getFlights().contains(flight1));
    }
    
    @Test
    @DisplayName("Add multiple flights")
/**
 * Performs the testAddMultipleFlights operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testAddMultipleFlights() throws FlightBookingSystemException {
        fbs.addFlight(flight1);
        fbs.addFlight(flight2);
        assertEquals(2, fbs.getFlights().size());
    }
    
    @Test
    @DisplayName("Duplicate flight ID throws exception")
/**
 * Performs the testAddDuplicateFlightId operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testAddDuplicateFlightId() throws FlightBookingSystemException {
        fbs.addFlight(flight1);
        Flight duplicate = new Flight(1, "BA999", "Paris", "Berlin", testDate, 100, 200.00);
        assertThrows(IllegalArgumentException.class, () -> fbs.addFlight(duplicate));
    }
    
    @Test
    @DisplayName("Duplicate flight number and date throws exception")
/**
 * Performs the testAddDuplicateFlightNumberAndDate operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testAddDuplicateFlightNumberAndDate() throws FlightBookingSystemException {
        fbs.addFlight(flight1);
        Flight duplicate = new Flight(10, "BA123", "London", "New York", testDate, 200, 450.00);
        assertThrows(FlightBookingSystemException.class, () -> fbs.addFlight(duplicate));
    }
    
    @Test
    @DisplayName("Get flight by ID")
/**
 * Performs the testGetFlightByID operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testGetFlightByID() throws FlightBookingSystemException {
        fbs.addFlight(flight1);
        Flight retrieved = fbs.getFlightByID(1);
        assertSame(flight1, retrieved);
    }
    
    @Test
    @DisplayName("Get flight by ID not found throws exception")
/**
 * Performs the testGetFlightByIDNotFound operation.
 */
    public void testGetFlightByIDNotFound() {
        assertThrows(FlightBookingSystemException.class, () -> fbs.getFlightByID(999));
    }
    
    @Test
    @DisplayName("Get flights excludes deleted")
/**
 * Performs the testGetFlightsExcludesDeleted operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testGetFlightsExcludesDeleted() throws FlightBookingSystemException {
        fbs.addFlight(flight1);
        fbs.addFlight(flight2);
        flight1.setDeleted(true);
        List<Flight> flights = fbs.getFlights();
        assertEquals(1, flights.size());
        assertFalse(flights.contains(flight1));
    }
    
    @Test
    @DisplayName("Get all flights includes deleted")
/**
 * Performs the testGetAllFlightsIncludesDeleted operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testGetAllFlightsIncludesDeleted() throws FlightBookingSystemException {
        fbs.addFlight(flight1);
        fbs.addFlight(flight2);
        flight1.setDeleted(true);
        assertEquals(2, fbs.getAllFlights().size());
    }
    
    @Test
    @DisplayName("Add customer successfully")
/**
 * Performs the testAddCustomer operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testAddCustomer() throws FlightBookingSystemException {
        fbs.addCustomer(customer1);
        assertEquals(1, fbs.getCustomers().size());
    }
    
    @Test
    @DisplayName("Duplicate customer ID throws exception")
/**
 * Performs the testAddDuplicateCustomerId operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testAddDuplicateCustomerId() throws FlightBookingSystemException {
        fbs.addCustomer(customer1);
        Customer duplicate = new Customer(1, "Bob", "0770090", "bob@email.com");
        assertThrows(IllegalArgumentException.class, () -> fbs.addCustomer(duplicate));
    }
    
    @Test
    @DisplayName("Get customer by ID")
/**
 * Performs the testGetCustomerByID operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testGetCustomerByID() throws FlightBookingSystemException {
        fbs.addCustomer(customer1);
        Customer retrieved = fbs.getCustomerByID(1);
        assertSame(customer1, retrieved);
    }
    
    @Test
    @DisplayName("Get customer by ID not found throws exception")
/**
 * Performs the testGetCustomerByIDNotFound operation.
 */
    public void testGetCustomerByIDNotFound() {
        assertThrows(FlightBookingSystemException.class, () -> fbs.getCustomerByID(999));
    }
    
    @Test
    @DisplayName("Add user")
/**
 * Performs the testAddUser operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testAddUser() throws FlightBookingSystemException {
        User user = new User("admin", "pass123", Role.ADMIN, null);
        fbs.addUser(user);
        assertEquals(1, fbs.getUsers().size());
    }
    
    @Test
    @DisplayName("Duplicate username throws exception")
/**
 * Performs the testAddDuplicateUsername operation.
 * @throws FlightBookingSystemException if an error occurs
 */
    public void testAddDuplicateUsername() throws FlightBookingSystemException {
        User user1 = new User("admin", "pass123", Role.ADMIN, null);
        User user2 = new User("admin", "pass456", Role.PASSENGER, null);
        fbs.addUser(user1);
        assertThrows(FlightBookingSystemException.class, () -> fbs.addUser(user2));
    }
}
