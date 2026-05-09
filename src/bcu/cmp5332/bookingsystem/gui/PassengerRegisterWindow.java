package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import bcu.cmp5332.bookingsystem.data.UserDataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * GUI window class for PassengerRegister.
 * Provides user interface components and handles user interactions.
 *
 * @author Flight Booking System Team
 * @version 1.0
 */
public class PassengerRegisterWindow extends JFrame implements ActionListener {

/**
 * The fbs field.
 */
    private FlightBookingSystem fbs;

/**
 * The usernameField field.
 */
    private JTextField nameField, phoneField, emailField, usernameField;
/**
 * The confirmPasswordField field.
 */
    private JPasswordField passwordField, confirmPasswordField;
/**
 * The showPasswordCheck field.
 */
    private JCheckBox showPasswordCheck;
/**
 * The backBtn field.
 */
    private JButton registerBtn, backBtn;

/**
 * Constructs a new instance of this class.
 * @param fbs the fbs to set
 */
    public PassengerRegisterWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

/**
 * Performs the initialize operation.
 */
    private void initialize() {
        setTitle("Passenger Registration");
        setSize(500, 600);
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

        /* ================= HEADER ================= */
        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel headerLabel = GUIUtils.createHeadingLabel("Passenger Registration");
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(headerLabel);

        /* ================= FORM ================= */
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Personal Info
        formPanel.add(GUIUtils.createSubheaderLabel("Personal Information"));
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(GUIUtils.createLabel("Full Name"));
        formPanel.add(Box.createVerticalStrut(5));
        nameField = GUIUtils.createStyledTextField(20);
        nameField.setMaximumSize(new Dimension(450, 40));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(GUIUtils.createLabel("Phone Number"));
        formPanel.add(Box.createVerticalStrut(5));
        phoneField = GUIUtils.createStyledTextField(20);
        phoneField.setMaximumSize(new Dimension(450, 40));
        formPanel.add(phoneField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(GUIUtils.createLabel("Email Address"));
        formPanel.add(Box.createVerticalStrut(5));
        emailField = GUIUtils.createStyledTextField(20);
        emailField.setMaximumSize(new Dimension(450, 40));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(25));

        // Account Info
        formPanel.add(GUIUtils.createSubheaderLabel("Account Information"));
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(GUIUtils.createLabel("Username"));
        formPanel.add(Box.createVerticalStrut(5));
        usernameField = GUIUtils.createStyledTextField(20);
        usernameField.setMaximumSize(new Dimension(450, 40));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(GUIUtils.createLabel("Password"));
        formPanel.add(Box.createVerticalStrut(5));
        passwordField = new JPasswordField(20);
        GUIUtils.stylePasswordField(passwordField);
        passwordField.setMaximumSize(new Dimension(450, 40));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(12));

        formPanel.add(GUIUtils.createLabel("Confirm Password"));
        formPanel.add(Box.createVerticalStrut(5));
        confirmPasswordField = new JPasswordField(20);
        GUIUtils.stylePasswordField(confirmPasswordField);
        confirmPasswordField.setMaximumSize(new Dimension(450, 40));
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createVerticalStrut(10));

        // Show Password Checkbox
        showPasswordCheck = new JCheckBox("Show passwords");
        showPasswordCheck.setFont(new Font("Dialog", Font.PLAIN, 12));
        showPasswordCheck.setFocusPainted(false);
        showPasswordCheck.addActionListener(e -> {
            char echo = showPasswordCheck.isSelected() ? (char) 0 : '•';
            passwordField.setEchoChar(echo);
            confirmPasswordField.setEchoChar(echo);
        });
        formPanel.add(showPasswordCheck);
        formPanel.add(Box.createVerticalStrut(25));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        registerBtn = GUIUtils.createStyledButton("Register");
        backBtn = GUIUtils.createSecondaryButton("Back");

        registerBtn.addActionListener(this);
        backBtn.addActionListener(this);

        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);
        formPanel.add(buttonPanel);

        // Wrap form in scroll pane to ensure all content is accessible
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(null);
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formScrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
        nameField.requestFocus();
    }

    @Override
/**
 * Performs the actionPerformed operation.
 * @param e the e parameter
 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerBtn) {
            performRegistration();
        } else if (e.getSource() == backBtn) {
            dispose();
            new WelcomeScreen(fbs);
        }
    }

/**
 * Performs the performRegistration operation.
 */
    private void performRegistration() {
        try {
/**
 * Retrieves the text value.
 */
            String name = nameField.getText().trim();
            if (name.isEmpty()) throw new FlightBookingSystemException("Name is required");

/**
 * Retrieves the text value.
 */
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) throw new FlightBookingSystemException("Phone is required");
            if (!phone.matches("\\d{10,11}"))
                throw new FlightBookingSystemException("Phone must be 10–11 digits");

/**
 * Retrieves the text value.
 */
            String email = emailField.getText().trim();
            if (email.isEmpty()) throw new FlightBookingSystemException("Email is required");
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
                throw new FlightBookingSystemException("Invalid email format");

/**
 * Retrieves the text value.
 */
            String username = usernameField.getText().trim();
            if (username.isEmpty()) throw new FlightBookingSystemException("Username is required");
            if (username.length() < 3)
                throw new FlightBookingSystemException("Username must be at least 3 characters");

            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());

            if (password.isEmpty())
                throw new FlightBookingSystemException("Password is required");
            if (password.length() < 6)
                throw new FlightBookingSystemException("Password must be at least 6 characters");
            if (!password.equals(confirm))
                throw new FlightBookingSystemException("Passwords do not match");

            AddCustomer addCustomer = new AddCustomer(name, phone, email);
            addCustomer.execute(fbs);

/**
 * Retrieves the customers value.
 */
            int customerId = fbs.getCustomers()
                    .get(fbs.getCustomers().size() - 1)
                    .getId();

            // Hash the password before storing
/**
 * Performs the hashPassword operation.
 */
            String hashedPassword = bcu.cmp5332.bookingsystem.util.PasswordUtil.hashPassword(password);
            User newUser = new User(username, hashedPassword, Role.PASSENGER, customerId);
            fbs.addUser(newUser);

            UserDataManager.storeUsers(fbs); 

            GUIUtils.showSuccess(this, "Registration successful! You can now log in.");
            dispose();
            new WelcomeScreen(fbs);

        } catch (FlightBookingSystemException | IOException ex) {
            GUIUtils.showError(this, ex.getMessage());
        }
    }
}
