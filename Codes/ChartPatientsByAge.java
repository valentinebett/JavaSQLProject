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

public class ChartPatientsByAge {

    public static void showChart() {
        try {
            double[] ages = getAges();

            HistogramDataset dataset = new HistogramDataset();
            dataset.addSeries("Age", ages, 8);

            JFreeChart chart = ChartFactory.createHistogram(
                    "Patient Age Distribution",
                    "Age Range",
                    "Number of Patients",
                    dataset,
                    PlotOrientation.VERTICAL,
                    false, true, false
            );

            displayChart(chart, "Patient Age Distribution");
            ChartPDFExporter.exportToPDF(chart, "PatientAgeDistribution.pdf",
                    "This histogram shows how patient ages are distributed. " +
                            "It helps visualize dominant age groups in hospital visits.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double[] getAges() throws Exception {
        List<Double> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT age FROM patients;");
        while (rs.next()) list.add(rs.getDouble("age"));
        conn.close();
        return list.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private static void displayChart(JFreeChart chart, String title) {
        JFrame frame = new JFrame(title);
        frame.setSize(800, 600);
        frame.add(new ChartPanel(chart), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}

