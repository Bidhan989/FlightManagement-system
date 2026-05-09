package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.IOException;


/**
 * Interface defining the contract for implementation.
 * Provides method signatures that implementing classes must define.
 */
public interface DataManager {
    
/**
 * The SEPARATOR field.
 */
    public static final String SEPARATOR = "::";
    
/**
 * Performs the loadData operation.
 * @param fbs the fbs parameter
 * @param IOException the IOException parameter
 * @throws IOException if an error occurs
 * @throws FlightBookingSystemException if an error occurs
 */
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException;
/**
 * Performs the storeData operation.
 * @param fbs the fbs parameter
 * @throws IOException if an error occurs
 */
    public void storeData(FlightBookingSystem fbs) throws IOException;
    
}
