package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * GUI window for viewing all customers in the system.
 */
public class ViewCustomersWindow extends JFrame {
    
    private MainWindow mw;
/**
 * The customersTable field.
 */
    private JTable customersTable;
    
/**
 * Constructs a new instance of this class.
 * @param mw the mw to set
 */
    public ViewCustomersWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }
    
/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("Customers List");
        setSize(950, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("All Customers");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        headerPanel.add(titleLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create table columns
        String[] columns = new String[]{"ID", "Name", "Phone", "Email", "Bookings"};
        
        // Create table data
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
        
        // Populate table with customer data
        for (Customer customer : mw.getFlightBookingSystem().getCustomers()) {
            Object[] row = new Object[5];
            row[0] = customer.getId();
            row[1] = customer.getName();
            row[2] = customer.getPhone();
            row[3] = customer.getEmail();
            row[4] = customer.getBookings().size();
            model.addRow(row);
        }
        
        // Create table with styling
        customersTable = new JTable(model);
        customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customersTable.setFont(new Font("Dialog", Font.PLAIN, 12));
        customersTable.setRowHeight(40);
        customersTable.setShowGrid(true);
        customersTable.setGridColor(Color.LIGHT_GRAY);
        
        // Style table header
        JTableHeader header = customersTable.getTableHeader();
        header.setFont(new Font("Dialog", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        
        // Set column widths
        customersTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        customersTable.getColumnModel().getColumn(1).setPreferredWidth(220);
        customersTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        customersTable.getColumnModel().getColumn(3).setPreferredWidth(280);
        customersTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Apply alternating row colors
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        };
        
        for (int i = 0; i < customersTable.getColumnCount(); i++) {
            customersTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        // Add double-click listener
        customersTable.addMouseListener(new MouseAdapter() {
            @Override
/**
 * Performs the mouseClicked operation.
 * @param e the e parameter
 */
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
/**
 * Retrieves the selectedrow value.
 */
                    int selectedRow = customersTable.getSelectedRow();
                    if (selectedRow != -1) {
/**
 * Retrieves the valueat value.
 */
                        int customerId = (int) customersTable.getValueAt(selectedRow, 0);
                        showCustomerDetails(customerId);
                    }
                }
            }
        });
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(customersTable);
        
        // Create info label panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel infoLabel = new JLabel("Double-click on a customer to view detailed information");
        infoLabel.setFont(new Font("Dialog", Font.ITALIC, 11));
        infoPanel.add(infoLabel);
        
        JLabel countLabel = new JLabel(" | Total: " + mw.getFlightBookingSystem().getCustomers().size() + " customer(s)");
        countLabel.setFont(new Font("Dialog", Font.BOLD, 11));
        infoPanel.add(countLabel);
        
        // Add components to content panel
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(infoPanel, BorderLayout.SOUTH);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        this.getContentPane().add(mainPanel);
        
        setLocationRelativeTo(mw);
        setVisible(true);
    }
    
/**
 * Performs the showCustomerDetails operation.
 * @param customerId the customerId parameter
 */
    private void showCustomerDetails(int customerId) {
        try {
            Customer customer = mw.getFlightBookingSystem().getCustomerByID(customerId);
            
            StringBuilder details = new StringBuilder();
            details.append("CUSTOMER DETAILS\n");
            details.append("=======================================\n\n");
            details.append("Customer ID: ").append(customer.getId()).append("\n");
            details.append("Name: ").append(customer.getName()).append("\n");
            details.append("Phone: ").append(customer.getPhone()).append("\n");
            details.append("Email: ").append(customer.getEmail()).append("\n\n");
            
            details.append("BOOKINGS (").append(customer.getBookings().size()).append("):\n");
            details.append("---------------------------------------\n");
            
            if (customer.getBookings().isEmpty()) {
                details.append("No bookings found.\n");
            } else {
                int count = 1;
                for (Booking booking : customer.getBookings()) {
                    details.append(count++).append(". ");
                    details.append("Flight ").append(booking.getFlight().getFlightNumber());
                    details.append(" - ").append(booking.getFlight().getOrigin());
                    details.append(" -> ").append(booking.getFlight().getDestination());
                    details.append(" (").append(booking.getFlight().getDepartureDate()).append(")");
                    details.append("\n   Booked on: ").append(booking.getBookingDate());
                    details.append("\n\n");
                }
            }
            
            JTextArea textArea = new JTextArea(details.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setCaretPosition(0);
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(this, 
                scrollPane,
                "Customer Details - " + customer.getName(), 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, 
                ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}