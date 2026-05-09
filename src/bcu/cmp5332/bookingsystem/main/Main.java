package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.gui.WelcomeScreen;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.io.*;

/**
 * Main entry point for the Flight Booking System.
 * Starts with CLI, type 'loadgui' to launch GUI with authentication.
 * 
 * @version 8.1 (CLI First, GUI on demand)
 */
/**
 * Main class for the Flight Booking System.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class Main {

/**
 * Performs the main operation.
 * @param IOException the IOException parameter
 * @throws IOException if an error occurs
 * @throws FlightBookingSystemException if an error occurs
 */
    public static void main(String[] args) throws IOException, FlightBookingSystemException {
        
        FlightBookingSystem fbs = FlightBookingSystemData.load();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Flight Booking System - CLI Mode");
        System.out.println("Enter 'help' to see available commands.");
        System.out.println("Enter 'loadgui' to launch GUI with authentication.");
        System.out.println();
        
        while (true) {
            System.out.print("> ");
/**
 * Performs the readLine operation.
 */
            String line = br.readLine();
            
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            
            if (line.equalsIgnoreCase("exit")) {
                break;
            }
            
            if (line.equalsIgnoreCase("loadgui")) {
                // Launch GUI in separate thread
                final FlightBookingSystem guiFbs = fbs;
                javax.swing.SwingUtilities.invokeLater(() -> {
                    new WelcomeScreen(guiFbs);
                });
                System.out.println("GUI launched. You can continue using CLI or close it.");
                continue;
            }

            try {
                Command command = CommandParser.parse(line);
                command.execute(fbs);
                
                // Auto save after every command
                FlightBookingSystemData.store(fbs);
                
            } catch (FlightBookingSystemException ex) {
                System.out.println(ex.getMessage());
            }
        }
        
        // Final save on exit
        FlightBookingSystemData.store(fbs);
        System.out.println("Data saved successfully. Goodbye!");
        System.exit(0);
    }
}

