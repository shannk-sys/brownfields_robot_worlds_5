//package za.co.wethinkcode.robots.ORM;
//
////import org.hibernate.query.sqm.spi.BaseSemanticQueryWalker;
//import net.lemnik.eodsql.BaseQuery;
//import net.lemnik.eodsql.Select;
//import net.lemnik.eodsql.Update;
//import za.co.wethinkcode.robots.ORM.WorldDO;
//import java.util.List;
//
//public interface WorldDAO extends BaseQuery {
//    @Select("SELECT * FROM worlds WHERE id = ?{1}")
//    WorldDO getWorldById(int id);
//
//    @Select("SELECT * FROM worlds")
//    List<WorldDO> getAllWorlds();
//
//    @Update("INSERT INTO worlds(id, name, width, height) VALUES (?{1}, ?{2}, ?{3}, ?{4})")
//    void insertWorld(int id, String name, int width, int height);
//
//    @Update("DELETE FROM worlds WHERE id = ?{1}")
//    void deleteWorld(int id);
//
//}
