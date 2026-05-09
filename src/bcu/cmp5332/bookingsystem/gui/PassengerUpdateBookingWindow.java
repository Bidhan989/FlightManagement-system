package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.UpdateBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Window for passengers to update (rebook) their bookings.
 */
public class PassengerUpdateBookingWindow extends JFrame implements ActionListener {
    
    private PassengerPortalWindow portalWindow;
/**
 * The newFlightIdField field.
 */
    private JTextField oldFlightIdField, newFlightIdField;
/**
 * The cancelBtn field.
 */
    private JButton updateBtn, cancelBtn;
    
/**
 * Constructs a new instance of this class.
 * @param portalWindow the portalWindow to set
 */
    public PassengerUpdateBookingWindow(PassengerPortalWindow portalWindow) {
        this.portalWindow = portalWindow;
        initialize();
    }
    
/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("Update My Booking");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Current Flight ID:"), gbc);
        
        oldFlightIdField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(oldFlightIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("New Flight ID:"), gbc);
        
        newFlightIdField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(newFlightIdField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        updateBtn = new JButton("Update Booking");
        updateBtn.addActionListener(this);
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
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
        if (e.getSource() == updateBtn) {
            updateBooking();
        } else if (e.getSource() == cancelBtn) {
            this.dispose();
        }
    }
    
/**
 * Performs the updateBooking operation.
 */
    private void updateBooking() {
        try {
/**
 * Retrieves the currentcustomer value.
 */
            int customerId = portalWindow.getCurrentCustomer().getId();
/**
 * Performs the parseInt operation.
 */
            int oldFlightId = Integer.parseInt(oldFlightIdField.getText());
/**
 * Performs the parseInt operation.
 */
            int newFlightId = Integer.parseInt(newFlightIdField.getText());
            
            UpdateBooking cmd = new UpdateBooking(customerId, oldFlightId, newFlightId);
            cmd.execute(portalWindow.getFlightBookingSystem());
            
            portalWindow.saveData();
            
            JOptionPane.showMessageDialog(this,
                "Booking updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            portalWindow.displayMyBookings();
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
