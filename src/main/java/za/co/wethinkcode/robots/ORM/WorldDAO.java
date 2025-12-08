package za.co.wethinkcode.robots.ORM;

//import org.hibernate.query.sqm.spi.BaseSemanticQueryWalker;
import net.lemnik.eodsql.BaseQuery;
import net.lemnik.eodsql.Select;
import net.lemnik.eodsql.Update;
import za.co.wethinkcode.robots.ORM.WorldDO;
import java.util.List;

/**
 * Data Access Interface (DAO) specifically for interacting with the 'worlds' table.
 * It provides CRUD operations for WorldDO objects.
 */
public interface WorldDAO extends BaseQuery {

   /**
    * Retrieves a world by its primary key ID.
    *
    * @param id The ID of the world to retrieve.
    * @return A WorldDO object.
    */
   @Select("SELECT * FROM worlds WHERE id = ?{1}")
   WorldDO getWorldById(int id);

   /**
    * Retrieves a list of all worlds in the database.
    *
    * @return A list of WorldDO objects.
    */
   @Select("SELECT * FROM worlds")
   List<WorldDO> getAllWorlds();

   /**
    * Inserts a new world record.
    *
    * @param id The primary key ID.
    * @param name The name of the world.
    * @param width The width dimension of the world.
    * @param height The height dimension of the world.
    */
   @Update("INSERT INTO worlds(id, name, width, height) VALUES (?{1}, ?{2}, ?{3}, ?{4})")
   void insertWorld(int id, String name, int width, int height);

   /**
    * Deletes a world by its primary key ID.
    *
    * @param id The ID of the world to delete.
    */
   @Update("DELETE FROM worlds WHERE id = ?{1}")
   void deleteWorld(int id);

}
