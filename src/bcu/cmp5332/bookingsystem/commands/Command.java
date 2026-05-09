package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;



/**
 * Interface defining the contract for implementation.
 * Provides method signatures that implementing classes must define.
 */
public interface Command {

/**
 * The HELP_MESSAGE field.
 */
	public static final String HELP_MESSAGE =
		      "╔══════════════════════════════════════════════════════════════════════════╗\n"
		    + "║        Flight Booking System - Available Commands                        ║\n"
		    + "╠══════════════════════════════════════════════════════════════════════════╣\n"
		    + "║ VIEWING                                                                  ║\n"
		    + "║  listflights                     - List all flights                      ║\n"
		    + "║  listcustomers                   - List all customers                    ║\n"
		    + "║  showflight <id>                 - Show flight details                   ║\n"
		    + "║  showcustomer <id>               - Show customer details                 ║\n"
		    + "║  showbooking <id>                - Show customer's bookings              ║\n"
		    + "║  showpricing <id>                - Show dynamic pricing breakdown        ║\n"
		    + "╠══════════════════════════════════════════════════════════════════════════╣\n"
		    + "║ ADDING                                                                   ║\n"
		    + "║  addflight                       - Add a new flight                      ║\n"
		    + "║  addcustomer                     - Add a new customer                    ║\n"
		    + "║  addbooking <cust> <flt>         - Create a booking                      ║\n"
		    + "╠══════════════════════════════════════════════════════════════════════════╣\n"
		    + "║ REMOVING                                                                 ║\n"
		    + "║  deleteflight <id>               - Delete a flight (soft delete)         ║\n"
		    + "║  deletecustomer <id>             - Delete a customer (soft delete)       ║\n"
		    + "║  cancelbooking <c> <f>           - Cancel a booking (shows fee)          ║\n"
		    + "║  updatebooking <c> <old> <new>   - Rebook to different flight            ║\n"
		    + "╠══════════════════════════════════════════════════════════════════════════╣\n"
		    + "║ SYSTEM                                                                   ║\n"
/**
 * Interface defining the contract for implementation.
 * Provides method signatures that implementing classes must define.
 */
		    + "║  loadgui                         - Launch GUI interface                  ║\n"
		    + "║  help                            - Show this help menu                   ║\n"
		    + "║  exit                            - Exit system (saves data)              ║\n"
		    + "╚══════════════════════════════════════════════════════════════════════════╝\n"
		    + "\nTip: Use 'loadgui' for advanced features like Dashboard and Search!";


    
/**
 * Executes this command on the flight booking system.
 * @param flightBookingSystem the flightBookingSystem parameter
 * @throws FlightBookingSystemException if an error occurs
 */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException;
    
}
