package za.co.wethinkcode.robots.webapi;

import io.javalin.http.Context;
import io.javalin.http.HttpCode;
import za.co.wethinkcode.robots.domain.World;
// REMOVED: import za.co.wethinkcode.robots.persistence.WorldDaoImpl;

// ADDED IMPORTS for ORM and SQL logic
import net.lemnik.eodsql.QueryTool;
import za.co.wethinkcode.robots.ORM.WorldDataDAO;
import za.co.wethinkcode.robots.ORM.WorldDO;
import za.co.wethinkcode.robots.ORM.ObstacleDO;
import za.co.wethinkcode.robots.domain.Obstacle;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
// END ADDED IMPORTS

import com.fasterxml.jackson.databind.ObjectMapper;

public class RobotWorldApiHandler {
    // Replaced the old persistence class instance with the combined DAO instance
    private static final WorldDataDAO dataDAO = getDao();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String DB_URL = "jdbc:sqlite:robots.db"; // Retaining original DB URL

    /**
     * Helper method to establish a connection and get the DAO instance.
     * NOTE: This logic was moved here from the removed persistence layer.
     */
    private static WorldDataDAO getDao() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            return QueryTool.getQuery(connection, WorldDataDAO.class);
        } catch (SQLException e) {
            // In a production app, you would use a logger here.
            System.err.println("FATAL: Could not connect to database or instantiate WorldDataDAO. " + e.getMessage());
            throw new RuntimeException("Database initialization failed.", e);
        }
    }

    /**
     * Helper method to map the Data Object (DO) to the Domain Object (World),
     * including retrieving and mapping its associated Obstacles.
     */
    private static World mapDoToDomain(WorldDO worldDO) {
        if (worldDO == null) return null;

        // 1. Retrieve Obstacle DOs using the combined DAO
        List<ObstacleDO> obstacleDOs = dataDAO.getObstacleForWorld(worldDO.id);

        // 2. Map Obstacle DOs to Obstacle Domain objects
        List<Obstacle> domainObstacles = obstacleDOs.stream()
            // ObstacleDO has 'size'; Obstacle domain object uses 'width' and 'height'.
            .map(odo -> new Obstacle(odo.x, odo.y, odo.size, odo.size))
            .collect(Collectors.toList());

        // 3. Create and return the World domain object
        // NOTE: Assuming World domain object has a suitable constructor.
        return new World(worldDO.name, worldDO.width, worldDO.height, domainObstacles); 
    }

    public static void getCurrentWorld(Context ctx) {
        // The DbConnect setup uses ID 1 for "defaultWorld".
        World world = mapDoToDomain(dataDAO.getWorldById(1));

        if (world == null) {
            ctx.status(HttpCode.NOT_FOUND).result("World not found");
        } else {
            ctx.json(world);
        }
    }

    public static void getNamedWorld(Context ctx) {
        String name = ctx.pathParam("name");
        
        // Find the WorldDO by name (WorldDataDAO does not have a get by name, so we fetch all and filter)
        WorldDO worldDO = dataDAO.getAllWorlds().stream()
            .filter(wdo -> wdo.name.equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);

        World world = mapDoToDomain(worldDO);

        if (world == null) {
            ctx.status(HttpCode.NOT_FOUND).result("World not found");
        } else {
            ctx.json(world);
        }
    }

    public void robotCommand(Context ctx) {
        String robotName = ctx.pathParam("name");
        try {
            Map<String, Object> command = mapper.readValue(ctx.body(), Map.class);
            String cmd = (String) command.get("command");
            Map<String, Object> response;

            switch (cmd) {
                case "launch":
                    // TODO: Call your domain logic to launch a robot
                    response = Map.of(
                        "result", "OK",
                        "data", Map.of("message", "Robot " + robotName + " launched")
                    );
                    ctx.status(200).json(response);
                    break;
                case "look":
                    // TODO: Call your domain logic to perform look
                    response = Map.of(
                        "result", "OK",
                        "data", Map.of("message", "Looked around")
                    );
                    ctx.status(200).json(response);
                    break;
                default:
                    ctx.status(400).json(Map.of("error", "Unknown command"));
            }
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "Invalid request"));
        }
    }

    public static void launchRobot(Context ctx) {
        String robotName = ctx.pathParam("name");
    }
}