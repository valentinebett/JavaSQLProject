package org.example;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class: ChartPatientsByAge
 * Purpose: Generates a histogram of patient ages from the database.
 *          Also exports the chart as a PDF with a descriptive summary.
 */
public class ChartPatientsByAge {

    /**
     * Creates and displays the histogram chart
     */
    public static void showChart() {
        try {
            // Retrieve patient ages from the database
            double[] ages = getAges();

            // Create dataset for histogram
            HistogramDataset dataset = new HistogramDataset();
            dataset.addSeries("Age", ages, 8); // 8 bins

            // Create the histogram chart
            JFreeChart chart = ChartFactory.createHistogram(
                    "Patient Age Distribution", // Chart title
                    "Age Range",               // X-axis label
                    "Number of Patients",      // Y-axis label
                    dataset,                   // Dataset
                    PlotOrientation.VERTICAL,  // Orientation
                    false,                     // Legend? No
                    true,                      // Tooltips? Yes
                    false                      // URLs? No
            );

            // Display chart in a JFrame
            displayChart(chart, "Patient Age Distribution");

            // Export chart as PDF with description
            ChartPDFExporter.exportToPDF(chart, "PatientAgeDistribution.pdf",
                    "This histogram shows how patient ages are distributed. " +
                            "It helps visualize dominant age groups in hospital visits.");

        } catch (Exception e) {
            // Print any exceptions for debugging
            e.printStackTrace();
        }
    }

    /**
     * Fetches all patient ages from the database
     * @return array of ages as doubles
     * @throws Exception if query fails
     */
    private static double[] getAges() throws Exception {
        List<Double> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();  // Connect to DB
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT age FROM patients;"); // Query ages

        // Add each age to the list
        while (rs.next()) list.add(rs.getDouble("age"));

        // Close connection
        conn.close();

        // Convert List<Double> to double[] for histogram
        return list.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * Displays the given JFreeChart in a JFrame
     * @param chart chart to display
     * @param title title of the JFrame
     */
    private static void displayChart(JFreeChart chart, String title) {
        JFrame frame = new JFrame(title);                    // Create window
        frame.setSize(800, 600);                             // Set size
        frame.add(new ChartPanel(chart), BorderLayout.CENTER); // Add chart panel
        frame.setVisible(true);                               // Show window
    }
}
