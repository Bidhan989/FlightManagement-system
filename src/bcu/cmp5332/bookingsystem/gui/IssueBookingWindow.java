package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.IssueBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * GUI window for creating a new booking.
 * Allows selection of customer and flight with validation.
 * 
 * @version 3.0 (GUI Implementation)
 */
public class IssueBookingWindow extends JFrame implements ActionListener {
    
    private MainWindow mw;
    private JTextField customerIdText = new JTextField();
    private JTextField flightIdText = new JTextField();
    
    private JButton issueBtn = new JButton("Issue Booking");
    private JButton cancelBtn = new JButton("Cancel");
    private JButton browseCustomersBtn = new JButton("Browse...");
    private JButton browseFlightsBtn = new JButton("Browse...");

    /**
     * Creates a new IssueBookingWindow.
     * 
     * @param mw the main window reference
     */
    public IssueBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setTitle("Issue New Booking");
        setSize(500, 250);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Top panel with input fields
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 3, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        topPanel.add(new JLabel("Customer ID: *"));
        topPanel.add(customerIdText);
        topPanel.add(browseCustomersBtn);
        
        topPanel.add(new JLabel("Flight ID: *"));
        topPanel.add(flightIdText);
        topPanel.add(browseFlightsBtn);
        
        topPanel.add(new JLabel("* Required fields"));
        topPanel.add(new JLabel(""));
        topPanel.add(new JLabel(""));

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3, 5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(issueBtn);
        bottomPanel.add(cancelBtn);

        // Add action listeners
        issueBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        browseCustomersBtn.addActionListener(this);
        browseFlightsBtn.addActionListener(this);

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
        if (ae.getSource() == issueBtn) {
            issueBooking();
        } else if (ae.getSource() == cancelBtn) {
            this.dispose();
        } else if (ae.getSource() == browseCustomersBtn) {
            browseCustomers();
        } else if (ae.getSource() == browseFlightsBtn) {
            browseFlights();
        }
    }

    /**
     * Validates input and creates a new booking.
     */
    private void issueBooking() {
        try {
            // Get input values
            String customerIdStr = customerIdText.getText().trim();
            String flightIdStr = flightIdText.getText().trim();
            
            // Validation
            if (customerIdStr.isEmpty()) {
                throw new FlightBookingSystemException("Customer ID is required.");
            }
            
            if (flightIdStr.isEmpty()) {
                throw new FlightBookingSystemException("Flight ID is required.");
            }
            
            // Parse IDs
            int customerId;
            int flightId;
            
            try {
                customerId = Integer.parseInt(customerIdStr);
            } catch (NumberFormatException e) {
                throw new FlightBookingSystemException("Customer ID must be a number.");
            }
            
            try {
                flightId = Integer.parseInt(flightIdStr);
            } catch (NumberFormatException e) {
                throw new FlightBookingSystemException("Flight ID must be a number.");
            }
            
            // Verify customer and flight exist
            Customer customer = mw.getFlightBookingSystem().getCustomerByID(customerId);
            Flight flight = mw.getFlightBookingSystem().getFlightByID(flightId);
            
            // Check if customer already has booking for this flight
            for (Booking booking : customer.getBookings()) {
                if (booking.getFlight().getId() == flightId) {
                    throw new FlightBookingSystemException(
                        "Customer already has a booking for this flight.");
                }
            }
            
            // Execute the issue booking command
            IssueBooking issueBookingCmd = new IssueBooking(customerId, flightId);
            issueBookingCmd.execute(mw.getFlightBookingSystem());
            
            // AUTO-SAVE: Save data immediately
            mw.saveData();
            
            // Calculate dynamic price for display
            double finalPrice = flight.getCurrentPrice(mw.getFlightBookingSystem().getSystemDate());
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Booking issued successfully!\n\n" +
                "Customer: " + customer.getName() + "\n" +
                "Flight: " + flight.getFlightNumber() + " - " + 
                flight.getOrigin() + " to " + flight.getDestination() + "\n" +
                "Departure: " + flight.getDepartureDate() + "\n" +
                "Price: £" + String.format("%.2f", finalPrice) + "\n\n" +
                "Would you like to see pricing breakdown?",
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
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
     * Shows a list of customers to browse and select from.
     */
    private void browseCustomers() {
        // Create list of customers
        String[] customerOptions = new String[mw.getFlightBookingSystem().getCustomers().size()];
        int i = 0;
        for (Customer customer : mw.getFlightBookingSystem().getCustomers()) {
            customerOptions[i++] = customer.getId() + " - " + customer.getName() + 
                                   " (" + customer.getPhone() + ")";
        }
        
        if (customerOptions.length == 0) {
            JOptionPane.showMessageDialog(this, 
                "No customers found in the system.", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Show selection dialog
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select a customer:",
            "Browse Customers",
            JOptionPane.QUESTION_MESSAGE,
            null,
            customerOptions,
            customerOptions[0]
        );
        
        if (selected != null) {
            // Extract customer ID from selection
            String customerId = selected.substring(0, selected.indexOf(" - "));
            customerIdText.setText(customerId);
        }
    }
    
    /**
     * Shows a list of flights to browse and select from.
     */
    private void browseFlights() {
        // Create list of flights
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
        
        // Show selection dialog
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select a flight:",
            "Browse Flights",
            JOptionPane.QUESTION_MESSAGE,
            null,
            flightOptions,
            flightOptions[0]
        );
        
        if (selected != null) {
            // Extract flight ID from selection
            String flightId = selected.substring(0, selected.indexOf(" - "));
            flightIdText.setText(flightId);
        }
    }
}
