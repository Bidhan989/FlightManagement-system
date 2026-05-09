package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;

/**
 * Dashboard window displaying system statistics and analytics.
 */
public class DashboardWindow extends JFrame {
    
    private MainWindow mw;
/**
 * The fbs field.
 */
    private FlightBookingSystem fbs;
    
/**
 * Constructs a new instance of this class.
 * @param mw the mw to set
 */
    public DashboardWindow(MainWindow mw) {
        this.mw = mw;
        this.fbs = mw.getFlightBookingSystem();
        initialize();
    }
    
/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("System Dashboard");
        setSize(900, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Main panel with title
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel("Flight Booking System Dashboard");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Calculate statistics
/**
 * Retrieves the flights value.
 */
        int totalFlights = fbs.getFlights().size();
/**
 * Retrieves the customers value.
 */
        int totalCustomers = fbs.getCustomers().size();
/**
 * Retrieves the totalbookings value.
 */
        int totalBookings = getTotalBookings();
/**
 * Retrieves the totalpassengers value.
 */
        int totalPassengers = getTotalPassengers();
/**
 * Retrieves the totalcapacity value.
 */
        int totalCapacity = getTotalCapacity();
        double averageLoadFactor = totalCapacity > 0 ? 
            (double) totalPassengers / totalCapacity * 100 : 0;
        
        // Create stat cards with simple colors
        statsPanel.add(createStatCard("Total Flights", String.valueOf(totalFlights), 
            new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Total Customers", String.valueOf(totalCustomers), 
            new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Total Bookings", String.valueOf(totalBookings), 
            new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Total Passengers", String.valueOf(totalPassengers), 
            new Color(241, 196, 15)));
        statsPanel.add(createStatCard("Total Capacity", String.valueOf(totalCapacity), 
            new Color(230, 126, 34)));
        statsPanel.add(createStatCard("Avg Load Factor", 
            String.format("%.1f%%", averageLoadFactor), new Color(231, 76, 60)));
        
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        
        // Details panel
        JPanel detailsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        
        // Top flights
        JPanel topFlightsPanel = createTopFlightsPanel();
        detailsPanel.add(topFlightsPanel);
        
        // Top customers
        JPanel topCustomersPanel = createTopCustomersPanel();
        detailsPanel.add(topCustomersPanel);
        
        mainPanel.add(detailsPanel, BorderLayout.SOUTH);
        
        this.add(mainPanel);
        
        setLocationRelativeTo(mw);
        setVisible(true);
    }
    
/**
 * Performs the createStatCard operation.
 * @param label the label parameter
 * @param value the value parameter
 * @param color the color parameter
 * @return the JPanel result
 */
    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Dialog", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
        labelLabel.setForeground(Color.WHITE);
        labelLabel.setHorizontalAlignment(JLabel.CENTER);
        
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(labelLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
/**
 * Performs the createTopFlightsPanel operation.
 * @return the JPanel result
 */
    private JPanel createTopFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Most Popular Flights"));
        
        StringBuilder content = new StringBuilder();
        content.append("Top 5 Flights by Passenger Count:\n\n");
        
        fbs.getFlights().stream()
            .sorted((f1, f2) -> Integer.compare(f2.getPassengers().size(), f1.getPassengers().size()))
            .limit(5)
            .forEach(flight -> {
                content.append(String.format("%s (%s -> %s)\n", 
                    flight.getFlightNumber(), flight.getOrigin(), flight.getDestination()));
                content.append(String.format("  Passengers: %d/%d (%.0f%% full)\n\n", 
                    flight.getPassengers().size(), flight.getCapacity(),
                    (double) flight.getPassengers().size() / flight.getCapacity() * 100));
            });
        
        JTextArea textArea = new JTextArea(content.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        panel.add(scrollPane);
        
        return panel;
    }
    
/**
 * Performs the createTopCustomersPanel operation.
 * @return the JPanel result
 */
    private JPanel createTopCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Most Active Customers"));
        
        StringBuilder content = new StringBuilder();
        content.append("Top 5 Customers by Booking Count:\n\n");
        
        fbs.getCustomers().stream()
            .sorted((c1, c2) -> Integer.compare(c2.getBookings().size(), c1.getBookings().size()))
            .limit(5)
            .forEach(customer -> {
                content.append(String.format("%s\n", customer.getName()));
                content.append(String.format("  Email: %s\n", customer.getEmail()));
                content.append(String.format("  Bookings: %d\n\n", customer.getBookings().size()));
            });
        
        JTextArea textArea = new JTextArea(content.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        panel.add(scrollPane);
        
        return panel;
    }
    
/**
 * Retrieves the totalbookings value.
 * @return the int result
 */
    private int getTotalBookings() {
        int total = 0;
        for (Customer customer : fbs.getCustomers()) {
            total += customer.getBookings().size();
        }
        return total;
    }
    
/**
 * Retrieves the totalpassengers value.
 * @return the int result
 */
    private int getTotalPassengers() {
        int total = 0;
        for (Flight flight : fbs.getFlights()) {
            total += flight.getPassengers().size();
        }
        return total;
    }
    
/**
 * Retrieves the totalcapacity value.
 * @return the int result
 */
    private int getTotalCapacity() {
        int total = 0;
        for (Flight flight : fbs.getFlights()) {
            total += flight.getCapacity();
        }
        return total;
    }
}