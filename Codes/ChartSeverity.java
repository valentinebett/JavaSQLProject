package org.example;

import org.jfree.chart.*;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChartSeverity {

    public static void showChart() {
        try {
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT severity, COUNT(*) AS count FROM diagnoses GROUP BY severity;")) {

                while (rs.next()) {
                    dataset.setValue(rs.getString("severity"), rs.getInt("count"));
                }
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Severity Distribution of Diagnoses",
                    dataset,
                    true, true, false
            );

            displayChart(chart, "Diagnosis Severity Distribution");
            ChartPDFExporter.exportToPDF(chart, "SeverityDistribution.pdf",
                    "This pie chart shows the distribution of diagnoses by severity (Mild, Moderate, Severe). " +
                            "It helps visualize how serious most hospital cases tend to be overall.");

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
