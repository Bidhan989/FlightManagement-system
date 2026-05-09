package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Manages the persistence of customer data.
 * Handles loading customers from and storing customers to text files.
 * 
 * @version 2.0 (Added email support)
 */
/**
 * Data manager class for persisting and loading Customer data.
 * Handles file I/O operations for data persistence.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class CustomerDataManager implements DataManager {
    
/**
 * The RESOURCE field.
 */
    private final String RESOURCE = "./resources/data/customers.txt";
    
    /**
     * Loads customer data from the text file into the system.
     * Supports both old format (without email) and new format (with email).
     * 
     * @param fbs the flight booking system to load data into
     * @throws IOException if there's an error reading the file
     * @throws FlightBookingSystemException if there's an error parsing customer data
     */
    @Override
/**
 * Performs the loadData operation.
 * @param fbs the fbs parameter
 * @param IOException the IOException parameter
 * @throws IOException if an error occurs
 * @throws FlightBookingSystemException if an error occurs
 */
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        try (Scanner sc = new Scanner(new File(RESOURCE))) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
/**
 * Performs the nextLine operation.
 */
                String line = sc.nextLine();
                String[] properties = line.split(SEPARATOR, -1);
                try {
/**
 * Performs the parseInt operation.
 */
                    int id = Integer.parseInt(properties[0]);
                    String name = properties[1];
                    String phone = properties[2];
                    
                    Customer customer;
                    // Check if email is present (new format)
                    if (properties.length >= 4 && !properties[3].isEmpty()) {
                        String email = properties[3];
                        customer = new Customer(id, name, phone, email);
                        
                        // Check if deleted flag is present 
                        if (properties.length >= 5 && !properties[4].isEmpty()) {
/**
 * Performs the parseBoolean operation.
 */
                            boolean deleted = Boolean.parseBoolean(properties[4]);
                            customer.setDeleted(deleted);
                        }
                    } else {
                        // Old format - no email
                        customer = new Customer(id, name, phone, "");
                    }
                    
                    fbs.addCustomer(customer);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse customer id " + 
                        properties[0] + " on line " + line_idx + "\nError: " + ex);
                }
                line_idx++;
            }
        }
    }
    
    /**
     * Stores customer data from the system to the text file.
     * Saves in new format with email and deleted flag.
     * Stores ALL customers including deleted ones.
     * 
     * @param fbs the flight booking system to save data from
     * @throws IOException if there's an error writing to the file
     */
    @Override
/**
 * Performs the storeData operation.
 * @param fbs the fbs parameter
 * @throws IOException if an error occurs
 */
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            // Get ALL customers including deleted ones for complete persistence
            for (Customer customer : fbs.getAllCustomers()) {
                out.print(customer.getId() + SEPARATOR);
                out.print(customer.getName() + SEPARATOR);
                out.print(customer.getPhone() + SEPARATOR);
                out.print(customer.getEmail() + SEPARATOR);
                out.print(customer.isDeleted() + SEPARATOR);  // Save deleted flag
                out.println();
            }
        }
    }
}
