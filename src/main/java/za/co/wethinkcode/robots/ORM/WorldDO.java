package za.co.wethinkcode.robots.ORM;

import net.lemnik.eodsql.ResultColumn;

/**
 * Data Object (DO) representing a row in the 'worlds' table.
 * This class is used by eodsql to map database columns to Java fields.
 */
public class WorldDO{

   /** Primary key for the world. */
   @ResultColumn("id")
   public int id;

   /** Name of the world (e.g., "defaultWorld"). */
   @ResultColumn("name")
   public String name;

   /** Width dimension of the world grid. */
   @ResultColumn("width")
   public int width;

   /** Height dimension of the world grid. */
   @ResultColumn("height")
   public int height;

   /** Default constructor required by eodsql */
   public WorldDO(){}


}
