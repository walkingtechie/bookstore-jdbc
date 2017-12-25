package com.walking.techie.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static Connection connection = null;

    public static Connection getDBConnection() throws ClassNotFoundException, SQLException {

        // Fully qualified name of Driver class
        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore?useSSL=false", "root", "santosh");
        return connection;
    }
}