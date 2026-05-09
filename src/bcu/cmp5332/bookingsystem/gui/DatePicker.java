package bcu.cmp5332.bookingsystem.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * A custom date picker component for selecting dates with a calendar interface.
 */
public class DatePicker extends JPanel {
    private JTextField dateField;
    private JButton calendarButton;
    private LocalDate selectedDate;
    private LocalDate minDate;
    
    /**
     * Creates a new DatePicker with today's date as minimum.
     */
    public DatePicker() {
        this(LocalDate.now());
    }
    
    /**
     * Creates a new DatePicker with specified minimum date.
     * @param minDate the minimum selectable date
     */
    public DatePicker(LocalDate minDate) {
        this.minDate = minDate;
        this.selectedDate = null;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(5, 0));
        
        // Text field to display selected date
        dateField = new JTextField(10);
        dateField.setEditable(true);
        dateField.setToolTipText("Format: YYYY-MM-DD or click calendar button");
        
        // Calendar button
        calendarButton = new JButton("Cal");
        calendarButton.setToolTipText("Open calendar");
        calendarButton.setFocusPainted(false);
        calendarButton.setPreferredSize(new Dimension(50, 25));
        
        calendarButton.addActionListener(e -> showCalendarDialog());
        
        add(dateField, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);
    }
    
    /**
     * Shows the calendar dialog for date selection.
     */
    private void showCalendarDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Date", true);
        dialog.setLayout(new BorderLayout());
        
        CalendarPanel calendarPanel = new CalendarPanel(minDate);
        dialog.add(calendarPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectButton = new JButton("Select");
        JButton cancelButton = new JButton("Cancel");
        JButton todayButton = new JButton("Today");
        
        selectButton.addActionListener(e -> {
            LocalDate selected = calendarPanel.getSelectedDate();
            if (selected != null) {
                setDate(selected);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Please select a date", 
                    "No Date Selected", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        todayButton.addActionListener(e -> {
            if (!LocalDate.now().isBefore(minDate)) {
                setDate(LocalDate.now());
                dialog.dispose();
            } else {
                setDate(minDate);
                dialog.dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(todayButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    /**
     * Gets the selected date.
     * @return the selected date or null if none selected
     */
    public LocalDate getDate() {
        String text = dateField.getText().trim();
        if (text.isEmpty()) {
            return selectedDate;
        }
        try {
            return LocalDate.parse(text);
        } catch (Exception e) {
            return selectedDate;
        }
    }
    
    /**
     * Sets the date.
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.selectedDate = date;
        if (date != null) {
            dateField.setText(date.toString());
        } else {
            dateField.setText("");
        }
    }
    
    /**
     * Gets the text from the date field.
     * @return the date text
     */
    public String getText() {
        return dateField.getText();
    }
    
    /**
     * Sets the minimum selectable date.
     * @param minDate the minimum date
     */
    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }
    
    /**
     * Inner class for the calendar panel.
     */
    private class CalendarPanel extends JPanel {
        private YearMonth currentMonth;
        private LocalDate selectedDate;
        private LocalDate minDate;
        private JLabel monthLabel;
        private JPanel daysPanel;
        private JButton[][] dayButtons;
        
        public CalendarPanel(LocalDate minDate) {
            this.minDate = minDate;
            this.currentMonth = YearMonth.now();
            this.selectedDate = null;
            initComponents();
        }
        
        private void initComponents() {
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Navigation panel
            JPanel navPanel = new JPanel(new BorderLayout());
            
            JButton prevButton = new JButton("<");
            JButton nextButton = new JButton(">");
            monthLabel = new JLabel("", SwingConstants.CENTER);
            monthLabel.setFont(new Font("Dialog", Font.BOLD, 14));
            
            prevButton.addActionListener(e -> {
                currentMonth = currentMonth.minusMonths(1);
                updateCalendar();
            });
            
            nextButton.addActionListener(e -> {
                currentMonth = currentMonth.plusMonths(1);
                updateCalendar();
            });
            
            navPanel.add(prevButton, BorderLayout.WEST);
            navPanel.add(monthLabel, BorderLayout.CENTER);
            navPanel.add(nextButton, BorderLayout.EAST);
            
            add(navPanel, BorderLayout.NORTH);
            
            // Days of week header
            JPanel headerPanel = new JPanel(new GridLayout(1, 7, 2, 2));
            String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            for (String day : daysOfWeek) {
                JLabel label = new JLabel(day, SwingConstants.CENTER);
                label.setFont(new Font("Dialog", Font.BOLD, 11));
                headerPanel.add(label);
            }
            
            // Main panel to hold both header and days
            JPanel calendarContent = new JPanel(new BorderLayout(2, 2));
            calendarContent.add(headerPanel, BorderLayout.NORTH);
            
            // Days panel
            daysPanel = new JPanel(new GridLayout(6, 7, 2, 2));
            dayButtons = new JButton[6][7];
            
            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 7; col++) {
                    JButton button = new JButton();
                    button.setFocusPainted(false);
                    button.setMargin(new Insets(2, 2, 2, 2));
                    dayButtons[row][col] = button;
                    daysPanel.add(button);
                    
                    final int r = row;
                    final int c = col;
                    button.addActionListener(e -> selectDay(r, c));
                }
            }
            
            calendarContent.add(daysPanel, BorderLayout.CENTER);
            add(calendarContent, BorderLayout.CENTER);
            
            updateCalendar();
        }
        
        private void updateCalendar() {
            monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) 
                             + " " + currentMonth.getYear());
            
            LocalDate firstOfMonth = currentMonth.atDay(1);
            int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;
            int daysInMonth = currentMonth.lengthOfMonth();
            
            int day = 1;
            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 7; col++) {
                    JButton button = dayButtons[row][col];
                    button.setText("");
                    button.setEnabled(false);
                    button.setBackground(null);
                    button.setForeground(Color.BLACK);
                    
                    if (row == 0 && col < firstDayOfWeek) {
                        // Empty cells before month starts
                        continue;
                    }
                    
                    if (day <= daysInMonth) {
                        LocalDate date = currentMonth.atDay(day);
                        button.setText(String.valueOf(day));
                        
                        // Check if date is before minimum date
                        if (date.isBefore(minDate)) {
                            button.setEnabled(false);
                            button.setForeground(Color.LIGHT_GRAY);
                        } else {
                            button.setEnabled(true);
                            
                            // Highlight today
                            if (date.equals(LocalDate.now())) {
                                button.setBackground(new Color(200, 220, 255));
                            }
                            
                            // Highlight selected date
                            if (date.equals(selectedDate)) {
                                button.setBackground(new Color(100, 150, 255));
                                button.setForeground(Color.WHITE);
                            }
                        }
                        
                        day++;
                    }
                }
            }
        }
        
        private void selectDay(int row, int col) {
            JButton button = dayButtons[row][col];
            if (button.isEnabled() && !button.getText().isEmpty()) {
                int day = Integer.parseInt(button.getText());
                selectedDate = currentMonth.atDay(day);
                updateCalendar();
            }
        }
        
        public LocalDate getSelectedDate() {
            return selectedDate;
        }
    }
}
