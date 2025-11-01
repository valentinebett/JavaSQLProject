package org.example;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChartTreatment {

    public static void showChart() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT medication, COUNT(*) AS count " +
                                 "FROM treatment " +
                                 "GROUP BY medication " +
                                 "ORDER BY count DESC " +
                                 "LIMIT 10;")) {

                while (rs.next()) {
                    dataset.addValue(rs.getInt("count"), "Medications", rs.getString("medication"));
                }
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Top 10 Most Prescribed Medications",
                    "Medication",
                    "Number of Prescriptions",
                    dataset,
                    PlotOrientation.VERTICAL,
                    false, true, false
            );

            displayChart(chart, "Most Prescribed Medications");
            ChartPDFExporter.exportToPDF(chart, "TopMedications.pdf",
                    "This bar chart shows the medications most commonly prescribed in the hospital. " +
                            "It helps identify treatment trends and inventory priorities.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayChart(JFreeChart chart, String title) {
        JFrame frame = new JFrame(title);
        frame.setSize(800, 600);
        frame.add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
