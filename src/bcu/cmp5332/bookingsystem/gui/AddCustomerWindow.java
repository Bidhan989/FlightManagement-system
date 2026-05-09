package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * GUI window for adding a new customer to the system.
 * Provides input fields for name, phone, and email with validation.
 * 
 * @version 3.0 (GUI Implementation)
 */
/**
 * GUI window class for AddCustomer.
 * Provides user interface components and handles user interactions.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class AddCustomerWindow extends JFrame implements ActionListener {
    
/**
 * The mw field.
 */
    private MainWindow mw;
    private JTextField nameText = new JTextField();
    private JTextField phoneText = new JTextField();
    private JTextField emailText = new JTextField();
    
    private JButton addBtn = new JButton("Add Customer");
    private JButton cancelBtn = new JButton("Cancel");

    /**
     * Creates a new AddCustomerWindow.
     * 
     * @param mw the main window reference
     */
    public AddCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setTitle("Add New Customer");
        setSize(450, 250);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Top panel with input fields
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(4, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        topPanel.add(new JLabel("Name: *"));
        topPanel.add(nameText);
        
        topPanel.add(new JLabel("Phone: *"));
        topPanel.add(phoneText);
        
        topPanel.add(new JLabel("Email: *"));
        topPanel.add(emailText);
        
        topPanel.add(new JLabel("* Required fields"));
        topPanel.add(new JLabel(""));

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 3, 5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        bottomPanel.add(new JLabel(""));
        bottomPanel.add(addBtn);
        bottomPanel.add(cancelBtn);

        // Add action listeners
        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

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
        if (ae.getSource() == addBtn) {
            addCustomer();
        } else if (ae.getSource() == cancelBtn) {
            this.dispose();
        }
    }

    /**
     * Validates input and adds a new customer to the system.
     */
    private void addCustomer() {
        try {
            // Get input values
/**
 * Retrieves the text value.
 */
            String name = nameText.getText().trim();
/**
 * Retrieves the text value.
 */
            String phone = phoneText.getText().trim();
/**
 * Retrieves the text value.
 */
            String email = emailText.getText().trim();
            
            // Validation
            if (name.isEmpty()) {
                throw new FlightBookingSystemException("Name is required.");
            }
            
            if (phone.isEmpty()) {
                throw new FlightBookingSystemException("Phone number is required.");
            }
            
            // Validate phone format (basic check)
            if (!phone.matches("^[0-9+\\-\\s()]+$")) {
                throw new FlightBookingSystemException("Invalid phone number format.");
            }
            
            if (email.isEmpty()) {
                throw new FlightBookingSystemException("Email is required.");
            }
            
            // Validate email format (basic check)
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new FlightBookingSystemException("Invalid email format.");
            }
            
            // Execute the add customer command
            AddCustomer addCustomerCmd = new AddCustomer(name, phone, email);
            addCustomerCmd.execute(mw.getFlightBookingSystem());
            
            // AUTO-SAVE: Persist data immediately
            mw.saveData();
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                "Customer added successfully!\n" +
                "Name: " + name + "\n" +
                "Phone: " + phone + "\n" +
                "Email: " + email,
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
}
