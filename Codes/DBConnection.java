package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_database?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root"; // <-- your MySQL username
    private static final String PASSWORD = "1234"; // <-- your MySQL password

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to the database!");
            return conn;
        } catch (Exception e) {
            System.out.println("❌ Database connection failed!");
            e.printStackTrace();
            return null;
        }
    }

    // 👇 Add this main method
    public static void main(String[] args) {
        getConnection();  // This tests your connection
    }
}
