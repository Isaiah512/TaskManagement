import java.sql.*;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:tasks.db");
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeDatabase() throws SQLException {
        Statement stmt = connection.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT NOT NULL)";
        stmt.execute(createTableSQL);
    }

    public void addTask(String task) {
        try {
            String insertSQL = "INSERT INTO tasks (description) VALUES (?)";
            PreparedStatement pstmt = connection.prepareStatement(insertSQL);
            pstmt.setString(1, task);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(String oldTask, String newTask) {
        try {
            String updateSQL = "UPDATE tasks SET description = ? WHERE description = ?";
            PreparedStatement pstmt = connection.prepareStatement(updateSQL);
            pstmt.setString(1, newTask);
            pstmt.setString(2, oldTask);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(String task) {
        try {
            String deleteSQL = "DELETE FROM tasks WHERE description = ?";
            PreparedStatement pstmt = connection.prepareStatement(deleteSQL);
            pstmt.setString(1, task);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

