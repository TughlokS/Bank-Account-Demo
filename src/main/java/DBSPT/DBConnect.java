package DBSPT;

import java.sql.*;

public class DBConnect {
    
    private static String jdbc_url = "jdbc:mysql://localhost:3306/";
    private static String username = "root";
    private static String password = "server!port36";





    private static Connection connectToDB(String database) throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection dbConnect = DriverManager.getConnection(jdbc_url, username, password);
        //System.out.println("Connected to the " + database + " database");
        return dbConnect;
    }

    private static void switchToDB(Connection conn,String query) throws ClassNotFoundException, SQLException{
        Statement state = conn.createStatement();
        state.execute(query);
    }

    // returns ResultSet (which can be used to iterate through results, and get access to data)
    public static ResultSet executeResultsQuery(String query,String database) throws ClassNotFoundException, SQLException{
        Connection conn = connectToDB(database);
        switchToDB(conn,"USE " + database);
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery(query);
        return rs;
    }

    // executes sql query, can execute multiple statements separated by ";" and must be in one string
    public static void executeQuery(String query, String database) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Statement state = null;

        try {
            conn = connectToDB(database);
            switchToDB(conn, "USE " + database);
            state = conn.createStatement();
            conn.setAutoCommit(false); // Start transaction

            String[] sqlStatements = query.split(";");

            for (String statement : sqlStatements) {
                if (!statement.trim().isEmpty()) {
                    System.out.println("Executing: " + statement.trim());
                    state.execute(statement.trim());
                }
            }

            conn.commit();
            // System.out.println("All statements executed successfully.");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    System.err.println("Error during rollback: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Error executing query: " + e.getMessage());
            throw e;
        } finally {
            if (state != null) {
                try {
                    state.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

}