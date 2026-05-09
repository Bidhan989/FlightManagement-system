package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * GUI window for searching and filtering flights.
 * Provides advanced search capabilities with multiple criteria.
 * 
 * @version 5.0 (Advanced search)
 */
public class SearchFlightsWindow extends JFrame implements ActionListener {
    
    private MainWindow mw;
    private JTextField destinationText = new JTextField();
    private JTextField originText = new JTextField();
    private JCheckBox availableOnlyCheck = new JCheckBox("Available seats only");
    private JButton searchBtn = new JButton("Search");
    private JButton clearBtn = new JButton("Clear");
    private JButton closeBtn = new JButton("Close");
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    
    /**
     * Creates a new SearchFlightsWindow.
     * 
     * @param mw the main window reference
     */
    public SearchFlightsWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }
    
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setTitle("Search Flights");
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Search criteria panel
        JPanel criteriaPanel = new JPanel(new GridLayout(4, 2, 16, 16));
        criteriaPanel.setBorder(BorderFactory.createTitledBorder("Search Criteria"));
        
        criteriaPanel.add(new JLabel("Origin:"));
        criteriaPanel.add(originText);
        
        criteriaPanel.add(new JLabel("Destination:"));
        criteriaPanel.add(destinationText);
        
        criteriaPanel.add(new JLabel("Filters:"));
        criteriaPanel.add(availableOnlyCheck);
        
        criteriaPanel.add(new JLabel(""));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        buttonPanel.add(searchBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(closeBtn);
        criteriaPanel.add(buttonPanel);
        
        mainPanel.add(criteriaPanel, BorderLayout.NORTH);
        
        // Results table
        String[] columns = {"ID", "Flight No", "Origin", "Destination", "Date", 
                           "Price", "Current Price", "Seats", "Availability"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultsTable = new JTable(tableModel);
        resultsTable.setRowHeight(25);
        resultsTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Info label
        JLabel infoLabel = new JLabel(" Enter search criteria and click Search. Leave fields empty to show all flights.");
        mainPanel.add(infoLabel, BorderLayout.SOUTH);
        
        // Add action listeners
        searchBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        closeBtn.addActionListener(this);
        
        this.add(mainPanel);
        
        // Initial search to show all flights
        performSearch();
        
        // Center on screen
        setLocationRelativeTo(mw);
        setVisible(true);
    }
    
    /**
     * Handles button click events.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == searchBtn) {
            performSearch();
        } else if (ae.getSource() == clearBtn) {
            clearFields();
        } else if (ae.getSource() == closeBtn) {
            this.dispose();
        }
    }
    
    /**
     * Performs the search based on current criteria.
     * Only shows flights that have not yet departed.
     */
    private void performSearch() {
        String origin = originText.getText().trim().toLowerCase();
        String destination = destinationText.getText().trim().toLowerCase();
        boolean availableOnly = availableOnlyCheck.isSelected();
        
        // Clear current results
        tableModel.setRowCount(0);
        
        List<Flight> results = new ArrayList<>();
        LocalDate today = mw.getFlightBookingSystem().getSystemDate();
        
        for (Flight flight : mw.getFlightBookingSystem().getFlights()) {
            // Filter out past flights (departed flights)
            if (flight.getDepartureDate().isBefore(today)) {
                continue;
            }
            
            // Filter by origin
            if (!origin.isEmpty() && 
                !flight.getOrigin().toLowerCase().contains(origin)) {
                continue;
            }
            
            // Filter by destination
            if (!destination.isEmpty() && 
                !flight.getDestination().toLowerCase().contains(destination)) {
                continue;
            }
            
            // Filter by availability
            if (availableOnly && 
                flight.getPassengers().size() >= flight.getCapacity()) {
                continue;
            }
            
            results.add(flight);
        }
        
        // Populate table with results
        for (Flight flight : results) {
            Object[] row = new Object[9];
            row[0] = flight.getId();
            row[1] = flight.getFlightNumber();
            row[2] = flight.getOrigin();
            row[3] = flight.getDestination();
            row[4] = flight.getDepartureDate();
            row[5] = String.format("£%.2f", flight.getPrice());
            row[6] = String.format("£%.2f", flight.getCurrentPrice(today));
            row[7] = flight.getPassengers().size() + "/" + flight.getCapacity();
            
            int available = flight.getCapacity() - flight.getPassengers().size();
            if (available > 0) {
                row[8] = available + " seats available";
            } else {
                row[8] = "FULL";
            }
            
            tableModel.addRow(row);
        }
        
        // Update title with result count (showing only upcoming flights)
        setTitle("Search Flights - " + results.size() + " upcoming flight(s) found");
    }
    
    /**
     * Clears all search fields.
     */
    private void clearFields() {
        originText.setText("");
        destinationText.setText("");
        availableOnlyCheck.setSelected(false);
        performSearch();  // Show all flights
    }
}
