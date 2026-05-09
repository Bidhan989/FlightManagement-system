package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.CancellationFeeCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Window for passengers to cancel their own bookings.
 */
public class PassengerCancelBookingWindow extends JFrame implements ActionListener {
    
    private PassengerPortalWindow portalWindow;
/**
 * The flightIdField field.
 */
    private JTextField flightIdField;
/**
 * The backBtn field.
 */
    private JButton cancelBtn, browseBtn, backBtn;
    
/**
 * Constructs a new instance of this class.
 * @param portalWindow the portalWindow to set
 */
    public PassengerCancelBookingWindow(PassengerPortalWindow portalWindow) {
        this.portalWindow = portalWindow;
        initialize();
    }
    
/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("Cancel My Booking");
        setSize(550, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel flightLabel = new JLabel("Flight ID:");
        flightLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        panel.add(flightLabel, gbc);
        
        flightIdField = new JTextField(15);
        flightIdField.setFont(new Font("Dialog", Font.PLAIN, 12));
        flightIdField.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        panel.add(flightIdField, gbc);
        
        browseBtn = new JButton("Browse My Bookings");
        browseBtn.setFont(new Font("Dialog", Font.PLAIN, 11));
        browseBtn.addActionListener(this);
        gbc.gridx = 2;
        gbc.weightx = 0.5;
        panel.add(browseBtn, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cancelBtn = new JButton("Cancel Booking");
        cancelBtn.addActionListener(this);
        backBtn = new JButton("Back");
        backBtn.addActionListener(this);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(backBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(buttonPanel, gbc);
        
        add(panel);
        setLocationRelativeTo(portalWindow);
        setVisible(true);
    }
    
    @Override
/**
 * Performs the actionPerformed operation.
 * @param e the e parameter
 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            cancelBooking();
        } else if (e.getSource() == browseBtn) {
            browseMyBookings();
        } else if (e.getSource() == backBtn) {
            this.dispose();
        }
    }
    
/**
 * Performs the cancelBooking operation.
 */
    private void cancelBooking() {
        try {
/**
 * Retrieves the currentcustomer value.
 */
            int customerId = portalWindow.getCurrentCustomer().getId();
/**
 * Performs the parseInt operation.
 */
            int flightId = Integer.parseInt(flightIdField.getText());
            
            // Find booking
            Booking booking = null;
            for (Booking b : portalWindow.getCurrentCustomer().getBookings()) {
                if (b.getFlight().getId() == flightId) {
                    booking = b;
                    break;
                }
            }
            
            if (booking == null) {
                throw new FlightBookingSystemException("You don't have a booking for this flight.");
            }
            
            // Calculate fee
            double fee = CancellationFeeCalculator.calculateCancellationFee(
                booking.getFlight(), 
                portalWindow.getFlightBookingSystem().getSystemDate(),
                booking.getBookingPrice());
/**
 * Retrieves the bookingprice value.
 */
            double refund = booking.getBookingPrice() - fee;
            
            // Confirm
            int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Cancellation Fee: £%.2f\nRefund: £%.2f\n\nProceed?",
                    fee, refund),
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                CancelBooking cmd = new CancelBooking(customerId, flightId);
                cmd.execute(portalWindow.getFlightBookingSystem());
                portalWindow.saveData();
                
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully!");
                portalWindow.displayMyBookings();
                this.dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
/**
 * Performs the browseMyBookings operation.
 */
    private void browseMyBookings() {
        java.util.List<String> myBookings = new java.util.ArrayList<>();
        for (Booking b : portalWindow.getCurrentCustomer().getBookings()) {
            myBookings.add(String.format("Flight ID %d: %s - %s to %s (%s)",
                b.getFlight().getId(), b.getFlight().getFlightNumber(),
                b.getFlight().getOrigin(), b.getFlight().getDestination(),
                b.getFlight().getDepartureDate()));
        }
        
        if (myBookings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have no bookings.");
            return;
        }
        
/**
 * Performs the showInputDialog operation.
 */
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select booking to cancel:", "My Bookings",
            JOptionPane.PLAIN_MESSAGE, null,
            myBookings.toArray(), myBookings.get(0));
        
        if (selected != null) {
/**
 * Performs the substring operation.
 */
            String idStr = selected.substring(10, selected.indexOf(":"));
            flightIdField.setText(idStr);
        }
    }
}