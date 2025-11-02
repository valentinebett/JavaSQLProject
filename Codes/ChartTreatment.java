package org.example;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Class: ChartTreatment
 * Purpose: Generates a bar chart showing the top 10 most prescribed medications.
 *          Also exports the chart as a PDF with a descriptive summary.
 */
public class ChartTreatment {

    /**
     * Creates and displays the bar chart
     */
    public static void showChart() {
        try {
            // Create dataset for the bar chart
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // Fetch medication counts from the database
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT medication, COUNT(*) AS count " +
                                 "FROM treatment " +
                                 "GROUP BY medication " +
                                 "ORDER BY count DESC " +
                                 "LIMIT 10;")) {

                // Add each medication and its count to the dataset
                while (rs.next()) {
                    dataset.addValue(rs.getInt("count"), "Medications", rs.getString("medication"));
                }
            }

            // Create the bar chart
            JFreeChart chart = ChartFactory.createBarChart(
                    "Top 10 Most Prescribed Medications", // Chart title
                    "Medication",                         // X-axis label
                    "Number of Prescriptions",            // Y-axis label
                    dataset,                              // Dataset
                    PlotOrientation.VERTICAL,             // Orientation
                    false,                                // Legend? No
                    true,                                 // Tooltips
                    false                                 // URLs? No
            );

            // Display chart in a JFrame
            displayChart(chart, "Most Prescribed Medications");

            // Export chart as PDF with description
            ChartPDFExporter.exportToPDF(chart, "TopMedications.pdf",
                    "This bar chart shows the medications most commonly prescribed in the hospital. " +
                            "It helps identify treatment trends and inventory priorities.");

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
        JFrame frame = new JFrame(title);                     // Create window
        frame.setSize(800, 600);                              // Set size
        frame.add(new ChartPanel(chart), BorderLayout.CENTER); // Add chart panel
        frame.setVisible(true);                                // Show window
    }
}
