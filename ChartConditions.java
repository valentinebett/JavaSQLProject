package org.example;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChartConditions {

    public static void showChart() {
        try {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT condition_name, COUNT(*) AS count " +
                                 "FROM diagnoses " +
                                 "GROUP BY condition_name " +
                                 "ORDER BY count DESC " +
                                 "LIMIT 10;")) {

                while (rs.next()) {
                    dataset.addValue(rs.getInt("count"), "Conditions", rs.getString("condition_name"));
                }
            }

            JFreeChart chart = ChartFactory.createBarChart(
                    "Top 10 Most Common Conditions",
                    "Condition",
                    "Number of Cases",
                    dataset,
                    PlotOrientation.VERTICAL,
                    false, true, false
            );

            displayChart(chart, "Top 10 Common Conditions");
            ChartPDFExporter.exportToPDF(chart, "TopConditions.pdf",
                    "This bar chart shows the top 10 most frequently diagnosed medical conditions in the hospital. " +
                            "Higher bars represent more frequent conditions, helping the hospital focus on major health issues.");

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
