package bcu.cmp5332.bookingsystem.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite for the Flight Booking System using JUnit 5.
 * Runs all unit tests for the system.
 *
 * <p>To run all tests in Eclipse:</p>
 * <pre>
 * Right-click this class → Run As → JUnit Test
 * </pre>
 */

@Suite
@SelectClasses({
    FlightTest.class,
    CustomerTest.class,
    BookingTest.class,
    FlightBookingSystemTest.class,
    DynamicPricingStrategyTest.class
})
/**
 * JUnit test class for testing Alls functionality.
 * Contains unit tests to verify correct behavior of the class.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class AllTests {
    // This class remains empty; it is used only as a holder for the above annotations
}
