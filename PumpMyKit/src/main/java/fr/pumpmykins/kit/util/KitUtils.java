package fr.pumpmykins.kit.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fr.pumpmykins.kit.MainKit;
import net.minecraft.entity.player.EntityPlayer;

public class KitUtils {

	public static MySQL sql = MainKit.getMySQL();
	public static String tableName = "PmkKitTable";
	
	public static int getKitUse(EntityPlayer player, String kitname) throws SQLException {
		
		int use = 0;
		
		ResultSet rs = sql.getResult("SELECT * FROM "+tableName+" WHERE (kitname = '" + kitname + "') AND (username = '"+player.getName() +"')");
		if(rs.first()) {
			do {
				
				if(!rs.getBoolean("used")) {
					use++;
				}
				
			} while (rs.next());
		}
		
		return use;
	}
	
	public static Map<String, Integer> getAllKitUse(EntityPlayer player) throws SQLException{
		
		Map<String, Integer> hm = new HashMap<>();
		
		ResultSet rs = sql.getResult("SELECT * FROM "+tableName+" WHERE (username = '"+player.getName() +"')");
		
		if(rs.first()) {
			do {
				if(rs.getBoolean("used")) {
					
					String kitname = rs.getString("kitname");
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
	
	public static void kitUse(EntityPlayer player, String kitname) throws SQLException {
		
		if(getKitUse(player, kitname) > 0) {
			
			ResultSet rs = sql.getResult("SELECT * FROM "+tableName+" WHERE (kitname = '" + kitname + "') AND (username = '"+player.getName() +"')");
			if(!rs.getBoolean("used")) {
				
				use(rs.getString("buyId"));
			} else {
				while(rs.next()) {
					
					if(rs.getBoolean("used")) {
						
						use(rs.getString("buyId"));
						break;
					}
				}
			}
		}
	}
	
	public static void use(String buyId) {
		
		sql.update("UPDATE "+tableName+" SET used = '1' WHERE buyId = '"+buyId+"'");
	}
	
	public static void unUse(String buyId) {
		
		sql.update("UPDATE "+tableName+" SET used = '0' WHERE buyId = '"+buyId+"'");
	}
	
	public static void add(String buyId, String username, String kitname) {
		
		sql.update("INSERT INTO "+tableName+"(buyId, username, kitname) VALUES ('"
				+buyId
				+"','"
				+username
				+"','"
				+kitname
				+"')");
		
	}
	
}
