package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Class: DataImporter
 * Purpose: Imports CSV data into the hospital database tables:
 *          patients, visits, diagnoses, and treatment.
 */
public class DataImporter {

    /**
     * Main method to import all CSV files
     */
    public static void main(String[] args) {
        importPatients("patients.csv");         // Import patient data
        importVisits("visits.csv");             // Import visits data
        importDiagnoses("diagnoses.csv");       // Import diagnoses data
        importTreatment("treatments.csv");      // Import treatments data
    }

    /**
     * Imports patient data
     */
    public static void importPatients(String fileName) {
        String sql = "INSERT INTO patients (patient_id, first_name, last_name, age, gender, city) VALUES (?, ?, ?, ?, ?, ?)";
        importData(fileName, sql, 6); // 6 columns in patients table
    }

    /**
     * Imports visit data
     */
    public static void importVisits(String fileName) {
        String sql = "INSERT INTO visits (visit_id, patient_id, visit_date, doctor, reason) VALUES (?, ?, ?, ?, ?)";
        importData(fileName, sql, 5); // 5 columns in visits table
    }

    /**
     * Imports diagnoses data
     */
    public static void importDiagnoses(String fileName) {
        String sql = "INSERT INTO diagnoses (diagnosis_id, visit_id, condition_name, severity) VALUES (?, ?, ?, ?)";
        importData(fileName, sql, 4); // 4 columns in diagnoses table
    }

    /**
     * Imports treatment data
     */
    public static void importTreatment(String fileName) {
        String sql = "INSERT INTO treatment (treatment_id, diagnosis_id, medication, dosage, duration) VALUES (?, ?, ?, ?, ?)";
        importData(fileName, sql, 5); // 5 columns in treatment table
    }

    /**
     * Universal CSV importer
     * @param fileName CSV file to import
     * @param sql SQL insert statement
     * @param columns Number of columns expected
     */
    private static void importData(String fileName, String sql, int columns) {
        try {
            // Load the CSV file from the resources folder
            URL resource = DataImporter.class.getClassLoader().getResource(fileName);
            if (resource == null) {
                System.out.println("❌ File not found in resources: " + fileName);
                return;
            }

            File file = new File(resource.toURI());
            BufferedReader br = new BufferedReader(new FileReader(file));

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                String line;
                br.readLine(); // skip header row
                int count = 0;

                while ((line = br.readLine()) != null) {
                    // Split line by tab first; if not enough columns, try comma
                    String[] data = line.split("\t");
                    if (data.length < columns) {
                        data = line.split(",");
                    }

                    // Set each column value in PreparedStatement
                    for (int i = 0; i < columns; i++) {
                        pstmt.setString(i + 1, data[i]);
                    }

                    pstmt.addBatch(); // add to batch for efficiency
                    count++;

                    // Execute batch every 100 records
                    if (count % 100 == 0) pstmt.executeBatch();
                }

                pstmt.executeBatch(); // execute remaining records
                System.out.println("✅ " + count + " records imported for file: " + fileName);

            }

        } catch (Exception e) {
            System.out.println("❌ Error importing " + fileName);
            e.printStackTrace();
        }
    }
}
