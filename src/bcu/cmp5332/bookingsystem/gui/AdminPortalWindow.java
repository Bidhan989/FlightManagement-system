package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * GUI window class for AdminPortal.
 * Provides user interface components and handles user interactions.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class AdminPortalWindow extends JFrame implements ActionListener {

/**
 * The fbs field.
 */
    private FlightBookingSystem fbs;
/**
 * The mainWindow field.
 */
    private MainWindow mainWindow;

/**
 * The menuBar field.
 */
    private JMenuBar menuBar;
/**
 * The systemMenu field.
 */
    private JMenu flightsMenu, customersMenu, bookingsMenu, reportsMenu, systemMenu;
/**
 * The flightsSearch field.
 */
    private JMenuItem flightsView, flightsAdd, flightsDelete, flightsSearch;
/**
 * The customersDelete field.
 */
    private JMenuItem customersView, customersAdd, customersDelete;
/**
 * The bookingsUpdate field.
 */
    private JMenuItem bookingsView, bookingsCancel, bookingsUpdate;
/**
 * The reportsSearch field.
 */
    private JMenuItem reportsDashboard, reportsSearch;
/**
 * The systemExit field.
 */
    private JMenuItem systemLogout, systemExit;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BG_COLOR = new Color(236, 240, 241);
/**
 * The CARD_BG field.
 */
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(44, 62, 80);
    private static final Color TEXT_LIGHT = new Color(127, 140, 141);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);

/**
 * Performs the AdminPortalWindow operation.
 * @param fbs the fbs parameter
 * @return the public result
 */
    public AdminPortalWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        this.mainWindow = new MainWindow(fbs);
        this.mainWindow.setVisible(false);
        initialize();
    }

/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("Admin Portal - Flight Booking System");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

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

        createMenuBar();
        displayWelcomeDashboard();
        setVisible(true);
    }

/**
 * Performs the createMenuBar operation.
 */
    private void createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBackground(TEXT_DARK);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PRIMARY_COLOR));
        menuBar.setPreferredSize(new Dimension(0, 50));
        menuBar.setOpaque(true);

        // Flights Menu
        flightsMenu = createStyledMenu("Flights");
        flightsView = createStyledMenuItem("View All Flights", "✈");
        flightsAdd = createStyledMenuItem("Add Flight", "+");
        flightsDelete = createStyledMenuItem("Delete Flight", "×");
        flightsSearch = createStyledMenuItem("Search Flights", "🔍");
        flightsMenu.add(flightsView);
        flightsMenu.add(flightsAdd);
        flightsMenu.add(flightsDelete);
        flightsMenu.addSeparator();
        flightsMenu.add(flightsSearch);
        menuBar.add(flightsMenu);

        // Customers Menu
        customersMenu = createStyledMenu("Customers");
        customersView = createStyledMenuItem("View All Customers", "👥");
        customersAdd = createStyledMenuItem("Add Customer", "+");
        customersDelete = createStyledMenuItem("Delete Customer", "×");
        customersMenu.add(customersView);
        customersMenu.add(customersAdd);
        customersMenu.add(customersDelete);
        menuBar.add(customersMenu);

        // Bookings Menu
        bookingsMenu = createStyledMenu("Bookings");
        bookingsView = createStyledMenuItem("View All Bookings", "📋");
        bookingsCancel = createStyledMenuItem("Cancel Booking", "×");
        bookingsUpdate = createStyledMenuItem("Update Booking", "✎");
        bookingsMenu.add(bookingsView);
        bookingsMenu.add(bookingsCancel);
        bookingsMenu.add(bookingsUpdate);
        menuBar.add(bookingsMenu);

        // Reports Menu
        reportsMenu = createStyledMenu("Reports");
        reportsDashboard = createStyledMenuItem("Dashboard", "📊");
        reportsSearch = createStyledMenuItem("Search Flights", "🔍");
        reportsMenu.add(reportsDashboard);
        reportsMenu.add(reportsSearch);
        menuBar.add(reportsMenu);

        // System Menu
        systemMenu = createStyledMenu("System");
        systemLogout = createStyledMenuItem("Logout", "🚪");
        systemExit = createStyledMenuItem("Exit", "×");
        systemMenu.add(systemLogout);
        systemMenu.addSeparator();
        systemMenu.add(systemExit);
        menuBar.add(systemMenu);

        setJMenuBar(menuBar);
    }

/**
 * Performs the createStyledMenu operation.
 * @param text the text parameter
 * @return the JMenu result
 */
    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(" " + text + " ");
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menu.setOpaque(true);
        menu.setBackground(TEXT_DARK);
        return menu;
    }

/**
 * Performs the createStyledMenuItem operation.
 * @param text the text parameter
 * @param icon the icon parameter
 * @return the JMenuItem result
 */
    private JMenuItem createStyledMenuItem(String text, String icon) {
        JMenuItem item = new JMenuItem(icon + "  " + text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBackground(Color.WHITE);
        item.setForeground(TEXT_DARK);
        item.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        item.addActionListener(this);
        return item;
    }

    @Override
/**
 * Performs the actionPerformed operation.
 * @param e the e parameter
 */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == flightsView) displayAllFlights();
        else if (source == flightsAdd) new AddFlightWindow(mainWindow);
        else if (source == flightsDelete) new DeleteFlightWindow(mainWindow);
        else if (source == flightsSearch) new SearchFlightsWindow(mainWindow);
        else if (source == customersView) displayAllCustomers();
        else if (source == customersAdd) new AddCustomerWindow(mainWindow);
        else if (source == customersDelete) new DeleteCustomerWindow(mainWindow);
        else if (source == bookingsView) displayAllBookings();
        else if (source == bookingsCancel) new CancelBookingWindow(mainWindow);
        else if (source == bookingsUpdate) new UpdateBookingWindow(mainWindow);
        else if (source == reportsDashboard) displayWelcomeDashboard();
        else if (source == reportsSearch) new SearchFlightsWindow(mainWindow);
        else if (source == systemLogout) handleLogout();
        else if (source == systemExit) handleExit();
    }

/**
 * Performs the displayWelcomeDashboard operation.
 */
    private void displayWelcomeDashboard() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setOpaque(true);
        
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setOpaque(false);
        
        JLabel subtitleLabel = new JLabel("Welcome, " + Session.getInstance().getUsername());
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_LIGHT);
        subtitleLabel.setOpaque(false);
        
        JPanel titleBox = new JPanel(new GridLayout(2, 1, 0, 5));
        titleBox.setBackground(BG_COLOR);
        titleBox.add(titleLabel);
        titleBox.add(subtitleLabel);
        
        headerPanel.add(titleBox, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Stats Cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        statsPanel.setBackground(BG_COLOR);

/**
 * Retrieves the flights value.
 */
        int totalFlights = fbs.getFlights().size();
/**
 * Retrieves the customers value.
 */
        int totalCustomers = fbs.getCustomers().size();
        int totalBookings = 0;
        for (Customer c : fbs.getCustomers()) {
            totalBookings += c.getBookings().size();
        }

        statsPanel.add(createStatCard("Total Flights", String.valueOf(totalFlights), PRIMARY_COLOR, "✈"));
        statsPanel.add(createStatCard("Total Customers", String.valueOf(totalCustomers), SUCCESS_COLOR, "👥"));
        statsPanel.add(createStatCard("Total Bookings", String.valueOf(totalBookings), WARNING_COLOR, "📋"));

        mainPanel.add(statsPanel, BorderLayout.CENTER);

        // Quick Actions
        JPanel actionsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        actionsPanel.setBackground(BG_COLOR);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        actionsPanel.add(createActionCard("View Flights", "Manage all flights", PRIMARY_COLOR, e -> displayAllFlights()));
        actionsPanel.add(createActionCard("View Customers", "Customer management", SUCCESS_COLOR, e -> displayAllCustomers()));
        actionsPanel.add(createActionCard("View Bookings", "Booking overview", WARNING_COLOR, e -> displayAllBookings()));
        actionsPanel.add(createActionCard("Add Flight", "Create new flight", PRIMARY_COLOR, e -> new AddFlightWindow(mainWindow)));
        actionsPanel.add(createActionCard("Add Customer", "Register customer", SUCCESS_COLOR, e -> new AddCustomerWindow(mainWindow)));
        actionsPanel.add(createActionCard("Search Flights", "Find flights", PRIMARY_COLOR, e -> new SearchFlightsWindow(mainWindow)));

        mainPanel.add(actionsPanel, BorderLayout.SOUTH);

        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

/**
 * Performs the createStatCard operation.
 * @param title the title parameter
 * @param value the value parameter
 * @param color the color parameter
 * @param icon the icon parameter
 * @return the JPanel result
 */
    private JPanel createStatCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        // Icon panel
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setForeground(color);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        JPanel iconPanel = new JPanel(new GridBagLayout());
        iconPanel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        iconPanel.setPreferredSize(new Dimension(90, 90));
        iconPanel.setBorder(BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 50), 2, true));
        iconPanel.setOpaque(true);
        iconPanel.add(iconLabel);

        // Text panel
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        textPanel.setBackground(CARD_BG);
        textPanel.setOpaque(true);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        valueLabel.setForeground(TEXT_DARK);
        valueLabel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(TEXT_LIGHT);
        titleLabel.setOpaque(false);
        
        textPanel.add(valueLabel);
        textPanel.add(titleLabel);

        card.add(iconPanel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

/**
 * Performs the createActionCard operation.
 * @param title the title parameter
 * @param description the description parameter
 * @param color the color parameter
 * @param action the action parameter
 * @return the JPanel result
 */
    private JPanel createActionCard(String title, String description, Color color, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setOpaque(false);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(TEXT_LIGHT);
        descLabel.setOpaque(false);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(CARD_BG);
        textPanel.setOpaque(true);
        textPanel.add(titleLabel);
        textPanel.add(descLabel);

        JButton actionBtn = new JButton("→");
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        actionBtn.setForeground(color);
        actionBtn.setBackground(CARD_BG);
        actionBtn.setBorderPainted(false);
        actionBtn.setFocusPainted(false);
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionBtn.addActionListener(action);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(actionBtn, BorderLayout.EAST);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
/**
 * Performs the mouseEntered operation.
 * @param evt the evt parameter
 */
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(240, 243, 245));
                textPanel.setBackground(new Color(240, 243, 245));
                actionBtn.setBackground(new Color(240, 243, 245));
            }
/**
 * Performs the mouseExited operation.
 * @param evt the evt parameter
 */
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(CARD_BG);
                textPanel.setBackground(CARD_BG);
                actionBtn.setBackground(CARD_BG);
            }
/**
 * Performs the mouseClicked operation.
 * @param evt the evt parameter
 */
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.actionPerformed(null);
            }
        });

        return card;
    }

/**
 * Performs the displayAllFlights operation.
 */
    public void displayAllFlights() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header with search
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel("✈  All Flights");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_DARK);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(BG_COLOR);
        
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        String[] columns = {"ID", "Flight No", "Origin", "Destination", "Date", "Capacity", "Available", "Price"};
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        for (Flight flight : fbs.getFlights()) {
/**
 * Retrieves the capacity value.
 */
            int available = flight.getCapacity() - flight.getPassengers().size();
            model.addRow(new Object[]{
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureDate().format(formatter),
                flight.getCapacity(),
                available,
                String.format("£%.2f", flight.getPrice())
            });
        }

        JTable table = new JTable(model);
        styleEnhancedTable(table);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
/**
 * Performs the changedUpdate operation.
 * @param e the e parameter
 */
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
/**
 * Performs the removeUpdate operation.
 * @param e the e parameter
 */
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
/**
 * Performs the insertUpdate operation.
 * @param e the e parameter
 */
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            
/**
 * Performs the filter operation.
 */
            public void filter() {
/**
 * Retrieves the text value.
 */
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        infoPanel.setBackground(CARD_BG);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel countLabel = new JLabel("📊 Total: " + fbs.getFlights().size() + " flights");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        countLabel.setForeground(TEXT_DARK);
        infoPanel.add(countLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

/**
 * Performs the displayAllCustomers operation.
 */
    public void displayAllCustomers() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel titleLabel = new JLabel("👥  All Customers");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_DARK);

        String[] columns = {"ID", "Name", "Phone", "Email", "Bookings"};
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

        for (Customer customer : fbs.getCustomers()) {
            model.addRow(new Object[]{
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getBookings().size()
            });
        }

        JTable table = new JTable(model);
        styleEnhancedTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        infoPanel.setBackground(CARD_BG);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel countLabel = new JLabel("📊 Total: " + fbs.getCustomers().size() + " customers");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        countLabel.setForeground(TEXT_DARK);
        infoPanel.add(countLabel);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

/**
 * Performs the displayAllBookings operation.
 */
    public void displayAllBookings() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Header with filters
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel("📋  All Bookings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_DARK);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(BG_COLOR);

        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All Statuses", "Booked", "Rebooked", "Cancelled"});
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusFilter.setPreferredSize(new Dimension(150, 35));

        JTextField searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        filterPanel.add(new JLabel("Filter: "));
        filterPanel.add(statusFilter);
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(new JLabel("Search: "));
        filterPanel.add(searchField);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(filterPanel, BorderLayout.EAST);

        String[] columns = {"Customer ID", "Customer Name", "Flight No", "Route", "Date", "Booked On", "Price", "Status"};
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        int totalBookings = 0;
        int bookedCount = 0, rebookedCount = 0, cancelledCount = 0;

        for (Customer customer : fbs.getCustomers()) {
            for (Booking booking : customer.getBookings()) {
                totalBookings++;
                Flight flight = booking.getFlight();
                
/**
 * Retrieves the status value.
 */
                String statusText = booking.getStatus().toString();
                switch (booking.getStatus()) {
                    case BOOKED: bookedCount++; break;
                    case REBOOKED: rebookedCount++; break;
                    case CANCELLED: cancelledCount++; break;
                }

                model.addRow(new Object[]{
                    customer.getId(),
                    customer.getName(),
                    flight.getFlightNumber(),
                    flight.getOrigin() + " → " + flight.getDestination(),
                    flight.getDepartureDate().format(formatter),
                    booking.getBookingDate().format(formatter),
                    String.format("£%.2f", booking.getBookingPrice()),
                    statusText
                });
            }
        }

        JTable table = new JTable(model);
        styleEnhancedTable(table);

        // Custom renderer for status column
        table.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
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

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Search functionality
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
/**
 * Performs the changedUpdate operation.
 * @param e the e parameter
 */
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
/**
 * Performs the removeUpdate operation.
 * @param e the e parameter
 */
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
/**
 * Performs the insertUpdate operation.
 * @param e the e parameter
 */
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            
/**
 * Performs the filter operation.
 */
            public void filter() {
                applyFilters();
            }
            
/**
 * Performs the applyFilters operation.
 */
            private void applyFilters() {
                java.util.List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();
                
/**
 * Retrieves the text value.
 */
                String searchText = searchField.getText();
                if (searchText.trim().length() > 0) {
                    filters.add(RowFilter.regexFilter("(?i)" + searchText));
                }
                
/**
 * Retrieves the selecteditem value.
 */
                String selectedStatus = (String) statusFilter.getSelectedItem();
                if (!"All Statuses".equals(selectedStatus)) {
                    filters.add(RowFilter.regexFilter(selectedStatus, 7));
                }
                
                if (filters.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.andFilter(filters));
                }
            }
        });

        // Status filter functionality
        statusFilter.addActionListener(e -> {
            java.util.List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();
            
/**
 * Retrieves the text value.
 */
            String searchText = searchField.getText();
            if (searchText.trim().length() > 0) {
                filters.add(RowFilter.regexFilter("(?i)" + searchText));
            }
            
/**
 * Retrieves the selecteditem value.
 */
            String selectedStatus = (String) statusFilter.getSelectedItem();
            if (!"All Statuses".equals(selectedStatus)) {
                filters.add(RowFilter.regexFilter(selectedStatus, 7));
            }
            
            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        // Enhanced info panel with statistics
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        infoPanel.setBackground(CARD_BG);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel totalLabel = new JLabel("📊 Total: " + totalBookings);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(TEXT_DARK);

        JLabel bookedLabel = new JLabel("✓ Booked: " + bookedCount);
        bookedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bookedLabel.setForeground(SUCCESS_COLOR);

        JLabel rebookedLabel = new JLabel("↻ Rebooked: " + rebookedCount);
        rebookedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rebookedLabel.setForeground(WARNING_COLOR);

        JLabel cancelledLabel = new JLabel("× Cancelled: " + cancelledCount);
        cancelledLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelledLabel.setForeground(DANGER_COLOR);

        infoPanel.add(totalLabel);
        infoPanel.add(new JLabel("|"));
        infoPanel.add(bookedLabel);
        infoPanel.add(rebookedLabel);
        infoPanel.add(cancelledLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);

        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

/**
 * Performs the styleEnhancedTable operation.
 * @param table the table parameter
 */
    private void styleEnhancedTable(JTable table) {
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionForeground(Color.WHITE);
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setShowGrid(true);
        table.setGridColor(new Color(240, 240, 240));
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(TEXT_DARK);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 45));

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setForeground(Color.WHITE);
                label.setBackground(TEXT_DARK);
                label.setOpaque(true);
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
                return label;
            }
        });

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(250, 250, 250));
                    }
                    ((JLabel) c).setForeground(TEXT_DARK);
                }
                
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }

/**
 * Performs the handleLogout operation.
 */
    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                FlightBookingSystemData.store(fbs);
                Session.getInstance().logout();
                dispose();
                new WelcomeScreen(fbs);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

/**
 * Performs the handleExit operation.
 */
    private void handleExit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Save changes before exiting?",
            "Confirm Exit",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                FlightBookingSystemData.store(fbs);
                System.exit(0);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (choice == JOptionPane.NO_OPTION) {
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
 * Performs the displayUpdatedBookings operation.
 */
    public void displayUpdatedBookings() {
        displayAllBookings();
    }
}
