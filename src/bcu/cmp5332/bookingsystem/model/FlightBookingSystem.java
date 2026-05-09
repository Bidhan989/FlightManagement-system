package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents the main flight booking system.
 * Manages flights, customers, and the system date.
 * 
 * @version 6.0 (Uses real system date)
 */
public class FlightBookingSystem {
    
    // Uses actual system date (real-time)
    // For production deployment - reflects current calendar date
    private final LocalDate systemDate = LocalDate.now();
    
    private final Map<Integer, Customer> customers = new TreeMap<>();
    private final Map<Integer, Flight> flights = new TreeMap<>();
    private final Map<String, User> users = new TreeMap<>();

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public void addUser(User user) throws FlightBookingSystemException {
        if (users.containsKey(user.getUsername())) {
            throw new FlightBookingSystemException("Username already exists");
        }
        users.put(user.getUsername(), user);
    }


    /**
     * Gets the current system date.
     * Returns the actual current date from the system clock.
     * 
     * @return the system date (current date)
     */
    public LocalDate getSystemDate() {
        return systemDate;
    }

    /**
     * Gets all active (non-deleted) flights in the system.
     * 
     * @return an unmodifiable list of all non-deleted flights
     */
    public List<Flight> getFlights() {
        List<Flight> out = new ArrayList<>();
        for (Flight flight : flights.values()) {
            if (!flight.isDeleted()) {
                out.add(flight);
            }
        }
        return Collections.unmodifiableList(out);
    }
    
    /**
     * Gets ALL flights in the system including deleted ones.
     * Used for data persistence.
     * 
     * @return an unmodifiable list of all flights (including deleted)
     */
    public List<Flight> getAllFlights() {
        List<Flight> out = new ArrayList<>(flights.values());
        return Collections.unmodifiableList(out);
    }

    /**
     * Gets a flight by its ID.
     * 
     * @param id the flight ID
     * @return the flight with the specified ID
     * @throws FlightBookingSystemException if no flight with that ID exists
     */
    public Flight getFlightByID(int id) throws FlightBookingSystemException {
        if (!flights.containsKey(id)) {
            throw new FlightBookingSystemException("There is no flight with that ID.");
        }
        return flights.get(id);
    }

    /**
     * Gets all active (non-deleted) customers in the system.
     * 
     * @return an unmodifiable list of all non-deleted customers
     */
    public List<Customer> getCustomers() {
        List<Customer> out = new ArrayList<>();
        for (Customer customer : customers.values()) {
            if (!customer.isDeleted()) {
                out.add(customer);
            }
        }
        return Collections.unmodifiableList(out);
    }
    
    /**
     * Gets ALL customers in the system including deleted ones.
     * Used for data persistence.
     * 
     * @return an unmodifiable list of all customers (including deleted)
     */
    public List<Customer> getAllCustomers() {
        List<Customer> out = new ArrayList<>(customers.values());
        return Collections.unmodifiableList(out);
    }

    /**
     * Gets a customer by their ID.
     * 
     * @param id the customer ID
     * @return the customer with the specified ID
     * @throws FlightBookingSystemException if no customer with that ID exists
     */
    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
        if (!customers.containsKey(id)) {
            throw new FlightBookingSystemException("There is no customer with that ID.");
        }
        return customers.get(id);
    }

    /**
     * Adds a new flight to the system.
     * 
     * @param flight the flight to add
     * @throws FlightBookingSystemException if a duplicate flight exists
     */
    public void addFlight(Flight flight) throws FlightBookingSystemException {
        if (flights.containsKey(flight.getId())) {
            throw new IllegalArgumentException("Duplicate flight ID.");
        }
        for (Flight existing : flights.values()) {
            if (existing.getFlightNumber().equals(flight.getFlightNumber()) 
                && existing.getDepartureDate().isEqual(flight.getDepartureDate())) {
                throw new FlightBookingSystemException("There is a flight with same "
                        + "number and departure date in the system");
            }
        }
        flights.put(flight.getId(), flight);
    }

    /**
     * Adds a new customer to the system.
     * 
     * @param customer the customer to add
     * @throws FlightBookingSystemException if a duplicate customer ID exists
     */
    public void addCustomer(Customer customer) throws FlightBookingSystemException {
        if (customers.containsKey(customer.getId())) {
            throw new IllegalArgumentException("Duplicate customer ID.");
        }
        customers.put(customer.getId(), customer);
    }
}
