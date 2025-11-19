package utilities.DB_utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB_Utils {
    public static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    // Veritabanına bağlan
    public static void createConnection(String url, String username, String password) {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }

    // Sorgu çalıştır
    public static void executeQuery(String query) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException("Query execution failed: " + e.getMessage());
        }
    }

    // Tek bir değer almak için (örneğin COUNT, SUM)
    public static Object getCellValue(String query) {
        Object value = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                value = resultSet.getObject(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("getCellValue error: " + e.getMessage());
        }
        return value;
    }

    // Tüm verileri liste olarak almak
    public static List<List<Object>> getQueryResultList(String query) {
        List<List<Object>> resultList = new ArrayList<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getObject(i));
                }
                resultList.add(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Query result error: " + e.getMessage());
        }
        return resultList;
    }

    // Bağlantıyı kapat
    public static void closeConnection() {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            System.out.println("🔒 Database connection closed.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close DB connection: " + e.getMessage());
        }
    }

}
