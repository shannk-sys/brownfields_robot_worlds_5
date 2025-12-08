package za.co.wethinkcode.robots.ORM;

import net.lemnik.eodsql.QueryTool;
import za.co.wethinkcode.robots.ORM.WorldDataDAO;
import za.co.wethinkcode.robots.ORM.WorldDAO;
import za.co.wethinkcode.robots.ORM.ObstacleDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;


/**
* DbTest is a small command-line tool used to check that we can connect to a SQLite database.
*
* By default (without any command-line arguments) it attempts to create a SQLite table in an in-memory database.
* If it succeeds, we assume that all the working parts we need to use SQLite databases are in place and working.
*
* The only command-line argument this app understands is
*  `-f <filename>`
*  which tells that application to create the test table in a real (disk-resident) database named by the given
*  filename. Note that the application _does not delete_ the named file, but leaves it in the filesystem
*  for later examination if desired.
*/

/**
* DbConnect is a utility class used to establish a connection to a SQLite database,
* create the necessary tables ('worlds' and 'obstacles'), and insert initial sample data.
*/
public class DbConnect {
    // URL for an in-memory SQLite database (non-persistent)
   public static final String IN_MEMORY_DB_URL = "jdbc:sqlite::memory:";
   // Prefix for a disk-resident SQLite database
   public static final String DISK_DB_URL = "jdbc:sqlite:";

   /**
    * Main method, primarily instantiates DbConnect to run the database setup logic.
    *
    * @param args Command-line arguments (not explicitly processed in this version).
    */
   public static void main(String[] args) {

       DbConnect app = new DbConnect();
   }
   // The default URL for the persistent database file
   private String dbUrl = "jdbc:sqlite:robotworld.db";

   /**
    * Private constructor handles the main database setup sequence: 
    * 1. Connects to the database.
    * 2. Calls createTables to ensure schema exists.
    * 3. Uses WorldDataDAO to insert sample data.
    */
   private DbConnect() {
        // Use try-with-resources to ensure the Connection is closed
       try (final Connection connection = DriverManager.getConnection(dbUrl)) {
           System.out.println("Connected to database ");

           // Create the 'worlds' and 'obstacles' tables if they don't exist
           createTables(connection);

        //    WorldDAO worldDAO = QueryTool.getQuery(connection, WorldDAO.class);
        //    ObstacleDAO obstacleDAO = QueryTool.getQuery(connection, ObstacleDAO.class);
            
            // Get the consolidated DAO interface using eodsql's QueryTool
            WorldDataDAO dataDAO = QueryTool.getQuery(connection, WorldDataDAO.class);

            // Insert sample world data
            dataDAO.insertWorld(1,"defaultWorld",5, 5);

            // Insert sample obstacle data associated with world ID 1
            dataDAO.insertObstacle(1,1,2,1,1);
            System.out.println("Obstacle 1 inserted!");
            dataDAO.insertObstacle(2,3,4,2,1);
                System.out.println("Obstacle 2 inserted!");
           
                System.out.println("Tables and sample data inserted successfully! ");

       } catch (SQLException e) {
           System.err.println(e.getMessage());
       }
   }

    /**
    * Creates the 'worlds' and 'obstacles' tables in the database if they do not already exist.
    *
    * @param connection The active database connection.
    * @throws SQLException If a database access error occurs.
    */
    private void createTables(Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
           // Create worlds table
            String createWorldsTable = """
                CREATE TABLE IF NOT EXISTS worlds (
                    id INTEGER PRIMARY KEY,
                    name TEXT NOT NULL,
                    width INTEGER NOT NULL,
                    height INTEGER NOT NULL
                )
                """;
            stmt.execute(createWorldsTable);

           // Create obstacles table
            String createObstaclesTable = """
                CREATE TABLE IF NOT EXISTS obstacles (
                    id INTEGER PRIMARY KEY,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    size INTEGER NOT NULL,
                    world_id INTEGER NOT NULL,
                    FOREIGN KEY (world_id) REFERENCES worlds(id)
                )
                """;
            stmt.execute(createObstaclesTable);

            System.out.println("Tables created successfully!");
        }
    }
}

