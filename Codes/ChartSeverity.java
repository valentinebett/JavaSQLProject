package org.example;

import org.jfree.chart.*;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Class: ChartSeverity
 * Purpose: Generates a pie chart showing the distribution of diagnoses by severity.
 *          Also exports the chart as a PDF with a descriptive summary.
 */
public class ChartSeverity {

    /**
     * Creates and displays the pie chart
     */
    public static void showChart() {
        try {
            // Create dataset for the pie chart
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

            // Fetch severity counts from the database
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT severity, COUNT(*) AS count FROM diagnoses GROUP BY severity;")) {

                // Add each severity category and its count to the dataset
                while (rs.next()) {
                    dataset.setValue(rs.getString("severity"), rs.getInt("count"));
                }
            }

            // Create the pie chart
            JFreeChart chart = ChartFactory.createPieChart(
                    "Severity Distribution of Diagnoses", // Chart title
                    dataset,                              // Dataset
                    true,                                  // Include legend
                    true,                                  // Tooltips
                    false                                  // URLs? No
            );

            // Display chart in a JFrame
            displayChart(chart, "Diagnosis Severity Distribution");

            // Export chart as PDF with description
            ChartPDFExporter.exportToPDF(chart, "SeverityDistribution.pdf",
                    "This pie chart shows the distribution of diagnoses by severity (Mild, Moderate, Severe). " +
                            "It helps visualize how serious most hospital cases tend to be overall.");

        } catch (Exception e) {
            // Print exceptions for debugging
            e.printStackTrace();
        }
    }

    /**
     * Displays a given JFreeChart in a JFrame
     * @param chart the chart to display
     * @param title title of the window
     */
    private static void displayChart(JFreeChart chart, String title) {
        JFrame frame = new JFrame(title);                    // Create window
        frame.setSize(800, 600);                             // Set size
        frame.add(new ChartPanel(chart), BorderLayout.CENTER); // Add chart panel
        frame.setVisible(true);                               // Show window
    }
}
