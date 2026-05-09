package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

/**
 * GUI window class for Main.
 * Provides user interface components and handles user interactions.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class MainWindow extends JFrame implements ActionListener {

/**
 * The menuBar field.
 */
    private JMenuBar menuBar;
/**
 * The reportsMenu field.
 */
    private JMenu adminMenu, flightsMenu, bookingsMenu, customersMenu, reportsMenu;
/**
 * The adminExit field.
 */
    private JMenuItem adminExit;
/**
 * The flightsShowPassengers field.
 */
    private JMenuItem flightsView, flightsAdd, flightsDel, flightsShowPassengers;
/**
 * The bookingsCancel field.
 */
    private JMenuItem bookingsIssue, bookingsUpdate, bookingsCancel;
/**
 * The custDel field.
 */
    private JMenuItem custView, custAdd, custDel;
/**
 * The reportsSearch field.
 */
    private JMenuItem reportsDashboard, reportsSearch;

/**
 * The fbs field.
 */
    private FlightBookingSystem fbs;

/**
 * Constructs a new instance of this class.
 * @param fbs the fbs to set
 */
    public MainWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
/**
 * Constructs a new instance of this class.
 */
        System.out.println("MainWindow initializing with " + fbs.getFlights().size() + " flights");
        initialize();
    }

/**
 * Retrieves the flightbookingsystem value.
 * @return the FlightBookingSystem result
 */
    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }

/**
 * Performs the initialize operation.
 */
    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Flight Booking Management System - Admin Portal");
        setSize(1200, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menuBar = new JMenuBar();
        menuBar.setBorderPainted(false);
        setJMenuBar(menuBar);

        createAdminMenu();
        createFlightsMenu();
        createBookingsMenu();
        createCustomersMenu();
        createReportsMenu();

        displayWelcomeDashboard();
        
        setVisible(true);
    }

/**
 * Performs the createAdminMenu operation.
 */
    private void createAdminMenu() {
        adminMenu = createStyledMenu("Admin");
        adminExit = createStyledMenuItem("Exit System");
        adminMenu.add(adminExit);
        adminExit.addActionListener(this);
        menuBar.add(adminMenu);
    }

/**
 * Performs the createFlightsMenu operation.
 */
    private void createFlightsMenu() {
        flightsMenu = createStyledMenu("Flights");
        flightsView = createStyledMenuItem("View All Flights");
        flightsAdd = createStyledMenuItem("Add Flight");
        flightsDel = createStyledMenuItem("Delete Flight");
        flightsShowPassengers = createStyledMenuItem("Show Passengers");

        flightsMenu.add(flightsView);
        flightsMenu.add(flightsAdd);
        flightsMenu.addSeparator();
        flightsMenu.add(flightsShowPassengers);
        flightsMenu.addSeparator();
        flightsMenu.add(flightsDel);

        flightsView.addActionListener(this);
        flightsAdd.addActionListener(this);
        flightsDel.addActionListener(this);
        flightsShowPassengers.addActionListener(this);

        menuBar.add(flightsMenu);
    }

/**
 * Performs the createBookingsMenu operation.
 */
    private void createBookingsMenu() {
        bookingsMenu = createStyledMenu("Bookings");
        bookingsIssue = createStyledMenuItem("Issue Booking");
        bookingsUpdate = createStyledMenuItem("Update Booking");
        bookingsCancel = createStyledMenuItem("Cancel Booking");

        bookingsMenu.add(bookingsIssue);
        bookingsMenu.add(bookingsUpdate);
        bookingsMenu.add(bookingsCancel);

        bookingsIssue.addActionListener(this);
        bookingsUpdate.addActionListener(this);
        bookingsCancel.addActionListener(this);

        menuBar.add(bookingsMenu);
    }

/**
 * Performs the createCustomersMenu operation.
 */
    private void createCustomersMenu() {
        customersMenu = createStyledMenu("Customers");
        custView = createStyledMenuItem("View All Customers");
        custAdd = createStyledMenuItem("Add Customer");
        custDel = createStyledMenuItem("Delete Customer");

        customersMenu.add(custView);
        customersMenu.add(custAdd);
        customersMenu.addSeparator();
        customersMenu.add(custDel);

        custView.addActionListener(this);
        custAdd.addActionListener(this);
        custDel.addActionListener(this);

        menuBar.add(customersMenu);
    }

/**
 * Performs the createReportsMenu operation.
 */
    private void createReportsMenu() {
        reportsMenu = createStyledMenu("Reports");
        reportsDashboard = createStyledMenuItem("Dashboard");
        reportsSearch = createStyledMenuItem("Search Flights");

        reportsMenu.add(reportsDashboard);
        reportsMenu.add(reportsSearch);

        reportsDashboard.addActionListener(this);
        reportsSearch.addActionListener(this);

        menuBar.add(reportsMenu);
    }

/**
 * Performs the createStyledMenu operation.
 * @param text the text parameter
 * @return the JMenu result
 */
    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setFont(new Font("Dialog", Font.BOLD, 12));
        return menu;
    }

/**
 * Performs the createStyledMenuItem operation.
 * @param text the text parameter
 * @return the JMenuItem result
 */
    private JMenuItem createStyledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Dialog", Font.PLAIN, 11));
        return item;
    }

    @Override
/**
 * Performs the actionPerformed operation.
 * @param ae the ae parameter
 */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == adminExit) {
            saveData();
            System.exit(0);
        } else if (ae.getSource() == flightsView) {
            displayFlights();
        } else if (ae.getSource() == flightsAdd) {
            new AddFlightWindow(this);
        } else if (ae.getSource() == flightsDel) {
            new DeleteFlightWindow(this);
        } else if (ae.getSource() == flightsShowPassengers) {
            ViewPassengersWindow.showWithSelection(this);
        } else if (ae.getSource() == bookingsIssue) {
            new IssueBookingWindow(this);
        } else if (ae.getSource() == bookingsUpdate) {
            new UpdateBookingWindow(this);
        } else if (ae.getSource() == bookingsCancel) {
            new CancelBookingWindow(this);
        } else if (ae.getSource() == custView) {
            new ViewCustomersWindow(this);
        } else if (ae.getSource() == custAdd) {
            new AddCustomerWindow(this);
        } else if (ae.getSource() == custDel) {
            new DeleteCustomerWindow(this);
        } else if (ae.getSource() == reportsDashboard) {
            displayWelcomeDashboard();
        } else if (ae.getSource() == reportsSearch) {
            new SearchFlightsWindow(this);
        }
    }

/**
 * Performs the saveData operation.
 */
    public void saveData() {
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            GUIUtils.showError(this, "Error saving data: " + ex.getMessage());
        }
    }

/**
 * Performs the displayWelcomeDashboard operation.
 */
    private void displayWelcomeDashboard() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("FLIGHT BOOKING SYSTEM");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Admin Control Panel");
        subtitleLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(12));
        headerPanel.add(subtitleLabel);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

/**
 * Retrieves the flights value.
 */
        int totalFlights = fbs.getFlights().size();
/**
 * Retrieves the customers value.
 */
        int totalCustomers = fbs.getCustomers().size();
/**
 * Retrieves the flights value.
 */
        int upcomingFlights = (int) fbs.getFlights().stream()
                .filter(f -> !f.getDepartureDate().isBefore(fbs.getSystemDate()))
                .count();

        statsPanel.add(createStatCard("Total Flights", String.valueOf(totalFlights), 
            new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Total Customers", String.valueOf(totalCustomers), 
            new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Upcoming Flights", String.valueOf(upcomingFlights), 
            new Color(230, 126, 34)));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);

        this.getContentPane().removeAll();
        this.getContentPane().add(mainPanel);
        this.revalidate();
        this.repaint();
    }

/**
 * Performs the createStatCard operation.
 * @param label the label parameter
 * @param value the value parameter
 * @param color the color parameter
 * @return the JPanel result
 */
    private JPanel createStatCard(String label, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 3),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Dialog", Font.BOLD, 48));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Dialog", Font.PLAIN, 16));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(valueLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(labelText);

        return card;
    }

/**
 * Performs the displayFlights operation.
 */
    public void displayFlights() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel headerLabel = new JLabel("All Flights");
        headerLabel.setFont(new Font("Dialog", Font.BOLD, 28));
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Flight No", "Origin", "Destination", "Date", "Price", "Capacity", "Passengers"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
/**
 * Checks if the celleditable condition is true.
 * @param row the row parameter
 * @param column the column parameter
 * @return the boolean result
 */
            public boolean isCellEditable(int row, int column) { return false; }
        };

        LocalDate today = fbs.getSystemDate();
        for (Flight flight : fbs.getFlights()) {
            if (flight.getDepartureDate().isBefore(today)) continue;
            model.addRow(new Object[]{
                    flight.getId(),
                    flight.getFlightNumber(),
                    flight.getOrigin(),
                    flight.getDestination(),
                    flight.getDepartureDate(),
                    String.format("£%.2f", flight.getCurrentPrice(today)),
                    flight.getCapacity(),
                    flight.getPassengers().size() + "/" + flight.getCapacity()
            });
        }

        JTable table = new JTable(model);
        GUIUtils.styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        this.getContentPane().removeAll();
        this.getContentPane().add(mainPanel);
        this.revalidate();
        this.repaint();
    }
}