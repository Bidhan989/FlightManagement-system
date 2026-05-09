package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.CancellationFeeCalculator;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.*;

/**
 * GUI window for cancelling a booking.
 * Removes the booking from customer and removes customer from flight passengers.
 * 
 * @version 4.0 (Cancel booking functionality)
 */
public class CancelBookingWindow extends JFrame implements ActionListener {
    
    private MainWindow mw;
    private JTextField customerIdText = new JTextField();
    private JTextField flightIdText = new JTextField();
    
    private JButton cancelBookingBtn = new JButton("Cancel Booking");
    private JButton closeBtn = new JButton("Close");
    private JButton browseCustomersBtn = new JButton("Browse...");
    private JButton browseFlightsBtn = new JButton("Browse...");

    /**
     * Creates a new CancelBookingWindow.
     * 
     * @param mw the main window reference
     */
    public CancelBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setTitle("Cancel Booking");
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
        bottomPanel.add(cancelBookingBtn);
        bottomPanel.add(closeBtn);

        // Add action listeners
        cancelBookingBtn.addActionListener(this);
        closeBtn.addActionListener(this);
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
        if (ae.getSource() == cancelBookingBtn) {
            cancelBooking();
        } else if (ae.getSource() == closeBtn) {
            this.dispose();
        } else if (ae.getSource() == browseCustomersBtn) {
            browseCustomers();
        } else if (ae.getSource() == browseFlightsBtn) {
            browseFlights();
        }
    }

    /**
     * Validates input and cancels the booking.
     * Shows cancellation fee before confirming.
     */
    private void cancelBooking() {
        try {
            String customerIdStr = customerIdText.getText().trim();
            String flightIdStr = flightIdText.getText().trim();
            
            if (customerIdStr.isEmpty()) {
                throw new FlightBookingSystemException("Customer ID is required.");
            }
            
            if (flightIdStr.isEmpty()) {
                throw new FlightBookingSystemException("Flight ID is required.");
            }
            
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
            
            // Find the booking to get its price
            Booking bookingToCancel = null;
            for (Booking booking : customer.getBookings()) {
                if (booking.getFlight().getId() == flightId) {
                    bookingToCancel = booking;
                    break;
                }
            }
            
            if (bookingToCancel == null) {
                throw new FlightBookingSystemException(
                    "No booking found for " + customer.getName() + " on flight " + flight.getFlightNumber());
            }
            
            // Calculate cancellation fee (Tier 5+)
            LocalDate today = mw.getFlightBookingSystem().getSystemDate();
            double bookingPrice = bookingToCancel.getBookingPrice();
            double cancellationFee = CancellationFeeCalculator.calculateCancellationFee(
                flight, today, bookingPrice);
            double refundAmount = bookingPrice - cancellationFee;
            
            // Confirm cancellation with fee information
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this booking?\n\n" +
                "Customer: " + customer.getName() + "\n" +
                "Flight: " + flight.getFlightNumber() + " - " +
                flight.getOrigin() + " → " + flight.getDestination() + "\n" +
                "Date: " + flight.getDepartureDate() + "\n\n" +
                "FINANCIAL SUMMARY:\n" +
                "  Booking Price:      £" + String.format("%.2f", bookingPrice) + "\n" +
                "  Cancellation Fee:   £" + String.format("%.2f", cancellationFee) + "\n" +
                "  ────────────────────────────\n" +
                "  Refund Amount:      £" + String.format("%.2f", refundAmount) + "\n",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            // Execute the cancel booking command
            CancelBooking cancelCmd = new CancelBooking(customerId, flightId);
            cancelCmd.execute(mw.getFlightBookingSystem());
            
            // AUTO-SAVE: Save data immediately
            mw.saveData();
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Booking cancelled successfully!\n\n" +
                "Booking Price:      £" + String.format("%.2f", bookingPrice) + "\n" +
                "Cancellation Fee:   £" + String.format("%.2f", cancellationFee) + "\n" +
                "Refund Amount:      £" + String.format("%.2f", refundAmount),
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
        String[] customerOptions = new String[mw.getFlightBookingSystem().getCustomers().size()];
        int i = 0;
        for (Customer customer : mw.getFlightBookingSystem().getCustomers()) {
            customerOptions[i++] = customer.getId() + " - " + customer.getName() + 
                                   " (" + customer.getPhone() + ") - " +
                                   customer.getBookings().size() + " booking(s)";
        }
        
        if (customerOptions.length == 0) {
            JOptionPane.showMessageDialog(this,
                "No customers found in the system.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
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
            String customerId = selected.substring(0, selected.indexOf(" - "));
            customerIdText.setText(customerId);
        }
    }
    
    /**
     * Shows a list of flights to browse and select from.
     */
    private void browseFlights() {
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
            "Select a flight:",
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
