package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Enhanced Passenger Portal Window with modern dashboard design.
 * Features improved visual hierarchy and interactive cards.
 * 
 * @version 11.0 (Improved Dashboard)
 */
/**
 * GUI window class for PassengerPortal.
 * Provides user interface components and handles user interactions.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class PassengerPortalWindow extends JFrame implements ActionListener {
    
/**
 * The fbs field.
 */
    private FlightBookingSystem fbs;
/**
 * The currentCustomer field.
 */
    private Customer currentCustomer;
    
/**
 * The menuBar field.
 */
    private JMenuBar menuBar;
/**
 * The accountMenu field.
 */
    private JMenu flightsMenu, bookingsMenu, profileMenu, accountMenu;
    
/**
 * The flightsBook field.
 */
    private JMenuItem flightsView, flightsSearch, flightsBook;
/**
 * The bookingsUpdate field.
 */
    private JMenuItem bookingsView, bookingsCancel, bookingsUpdate;
/**
 * The profileView field.
 */
    private JMenuItem profileView;
/**
 * The accountExit field.
 */
    private JMenuItem accountLogout, accountExit;
    
    // Color scheme - Modern Professional Design
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);  // Professional Blue
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);  // Green
    private static final Color WARNING_COLOR = new Color(230, 126, 34);   // Orange
    private static final Color DANGER_COLOR = new Color(231, 76, 60);     // Red
    private static final Color INFO_COLOR = new Color(52, 73, 94);        // Dark Gray-Blue
    private static final Color DARK_BG = new Color(52, 73, 94);           // Dark Gray for text
    private static final Color LIGHT_BG = new Color(236, 240, 241);       // Light Gray background
    private static final Color HEADER_BG = new Color(44, 62, 80);         // Dark Blue-Gray for headers
/**
 * The CARD_BG field.
 */
    private static final Color CARD_BG = Color.WHITE;                     // White for cards
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);      // Primary text color
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141); // Secondary text color
    
/**
 * Performs the PassengerPortalWindow operation.
 * @param fbs the fbs parameter
 * @return the public result
 */
    public PassengerPortalWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        loadCurrentCustomer();
        initialize();
    }
    
/**
 * Performs the loadCurrentCustomer operation.
 */
    private void loadCurrentCustomer() {
        try {
            Integer customerId = Session.getInstance().getCustomerId();
            if (customerId == null) {
                throw new FlightBookingSystemException("No customer linked to this account.");
            }
            this.currentCustomer = fbs.getCustomerByID(customerId);
        } catch (FlightBookingSystemException e) {
            JOptionPane.showMessageDialog(this,
                "Error loading customer: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("Passenger Portal - " + currentCustomer.getName());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
/**
 * Performs the windowClosing operation.
 * @param windowEvent the windowEvent parameter
 */
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                handleExit();
            }
        });
        
        // Create styled menu bar
        menuBar = new JMenuBar();
        menuBar.setBackground(HEADER_BG);
        menuBar.setBorderPainted(false);
        
        // === FLIGHTS MENU ===
        flightsMenu = createStyledMenu("Flights", HEADER_BG);
        flightsView = new JMenuItem("View Available Flights");
        flightsSearch = new JMenuItem("Search Flights");
        flightsBook = new JMenuItem("Book a Flight");
        
        flightsView.addActionListener(this);
        flightsSearch.addActionListener(this);
        flightsBook.addActionListener(this);
        
        flightsMenu.add(flightsView);
        flightsMenu.add(flightsSearch);
        flightsMenu.addSeparator();
        flightsMenu.add(flightsBook);
        menuBar.add(flightsMenu);
        
        // === MY BOOKINGS MENU ===
        bookingsMenu = createStyledMenu("My Bookings", SUCCESS_COLOR);
        bookingsView = new JMenuItem("View My Bookings");
        bookingsCancel = new JMenuItem("Cancel Booking");
        bookingsUpdate = new JMenuItem("Update Booking");
        
        bookingsView.addActionListener(this);
        bookingsCancel.addActionListener(this);
        bookingsUpdate.addActionListener(this);
        
        bookingsMenu.add(bookingsView);
        bookingsMenu.add(bookingsCancel);
        bookingsMenu.add(bookingsUpdate);
        menuBar.add(bookingsMenu);
        
        // === MY PROFILE MENU ===
        profileMenu = createStyledMenu("My Profile", HEADER_BG);
        profileView = new JMenuItem("View Profile");
        profileView.addActionListener(this);
        profileMenu.add(profileView);
        menuBar.add(profileMenu);
        
        // === ACCOUNT MENU ===
        accountMenu = createStyledMenu("Account", HEADER_BG);
        accountLogout = new JMenuItem("Logout");
        accountExit = new JMenuItem("Exit");
        
        accountLogout.addActionListener(this);
        accountExit.addActionListener(this);
        
        accountMenu.add(accountLogout);
        accountMenu.addSeparator();
        accountMenu.add(accountExit);
        menuBar.add(accountMenu);
        
        setJMenuBar(menuBar);
        getContentPane().setBackground(LIGHT_BG);
        
        displayWelcomeDashboard();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
/**
 * Performs the createStyledMenu operation.
 * @param text the text parameter
 * @param color the color parameter
 * @return the JMenu result
 */
    private JMenu createStyledMenu(String text, Color color) {
        JMenu menu = new JMenu(text);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menu.setForeground(Color.WHITE);
        menu.setOpaque(true);
        menu.setBackground(color);
        menu.setBorderPainted(false);
        return menu;
    }
    
    @Override
/**
 * Performs the actionPerformed operation.
 * @param e the e parameter
 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == flightsView) {
            displayAvailableFlights();
        } else if (e.getSource() == flightsSearch) {
            new PassengerSearchFlightsWindow(this);
        } else if (e.getSource() == flightsBook) {
            new PassengerBookFlightWindow(this);
        } else if (e.getSource() == bookingsView) {
            displayMyBookings();
        } else if (e.getSource() == bookingsCancel) {
            new PassengerCancelBookingWindow(this);
        } else if (e.getSource() == bookingsUpdate) {
            new PassengerUpdateBookingWindow(this);
        } else if (e.getSource() == profileView) {
            displayProfile();
        } else if (e.getSource() == accountLogout) {
            handleLogout();
        } else if (e.getSource() == accountExit) {
            handleExit();
        }
    }
    
/**
 * Performs the displayWelcomeDashboard operation.
 */
    private void displayWelcomeDashboard() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(LIGHT_BG);
        
        // Top Header with blue background
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(PRIMARY_COLOR);
        topHeader.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JPanel headerContent = new JPanel();
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));
        headerContent.setBackground(PRIMARY_COLOR);
        
        JLabel welcomeLabel = new JLabel("Welcome back, " + currentCustomer.getName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Your travel dashboard - manage your bookings and explore flights");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitleLabel.setForeground(new Color(189, 195, 199));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerContent.add(welcomeLabel);
        headerContent.add(Box.createVerticalStrut(8));
        headerContent.add(subtitleLabel);
        
        topHeader.add(headerContent, BorderLayout.WEST);
        
        // Content area
        JPanel contentArea = new JPanel(new BorderLayout(0, 20));
        contentArea.setBackground(LIGHT_BG);
        contentArea.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Stats cards panel
        JPanel statsContainer = new JPanel(new GridLayout(1, 3, 20, 0));
        statsContainer.setBackground(LIGHT_BG);
        
/**
 * Retrieves the bookings value.
 */
        int myBookings = currentCustomer.getBookings().size();
        int upcomingTrips = 0;
        LocalDate today = fbs.getSystemDate();
        
        for (Booking booking : currentCustomer.getBookings()) {
            if (!booking.getFlight().getDepartureDate().isBefore(today)) {
                upcomingTrips++;
            }
        }
        
        int availableFlights = 0;
        for (Flight flight : fbs.getFlights()) {
            if (!flight.getDepartureDate().isBefore(today) && 
                flight.getPassengers().size() < flight.getCapacity()) {
                availableFlights++;
            }
        }
        
        statsContainer.add(createEnhancedStatCard("My Bookings", String.valueOf(myBookings), 
            "Total reservations", SUCCESS_COLOR));
        statsContainer.add(createEnhancedStatCard("Upcoming Trips", String.valueOf(upcomingTrips), 
            "Active bookings", PRIMARY_COLOR));
        statsContainer.add(createEnhancedStatCard("Available Flights", String.valueOf(availableFlights), 
            "Ready to book", new Color(155, 89, 182)));
        
        // Quick actions panel with modern cards
        JPanel actionsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        actionsPanel.setBackground(LIGHT_BG);
        
        actionsPanel.add(createActionCard("Browse Flights", "View all available flights", PRIMARY_COLOR, 
            e -> displayAvailableFlights()));
        actionsPanel.add(createActionCard("Search Flights", "Find specific routes", new Color(155, 89, 182), 
            e -> new PassengerSearchFlightsWindow(this)));
        actionsPanel.add(createActionCard("My Bookings", "Manage reservations", SUCCESS_COLOR, 
            e -> displayMyBookings()));
        actionsPanel.add(createActionCard("My Profile", "View account details", new Color(52, 73, 94), 
            e -> displayProfile()));
        
        // Combine panels
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(LIGHT_BG);
        centerPanel.add(statsContainer, BorderLayout.NORTH);
        centerPanel.add(actionsPanel, BorderLayout.CENTER);
        
        contentArea.add(centerPanel, BorderLayout.CENTER);
        
        mainPanel.add(topHeader, BorderLayout.NORTH);
        mainPanel.add(contentArea, BorderLayout.CENTER);
        
        this.getContentPane().removeAll();
        this.getContentPane().add(mainPanel);
        this.revalidate();
        this.repaint();
    }
    
/**
 * Performs the createEnhancedStatCard operation.
 * @param title the title parameter
 * @param value the value parameter
 * @param subtitle the subtitle parameter
 * @param accentColor the accentColor parameter
 * @return the JPanel result
 */
    private JPanel createEnhancedStatCard(String title, String value, String subtitle, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 3),
            BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(subtitleLabel);
        
        return card;
    }
    
/**
 * Performs the createActionCard operation.
 * @param title the title parameter
 * @param description the description parameter
 * @param bgColor the bgColor parameter
 * @param action the action parameter
 * @return the JPanel result
 */
    private JPanel createActionCard(String title, String description, Color bgColor, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(bgColor);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(236, 240, 241));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(descLabel);
        
        card.add(content, BorderLayout.CENTER);
        
        // Add click listener
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
/**
 * Performs the mouseClicked operation.
 * @param e the e parameter
 */
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, null));
            }
            
            @Override
/**
 * Performs the mouseEntered operation.
 * @param e the e parameter
 */
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(brighten(bgColor));
                content.setBackground(brighten(bgColor));
            }
            
            @Override
/**
 * Performs the mouseExited operation.
 * @param e the e parameter
 */
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(bgColor);
                content.setBackground(bgColor);
            }
        });
        
        return card;
    }
    
/**
 * Performs the brighten operation.
 * @param color the color parameter
 * @return the Color result
 */
    private Color brighten(Color color) {
/**
 * Performs the min operation.
 */
        int r = Math.min(255, color.getRed() + 20);
/**
 * Performs the min operation.
 */
        int g = Math.min(255, color.getGreen() + 20);
/**
 * Performs the min operation.
 */
        int b = Math.min(255, color.getBlue() + 20);
        return new Color(r, g, b);
    }
    
/**
 * Performs the createStatCard operation.
 * @param icon the icon parameter
 * @param label the label parameter
 * @param value the value parameter
 * @param color the color parameter
 * @return the JPanel result
 */
    private JPanel createStatCard(String icon, String label, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(30, 20, 30, 20)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Dialog", Font.PLAIN, 12));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(labelText);
        
        return card;
    }
    
/**
 * Performs the displayAvailableFlights operation.
 */
    public void displayAvailableFlights() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with explicit colors
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        
        JLabel titleLabel = new JLabel("Available Flights");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        String[] columns = {"ID", "Flight No", "Origin", "Destination", "Date", "Price", "Available Seats"};
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
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
        
        LocalDate today = fbs.getSystemDate();
        int availableCount = 0;
        
        for (Flight flight : fbs.getFlights()) {
            if (flight.getDepartureDate().isBefore(today)) {
                continue;
            }
            
/**
 * Retrieves the capacity value.
 */
            int availableSeats = flight.getCapacity() - flight.getPassengers().size();
            if (availableSeats <= 0) {
                continue;
            }
            
            availableCount++;
            Object[] row = new Object[7];
            row[0] = flight.getId();
            row[1] = flight.getFlightNumber();
            row[2] = flight.getOrigin();
            row[3] = flight.getDestination();
            row[4] = flight.getDepartureDate();
            row[5] = String.format("$%.2f", flight.getPrice());
            row[6] = availableSeats;
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        styleTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(LIGHT_BG);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel infoLabel = new JLabel("Total available flights: " + availableCount);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoLabel.setForeground(TEXT_PRIMARY);
        infoPanel.add(infoLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        this.getContentPane().removeAll();
        this.getContentPane().add(mainPanel);
        this.revalidate();
        this.repaint();
    }
    
/**
 * Performs the styleTable operation.
 * @param table the table parameter
 */
    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(147, 197, 253));
        table.setSelectionForeground(Color.WHITE);
        
        // Configure table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(Color.WHITE);
        header.setBackground(HEADER_BG);
        header.setOpaque(true);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        
        // CRITICAL: Set default renderer to override any global settings from AdminPortalWindow
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value != null ? value.toString() : "");
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setForeground(Color.WHITE);
                label.setBackground(HEADER_BG);
                label.setOpaque(true);
                label.setHorizontalAlignment(CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return label;
            }
        });
        
        // Cell renderer for data rows
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                }
                setForeground(TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
                return this;
            }
        };
        
        // Apply cell renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }
    
/**
 * Performs the displayMyBookings operation.
 */
    public void displayMyBookings() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header with explicit colors
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(SUCCESS_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        
        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        String[] columns = {"Booking ID", "Flight No", "Origin", "Destination", "Date", "Booked On", "Price", "Status"};
        
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
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
        
        for (Booking booking : currentCustomer.getBookings()) {
            Object[] row = new Object[8];
            row[0] = model.getRowCount()+1;
            row[1] = booking.getFlight().getFlightNumber();
            row[2] = booking.getFlight().getOrigin();
            row[3] = booking.getFlight().getDestination();
            row[4] = booking.getFlight().getDepartureDate();
            row[5] = booking.getBookingDate();
            row[6] = String.format("£%.2f", booking.getBookingPrice());
            row[7] = booking.getStatus().toString();
            model.addRow(row);
        }
        
        JTable table = new JTable(model);
        styleTable(table);
        
        // Custom renderer for status column
        table.getColumnModel().getColumn(7).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                } else {
                    label.setBackground(Color.WHITE);
/**
 * Returns a string representation of this object.
 */
                    String status = value.toString();
                    if (status.equals("Booked")) {
                        label.setForeground(SUCCESS_COLOR);
                    } else if (status.equals("Rebooked")) {
                        label.setForeground(WARNING_COLOR);
                    } else if (status.equals("Cancelled")) {
                        label.setForeground(DANGER_COLOR);
                    }
                }
                
                return label;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(LIGHT_BG);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel infoLabel = new JLabel("Total bookings: " + currentCustomer.getBookings().size());
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoLabel.setForeground(TEXT_PRIMARY);
        infoPanel.add(infoLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        this.getContentPane().removeAll();
        this.getContentPane().add(mainPanel);
        this.revalidate();
        this.repaint();
    }
    
/**
 * Performs the displayProfile operation.
 */
    public void displayProfile() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JPanel profileCard = new JPanel();
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBackground(CARD_BG);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 3),
            BorderFactory.createEmptyBorder(50, 60, 50, 60)
        ));
        profileCard.setMaximumSize(new Dimension(700, 650));
        
        JPanel headerSection = new JPanel();
        headerSection.setLayout(new BoxLayout(headerSection, BoxLayout.Y_AXIS));
        headerSection.setBackground(CARD_BG);
        headerSection.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("MY PROFILE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerSection.add(titleLabel);
        
        profileCard.add(headerSection);
        profileCard.add(Box.createVerticalStrut(40));
        
        JPanel detailsBox = new JPanel();
        detailsBox.setLayout(new BoxLayout(detailsBox, BoxLayout.Y_AXIS));
        detailsBox.setBackground(CARD_BG);
        detailsBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2, true),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        detailsBox.setMaximumSize(new Dimension(600, 350));
        
        addProfileRow(detailsBox, "Customer ID:", String.valueOf(currentCustomer.getId()));
        addProfileRow(detailsBox, "Full Name:", currentCustomer.getName());
        addProfileRow(detailsBox, "Phone Number:", currentCustomer.getPhone());
        addProfileRow(detailsBox, "Email Address:", currentCustomer.getEmail());
        addProfileRow(detailsBox, "Username:", Session.getInstance().getUsername());
        
        profileCard.add(detailsBox);
        profileCard.add(Box.createVerticalStrut(30));
        
        // Add Dashboard button
        JButton dashboardBtn = new JButton("Go to Dashboard");
        dashboardBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dashboardBtn.setForeground(Color.WHITE);
        dashboardBtn.setBackground(PRIMARY_COLOR);
        dashboardBtn.setFocusPainted(false);
        dashboardBtn.setBorderPainted(false);
        dashboardBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dashboardBtn.setPreferredSize(new Dimension(200, 45));
        dashboardBtn.setMaximumSize(new Dimension(200, 45));
        dashboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        dashboardBtn.addMouseListener(new java.awt.event.MouseAdapter() {
/**
 * Performs the mouseEntered operation.
 * @param evt the evt parameter
 */
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashboardBtn.setBackground(new Color(52, 152, 219));
            }
/**
 * Performs the mouseExited operation.
 * @param evt the evt parameter
 */
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dashboardBtn.setBackground(PRIMARY_COLOR);
            }
        });
        
        dashboardBtn.addActionListener(e -> displayWelcomeDashboard());
        
        profileCard.add(dashboardBtn);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(profileCard, gbc);
        
        this.getContentPane().removeAll();
        this.getContentPane().add(mainPanel);
        this.revalidate();
        this.repaint();
    }
    
/**
 * Performs the addProfileRow operation.
 * @param panel the panel parameter
 * @param label the label parameter
 * @param value the value parameter
 */
    private void addProfileRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout(15, 0));
        rowPanel.setBackground(CARD_BG);
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        rowPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComp.setForeground(TEXT_SECONDARY);
        labelComp.setPreferredSize(new Dimension(180, 30));
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        valueComp.setForeground(TEXT_PRIMARY);
        
        rowPanel.add(labelComp, BorderLayout.WEST);
        rowPanel.add(valueComp, BorderLayout.CENTER);
        
        panel.add(rowPanel);
        panel.add(Box.createVerticalStrut(5));
    }
    
/**
 * Performs the saveData operation.
 */
    public void saveData() {
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving data: " + ex.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
/**
 * Performs the handleLogout operation.
 */
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            saveData();
            Session.getInstance().logout();
            this.dispose();
            
            try {
                FlightBookingSystem newFbs = FlightBookingSystemData.load();
                new WelcomeScreen(newFbs);
            } catch (Exception ex) {
                System.err.println("Error reloading system: " + ex.getMessage());
                System.exit(1);
            }
        }
    }
    
/**
 * Performs the handleExit operation.
 */
    private void handleExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit the system?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            saveData();
            Session.getInstance().logout();
            JOptionPane.showMessageDialog(this, 
                "Data saved successfully. Goodbye!", 
                "Exit", 
                JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
/**
 * Retrieves the flightbookingsystem value.
 * @return the FlightBookingSystem result
 */
    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }
    
/**
 * Retrieves the currentcustomer value.
 * @return the Customer result
 */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
}