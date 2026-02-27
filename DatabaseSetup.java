import java.sql.*;

/**
 * Database initialization utility for TODO application.
 * Run this before deploying the WAR file to ensure the database exists.
 */
public class DatabaseSetup {
    private static final String DB_URL = "jdbc:sqlite:todo.db";
    private static final String DRIVER = "org.sqlite.JDBC";

    public static void main(String[] args) {
        try {
            // Load SQLite JDBC driver
            Class.forName(DRIVER);
            System.out.println("✓ SQLite JDBC driver loaded");

            // Create connection (creates database if it doesn't exist)
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                System.out.println("✓ Connected to database: todo.db");

                // Create tasks table
                try (Statement stmt = conn.createStatement()) {
                    String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "title TEXT NOT NULL," +
                            "completed BOOLEAN DEFAULT 0)";
                    stmt.execute(sql);
                    System.out.println("✓ Tasks table created successfully");
                }

                // Add sample data (optional - comment out if not needed)
                addSampleData(conn);

                System.out.println("\n✓ Database initialization complete!");
                System.out.println("Database file: todo.db");
                System.out.println("Location: Project root directory");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("✗ SQLite JDBC driver not found!");
            System.err.println("Ensure sqlite-jdbc is in your classpath");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Adds sample tasks to the database
     */
    private static void addSampleData(Connection conn) {
        try {
            // Check if tasks already exist
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM tasks")) {
                rs.next();
                int count = rs.getInt("count");
                if (count > 0) {
                    System.out.println("✓ Database already contains " + count + " task(s)");
                    return;
                }
            }

            // Insert sample tasks
            String[] sampleTasks = {
                    "Learn Java REST APIs",
                    "Build TODO application",
                    "Test SQLite integration",
                    "Create frontend UI"
            };

            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO tasks (title, completed) VALUES (?, ?)")) {
                for (String taskTitle : sampleTasks) {
                    pstmt.setString(1, taskTitle);
                    pstmt.setBoolean(2, false);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                System.out.println("✓ Added " + sampleTasks.length + " sample tasks");
            }
        } catch (SQLException e) {
            System.out.println("✓ Tables exist or sample data already loaded");
        }
    }
}
