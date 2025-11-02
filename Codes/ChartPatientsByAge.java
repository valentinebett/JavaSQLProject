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
 * Purpose: Generates and displays a histogram showing the distribution of patient ages.
 *          Also exports the chart to PDF with a descriptive summary.
 */
public class ChartPatientsByAge {

    /**
     * Main method to create and show the patient age histogram
     */

    public static void showChart() {
        try {
            // Retrieve ages from the database
            double[] ages = getAges();

            // Create a dataset for the histogram
            HistogramDataset dataset = new HistogramDataset();
             // Add a series of data: "Age" is the label, 8 is the number of bins
            dataset.addSeries("Age", ages, 8);

             // Create the histogram chart
            JFreeChart chart = ChartFactory.createHistogram(
                    "Patient Age Distribution", // Chart title
                    "Age Range",                // X-axis label
                    "Number of Patients",       // Y-axis label
                    dataset,
                    PlotOrientation.VERTICAL,   // Orientation of bars
                    false,                      // Include legend? false
                    true,                       // Show tooltips? true
                    false                       // URLs? false
            );

            // Display chart in a JFrame
            displayChart(chart, "Patient Age Distribution");

           // Export chart to PDF with descriptive text
            ChartPDFExporter.exportToPDF(chart, "PatientAgeDistribution.pdf",
                    "This histogram shows how patient ages are distributed. " +
                            "It helps visualize dominant age groups in hospital visits.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects to the database and fetches all patient ages
     * @return array of ages as double[]
     * @throws Exception if database connection or query fails
     */

    private static double[] getAges() throws Exception {
        List<Double> list = new ArrayList<>();// Temporary list to store ages

         // Connect to database
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();

        // Execute query to fetch ages
        ResultSet rs = stmt.executeQuery("SELECT age FROM patients;");

        // Add each age to the list
        while (rs.next()) list.add(rs.getDouble("age"));

        // Close connection
        conn.close();

        // Convert List<Double> to primitive double[] for histogram dataset
        return list.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private static void displayChart(JFreeChart chart, String title) {
        JFrame frame = new JFrame(title);      // Create a new window
        frame.setSize(800, 600);               // Set window size
        frame.add(new ChartPanel(chart), BorderLayout.CENTER); // Add chart to window
        frame.setVisible(true); // Show the window
    }
}

