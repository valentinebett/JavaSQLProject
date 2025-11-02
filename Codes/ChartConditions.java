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
 * Class: ChartConditions
 * Purpose: Generates a bar chart showing the top 10 most common medical conditions
 *          based on hospital diagnoses. Also exports the chart to PDF with description.
 */
public class ChartConditions {

    /**
     * Main method to create and display the chart
     */
    public static void showChart() {
        try {
            // Create dataset for the chart
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            // Connect to the database and fetch top 10 conditions
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT condition_name, COUNT(*) AS count " +
                                 "FROM diagnoses " +
                                 "GROUP BY condition_name " +
                                 "ORDER BY count DESC " +
                                 "LIMIT 10;")) {

                // Loop through result set and add each condition to the dataset
                while (rs.next()) {
                    dataset.addValue(
                            rs.getInt("count"),               // Number of cases
                            "Conditions",                     // Series label
                            rs.getString("condition_name")    // Category (X-axis)
                    );
                }
            }

            // Create the bar chart
            JFreeChart chart = ChartFactory.createBarChart(
                    "Top 10 Most Common Conditions",  // Chart title
                    "Condition",                      // X-axis label
                    "Number of Cases",                // Y-axis label
                    dataset,                          // Dataset
                    PlotOrientation.VERTICAL,         // Chart orientation
                    false,                            // Include legend? No
                    true,                             // Show tooltips? Yes
                    false                             // URLs? No
            );

            // Display the chart in a JFrame window
            displayChart(chart, "Top 10 Common Conditions");

            // Export the chart to a PDF with a description
            ChartPDFExporter.exportToPDF(chart, "TopConditions.pdf",
                    "This bar chart shows the top 10 most frequently diagnosed medical conditions in the hospital. " +
                            "Higher bars represent more frequent conditions, helping the hospital focus on major health issues.");

        } catch (Exception e) {
            // Print stack trace if any exception occurs
            e.printStackTrace();
        }
    }

    /**
     * Displays a JFreeChart in a JFrame window
     * @param chart JFreeChart object to display
     * @param title Window title
     */
    private static void displayChart(JFreeChart chart, String title) {
        JFrame frame = new JFrame(title);             // Create window
        frame.setSize(800, 600);                      // Set window size
        frame.add(new ChartPanel(chart), BorderLayout.CENTER); // Add chart to window
        frame.setVisible(true);                       // Make window visible
    }
}
