package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Main data loader and saver for the flight booking system.
 * Manages all data persistence using multiple DataManager implementations.
 * Implements atomic operations with backup and rollback capabilities.
 * 
 * @version 3.0 (Added atomic operations and error handling)
 */
/**
 * Data manager class for persisting and loading FlightBookingSystemData data.
 * Handles file I/O operations for data persistence.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class FlightBookingSystemData {
    
    private static final List<DataManager> dataManagers = new ArrayList<>();
/**
 * The DATA_DIR field.
 */
    private static final String DATA_DIR = "./resources/data/";
/**
 * The BACKUP_SUFFIX field.
 */
    private static final String BACKUP_SUFFIX = ".backup";
    
    // runs only once when the class gets loaded to memory
    static {
        // Order matters! Load flights and customers before bookings
        dataManagers.add(new FlightDataManager());
        dataManagers.add(new CustomerDataManager());
        dataManagers.add(new BookingDataManager());
    }
    
    /**
     * Loads all data from text files into a new FlightBookingSystem.
     * 
     * @return a FlightBookingSystem populated with data from files
     * @throws FlightBookingSystemException if there's an error parsing data
     * @throws IOException if there's an error reading files
     */
/**
 * Performs the load operation.
 * @param FlightBookingSystemException the FlightBookingSystemException parameter
 * @return the FlightBookingSystem result
 * @throws FlightBookingSystemException if an error occurs
 * @throws IOException if an error occurs
 */
    public static FlightBookingSystem load() throws FlightBookingSystemException, IOException {
        FlightBookingSystem fbs = new FlightBookingSystem();
        
        for (DataManager dm : dataManagers) {
            try {
                dm.loadData(fbs);
            } catch (IOException e) {
                throw new IOException("Error loading data: " + e.getMessage() + 
                    "\nPlease check that data files exist and are readable.", e);
            }
        }
        
        return fbs;
    }

    /**
     * Stores all data from the FlightBookingSystem to text files.
     * Implements atomic operation with backup and rollback on failure.
     * 
     * @param fbs the flight booking system to save
     * @throws IOException if there's an error writing to files
     */
/**
 * Performs the store operation.
 * @param fbs the fbs parameter
 * @throws IOException if an error occurs
 */
    public static void store(FlightBookingSystem fbs) throws IOException {
        List<File> backupFiles = new ArrayList<>();
        List<File> originalFiles = new ArrayList<>();
        boolean success = false;
        
        try {
            // Step 1: Create backups of all existing data files
            File flightsFile = new File(DATA_DIR + "flights.txt");
            File customersFile = new File(DATA_DIR + "customers.txt");
            File bookingsFile = new File(DATA_DIR + "bookings.txt");
            
            File flightsBackup = new File(DATA_DIR + "flights.txt" + BACKUP_SUFFIX);
            File customersBackup = new File(DATA_DIR + "customers.txt" + BACKUP_SUFFIX);
            File bookingsBackup = new File(DATA_DIR + "bookings.txt" + BACKUP_SUFFIX);
            
            // Create backups if original files exist
            if (flightsFile.exists()) {
                Files.copy(flightsFile.toPath(), flightsBackup.toPath(), 
                          StandardCopyOption.REPLACE_EXISTING);
                backupFiles.add(flightsBackup);
                originalFiles.add(flightsFile);
            }
            
            if (customersFile.exists()) {
                Files.copy(customersFile.toPath(), customersBackup.toPath(), 
                          StandardCopyOption.REPLACE_EXISTING);
                backupFiles.add(customersBackup);
                originalFiles.add(customersFile);
            }
            
            if (bookingsFile.exists()) {
                Files.copy(bookingsFile.toPath(), bookingsBackup.toPath(), 
                          StandardCopyOption.REPLACE_EXISTING);
                backupFiles.add(bookingsBackup);
                originalFiles.add(bookingsFile);
            }
            
            // Step 2: Attempt to save all data
            for (DataManager dm : dataManagers) {
                dm.storeData(fbs);
            }
            
            // If we got here, all saves were successful
            success = true;
            
        } catch (IOException e) {
            // Step 3a: If save failed, restore from backups
            System.err.println("Error saving data: " + e.getMessage());
            System.err.println("Attempting to restore from backup...");
            
            try {
                for (int i = 0; i < backupFiles.size(); i++) {
                    if (backupFiles.get(i).exists()) {
                        Files.copy(backupFiles.get(i).toPath(), 
                                 originalFiles.get(i).toPath(), 
                                 StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                System.err.println("Data restored from backup successfully.");
            } catch (IOException restoreError) {
                System.err.println("CRITICAL: Failed to restore from backup: " + 
                                 restoreError.getMessage());
            }
            
            throw new IOException("Failed to save data. Changes have been rolled back. " +
                                "Original error: " + e.getMessage(), e);
            
        } finally {
            // Step 3b: If successful, delete backup files
            if (success) {
                for (File backup : backupFiles) {
                    if (backup.exists()) {
                        backup.delete();
                    }
                }
            }
        }
    }
    
    /**
     * Checks if data files are writable.
     * Useful for testing error handling.
     * 
     * @return true if all data files are writable, false otherwise
     */
/**
 * Performs the areDataFilesWritable operation.
 * @return the boolean result
 */
    public static boolean areDataFilesWritable() {
        File flightsFile = new File(DATA_DIR + "flights.txt");
        File customersFile = new File(DATA_DIR + "customers.txt");
        File bookingsFile = new File(DATA_DIR + "bookings.txt");
        
        return (!flightsFile.exists() || flightsFile.canWrite()) &&
               (!customersFile.exists() || customersFile.canWrite()) &&
               (!bookingsFile.exists() || bookingsFile.canWrite());
    }
    
    /**
     * Validates data file integrity.
     * 
     * @return true if data files exist and are readable
     */
    public static boolean validateDataFiles() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            return false;
        }
        
        File flightsFile = new File(DATA_DIR + "flights.txt");
        File customersFile = new File(DATA_DIR + "customers.txt");
        File bookingsFile = new File(DATA_DIR + "bookings.txt");
        
        return flightsFile.exists() && flightsFile.canRead() &&
               customersFile.exists() && customersFile.canRead() &&
               bookingsFile.exists() && bookingsFile.canRead();
    }
}
