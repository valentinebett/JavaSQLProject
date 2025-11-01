package org.example;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        setTitle("🏥 Hospital Analytics Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));
        setLocationRelativeTo(null);

        // Buttons for each chart
        JButton btnAge = new JButton("👶 Patient Age Distribution (Histogram)");
        JButton btnConditions = new JButton("📊 Top 10 Common Conditions (Bar Chart)");
        JButton btnSeverity = new JButton("🥧 Diagnosis Severity Distribution (Pie Chart)");
        JButton btnTreatment = new JButton("💊 Most Prescribed Medications (Bar Chart)");

        // Add to window
        add(btnAge);
        add(btnConditions);
        add(btnSeverity);
        add(btnTreatment);

        // Actions for each button
        btnAge.addActionListener(e -> ChartPatientsByAge.showChart());
        btnConditions.addActionListener(e -> ChartConditions.showChart());
        btnSeverity.addActionListener(e -> ChartSeverity.showChart());
        btnTreatment.addActionListener(e -> ChartTreatment.showChart());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainDashboard dashboard = new MainDashboard();
            dashboard.setVisible(true);
        });
    }
}
