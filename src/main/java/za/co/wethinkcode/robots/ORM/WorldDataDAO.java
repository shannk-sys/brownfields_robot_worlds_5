package za.co.wethinkcode.robots.ORM;

import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;
import java.util.List;

/**
 * Data Access Interface (DAO) consolidating operations for both World and Obstacle data.
 * This combines the functionality of WorldDAO and ObstacleDAO into a single interface
 * using the eodsql library for mapping Java methods to SQL queries.
 */
public interface WorldDataDAO extends BaseQuery {

    // ------------------------------------
    //  WORLD Operations (from WorldDAO)
    // ------------------------------------

/**
     * Inserts a new world record into the 'worlds' table.
     * The SQL statement uses placeholder ?{n} to map method arguments.
     *
     * @param id The primary key ID of the world.
     * @param name The name of the world.
     * @param width The width of the world grid.
     * @param height The height of the world grid.
     */
    @Update("INSERT INTO worlds(id, name, width, height) VALUES (?{1}, ?{2}, ?{3}, ?{4})")
    void insertWorld(int id, String name, int width, int height);

    /**
     * Retrieves a single world record by its primary key ID.
     *
     * @param id The ID of the world to retrieve.
     * @return A WorldDO object representing the world, or null if not found.
     */
    @Select("SELECT * FROM worlds WHERE id = ?{1}")
    WorldDO getWorldById(int id);

    /**
     * Retrieves a list of all world records from the database.
     *
     * @return A List of WorldDO objects.
     */
    @Select("SELECT * FROM worlds")
    List<WorldDO> getAllWorlds();

    /**
     * Deletes a world record by its primary key ID.
     *
     * @param id The ID of the world to delete.
     */
    @Update("DELETE FROM worlds WHERE id = ?{1}")
    void deleteWorld(int id);

    // ------------------------------------
    //  OBSTACLE Operations (from ObstacleDAO)
    // ------------------------------------

    /**
     * Inserts a new obstacle record.
     * FIX: The SQL is corrected to use ?{5} for world_id.
     */
    @Update("INSERT INTO obstacles(id, x, y, size, world_id) VALUES(?{1}, ?{2}, ?{3}, ?{4}, ?{5})")
    void insertObstacle(int id, int x, int y, int size, int worldId);

    /**
     * Retrieves an obstacle by its primary key ID.
     *
     * @param id The ID of the obstacle to retrieve.
     * @return An ObstacleDO object representing the obstacle, or null if not found.
     */
    @Select("SELECT * FROM obstacles WHERE id = ?{1}")
    ObstacleDO getObstacleById(int id);

    /**
     * Retrieves a list of all obstacles associated with a specific world ID (foreign key lookup).
     *
     * @param worldId The ID of the world whose obstacles are to be retrieved.
     * @return A List of ObstacleDO objects.
     */
    @Select("SELECT * FROM obstacles WHERE world_id = ?{1}")
    List<ObstacleDO> getObstacleForWorld(int worldId);

    /**
     * Retrieves a list of all obstacle records in the database.
     *
     * @return A List of ObstacleDO objects.
     */
    @Select("SELECT * FROM obstacles")
    List<ObstacleDO> getAllObstacles();

    /**
     * Deletes an obstacle record by its primary key ID.
     *
     * @param id The ID of the obstacle to delete.
     */
    @Update("DELETE FROM obstacles WHERE id = ?{1}")
    void deleteObstacle(int id);
}