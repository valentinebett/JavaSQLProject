package org.example;

import javax.swing.*;
import java.awt.*;

/**
 * Class: MainDashboard
 * Purpose: Main GUI dashboard for the hospital analytics project.
 *          Provides buttons to display all charts interactively.
 */
public class MainDashboard extends JFrame {

    /**
     * Constructor sets up the main window and buttons
     */
    public MainDashboard() {
        // Set window title
        setTitle("🏥 Hospital Analytics Dashboard");

        // Set window size
        setSize(600, 400);

        // Close the program when window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use GridLayout: 4 rows, 1 column, 10px vertical and horizontal gaps
        setLayout(new GridLayout(4, 1, 10, 10));

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Create buttons for each chart
        JButton btnAge = new JButton("👶 Patient Age Distribution (Histogram)");
        JButton btnConditions = new JButton("📊 Top 10 Common Conditions (Bar Chart)");
        JButton btnSeverity = new JButton("🥧 Diagnosis Severity Distribution (Pie Chart)");
        JButton btnTreatment = new JButton("💊 Most Prescribed Medications (Bar Chart)");

        // Add buttons to the window
        add(btnAge);
        add(btnConditions);
        add(btnSeverity);
        add(btnTreatment);

        // Set actions for each button to display the respective chart
        btnAge.addActionListener(e -> ChartPatientsByAge.showChart());
        btnConditions.addActionListener(e -> ChartConditions.showChart());
        btnSeverity.addActionListener(e -> ChartSeverity.showChart());
        btnTreatment.addActionListener(e -> ChartTreatment.showChart());
    }

    /**
     * Main method to launch the dashboard GUI
     */
    public static void main(String[] args) {
        // Ensure GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainDashboard dashboard = new MainDashboard();
            dashboard.setVisible(true); // Make the dashboard visible
        });
    }
}
