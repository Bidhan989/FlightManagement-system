package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.IssueBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

/**
 * Window for passengers to book flights.
 * Only shows available flights that the passenger can book.
 * 
 * @version 8.0 (Authentication - Passenger Booking)
 */
/**
 * GUI window class for PassengerBookFlight.
 * Provides user interface components and handles user interactions.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class PassengerBookFlightWindow extends JFrame implements ActionListener {
    
/**
 * The portalWindow field.
 */
    private PassengerPortalWindow portalWindow;
/**
 * The flightIdField field.
 */
    private JTextField flightIdField;
/**
 * The cancelBtn field.
 */
    private JButton bookBtn, browseBtn, cancelBtn;
    
/**
 * Constructs a new instance of this class.
 * @param portalWindow the portalWindow to set
 */
    public PassengerBookFlightWindow(PassengerPortalWindow portalWindow) {
        this.portalWindow = portalWindow;
        initialize();
    }
    
/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("Book a Flight");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Flight ID:"), gbc);
        
        flightIdField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(flightIdField, gbc);
        
        browseBtn = new JButton("Browse");
        browseBtn.addActionListener(this);
        gbc.gridx = 2;
        panel.add(browseBtn, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bookBtn = new JButton("Book Flight");
        bookBtn.addActionListener(this);
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(this);
        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);
        
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
        if (e.getSource() == bookBtn) {
            bookFlight();
        } else if (e.getSource() == browseBtn) {
            browseFlights();
        } else if (e.getSource() == cancelBtn) {
            this.dispose();
        }
    }
    
/**
 * Performs the bookFlight operation.
 */
    private void bookFlight() {
        try {
/**
 * Performs the parseInt operation.
 */
            int flightId = Integer.parseInt(flightIdField.getText());
/**
 * Retrieves the currentcustomer value.
 */
            int customerId = portalWindow.getCurrentCustomer().getId();
            
            IssueBooking cmd = new IssueBooking(customerId, flightId);
            cmd.execute(portalWindow.getFlightBookingSystem());
            
            portalWindow.saveData();
            
            JOptionPane.showMessageDialog(this,
                "Booking successful!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            portalWindow.displayMyBookings();
            this.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid flight ID",
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
 * Performs the browseFlights operation.
 */
    private void browseFlights() {
        LocalDate today = portalWindow.getFlightBookingSystem().getSystemDate();
        java.util.List<String> availableFlights = new java.util.ArrayList<>();
        
        for (Flight flight : portalWindow.getFlightBookingSystem().getFlights()) {
            if (flight.getDepartureDate().isBefore(today)) continue;
/**
 * Retrieves the capacity value.
 */
            int available = flight.getCapacity() - flight.getPassengers().size();
            if (available <= 0) continue;
            
            availableFlights.add(String.format("ID %d: %s - %s to %s (%s) - £%.2f - %d seats",
                flight.getId(), flight.getFlightNumber(), flight.getOrigin(),
                flight.getDestination(), flight.getDepartureDate(),
                flight.getCurrentPrice(today), available));
        }
        
        if (availableFlights.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No available flights found.",
                "No Flights",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
/**
 * Performs the showInputDialog operation.
 */
        String selected = (String) JOptionPane.showInputDialog(this,
            "Select a flight:",
            "Available Flights",
            JOptionPane.PLAIN_MESSAGE,
            null,
            availableFlights.toArray(),
            availableFlights.get(0));
        
        if (selected != null) {
/**
 * Performs the substring operation.
 */
            String idStr = selected.substring(3, selected.indexOf(":"));
            flightIdField.setText(idStr);
        }
    }
}
