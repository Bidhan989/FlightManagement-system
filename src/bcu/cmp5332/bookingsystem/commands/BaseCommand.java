package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Abstract base class for all commands.
 * 
 * OOP Principles Demonstrated:
 * 1. Abstraction: Abstract class defines common behavior for all commands
 * 2. Inheritance: All command classes extend this base class
 * 3. Encapsulation: Protected fields and methods accessible only to subclasses
 * 4. Polymorphism: execute() method overridden by each command
 * 
 * @version 9.0 (OOP Demonstration)
 */
public abstract class BaseCommand implements Command {
    
    /**
     * Name of the command (for logging/debugging).
     * Encapsulation: Protected field accessible to subclasses only
     */
    protected String commandName;
    
    /**
     * Timestamp when command was created.
     * Encapsulation: Protected field
     */
    protected long createdAt;
    
    /**
     * Default constructor.
     * Inheritance: Called by all subclass constructors
     */
    protected BaseCommand() {
        this.createdAt = System.currentTimeMillis();
        this.commandName = this.getClass().getSimpleName();
    }
    
    /**
     * Executes the command.
     * Abstraction: Abstract method must be implemented by all subclasses
     * Polymorphism: Each subclass provides its own implementation
     * 
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if command execution fails
     */
    @Override
    public abstract void execute(FlightBookingSystem fbs) throws FlightBookingSystemException;
    
    /**
     * Validates common preconditions before executing command.
     * Encapsulation: Protected method for subclass use
     * Exception Handling: Throws exception if validation fails
     * 
     * @param fbs the flight booking system
     * @throws FlightBookingSystemException if validation fails
     */
    protected void validatePreconditions(FlightBookingSystem fbs) throws FlightBookingSystemException {
        if (fbs == null) {
            throw new FlightBookingSystemException("Flight Booking System cannot be null");
        }
    }
    
    /**
     * Logs command execution.
     * Encapsulation: Protected method for subclass use
     * 
     * @param message the message to log
     */
    protected void log(String message) {
        System.out.println("[" + commandName + "] " + message);
    }
    
    /**
     * Gets the command name.
     * Encapsulation: Public getter for private data
     * 
     * @return the command name
     */
    public String getCommandName() {
        return commandName;
    }
    
    /**
     * Gets when the command was created.
     * Encapsulation: Public getter for private data
     * 
     * @return timestamp in milliseconds
     */
    public long getCreatedAt() {
        return createdAt;
    }
}
