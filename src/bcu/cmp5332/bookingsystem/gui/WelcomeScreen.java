package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI window class for WelcomeScreen.
 * Provides user interface components and handles user interactions.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class WelcomeScreen extends JFrame implements ActionListener {

/**
 * The fbs field.
 */
    private FlightBookingSystem fbs;
/**
 * The exitBtn field.
 */
    private JButton adminLoginBtn, passengerLoginBtn, registerBtn, exitBtn;

/**
 * Constructs a new instance of this class.
 * @param fbs the fbs to set
 */
    public WelcomeScreen(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("Flight Booking System");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(GUIUtils.BG_PRIMARY);

        /* ================= HEADER ================= */
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(GUIUtils.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // Flight icon (using Unicode)
        JLabel iconLabel = new JLabel("✈");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setForeground(GUIUtils.TEXT_WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("FLIGHT BOOKING SYSTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(GUIUtils.TEXT_WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Your Journey Begins Here");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(GUIUtils.PRIMARY_LIGHT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);

        /* ================= CENTER ================= */
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(GUIUtils.BG_PRIMARY);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 80, 40, 80));

        JLabel instructionLabel = new JLabel("Please select an option to continue:");
        instructionLabel.setFont(GUIUtils.FONT_BODY);
        instructionLabel.setForeground(GUIUtils.TEXT_SECONDARY);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(instructionLabel);
        centerPanel.add(Box.createVerticalStrut(30));

        adminLoginBtn = GUIUtils.createStyledButton("🔐 Administrator Login");
        passengerLoginBtn = GUIUtils.createSuccessButton("👤 Passenger Login");
        registerBtn = GUIUtils.createWarningButton("📝 Register as Passenger");
        exitBtn = GUIUtils.createSecondaryButton("⨯ Exit Application");

        JButton[] buttons = {
                adminLoginBtn, passengerLoginBtn, registerBtn, exitBtn
        };

        for (JButton btn : buttons) {
            btn.setMaximumSize(new Dimension(400, 50));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(this);
            centerPanel.add(btn);
            centerPanel.add(Box.createVerticalStrut(18));
        }

        /* ================= FOOTER ================= */
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(GUIUtils.BG_PRIMARY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 24, 15, 24));

        JLabel footerLabel = new JLabel("© 2026 Flight Booking System | Secure & Reliable");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(GUIUtils.TEXT_SECONDARY);
        footerPanel.add(footerLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    @Override
/**
 * Performs the actionPerformed operation.
 * @param e the e parameter
 */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == adminLoginBtn) {
            new AdminLoginWindow(fbs);
            dispose();

        } else if (e.getSource() == passengerLoginBtn) {
            new PassengerLoginWindow(fbs);
            dispose();

        } else if (e.getSource() == registerBtn) {
            new PassengerRegisterWindow(fbs);
            dispose();

        } else if (e.getSource() == exitBtn) {
            try {
                FlightBookingSystemData.store(fbs);
                GUIUtils.showInfo(this, "Data saved. Goodbye!");
            } catch (Exception ex) {
                System.err.println("Error saving data: " + ex.getMessage());
            }
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
}
