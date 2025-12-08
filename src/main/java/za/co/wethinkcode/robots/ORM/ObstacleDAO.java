package za.co.wethinkcode.robots.ORM;

import java.util.List;
import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;

/**
 * Data Access Interface (DAO) specifically for interacting with the 'obstacles' table.
 * It provides CRUD (Create, Read, Update, Delete) operations for ObstacleDO objects.
 */
public interface ObstacleDAO extends BaseQuery {

   /**
    * Retrieves an obstacle by its primary key ID.
    *
    * @param id The ID of the obstacle to retrieve.
    * @return An ObstacleDO object.
    */
   @Select("SELECT * FROM obstacles WHERE id = ?{1}")
   ObstacleDO getObstacleById(int id);

   /**
    * Retrieves a list of all obstacles associated with a specific world ID.
    *
    * @param worldId The ID of the world whose obstacles are to be retrieved.
    * @return A list of ObstacleDO objects.
    */
   @Select("SELECT * FROM obstacles WHERE world_id = ?{1}")
   List<ObstacleDO> getObstacleForWorld(int worldId);

   /**
    * Retrieves a list of all obstacles in the database.
    *
    * @return A list of all ObstacleDO objects.
    */
   @Select("SELECT * FROM obstacles")
   List<ObstacleDO> getAllObstacles();


   /**
    * Inserts a new obstacle record.
    *
    * @param id The primary key ID.
    * @param x The X-coordinate.
    * @param y The Y-coordinate.
    * @param size The size of the obstacle.
    * @param worldId The foreign key referencing the world.
    */
   @Update("INSERT INTO obstacles(id, x, y, size, world_id) VALUES(?{1}, ?{2}, ?{3}, ?{4}, ?{5})")
   void insertObstacle(int id, int x, int y, int size, int worldId);


   /**
    * Deletes an obstacle by its primary key ID.
    *
    * @param id The ID of the obstacle to delete.
    */
   @Update("DELETE FROM obstacles WHERE id = ?{1}")
   void deleteObstacle(int id);



}
