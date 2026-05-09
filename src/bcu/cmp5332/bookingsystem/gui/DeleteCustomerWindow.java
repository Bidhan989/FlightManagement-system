package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.DeleteCustomer;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * GUI window for deleting a customer from the system.
 * Uses soft delete.
 * 
 * @version 4.0 (Delete functionality)
 */
public class DeleteCustomerWindow extends JFrame implements ActionListener {
    
    private MainWindow mw;
    private JTextField customerIdText = new JTextField();
    private JButton deleteBtn = new JButton("Delete Customer");
    private JButton cancelBtn = new JButton("Cancel");
    private JButton browseBtn = new JButton("Browse...");

    /**
     * Creates a new DeleteCustomerWindow.
     * 
     * @param mw the main window reference
     */
    public DeleteCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setTitle("Delete Customer");
        setSize(450, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Top panel with input fields
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 3, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        topPanel.add(new JLabel("Customer ID: *"));
        topPanel.add(customerIdText);
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
            deleteCustomer();
        } else if (ae.getSource() == cancelBtn) {
            this.dispose();
        } else if (ae.getSource() == browseBtn) {
            browseCustomer();
        }
    }

    /**
     * Validates input and deletes the customer.
     */
    private void deleteCustomer() {
        try {
            String customerIdStr = customerIdText.getText().trim();
            
            if (customerIdStr.isEmpty()) {
                throw new FlightBookingSystemException("Customer ID is required.");
            }
            
            int customerId;
            try {
                customerId = Integer.parseInt(customerIdStr);
            } catch (NumberFormatException e) {
                throw new FlightBookingSystemException("Customer ID must be a number.");
            }
            
            // Verify customer exists
            Customer customer = mw.getFlightBookingSystem().getCustomerByID(customerId);
            
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this customer?\n\n" +
                "Customer: " + customer.getName() + "\n" +
                "Phone: " + customer.getPhone() + "\n" +
                "Email: " + customer.getEmail() + "\n" +
                "Bookings: " + customer.getBookings().size() + "\n\n" +
                "Note: This is a soft delete. Data will be retained.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            // Execute the delete command
            DeleteCustomer deleteCmd = new DeleteCustomer(customerId);
            deleteCmd.execute(mw.getFlightBookingSystem());
            
            // AUTO-SAVE: Persist data immediately
            mw.saveData();
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Customer deleted successfully!\n\n" +
                "Customer #" + customerId + " has been removed from active customers.",
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
    private void browseCustomer() {
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
        
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select a customer to delete:",
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
}
