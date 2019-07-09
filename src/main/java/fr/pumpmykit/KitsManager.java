package fr.pumpmykit;

import java.sql.ResultSet;

import fr.pumpmykit.exceptions.InsufisentGlobalRandomException;
import fr.pumpmykit.exceptions.InsufisentSelectException;
import fr.pumpmykit.exceptions.UnfoudKitException;
import fr.pumpmykit.exceptions.UnfoundSqlProfileException;
import fr.pumpmykit.utils.Kit;
import fr.pumpmykit.utils.KitList;
import fr.pumpmykit.utils.MySql;
import net.minecraft.entity.player.EntityPlayerMP;

public class KitsManager {

	private MySql mySql;
	private boolean bddinitied;
	private KitList kitList;

	public MySql getMySql() {return mySql;}
	public boolean isBddInit() {return bddinitied;}
	public KitList getKitList() {return kitList;}
	public void setKitList(KitList kitList) {this.kitList = kitList;}

	public KitsManager(MySql mySql) throws Exception {
		this.mySql = mySql;
		this.initBddTables();
		this.bddinitied = true;
	}

	private void initBddTables() throws Exception {
		
		String createtable = "CREATE TABLE IF NOT EXISTS `playerskit` ( `uuid` VARCHAR(50) NOT NULL , `global_random` INT NOT NULL , `per_server_random` INT NOT NULL DEFAULT '0' ,`global_select` INT NOT NULL DEFAULT '0' , `per_server_select` INT NOT NULL , `init_date` BIGINT NOT NULL , PRIMARY KEY (`uuid`), UNIQUE (`uuid`)) ENGINE = InnoDB;";
		this.mySql.sendUpdate(createtable);	
		
		createtable = "CREATE TABLE IF NOT EXISTS `playerskit_" + KitsConfig.servername + "` ( `uuid` VARCHAR(50) NOT NULL , `select` INT NOT NULL , `init_date` BIGINT NOT NULL , PRIMARY KEY (`uuid`), UNIQUE (`uuid`)) ENGINE = InnoDB;";
		this.mySql.sendUpdate(createtable);		
		
	}
	
	private void initPlayerKitBdd(EntityPlayerMP player) throws Exception {
		
		ResultSet rs = this.mySql.sendQuery("SELECT * FROM `playerskit` WHERE `uuid`='" + player.getUniqueID().toString() + "'");
		
		this.mySql.sendUpdate("INSERT INTO `playerskit`(`uuid`, `select`, `init_date`) VALUES ('" + player.getUniqueID().toString() + "'," + rs.getInt("per_server_select") + "," + rs.getLong("init_date") + ")");
		
	}
	
	private void renewPlayerKitBdd(EntityPlayerMP player) throws Exception {
		
		ResultSet rs = this.mySql.sendQuery("SELECT * FROM `playerskit` WHERE `uuid`='" + player.getUniqueID().toString() + "'");
		
		this.mySql.sendUpdate("UPDATE `playerskit` SET `select`=" + rs.getInt("per_server_select") + ",`init_date`=" + rs.getLong("init_date") + " WHERE `uuid`='" + player.getUniqueID().toString() + "'");
		
	}
	
	public Kit getRandomKit(EntityPlayerMP player) throws Exception {
		
		ResultSet rs = this.mySql.sendQuery("SELECT * FROM `playerskit` WHERE `uuid`='" + player.getUniqueID().toString() + "'");
		
		if(!rs.first()) {
			
			throw new UnfoundSqlProfileException(player);
		}
			
		int global_random = rs.getInt("global_random");
		if(global_random <= 0) {
			throw new InsufisentGlobalRandomException(player);			
		}
		
		Kit kit = this.kitList.getRandomKit();
		
		if(kit == null) {
			throw new UnfoudKitException(player);
		}
		
		this.mySql.sendUpdate("UPDATE `playerskit` SET `global_random`=" + (global_random - 1) + " WHERE `uuid`='" + player.getUniqueID().toString() + "'");
		return kit;
		
	}
	
	public Kit selectKit(EntityPlayerMP player, String kitName) throws Exception {
		
		ResultSet rs = this.mySql.sendQuery("SELECT * FROM `playerskit` WHERE `uuid`='" + player.getUniqueID().toString() + "'");
		
		if(!rs.first()) {
			throw new UnfoundSqlProfileException(player);
		}
		
		long global_init_date = rs.getLong("init_date");
		
		rs = this.mySql.sendQuery("SELECT * FROM `playerskit" + KitsConfig.servername + "` WHERE `uuid`='" + player.getUniqueID().toString() + "'");	
		
		if(!rs.first()) {
			this.initPlayerKitBdd(player);
			rs = this.mySql.sendQuery("SELECT * FROM `playerskit" + KitsConfig.servername + "` WHERE `uuid`='" + player.getUniqueID().toString() + "'");
			if(!rs.first()) {
				throw new UnfoundSqlProfileException(player);
			}			
		}
		
		long local_init_date = rs.getLong("init_date");
		
		if(global_init_date != local_init_date) {
			this.renewPlayerKitBdd(player);	
			rs = this.mySql.sendQuery("SELECT * FROM `playerskit" + KitsConfig.servername + "` WHERE `uuid`='" + player.getUniqueID().toString() + "'");
			if(!rs.first()) {
				throw new UnfoundSqlProfileException(player);
			}
		}
		
		int per_server_select = rs.getInt("select");
		
		if(per_server_select <= 0) {
			throw new InsufisentSelectException(player);
		}
		
		Kit kit = this.kitList.getKit(kitName);
		
		if(kit == null) {
			throw new UnfoudKitException(player,kitName);
		}
		
		return kit;
		
	}
	
}
