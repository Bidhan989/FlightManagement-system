package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.UpdateBooking;
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
 * GUI window for updating (rebooking) a booking to a different flight.
 * Shows rebooking fee and price difference.
 * 
 * @version 5.1 (Rebooking with fees)
 */
public class UpdateBookingWindow extends JFrame implements ActionListener {
    
    private MainWindow mw;
    private JTextField customerIdText = new JTextField();
    private JTextField oldFlightIdText = new JTextField();
    private JTextField newFlightIdText = new JTextField();
    
    private JButton updateBtn = new JButton("Update Booking");
    private JButton closeBtn = new JButton("Close");
    private JButton browseCustomersBtn = new JButton("Browse...");
    private JButton browseOldFlightBtn = new JButton("Browse...");
    private JButton browseNewFlightBtn = new JButton("Browse...");

    /**
     * Creates a new UpdateBookingWindow.
     * 
     * @param mw the main window reference
     */
    public UpdateBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setTitle("Update Booking (Rebook)");
        setSize(550, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Top panel with input fields
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(4, 3, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        topPanel.add(new JLabel("Customer ID: *"));
        topPanel.add(customerIdText);
        topPanel.add(browseCustomersBtn);
        
        topPanel.add(new JLabel("Current Flight ID: *"));
        topPanel.add(oldFlightIdText);
        topPanel.add(browseOldFlightBtn);
        
        topPanel.add(new JLabel("New Flight ID: *"));
        topPanel.add(newFlightIdText);
        topPanel.add(browseNewFlightBtn);
        
        topPanel.add(new JLabel("* Required fields"));
        topPanel.add(new JLabel(""));
        topPanel.add(new JLabel(""));

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3, 5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(updateBtn);
        bottomPanel.add(closeBtn);

        // Add action listeners
        updateBtn.addActionListener(this);
        closeBtn.addActionListener(this);
        browseCustomersBtn.addActionListener(this);
        browseOldFlightBtn.addActionListener(this);
        browseNewFlightBtn.addActionListener(this);

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
        if (ae.getSource() == updateBtn) {
            updateBooking();
        } else if (ae.getSource() == closeBtn) {
            this.dispose();
        } else if (ae.getSource() == browseCustomersBtn) {
            browseCustomers();
        } else if (ae.getSource() == browseOldFlightBtn) {
            browseOldFlight();
        } else if (ae.getSource() == browseNewFlightBtn) {
            browseNewFlight();
        }
    }

    /**
     * Validates input and updates the booking.
     */
    private void updateBooking() {
        try {
            String customerIdStr = customerIdText.getText().trim();
            String oldFlightIdStr = oldFlightIdText.getText().trim();
            String newFlightIdStr = newFlightIdText.getText().trim();
            
            if (customerIdStr.isEmpty()) {
                throw new FlightBookingSystemException("Customer ID is required.");
            }
            
            if (oldFlightIdStr.isEmpty()) {
                throw new FlightBookingSystemException("Current Flight ID is required.");
            }
            
            if (newFlightIdStr.isEmpty()) {
                throw new FlightBookingSystemException("New Flight ID is required.");
            }
            
            int customerId = Integer.parseInt(customerIdStr);
            int oldFlightId = Integer.parseInt(oldFlightIdStr);
            int newFlightId = Integer.parseInt(newFlightIdStr);
            
            if (oldFlightId == newFlightId) {
                throw new FlightBookingSystemException("New flight must be different from current flight.");
            }
            
            // Verify customer and flights exist
            Customer customer = mw.getFlightBookingSystem().getCustomerByID(customerId);
            Flight oldFlight = mw.getFlightBookingSystem().getFlightByID(oldFlightId);
            Flight newFlight = mw.getFlightBookingSystem().getFlightByID(newFlightId);
            
            // Find the booking
            Booking oldBooking = null;
            for (Booking booking : customer.getBookings()) {
                if (booking.getFlight().getId() == oldFlightId) {
                    oldBooking = booking;
                    break;
                }
            }
            
            if (oldBooking == null) {
                throw new FlightBookingSystemException(
                    "No booking found for " + customer.getName() + " on flight " + oldFlight.getFlightNumber());
            }
            
            // Calculate fees and prices
            LocalDate today = mw.getFlightBookingSystem().getSystemDate();
            double oldBookingPrice = oldBooking.getBookingPrice();
            double rebookingFee = CancellationFeeCalculator.calculateRebookingFee(
                oldFlight, today, oldBookingPrice);
            double newFlightPrice = newFlight.getCurrentPrice(today);
            double totalCost = rebookingFee + newFlightPrice - oldBookingPrice;
            
            // Confirm update
            String costMessage;
            if (totalCost >= 0) {
                costMessage = "Amount to Pay:      £" + String.format("%.2f", totalCost);
            } else {
                costMessage = "Refund Amount:      £" + String.format("%.2f", Math.abs(totalCost));
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Update this booking?\n\n" +
                "Customer: " + customer.getName() + "\n\n" +
                "OLD FLIGHT:\n" +
                "  " + oldFlight.getFlightNumber() + " - " +
                oldFlight.getOrigin() + " → " + oldFlight.getDestination() + "\n" +
                "  " + oldFlight.getDepartureDate() + "\n" +
                "  Price: £" + String.format("%.2f", oldBookingPrice) + "\n\n" +
                "NEW FLIGHT:\n" +
                "  " + newFlight.getFlightNumber() + " - " +
                newFlight.getOrigin() + " → " + newFlight.getDestination() + "\n" +
                "  " + newFlight.getDepartureDate() + "\n" +
                "  Price: £" + String.format("%.2f", newFlightPrice) + "\n\n" +
                "FINANCIAL SUMMARY:\n" +
                "  Rebooking Fee:      £" + String.format("%.2f", rebookingFee) + "\n" +
                "  New Flight Price:   £" + String.format("%.2f", newFlightPrice) + "\n" +
                "  Credit (old):       -£" + String.format("%.2f", oldBookingPrice) + "\n" +
                "  ──────────────────────────────\n" +
                "  " + costMessage,
                "Confirm Update",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            // Execute the update booking command
            UpdateBooking updateCmd = new UpdateBooking(customerId, oldFlightId, newFlightId);
            updateCmd.execute(mw.getFlightBookingSystem());
            
            // Auto Save: Save data immediately
            mw.saveData();
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Booking updated successfully!\n\n" +
                "New Flight: " + newFlight.getFlightNumber() + "\n" +
                costMessage,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Close the window
            this.dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "IDs must be numbers.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
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
                                   " (" + customer.getBookings().size() + " booking(s))";
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
     * Shows a list of flights for the current booking.
     */
    private void browseOldFlight() {
        try {
            String customerIdStr = customerIdText.getText().trim();
            if (customerIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please select a customer first.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int customerId = Integer.parseInt(customerIdStr);
            Customer customer = mw.getFlightBookingSystem().getCustomerByID(customerId);
            
            if (customer.getBookings().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "This customer has no bookings.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String[] bookingOptions = new String[customer.getBookings().size()];
            int i = 0;
            for (Booking booking : customer.getBookings()) {
                Flight flight = booking.getFlight();
                bookingOptions[i++] = flight.getId() + " - " + flight.getFlightNumber() + " - " +
                                     flight.getOrigin() + " to " + flight.getDestination() +
                                     " (" + flight.getDepartureDate() + ")";
            }
            
            String selected = (String) JOptionPane.showInputDialog(
                this,
                "Select current booking:",
                "Browse Current Bookings",
                JOptionPane.QUESTION_MESSAGE,
                null,
                bookingOptions,
                bookingOptions[0]
            );
            
            if (selected != null) {
                String flightId = selected.substring(0, selected.indexOf(" - "));
                oldFlightIdText.setText(flightId);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading customer bookings: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows a list of available flights for rebooking.
     */
    private void browseNewFlight() {
        String[] flightOptions = new String[mw.getFlightBookingSystem().getFlights().size()];
        int i = 0;
        for (Flight flight : mw.getFlightBookingSystem().getFlights()) {
            int available = flight.getCapacity() - flight.getPassengers().size();
            flightOptions[i++] = flight.getId() + " - " + flight.getFlightNumber() + " - " +
                                 flight.getOrigin() + " to " + flight.getDestination() +
                                 " (" + flight.getDepartureDate() + ") - " +
                                 available + " seats available";
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
            "Select new flight:",
            "Browse Flights",
            JOptionPane.QUESTION_MESSAGE,
            null,
            flightOptions,
            flightOptions[0]
        );
        
        if (selected != null) {
            String flightId = selected.substring(0, selected.indexOf(" - "));
            newFlightIdText.setText(flightId);
        }
    }
}
