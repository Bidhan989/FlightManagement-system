package bcu.cmp5332.bookingsystem.model;

/**
 * Represents the different user roles in the system.
 * Each role has different permissions and access levels.
 * 
 * @version 8.0 (Authentication System)
 */
/**
 * Enumeration defining constant values.
 * Provides type-safe constants for the application.
 */
public enum Role {
    /**
     * Administrator role - full system access.
     * Can manage flights, customers, bookings, and view reports.
     */
    ADMIN,
    
    /**
     * Passenger role - limited access.
     * Can only view flights, make bookings, and manage their own bookings.
     */
    PASSENGER
}
