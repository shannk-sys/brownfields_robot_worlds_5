//package za.co.wethinkcode.robots.ORM;
//
//import net.lemnik.eodsql.QueryTool;
//import za.co.wethinkcode.robots.ORM.WorldDAO;
//import za.co.wethinkcode.robots.ORM.ObstacleDAO;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//
//import java.sql.SQLException;
//
//
///**
// * DbTest is a small command-line tool used to check that we can connect to a SQLite database.
// *
// * By default (without any command-line arguments) it attempts to create a SQLite table in an in-memory database.
// * If it succeeds, we assume that all the working parts we need to use SQLite databases are in place and working.
// *
// * The only command-line argument this app understands is
// *  `-f <filename>`
// *  which tells that application to create the test table in a real (disk-resident) database named by the given
// *  filename. Note that the application _does not delete_ the named file, but leaves it in the filesystem
// *  for later examination if desired.
// */
//public class DbConnect {
//    public static final String IN_MEMORY_DB_URL = "jdbc:sqlite::memory:";
//    public static final String DISK_DB_URL = "jdbc:sqlite:";
//
//    public static void main(String[] args) {
//
//        DbConnect app = new DbConnect();
//    }
//
//    private String dbUrl = "jdbc:sqlite:robotworld.db";
//
//    private DbConnect() {
//
//        try (final Connection connection = DriverManager.getConnection(dbUrl)) {
//            System.out.println("Connected to database ");
//
//            WorldDAO worldDAO = QueryTool.getQuery(connection, WorldDAO.class);
//            ObstacleDAO obstacleDAO = QueryTool.getQuery(connection, ObstacleDAO.class);
//
//            worldDAO.insertWorld(1,"defaultWorld",5, 5);
//
//            obstacleDAO.insertObstacle(1,1,2,1);
//            obstacleDAO.insertObstacle(2,3,4,2);
//
//            System.out.println("Tables and sample data inserted successfully! ");
//
//
//        } catch (SQLException e) {
//            System.err.println(e.getMessage());
//        }
//    }
//
//}
//
