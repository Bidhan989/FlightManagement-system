package bcu.cmp5332.bookingsystem.test;

import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Comprehensive unit tests for the Customer class using JUnit 5.
 * Tests all constructors, getters, setters, and business logic.
 */
@DisplayName("Customer Class Tests")
public class CustomerTest {
    
/**
 * The customer1 field.
 */
    private Customer customer1;
/**
 * The customer2 field.
 */
    private Customer customer2;
/**
 * The flight1 field.
 */
    private Flight flight1;
/**
 * The flight2 field.
 */
    private Flight flight2;
/**
 * The booking1 field.
 */
    private Booking booking1;
/**
 * The booking2 field.
 */
    private Booking booking2;
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
        bookingDate = LocalDate.of(2026, 2, 1);
        
        // Customer with full constructor (id, name, phone, email)
        customer1 = new Customer(1, "John Doe", "07700900123", "john.doe@email.com");
        
        // Customer with simple constructor (backward compatibility)
        customer2 = new Customer(2, "Jane Smith", "07700900456");
        
        // Create test flights
        flight1 = new Flight(1, "BA123", "London", "New York", 
                            LocalDate.of(2026, 3, 15), 200, 450.00);
        flight2 = new Flight(2, "LH456", "Frankfurt", "Tokyo", 
                            LocalDate.of(2026, 4, 20), 180, 550.00);
        
        // Create test bookings
        booking1 = new Booking(customer1, flight1, bookingDate);
        booking2 = new Booking(customer1, flight2, bookingDate);
    }
    
    @AfterEach
/**
 * Performs the tearDown operation.
 */
    public void tearDown() {
        customer1 = null;
        customer2 = null;
        flight1 = null;
        flight2 = null;
        booking1 = null;
        booking2 = null;
    }
    
    // ========== Constructor Tests ==========
    
    @Test
    @DisplayName("Full constructor should initialize all fields correctly")
/**
 * Performs the testFullConstructor operation.
 */
    public void testFullConstructor() {
        assertEquals(1, customer1.getId(), "Customer ID should be 1");
        assertEquals("John Doe", customer1.getName(), "Name should be John Doe");
        assertEquals("07700900123", customer1.getPhone(), "Phone should be 07700900123");
        assertEquals("john.doe@email.com", customer1.getEmail(), 
                     "Email should be john.doe@email.com");
        assertFalse(customer1.isDeleted(), "Customer should not be deleted by default");
    }
    
    @Test
    @DisplayName("Simple constructor should use empty email")
/**
 * Performs the testSimpleConstructor operation.
 */
    public void testSimpleConstructor() {
        assertEquals(2, customer2.getId(), "Customer ID should be 2");
        assertEquals("Jane Smith", customer2.getName(), "Name should be Jane Smith");
        assertEquals("07700900456", customer2.getPhone(), "Phone should be 07700900456");
        assertEquals("", customer2.getEmail(), "Email should be empty string");
        assertFalse(customer2.isDeleted(), "Customer should not be deleted by default");
    }
    
    @Test
    @DisplayName("Bookings list should be initialized as empty")
/**
 * Performs the testBookingsInitiallyEmpty operation.
 */
    public void testBookingsInitiallyEmpty() {
        List<Booking> bookings = customer1.getBookings();
        assertNotNull(bookings, "Bookings list should not be null");
        assertEquals(0, bookings.size(), "Bookings list should be empty initially");
    }
    
    // ========== Getter and Setter Tests ==========
    
    @Test
    @DisplayName("Get and set ID")
/**
 * Performs the testGetAndSetId operation.
 */
    public void testGetAndSetId() {
        customer1.setId(100);
        assertEquals(100, customer1.getId(), "ID should be updated to 100");
    }
    
    @Test
    @DisplayName("Get and set name")
/**
 * Performs the testGetAndSetName operation.
 */
    public void testGetAndSetName() {
        customer1.setName("Robert Johnson");
        assertEquals("Robert Johnson", customer1.getName(), "Name should be updated");
    }
    
    @Test
    @DisplayName("Get and set phone")
/**
 * Performs the testGetAndSetPhone operation.
 */
    public void testGetAndSetPhone() {
        customer1.setPhone("07700900999");
        assertEquals("07700900999", customer1.getPhone(), "Phone should be updated");
    }
    
    @Test
    @DisplayName("Get and set email")
/**
 * Performs the testGetAndSetEmail operation.
 */
    public void testGetAndSetEmail() {
        customer1.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", customer1.getEmail(), "Email should be updated");
    }
    
    @Test
    @DisplayName("Get and set deleted status")
/**
 * Performs the testGetAndSetDeleted operation.
 */
    public void testGetAndSetDeleted() {
        assertFalse(customer1.isDeleted(), "Customer should not be deleted initially");
        customer1.setDeleted(true);
        assertTrue(customer1.isDeleted(), "Customer should be marked as deleted");
        customer1.setDeleted(false);
        assertFalse(customer1.isDeleted(), "Customer should be restored");
    }
    
    // ========== Booking Management Tests ==========
    
    @Test
    @DisplayName("Add single booking")
/**
 * Performs the testAddBooking operation.
 */
    public void testAddBooking() {
        customer1.addBooking(booking1);
        assertEquals(1, customer1.getBookings().size(), "Should have 1 booking");
        assertTrue(customer1.getBookings().contains(booking1), 
                   "Bookings should contain booking1");
    }
    
    @Test
    @DisplayName("Add multiple bookings")
/**
 * Performs the testAddMultipleBookings operation.
 */
    public void testAddMultipleBookings() {
        customer1.addBooking(booking1);
        customer1.addBooking(booking2);
        assertEquals(2, customer1.getBookings().size(), "Should have 2 bookings");
        assertTrue(customer1.getBookings().contains(booking1), "Should contain booking1");
        assertTrue(customer1.getBookings().contains(booking2), "Should contain booking2");
    }
    
    @Test
    @DisplayName("Add duplicate booking should be allowed in List")
/**
 * Performs the testAddDuplicateBooking operation.
 */
    public void testAddDuplicateBooking() {
        customer1.addBooking(booking1);
        customer1.addBooking(booking1); // Adding same booking again
        // List allows duplicates
        assertEquals(2, customer1.getBookings().size(), 
                     "Should have 2 bookings (List allows duplicates)");
    }
    
    @Test
    @DisplayName("Remove booking")
/**
 * Performs the testRemoveBooking operation.
 */
    public void testRemoveBooking() {
        customer1.addBooking(booking1);
        customer1.addBooking(booking2);
        assertEquals(2, customer1.getBookings().size(), "Should have 2 bookings initially");
        
        customer1.removeBooking(booking1);
        assertEquals(1, customer1.getBookings().size(), "Should have 1 booking after removal");
        assertFalse(customer1.getBookings().contains(booking1), "Should not contain booking1");
        assertTrue(customer1.getBookings().contains(booking2), "Should still contain booking2");
    }
    
    @Test
    @DisplayName("Remove non-existent booking should not affect list")
/**
 * Performs the testRemoveNonExistentBooking operation.
 */
    public void testRemoveNonExistentBooking() {
        customer1.addBooking(booking1);
/**
 * Retrieves the bookings value.
 */
        int initialSize = customer1.getBookings().size();
        
        customer1.removeBooking(booking2); // Remove booking not in the list
        assertEquals(initialSize, customer1.getBookings().size(), "Size should remain unchanged");
    }
    
    @Test
    @DisplayName("GetBookings should return new list each time")
/**
 * Performs the testGetBookingsReturnsNewList operation.
 */
    public void testGetBookingsReturnsNewList() {
        customer1.addBooking(booking1);
        List<Booking> bookings1 = customer1.getBookings();
        List<Booking> bookings2 = customer1.getBookings();
        
        assertNotSame(bookings1, bookings2, "Should return new list instance each time");
        assertEquals(bookings1, bookings2, "Both lists should have same content");
    }
    
    @Test
    @DisplayName("Bookings are mutable but do not affect original")
/**
 * Performs the testBookingsAreImmutable operation.
 */
    public void testBookingsAreImmutable() {
        customer1.addBooking(booking1);
        List<Booking> bookings = customer1.getBookings();
        
        // Try to modify the returned list
        bookings.add(booking2);
        // The original customer's bookings should not be affected
        assertFalse(customer1.getBookings().contains(booking2), 
                   "Original customer bookings should not include booking2");
    }
    
    // ========== Details Display Tests ==========
    
    @Test
    @DisplayName("Get details short with email")
/**
 * Performs the testGetDetailsShortWithEmail operation.
 */
    public void testGetDetailsShortWithEmail() {
/**
 * Retrieves the detailsshort value.
 */
        String details = customer1.getDetailsShort();
        assertNotNull(details, "Details should not be null");
        assertTrue(details.contains("#1"), "Should contain customer ID");
        assertTrue(details.contains("John Doe"), "Should contain name");
        assertTrue(details.contains("07700900123"), "Should contain phone");
        assertTrue(details.contains("john.doe@email.com"), "Should contain email");
    }
    
    @Test
    @DisplayName("Get details short without email")
/**
 * Performs the testGetDetailsShortWithoutEmail operation.
 */
    public void testGetDetailsShortWithoutEmail() {
/**
 * Retrieves the detailsshort value.
 */
        String details = customer2.getDetailsShort();
        assertNotNull(details, "Details should not be null");
        assertTrue(details.contains("#2"), "Should contain customer ID");
        assertTrue(details.contains("Jane Smith"), "Should contain name");
        assertTrue(details.contains("07700900456"), "Should contain phone");
    }
    
    @Test
    @DisplayName("Get details short with empty email should not show dash")
/**
 * Performs the testGetDetailsShortEmptyEmail operation.
 */
    public void testGetDetailsShortEmptyEmail() {
        Customer customerEmptyEmail = new Customer(3, "Bob Brown", "07700900789", "");
/**
 * Retrieves the detailsshort value.
 */
        String details = customerEmptyEmail.getDetailsShort();
        assertFalse(details.endsWith(" - "), "Should not show empty email in details");
    }
    
    @Test
/**
 * Performs the DisplayName operation.
 */
    @DisplayName("Get details long with no bookings")
/**
 * Performs the testGetDetailsLongNoBookings operation.
 */
    public void testGetDetailsLongNoBookings() {
/**
 * Retrieves the detailslong value.
 */
        String details = customer1.getDetailsLong();
        assertNotNull(details, "Details should not be null");
        assertTrue(details.contains("#1"), "Should contain customer ID");
        assertTrue(details.contains("John Doe"), "Should contain name");
        assertTrue(details.contains("07700900123"), "Should contain phone");
        // Email check - flexible to handle different formats
        assertTrue(details.toLowerCase().contains("john.doe@email.com".toLowerCase()), 
                   "Should contain email");
        assertTrue(details.contains("Number of Bookings: 0"), "Should show 0 bookings");
        // Check for booking list section header (with newline) - more specific than just "Bookings:"
        assertFalse(details.contains("\nBookings:\n"), "Should not contain bookings section");
    }
    
    @Test
/**
 * Performs the DisplayName operation.
 */
    @DisplayName("Get details long with bookings")
/**
 * Performs the testGetDetailsLongWithBookings operation.
 */
    public void testGetDetailsLongWithBookings() {
        customer1.addBooking(booking1);
        customer1.addBooking(booking2);
/**
 * Retrieves the detailslong value.
 */
        String details = customer1.getDetailsLong();
        
        assertTrue(details.contains("Number of Bookings: 2"), "Should show 2 bookings");
        assertTrue(details.contains("\nBookings:\n"), "Should contain bookings section");
        assertTrue(details.contains("BA123"), "Should contain flight BA123");
        assertTrue(details.contains("LH456"), "Should contain flight LH456");
    }
    
    @Test
/**
 * Performs the DisplayName operation.
 */
    @DisplayName("Get details long with null email")
/**
 * Performs the testGetDetailsLongWithNullEmail operation.
 */
    public void testGetDetailsLongWithNullEmail() {
        Customer customerNullEmail = new Customer(4, "Alice Cooper", "07700900111", null);
/**
 * Retrieves the detailslong value.
 */
        String details = customerNullEmail.getDetailsLong();
        assertTrue(details.contains("Not provided"), 
                   "Should show 'Not provided' for null email");
    }
    
    @Test
/**
 * Performs the DisplayName operation.
 */
    @DisplayName("Get details long with empty email")
/**
 * Performs the testGetDetailsLongWithEmptyEmail operation.
 */
    public void testGetDetailsLongWithEmptyEmail() {
/**
 * Retrieves the detailslong value.
 */
        String details = customer2.getDetailsLong();
        assertTrue(details.contains("Not provided"), 
                   "Should show 'Not provided' for empty email");
    }
    
    // ========== Edge Case Tests ==========
    
    @Test
    @DisplayName("Customer with negative ID")
/**
 * Performs the testNegativeId operation.
 */
    public void testNegativeId() {
        Customer negativeIdCustomer = new Customer(-1, "Test User", "0770090", "test@email.com");
        assertEquals(-1, negativeIdCustomer.getId(), "Should allow negative ID");
    }
    
    @Test
    @DisplayName("Customer with zero ID")
/**
 * Performs the testZeroId operation.
 */
    public void testZeroId() {
        Customer zeroIdCustomer = new Customer(0, "Test User", "0770090", "test@email.com");
        assertEquals(0, zeroIdCustomer.getId(), "Should allow zero ID");
    }
    
    @Test
    @DisplayName("Customer with very large ID")
/**
 * Performs the testVeryLargeId operation.
 */
    public void testVeryLargeId() {
        Customer largeIdCustomer = new Customer(Integer.MAX_VALUE, "Test User", 
                                                "0770090", "test@email.com");
        assertEquals(Integer.MAX_VALUE, largeIdCustomer.getId(), 
                     "Should handle max integer ID");
    }
    
    @Test
    @DisplayName("Customer with empty name")
/**
 * Performs the testEmptyName operation.
 */
    public void testEmptyName() {
        Customer emptyNameCustomer = new Customer(5, "", "0770090", "test@email.com");
        assertEquals("", emptyNameCustomer.getName(), "Should allow empty name");
    }
    
    @Test
/**
 * Performs the DisplayName operation.
 */
    @DisplayName("Customer with very long name")
/**
 * Performs the testVeryLongName operation.
 */
    public void testVeryLongName() {
/**
 * Performs the repeat operation.
 */
        String longName = "A".repeat(1000);
        Customer longNameCustomer = new Customer(6, longName, "0770090", "test@email.com");
/**
 * Performs the assertEquals operation.
 */
        assertEquals(longName, longNameCustomer.getName(), "Should handle very long name");
    }
    
    @Test
    @DisplayName("Customer with special characters in name")
/**
 * Performs the testSpecialCharactersInName operation.
 */
    public void testSpecialCharactersInName() {
        Customer specialNameCustomer = new Customer(7, "John O'Brien-Smith (Jr.)", 
                                                    "0770090", "test@email.com");
        assertEquals("John O'Brien-Smith (Jr.)", specialNameCustomer.getName(), 
                     "Should handle special characters in name");
    }
    
    @Test
    @DisplayName("Customer with unicode characters in name")
/**
 * Performs the testUnicodeCharactersInName operation.
 */
    public void testUnicodeCharactersInName() {
        Customer unicodeNameCustomer = new Customer(8, "José María Müller", 
                                                    "0770090", "test@email.com");
        assertEquals("José María Müller", unicodeNameCustomer.getName(), 
                     "Should handle unicode characters");
    }
    
    @Test
    @DisplayName("Customer with empty phone")
/**
 * Performs the testEmptyPhone operation.
 */
    public void testEmptyPhone() {
        Customer emptyPhoneCustomer = new Customer(9, "Test User", "", "test@email.com");
        assertEquals("", emptyPhoneCustomer.getPhone(), "Should allow empty phone");
    }
    
    @Test
    @DisplayName("Customer with international phone format")
/**
 * Performs the testInternationalPhoneFormat operation.
 */
    public void testInternationalPhoneFormat() {
        Customer intlPhoneCustomer = new Customer(10, "Test User", 
                                                  "+44 (0)20 7946 0958", "test@email.com");
        assertEquals("+44 (0)20 7946 0958", intlPhoneCustomer.getPhone(), 
                     "Should handle international phone format");
    }
    
    @Test
    @DisplayName("Customer with invalid email format")
/**
 * Performs the testInvalidEmailFormat operation.
 */
    public void testInvalidEmailFormat() {
        Customer invalidEmailCustomer = new Customer(11, "Test User", "0770090", 
                                                     "notanemail");
        assertEquals("notanemail", invalidEmailCustomer.getEmail(), 
                     "Should allow invalid email format (validation is not enforced)");
    }
    
    @Test
    @DisplayName("Multiple bookings of same flight")
/**
 * Performs the testMultipleBookingsOfSameFlight operation.
 */
    public void testMultipleBookingsOfSameFlight() {
        Booking booking1a = new Booking(customer1, flight1, bookingDate);
        Booking booking1b = new Booking(customer1, flight1, bookingDate.plusDays(1));
        
        customer1.addBooking(booking1a);
        customer1.addBooking(booking1b);
        
        assertEquals(2, customer1.getBookings().size(), 
                     "Should allow multiple bookings of same flight");
    }
    
    @Test
    @DisplayName("Customer with many bookings")
/**
 * Performs the testManyBookings operation.
 */
    public void testManyBookings() {
        for (int i = 0; i < 100; i++) {
            Flight flight = new Flight(i, "FL" + i, "A", "B", 
                                      bookingDate.plusDays(i), 100, 100.0);
            Booking booking = new Booking(customer1, flight, bookingDate);
            customer1.addBooking(booking);
        }
        
        assertEquals(100, customer1.getBookings().size(), "Should handle many bookings");
    }
    
    // ========== Integration Tests ==========
    
    @Test
    @DisplayName("Complete customer lifecycle")
/**
 * Performs the testCompleteCustomerLifecycle operation.
 */
    public void testCompleteCustomerLifecycle() {
        // Create customer
        Customer customer = new Customer(20, "Test Customer", "07700900888", 
                                        "test@example.com");
        
        // Add bookings
        for (int i = 0; i < 5; i++) {
            Flight flight = new Flight(i, "FL" + i, "London", "Paris", 
                                      bookingDate.plusDays(i), 100, 100.0 + i * 10);
            Booking booking = new Booking(customer, flight, bookingDate);
            customer.addBooking(booking);
        }
        assertEquals(5, customer.getBookings().size(), "Should have 5 bookings");
        
        // Update details
        customer.setName("Updated Name");
        customer.setPhone("07700900777");
        customer.setEmail("updated@example.com");
        
        assertEquals("Updated Name", customer.getName(), "Name should be updated");
        assertEquals("07700900777", customer.getPhone(), "Phone should be updated");
        assertEquals("updated@example.com", customer.getEmail(), "Email should be updated");
        
        // Soft delete
        customer.setDeleted(true);
        assertTrue(customer.isDeleted(), "Customer should be deleted");
        assertEquals(5, customer.getBookings().size(), 
                     "Bookings should remain when deleted");
        
        // Restore
        customer.setDeleted(false);
        assertFalse(customer.isDeleted(), "Customer should be restored");
    }
    
    @Test
    @DisplayName("Customer with bookings details formatting")
/**
 * Performs the testCustomerWithBookingsDetails operation.
 */
    public void testCustomerWithBookingsDetails() {
        customer1.addBooking(booking1);
        customer1.addBooking(booking2);
        
/**
 * Retrieves the detailsshort value.
 */
        String shortDetails = customer1.getDetailsShort();
/**
 * Retrieves the detailslong value.
 */
        String longDetails = customer1.getDetailsLong();
        
        // Short details should be concise
        assertFalse(shortDetails.contains("Booking"), 
                    "Short details should not contain booking info");
        
        // Long details should contain booking info
        assertTrue(longDetails.contains("Bookings:"), 
                   "Long details should contain booking section");
        assertTrue(longDetails.contains("Number of Bookings: 2"), 
                   "Long details should show correct count");
    }
    
    @Test
    @DisplayName("Null handling in details should not throw exception")
/**
 * Performs the testNullHandlingInDetails operation.
 */
    public void testNullHandlingInDetails() {
        Customer customerWithNulls = new Customer(30, null, null, null);
        
        // Should not throw NullPointerException
/**
 * Retrieves the detailsshort value.
 */
        String shortDetails = customerWithNulls.getDetailsShort();
/**
 * Retrieves the detailslong value.
 */
        String longDetails = customerWithNulls.getDetailsLong();
        
        assertNotNull(shortDetails, "Short details should not be null");
        assertNotNull(longDetails, "Long details should not be null");
    }
    
    @Test
    @DisplayName("Booking order should be preserved")
/**
 * Performs the testBookingOrderPreserved operation.
 */
    public void testBookingOrderPreserved() {
        customer1.addBooking(booking1);
        customer1.addBooking(booking2);
        
        List<Booking> bookings = customer1.getBookings();
        assertEquals(booking1, bookings.get(0), "First booking should be booking1");
        assertEquals(booking2, bookings.get(1), "Second booking should be booking2");
    }
    
    @Test
    @DisplayName("Remove multiple occurrences of same booking")
/**
 * Performs the testRemovingMultipleOccurrencesOfSameBooking operation.
 */
    public void testRemovingMultipleOccurrencesOfSameBooking() {
        customer1.addBooking(booking1);
        customer1.addBooking(booking1);
        customer1.addBooking(booking1);
        
        assertEquals(3, customer1.getBookings().size(), "Should have 3 identical bookings");
        
        customer1.removeBooking(booking1);
        
        // ArrayList.remove() removes only first occurrence
        assertEquals(2, customer1.getBookings().size(), 
                     "Should have 2 bookings after one removal");
    }
}