package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user input and creates appropriate Command objects.
 * 
 * @version 2.0 (Added prompts for email, capacity, and price)
 */
public class CommandParser {
    
    /**
     * Parses a command line and returns the corresponding Command object.
     * 
     * @param line the input line to parse
     * @return the Command object
     * @throws IOException if there's an error reading input
     * @throws FlightBookingSystemException if the command is invalid
     */
/**
 * Performs the parse operation.
 * @param line the line parameter
 * @param IOException the IOException parameter
 * @return the Command result
 * @throws IOException if an error occurs
 * @throws FlightBookingSystemException if an error occurs
 */
    public static Command parse(String line) throws IOException, FlightBookingSystemException {
        try {
            String[] parts = line.split(" ");
            String cmd = parts[0];

            if (cmd.equals("addflight")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Flight Number: ");
/**
 * Performs the readLine operation.
 */
                String flightNumber = reader.readLine();
                System.out.print("Origin: ");
/**
 * Performs the readLine operation.
 */
                String origin = reader.readLine();
                System.out.print("Destination: ");
/**
 * Performs the readLine operation.
 */
                String destination = reader.readLine();
                LocalDate departureDate = parseDateWithAttempts(reader);
                
                // New: Prompt for capacity and price
                System.out.print("Capacity (passengers): ");
/**
 * Performs the parseInt operation.
 */
                int capacity = Integer.parseInt(reader.readLine());
                System.out.print("Price (£): ");
/**
 * Performs the parseDouble operation.
 */
                double price = Double.parseDouble(reader.readLine());
                
                return new AddFlight(flightNumber, origin, destination, departureDate, capacity, price);
                
            } else if (cmd.equals("addcustomer")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Name: ");
/**
 * Performs the readLine operation.
 */
                String name = reader.readLine();
                System.out.print("Phone: ");
/**
 * Performs the readLine operation.
 */
                String phone = reader.readLine();
                
                // New: Prompt for email
                System.out.print("Email: ");
/**
 * Performs the readLine operation.
 */
                String email = reader.readLine();
                
                return new AddCustomer(name, phone, email);
                
            } else if (cmd.equals("loadgui")) {
                return new LoadGUI();
                
            } else if (parts.length == 1) {
                if (line.equals("listflights")) {
                    return new ListFlights();
                } else if (line.equals("listcustomers")) {
                    return new ListCustomers();
                } else if (line.equals("help")) {
                    return new Help();
                } else if (line.equals("dashboard")) {  // Tier 5
                    throw new FlightBookingSystemException("Dashboard is only available in GUI mode. Use 'loadgui' first.");
                }
                
            } else if (parts.length == 2) {
/**
 * Performs the parseInt operation.
 */
                int id = Integer.parseInt(parts[1]);

                if (cmd.equals("showflight")) {
                    return new Command() {
                        @Override
/**
 * Executes this command on the flight booking system.
 * @param fbs the fbs parameter
 */
                        public void execute(bcu.cmp5332.bookingsystem.model.FlightBookingSystem fbs) 
                                throws FlightBookingSystemException {
                            System.out.println(fbs.getFlightByID(id).getDetailsLong());
                        }
                    };
                } else if (cmd.equals("showcustomer")) {
                    return new Command() {
                        @Override
/**
 * Executes this command on the flight booking system.
 * @param fbs the fbs parameter
 */
                        public void execute(bcu.cmp5332.bookingsystem.model.FlightBookingSystem fbs) 
                                throws FlightBookingSystemException {
                            System.out.println(fbs.getCustomerByID(id).getDetailsLong());
                        }
                    };
                } else if (cmd.equals("showbooking")) {
                    return new ShowBooking(id);
                } else if (cmd.equals("deleteflight")) {
                    return new DeleteFlight(id);
                } else if (cmd.equals("deletecustomer")) {
                    return new DeleteCustomer(id);
                } else if (cmd.equals("showpricing")) {  // Tier 5
                    return new ShowPricing(id);
                } else if (cmd.equals("searchflights")) {  // Tier 5 - search by destination
                    return new SearchFlights(parts[1]);
                }
                
            } else if (parts.length == 3) {
/**
 * Performs the parseInt operation.
 */
                int id1 = Integer.parseInt(parts[1]);
/**
 * Performs the parseInt operation.
 */
                int id2 = Integer.parseInt(parts[2]);

                if (cmd.equals("addbooking")) {
                    return new IssueBooking(id1, id2);
                } else if (cmd.equals("cancelbooking")) {
                    return new CancelBooking(id1, id2);
                }
            } else if (parts.length == 4) {
                if (parts[0].equals("updatebooking")) {
/**
 * Performs the parseInt operation.
 */
                    int customerId = Integer.parseInt(parts[1]);
/**
 * Performs the parseInt operation.
 */
                    int oldFlightId = Integer.parseInt(parts[2]);
/**
 * Performs the parseInt operation.
 */
                    int newFlightId = Integer.parseInt(parts[3]);
                    return new UpdateBooking(customerId, oldFlightId, newFlightId);
                }
            }
        } catch (NumberFormatException ex) {
            throw new FlightBookingSystemException("Invalid input format. Please use numbers where required.");
        }

        throw new FlightBookingSystemException("Invalid command. Type 'help' for available commands.");
    }
    
    /**
     * Prompts the user to enter a date with a specified number of attempts.
     * 
     * @param br the BufferedReader to read from
     * @param attempts the number of attempts allowed
     * @return the parsed LocalDate
     * @throws IOException if there's an error reading input
     * @throws FlightBookingSystemException if all attempts fail
     */
/**
 * Performs the parseDateWithAttempts operation.
 * @param br the br parameter
 * @param attempts the attempts parameter
 * @return the LocalDate result
 */
    private static LocalDate parseDateWithAttempts(BufferedReader br, int attempts) 
            throws IOException, FlightBookingSystemException {
        if (attempts < 1) {
            throw new IllegalArgumentException("Number of attempts should be higher than 0");
        }
        while (attempts > 0) {
            attempts--;
            System.out.print("Departure Date (\"YYYY-MM-DD\" format): ");
            try {
                LocalDate departureDate = LocalDate.parse(br.readLine());
                return departureDate;
            } catch (DateTimeParseException dtpe) {
                System.out.println("Date must be in YYYY-MM-DD format. " + attempts + " attempts remaining...");
            }
        }
        
        throw new FlightBookingSystemException("Incorrect departure date provided. Cannot create flight.");
    }
    
    /**
     * Prompts the user to enter a date with 3 attempts.
     * 
     * @param br the BufferedReader to read from
     * @return the parsed LocalDate
     * @throws IOException if there's an error reading input
     * @throws FlightBookingSystemException if all attempts fail
     */
/**
 * Performs the parseDateWithAttempts operation.
 * @param br the br parameter
 * @return the LocalDate result
 */
    private static LocalDate parseDateWithAttempts(BufferedReader br) 
            throws IOException, FlightBookingSystemException {
        return parseDateWithAttempts(br, 3);
    }
}
