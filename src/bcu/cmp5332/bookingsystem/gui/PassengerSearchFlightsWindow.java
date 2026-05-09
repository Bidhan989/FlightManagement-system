package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Flight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Window for passengers to search available flights.
 */
public class PassengerSearchFlightsWindow extends JFrame implements ActionListener {
    
    private PassengerPortalWindow portalWindow;
/**
 * The destinationField field.
 */
    private JTextField originField, destinationField;
/**
 * The availableOnlyCheck field.
 */
    private JCheckBox availableOnlyCheck;
/**
 * The closeBtn field.
 */
    private JButton searchBtn, clearBtn, closeBtn;
/**
 * The resultsTable field.
 */
    private JTable resultsTable;
/**
 * The tableModel field.
 */
    private DefaultTableModel tableModel;
    
/**
 * Constructs a new instance of this class.
 * @param portalWindow the portalWindow to set
 */
    public PassengerSearchFlightsWindow(PassengerPortalWindow portalWindow) {
        this.portalWindow = portalWindow;
        initialize();
    }
    
/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("Search Flights");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Search panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Criteria"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Origin:"), gbc);
        
        originField = new JTextField(15);
        gbc.gridx = 1;
        searchPanel.add(originField, gbc);
        
        gbc.gridx = 2;
        searchPanel.add(new JLabel("Destination:"), gbc);
        
        destinationField = new JTextField(15);
        gbc.gridx = 3;
        searchPanel.add(destinationField, gbc);
        
        availableOnlyCheck = new JCheckBox("Available Seats Only");
        availableOnlyCheck.setSelected(true);
        gbc.gridx = 4;
        searchPanel.add(availableOnlyCheck, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(this);
        clearBtn = new JButton("Clear");
        clearBtn.addActionListener(this);
        closeBtn = new JButton("Close");
        closeBtn.addActionListener(this);
        buttonPanel.add(searchBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(closeBtn);
        
        // Results table
        String[] columns = {"ID", "Flight No", "Origin", "Destination", "Date", "Price", "Available"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
/**
 * Checks if the celleditable condition is true.
 * @param row the row parameter
 * @param column the column parameter
 * @return the boolean result
 */
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultsTable = new JTable(tableModel);
        resultsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        
        // Add components
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Perform initial search
        performSearch();
        
        setLocationRelativeTo(portalWindow);
        setVisible(true);
    }
    
    @Override
/**
 * Performs the actionPerformed operation.
 * @param e the e parameter
 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchBtn) {
            performSearch();
        } else if (e.getSource() == clearBtn) {
            originField.setText("");
            destinationField.setText("");
            performSearch();
        } else if (e.getSource() == closeBtn) {
            this.dispose();
        }
    }
    
/**
 * Performs the performSearch operation.
 */
    private void performSearch() {
/**
 * Retrieves the text value.
 */
        String origin = originField.getText().trim().toLowerCase();
/**
 * Retrieves the text value.
 */
        String destination = destinationField.getText().trim().toLowerCase();
/**
 * Checks if the selected condition is true.
 */
        boolean availableOnly = availableOnlyCheck.isSelected();
        
        tableModel.setRowCount(0);
        LocalDate today = portalWindow.getFlightBookingSystem().getSystemDate();
        List<Flight> results = new ArrayList<>();
        
        for (Flight flight : portalWindow.getFlightBookingSystem().getFlights()) {
            // Only future flights
            if (flight.getDepartureDate().isBefore(today)) continue;
            
            // Filter by origin
            if (!origin.isEmpty() && !flight.getOrigin().toLowerCase().contains(origin)) continue;
            
            // Filter by destination
            if (!destination.isEmpty() && !flight.getDestination().toLowerCase().contains(destination)) continue;
            
            // Filter by availability
/**
 * Retrieves the capacity value.
 */
            int available = flight.getCapacity() - flight.getPassengers().size();
            if (availableOnly && available <= 0) continue;
            
            results.add(flight);
        }
        
        // Populate table
        for (Flight flight : results) {
            Object[] row = new Object[7];
            row[0] = flight.getId();
            row[1] = flight.getFlightNumber();
            row[2] = flight.getOrigin();
            row[3] = flight.getDestination();
            row[4] = flight.getDepartureDate();
            row[5] = String.format("£%.2f", flight.getCurrentPrice(today));
/**
 * Retrieves the capacity value.
 */
            int available = flight.getCapacity() - flight.getPassengers().size();
            row[6] = available > 0 ? available + " seats" : "FULL";
            tableModel.addRow(row);
        }
        
        setTitle("Search Flights - " + results.size() + " found");
    }
}
