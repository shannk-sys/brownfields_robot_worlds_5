package za.co.wethinkcode.robots.ORM;
import net.lemnik.eodsql.ResultColumn;

/**
 * Data Object (DO) representing a row in the 'obstacles' table.
 * This class is used by eodsql to map database columns to Java fields.
 */
public class ObstacleDO {

   /** Primary key for the obstacle. */
   @ResultColumn("id")
   public int id;

   /** Foreign key linking the obstacle to a specific world. */
   @ResultColumn("world_id")
   public int worldId;

   /** X-coordinate of the obstacle's position. */
   @ResultColumn("x")
   public int x;

   /** Y-coordinate of the obstacle's position. */
   @ResultColumn("y")
   public int y;

   // @ResultColumn("width")
   // public int width;

   // @ResultColumn("height")
   // public int height;

   /** The size or dimension of the obstacle. */
   @ResultColumn("size")
   public int size;

   /** Optional: The type of obstacle (currently unused in table definition). */
   @ResultColumn("type")
   public String type;

   /** Default constructor required by eodsql */
   public ObstacleDO(){}

}

