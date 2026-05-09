package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.DeleteFlight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * GUI window for deleting a flight from the system.
 * Uses soft delete
 * 
 * @version 4.0 (Delete functionality)
 */
public class DeleteFlightWindow extends JFrame implements ActionListener {
    
    private MainWindow mw;
    private JTextField flightIdText = new JTextField();
    private JButton deleteBtn = new JButton("Delete Flight");
    private JButton cancelBtn = new JButton("Cancel");
    private JButton browseBtn = new JButton("Browse...");

    /**
     * Creates a new DeleteFlightWindow.
     * 
     * @param mw the main window reference
     */
    public DeleteFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setTitle("Delete Flight");
        setSize(450, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Top panel with input fields
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 3, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        topPanel.add(new JLabel("Flight ID: *"));
        topPanel.add(flightIdText);
        topPanel.add(browseBtn);
        
        topPanel.add(new JLabel("* Required field"));
        topPanel.add(new JLabel(""));
        topPanel.add(new JLabel(""));

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3, 5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(deleteBtn);
        bottomPanel.add(cancelBtn);

        // Add action listeners
        deleteBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        browseBtn.addActionListener(this);

        // Add panels to frame
        this.getContentPane().add(topPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        
        // Center on screen
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    /**
     * Handles button click events.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == deleteBtn) {
            deleteFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.dispose();
        } else if (ae.getSource() == browseBtn) {
            browseFlight();
        }
    }

    /**
     * Validates input and deletes the flight.
     */
    private void deleteFlight() {
        try {
            String flightIdStr = flightIdText.getText().trim();
            
            if (flightIdStr.isEmpty()) {
                throw new FlightBookingSystemException("Flight ID is required.");
            }
            
            int flightId;
            try {
                flightId = Integer.parseInt(flightIdStr);
            } catch (NumberFormatException e) {
                throw new FlightBookingSystemException("Flight ID must be a number.");
            }
            
            // Verify flight exists
            Flight flight = mw.getFlightBookingSystem().getFlightByID(flightId);
            
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this flight?\n\n" +
                "Flight: " + flight.getFlightNumber() + "\n" +
                "Route: " + flight.getOrigin() + " → " + flight.getDestination() + "\n" +
                "Date: " + flight.getDepartureDate() + "\n\n" +
                "Note: This is a soft delete. Data will be retained.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            // Execute the delete command
            DeleteFlight deleteCmd = new DeleteFlight(flightId);
            deleteCmd.execute(mw.getFlightBookingSystem());
            
            // AUTO-SAVE: Persist data immediately
            mw.saveData();
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Flight deleted successfully!\n\n" +
                "Flight #" + flightId + " has been removed from active flights.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refresh flights view if showing
            mw.displayFlights();
            
            // Close the window
            this.dispose();
            
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows a list of flights to browse and select from.
     */
    private void browseFlight() {
        String[] flightOptions = new String[mw.getFlightBookingSystem().getFlights().size()];
        int i = 0;
        for (Flight flight : mw.getFlightBookingSystem().getFlights()) {
            flightOptions[i++] = flight.getId() + " - " + flight.getFlightNumber() + " - " +
                                 flight.getOrigin() + " to " + flight.getDestination() +
                                 " (" + flight.getDepartureDate() + ")";
        }
        
        if (flightOptions.length == 0) {
            JOptionPane.showMessageDialog(this,
                "No flights found in the system.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select a flight to delete:",
            "Browse Flights",
            JOptionPane.QUESTION_MESSAGE,
            null,
            flightOptions,
            flightOptions[0]
        );
        
        if (selected != null) {
            String flightId = selected.substring(0, selected.indexOf(" - "));
            flightIdText.setText(flightId);
        }
    }
}
