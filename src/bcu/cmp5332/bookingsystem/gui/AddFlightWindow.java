package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.commands.Command;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;

/**
 * Dialog for adding new flights with validation.
 */
public class AddFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
/**
 * The flightNoText field.
 */
    private JTextField flightNoText;
/**
 * The originText field.
 */
    private JTextField originText;
/**
 * The destinationText field.
 */
    private JTextField destinationText;
/**
 * The datePicker field.
 */
    private DatePicker datePicker;
/**
 * The capacityText field.
 */
    private JTextField capacityText;
/**
 * The priceText field.
 */
    private JTextField priceText;
/**
 * The addBtn field.
 */
    private JButton addBtn;
/**
 * The cancelBtn field.
 */
    private JButton cancelBtn;

/**
 * Constructs a new instance of this class.
 * @param mw the mw to set
 */
    public AddFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

/**
 * Performs the initialize operation.
 */
    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Use default
        }

        setTitle("Add New Flight");
        setSize(550, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        
        JLabel titleLabel = new JLabel("Add New Flight");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        headerPanel.add(titleLabel);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Flight Number
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Flight Number:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        flightNoText = new JTextField(20);
        formPanel.add(flightNoText, gbc);
        
        // Origin
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Origin:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        originText = new JTextField(20);
        formPanel.add(originText, gbc);
        
        // Destination
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Destination:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        destinationText = new JTextField(20);
        formPanel.add(destinationText, gbc);
        
        // Departure Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Departure Date:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JPanel datePanel = new JPanel(new BorderLayout(5, 0));
        
        // Initialize DatePicker with system date as minimum
        LocalDate systemDate = mw.getFlightBookingSystem().getSystemDate();
        datePicker = new DatePicker(systemDate);
        
        JLabel formatHint = new JLabel("(Click Cal or type YYYY-MM-DD)");
        formatHint.setFont(new Font("Dialog", Font.ITALIC, 11));
        
        datePanel.add(datePicker, BorderLayout.CENTER);
        datePanel.add(formatHint, BorderLayout.SOUTH);
        formPanel.add(datePanel, gbc);
        
        // Capacity
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Capacity:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        capacityText = new JTextField(20);
        capacityText.setToolTipText("Maximum number of passengers (e.g., 150)");
        formPanel.add(capacityText, gbc);
        
        // Price
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        formPanel.add(new JLabel("Price (£):"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        priceText = new JTextField(20);
        priceText.setToolTipText("Ticket price in pounds (e.g., 149.99)");
        formPanel.add(priceText, gbc);
        
        // Info panel
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel infoLabel = new JLabel("All fields are required. Use calendar picker for easy date selection.");
        infoLabel.setFont(new Font("Dialog", Font.ITALIC, 11));
        infoPanel.add(infoLabel);
        formPanel.add(infoPanel, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        addBtn = new JButton("Add Flight");
        cancelBtn = new JButton("Cancel");
        
        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        setLocationRelativeTo(mw);
        setVisible(true);
        
        // Focus on first field
        flightNoText.requestFocus();
    }

    @Override
/**
 * Performs the actionPerformed operation.
 * @param ae the ae parameter
 */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            addFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.dispose();
        }
    }

/**
 * Performs the addFlight operation.
 */
    private void addFlight() {
        try {
            // Validate inputs
/**
 * Retrieves the text value.
 */
            String flightNumber = flightNoText.getText().trim();
            if (flightNumber.isEmpty()) {
                throw new FlightBookingSystemException("Flight number cannot be empty");
            }
            
/**
 * Retrieves the text value.
 */
            String origin = originText.getText().trim();
            if (origin.isEmpty()) {
                throw new FlightBookingSystemException("Origin cannot be empty");
            }
            
/**
 * Retrieves the text value.
 */
            String destination = destinationText.getText().trim();
            if (destination.isEmpty()) {
                throw new FlightBookingSystemException("Destination cannot be empty");
            }
            
            if (origin.equalsIgnoreCase(destination)) {
                throw new FlightBookingSystemException("Origin and destination cannot be the same");
            }
            
            LocalDate departureDate = null;
            try {
                departureDate = datePicker.getDate();
                if (departureDate == null) {
                    throw new FlightBookingSystemException("Please select a departure date");
                }
            } catch (DateTimeParseException dtpe) {
                throw new FlightBookingSystemException("Date must be in YYYY-MM-DD format");
            }
            
            // Check if date is in the future
            LocalDate systemDate = mw.getFlightBookingSystem().getSystemDate();
            if (departureDate.isBefore(systemDate)) {
                throw new FlightBookingSystemException("Departure date must be today or in the future");
            }
            
            // Validate capacity
            String capacityStr = capacityText.getText().trim();
            if (capacityStr.isEmpty()) {
                throw new FlightBookingSystemException("Capacity cannot be empty");
            }
            
            int capacity;
            try {
                capacity = Integer.parseInt(capacityStr);
                if (capacity <= 0) {
                    throw new FlightBookingSystemException("Capacity must be a positive number");
                }
                if (capacity > 1000) {
                    throw new FlightBookingSystemException("Capacity cannot exceed 1000 passengers");
                }
            } catch (NumberFormatException e) {
                throw new FlightBookingSystemException("Capacity must be a valid number");
            }
            
            // Validate price
            String priceStr = priceText.getText().trim();
            if (priceStr.isEmpty()) {
                throw new FlightBookingSystemException("Price cannot be empty");
            }
            
            double price;
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    throw new FlightBookingSystemException("Price cannot be negative");
                }
                if (price > 100000) {
                    throw new FlightBookingSystemException("Price seems unreasonably high. Please check.");
                }
            } catch (NumberFormatException e) {
                throw new FlightBookingSystemException("Price must be a valid number (e.g., 149.99)");
            }
            
            // Create and execute command with capacity and price
            Command addFlight = new AddFlight(flightNumber, origin, destination, departureDate, capacity, price);
            addFlight.execute(mw.getFlightBookingSystem());
            
            // Auto-save
            mw.saveData();
            
            // Show success message
            JOptionPane.showMessageDialog(
                this,
                String.format("Flight added successfully!%nFlight: %s%nRoute: %s → %s%nDate: %s%nCapacity: %d passengers%nPrice: £%.2f", 
                             flightNumber, origin, destination, departureDate, capacity, price),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Refresh view and close
            mw.displayFlights();
            this.dispose();
            
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}