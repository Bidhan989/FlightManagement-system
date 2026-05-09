package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * GUI window for viewing all passengers on a specific flight.
 * Displays passenger information in a table format.
 * 
 * @version 3.0 (GUI Implementation)
 */
/**
 * GUI window class for ViewPassengers.
 * Provides user interface components and handles user interactions.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class ViewPassengersWindow extends JFrame {
    
/**
 * The mw field.
 */
    private MainWindow mw;
/**
 * The flight field.
 */
    private Flight flight;
/**
 * The passengersTable field.
 */
    private JTable passengersTable;
    
    /**
     * Creates a new ViewPassengersWindow.
     * 
     * @param mw the main window reference
     * @param flight the flight to display passengers for
     */
/**
 * Constructs a new instance of this class.
 * @param mw the mw to set
 * @param flight the flight to set
 */
    public ViewPassengersWindow(MainWindow mw, Flight flight) {
        this.mw = mw;
        this.flight = flight;
        initialize();
    }
    
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setTitle("Passengers on Flight " + flight.getFlightNumber());
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Create info panel with flight details
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        infoPanel.add(new JLabel("Flight: " + flight.getFlightNumber()));
        infoPanel.add(new JLabel("Route: " + flight.getOrigin() + " → " + flight.getDestination()));
        infoPanel.add(new JLabel("Departure: " + flight.getDepartureDate()));
        infoPanel.add(new JLabel("Capacity: " + flight.getPassengers().size() + "/" + flight.getCapacity()));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Create table columns
        String[] columns = new String[]{"Customer ID", "Name", "Phone", "Email"};
        
        // Create table data
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
/**
 * Checks if the celleditable condition is true.
 * @param row the row parameter
 * @param column the column parameter
 * @return the boolean result
 */
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        // Populate table with passenger data
        if (flight.getPassengers().isEmpty()) {
            // Show message if no passengers
            JLabel noPassengersLabel = new JLabel("No passengers booked on this flight yet.");
            noPassengersLabel.setHorizontalAlignment(JLabel.CENTER);
            this.getContentPane().add(infoPanel, BorderLayout.NORTH);
            this.getContentPane().add(noPassengersLabel, BorderLayout.CENTER);
        } else {
            // Populate table
            for (Customer passenger : flight.getPassengers()) {
                Object[] row = new Object[4];
                row[0] = passenger.getId();
                row[1] = passenger.getName();
                row[2] = passenger.getPhone();
                row[3] = passenger.getEmail();
                model.addRow(row);
            }
            
            // Create table
            passengersTable = new JTable(model);
            passengersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            passengersTable.setRowHeight(25);
            passengersTable.getTableHeader().setReorderingAllowed(false);
            
            // Set column widths
            passengersTable.getColumnModel().getColumn(0).setPreferredWidth(100); // ID
            passengersTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
            passengersTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Phone
            passengersTable.getColumnModel().getColumn(3).setPreferredWidth(250); // Email
            
            // Add table to scroll pane
            JScrollPane scrollPane = new JScrollPane(passengersTable);
            
            // Add components to frame
            this.getContentPane().add(infoPanel, BorderLayout.NORTH);
            this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        }
        
        // Center on screen
        setLocationRelativeTo(mw);
        setVisible(true);
    }
    
    /**
     * Static method to show passengers window with flight selection.
     * 
     * @param mw the main window reference
     */
    public static void showWithSelection(MainWindow mw) {
        // Create list of flights
        String[] flightOptions = new String[mw.getFlightBookingSystem().getFlights().size()];
        int i = 0;
        for (Flight flight : mw.getFlightBookingSystem().getFlights()) {
            flightOptions[i++] = flight.getId() + " - " + flight.getFlightNumber() + " - " +
                                 flight.getOrigin() + " to " + flight.getDestination() +
                                 " (" + flight.getPassengers().size() + "/" + 
                                 flight.getCapacity() + " passengers)";
        }
        
        if (flightOptions.length == 0) {
            JOptionPane.showMessageDialog(mw, 
                "No flights found in the system.", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Show selection dialog
/**
 * Performs the showInputDialog operation.
 */
        String selected = (String) JOptionPane.showInputDialog(
            mw,
            "Select a flight to view passengers:",
            "Select Flight",
            JOptionPane.QUESTION_MESSAGE,
            null,
            flightOptions,
            flightOptions[0]
        );
        
        if (selected != null) {
            try {
                // Extract flight ID from selection
/**
 * Performs the substring operation.
 */
                String flightIdStr = selected.substring(0, selected.indexOf(" - "));
/**
 * Performs the parseInt operation.
 */
                int flightId = Integer.parseInt(flightIdStr);
                Flight flight = mw.getFlightBookingSystem().getFlightByID(flightId);
                
                // Create and show window
                new ViewPassengersWindow(mw, flight);
                
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(mw, 
                    ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
