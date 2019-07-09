package old.fr.pumpmykit.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import old.fr.pumpmykit.MainKit;

public class KitUtils {

	public static String selectTable = MainKit.KITSELECTTABLE;
	public static String randomTable = MainKit.KITRANDOMTABLE;
	public static String buyKitTable = MainKit.BUYTABLE;

	public static List<Integer> getKitUse(EntityPlayer player, String kitname) throws SQLException {

		List<Integer> use = new ArrayList<Integer>();

		MySQL sql = MainKit.getMySQL();

		ResultSet rs = sql.getResult("SELECT * FROM "+buyKitTable+" WHERE (kitname = '" + kitname + "') AND (username = '"+player.getName() +"')");
		if(rs.first()) {
			do {

				if(!rs.getBoolean("used")) {
					use.add(rs.getInt("id"));
				}

			} while (rs.next());
		}

		return use;
	}

	public static Map<String, Integer> getAllKitUse(EntityPlayer player) throws SQLException{

		Map<String, Integer> hm = new HashMap<>();
		MySQL sql = MainKit.getMySQL();

		ResultSet rs = sql.getResult("SELECT * FROM "+buyKitTable+" WHERE (`username` ='"+player.getName()+"')");

		if(rs.first()) {
			do {

				String kitname = rs.getString("kitname");

				if(!rs.getBoolean("used")) {

					if(hm.containsKey(kitname)) {
						int use = hm.get(kitname);
						use++;
						hm.remove(kitname);
						hm.put(kitname, use);

					} else {

						hm.put(kitname, 1);
					}
				}

			} while(rs.next());
		}
		return hm;
	}

	public static void useKit(EntityPlayer player, String kitname) throws SQLException {

		List<Integer> use = getKitUse(player, kitname);

		if(use.size() > 0) {

			MySQL sql = MainKit.getMySQL();
			sql.update("UPDATE `"+buyKitTable+"` SET `used` = 1 WHERE `id` = "+use.get(0));

		}
	}

	public static void add(String buyId, String username, String kitname) throws SQLException {

		MySQL sql = MainKit.getMySQL();
		sql.update("INSERT INTO "+buyKitTable+"(buyId, username, kitname) VALUES ('"
				+buyId
				+"','"
				+username
				+"','"
				+kitname
				+"')");

	}

	public static void selectFirstUse(UUID user_uuid, int use) throws SQLException {

		MySQL sql = MainKit.getMySQL();
		sql.update("INSERT INTO `"+selectTable+"` (`user_uuid`, `kitnum`) VALUES ('"
				+user_uuid
				+"',"
				+use
				+")");
	}

	public static Optional<Integer> getSelectUse(UUID user_uuid) throws SQLException {

		MySQL sql = MainKit.getMySQL();
		ResultSet rs = sql.getResult("SELECT * FROM "+selectTable+" WHERE `user_uuid` = '"+user_uuid+"'");

		if(rs.first()) {

			return Optional.of(rs.getInt("kitnum"));
		} else {

			return Optional.ofNullable(null);
		}
	}

	public static void setSelectUse(UUID user_uuid, int newnum) throws SQLException {

		MySQL sql = MainKit.getMySQL();
		sql.update("UPDATE `"+selectTable+"` SET `kitnum`="+newnum+" WHERE `user_uuid`= '"+user_uuid+"'");
	}

	public static void randomFirstUse(UUID user_uuid, int use) throws SQLException {

		MySQL sql = MainKit.getMySQL();
		sql.update("INSERT INTO `"+randomTable+"` (`user_uuid`, `kitnum`) VALUES ('"
				+user_uuid
				+"',"
				+use
				+")");
	}

	public static Optional<Integer> getRandomUse(UUID user_uuid) throws SQLException {

		MySQL sql = MainKit.getMySQL();
		ResultSet rs = sql.getResult("SELECT * FROM "+randomTable+" WHERE `user_uuid` = '"+user_uuid+"'");

		if(rs.first()) {

			return Optional.of(rs.getInt("kitnum"));
		} else {

			return Optional.ofNullable(null);
		}
	}

	public static void setRandomUse(UUID user_uuid, int newnum) throws SQLException {

		MySQL sql = MainKit.getMySQL();
		sql.update("UPDATE `"+randomTable+"` SET `kitnum`="+newnum+" WHERE `user_uuid`= '"+user_uuid+"'");
	}
}

