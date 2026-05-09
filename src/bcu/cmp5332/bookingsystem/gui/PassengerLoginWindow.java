package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Role;
import bcu.cmp5332.bookingsystem.services.AuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * GUI window class for PassengerLogin.
 * Provides user interface components and handles user interactions.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class PassengerLoginWindow extends JFrame implements ActionListener {

/**
 * The fbs field.
 */
    private FlightBookingSystem fbs;

/**
 * The usernameField field.
 */
    private JTextField usernameField;
/**
 * The passwordField field.
 */
    private JPasswordField passwordField;
/**
 * The showPasswordCheck field.
 */
    private JCheckBox showPasswordCheck;
/**
 * The backButton field.
 */
    private JButton loginButton, backButton;

/**
 * Constructs a new instance of this class.
 * @param fbs the fbs to set
 */
    public PassengerLoginWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("Passenger Login");
        setSize(550, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
/**
 * Performs the windowClosing operation.
 * @param e the e parameter
 */
            public void windowClosing(WindowEvent e) {
                new WelcomeScreen(fbs);
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(GUIUtils.BG_PRIMARY);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(GUIUtils.SUCCESS_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setForeground(GUIUtils.TEXT_WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel headerLabel = new JLabel("Passenger Login");
        headerLabel.setFont(GUIUtils.FONT_HEADER);
        headerLabel.setForeground(GUIUtils.TEXT_WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Welcome back! Please sign in");
        subtitleLabel.setFont(GUIUtils.FONT_BODY);
        subtitleLabel.setForeground(new Color(200, 255, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(headerLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(GUIUtils.BG_PRIMARY);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 70, 40, 70));

        // Username
        JLabel usernameLabel = GUIUtils.createLabel("Username");
        usernameField = GUIUtils.createStyledTextField(20);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Password
        JLabel passwordLabel = GUIUtils.createLabel("Password");
        passwordField = new JPasswordField(20);
        GUIUtils.stylePasswordField(passwordField);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Show password checkbox
        showPasswordCheck = new JCheckBox("Show password");
        showPasswordCheck.setFont(GUIUtils.FONT_BODY);
        showPasswordCheck.setFocusPainted(false);
        showPasswordCheck.setBackground(GUIUtils.BG_PRIMARY);
        showPasswordCheck.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPasswordCheck.addActionListener(e ->
                passwordField.setEchoChar(
                        showPasswordCheck.isSelected() ? (char) 0 : '•'
                )
        );

        // Buttons
        loginButton = GUIUtils.createSuccessButton("Login");
        backButton = GUIUtils.createSecondaryButton("Back");

        loginButton.addActionListener(this);
        backButton.addActionListener(this);
        passwordField.addActionListener(this);

        // Layout
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(25));

        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(15));

        // Show password aligned to left
        showPasswordCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(showPasswordCheck);
        formPanel.add(Box.createVerticalStrut(40));

        // Buttons side by side
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(GUIUtils.BG_PRIMARY);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        loginButton.setPreferredSize(new Dimension(160, 45));
        backButton.setPreferredSize(new Dimension(160, 45));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
        usernameField.requestFocus();
    }

    @Override
/**
 * Performs the actionPerformed operation.
 * @param e the e parameter
 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton || e.getSource() == passwordField) {
            performLogin();
        } else if (e.getSource() == backButton) {
            dispose();
            new WelcomeScreen(fbs);
        }
    }

/**
 * Performs the performLogin operation.
 */
    private void performLogin() {
/**
 * Retrieves the text value.
 */
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            GUIUtils.showInfo(this, "Please enter username and password");
            return;
        }

        try {
            boolean authenticated =
                    AuthenticationService.authenticate(
                            fbs, username, password, Role.PASSENGER
                    );

            if (authenticated) {
                dispose();
                new PassengerPortalWindow(fbs);
            } else {
                GUIUtils.showError(this, "Invalid username or password");
                passwordField.setText("");
                passwordField.requestFocus();
            }

        } catch (Exception ex) {
            GUIUtils.showError(this, "Login error: " + ex.getMessage());
        }
    }
}