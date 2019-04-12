package fr.pumpmykins.kit;

import org.apache.logging.log4j.Logger;

import fr.pumpmykins.kit.command.*;
import fr.pumpmykins.kit.util.MySQL;
import fr.pumpmykins.kit.util.MySQL.MySQLCredentials;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;


@Mod(useMetadata=true, modid = "pmkkit", acceptableRemoteVersions="*")
public class MainKit {

	@Instance("pmkkit")
	public static MainKit instance;
	
	public static Logger logger;
	
	private static final String MODID = "pmkkit";
	
	private static final String KITLIST_KEY = MODID+"_kitlist";
	
	private KitList kitlistinstance;
	
	static MySQL mySQL;
	public String host = "";
	public String username = "";
	public String password = "";
	public String database = "";
	public int port = 3306;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		logger = event.getModLog();
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		PermissionAPI.registerNode("kit.add", DefaultPermissionLevel.OP, "Allow OP to add a Kit");
		PermissionAPI.registerNode("kit.delete", DefaultPermissionLevel.OP, "Allow OP to delete a Kit");
		PermissionAPI.registerNode("kit.buy.end", DefaultPermissionLevel.OP, "Allow OP to end the duration of a Kit");
		PermissionAPI.registerNode("kit.buy.start", DefaultPermissionLevel.OP, "Allow OP to give a Kit to someone");
		PermissionAPI.registerNode("kit.modify", DefaultPermissionLevel.OP, "Allow OP to modify a Kit");
		PermissionAPI.registerNode("kit.admin", DefaultPermissionLevel.OP, "Basic Admin Permission for the Mod");
		
		
		this.host = ModConfig.host;
		this.username = ModConfig.username;
		this.password = ModConfig.password;
		this.database = ModConfig.database;
		this.port = ModConfig.port;
		
		MySQLCredentials credentials = new MySQLCredentials(host, port, username, password, database);
		mySQL = new MySQL(credentials);
		mySQL.openConnection();
		if(mySQL.isConnected()) {
					
			System.out.println("§aMySQL connection success.");
			//BASE DE DONNER
			mySQL.update("CREATE TABLE IF NOT EXISTS PmkKitTable( `id` INT NOT NULL AUTO_INCREMENT,`buyId` VARCHAR(50) NOT NULL , `username` VARCHAR(16) NOT NULL , `kitname` VARCHAR(50) NOT NULL ,`used` BOOLEAN, buyAt DATETIME,PRIMARY KEY (`id`))");
		}
		
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		
		this.kitlistinstance = getData(event.getServer().getWorld(0));
		
		event.registerServerCommand(new KitBuyCommand());
		event.registerServerCommand(new KitEndBuyCommand());
		event.registerServerCommand(new KitHelpCommand());
		
		event.registerServerCommand(new KitViewCommand(this.kitlistinstance));
		event.registerServerCommand(new KitModifyCommand(this.kitlistinstance));
		event.registerServerCommand(new KitValidCommand(this.kitlistinstance));
		event.registerServerCommand(new KitAddCommand(this.kitlistinstance));
		event.registerServerCommand(new KitDeleteCommand(this.kitlistinstance));	
		event.registerServerCommand(new KitGetCommand(this.kitlistinstance));
	
		
		
	}
	
	@SubscribeEvent
	public static void configChange(ConfigChangedEvent event) {
		
		if(event.getModID().equals(MODID)) {
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
		}
	}
	
	@Config(modid = MODID)
	public static class ModConfig{
		
		@Config.Name("Hostname")
		@Config.Comment({"The MySQL hostname to login (IP)"})
		public static String host ="";
		
		@Config.Name("Username")
		@Config.Comment({"The MySQL Username to login"})
		public static String username ="";
		
		@Config.Name("Password")
		@Config.Comment({"The MySQL Password to login"})
		public static String password ="";
		
		@Config.Name("Database")
		@Config.Comment({"The MySQL database to login"})
		public static String database ="PmkKit_Database";
		
		@Config.Name("Port")
		@Config.Comment({"The MySQL Port of your server"})
		public static int port = 3306;
		
		
		
	}

	public static KitList getData(World w) {
		
		MapStorage storage = w.getMapStorage();
		KitList instance = (KitList) storage.getOrLoadData(KitList.class, KITLIST_KEY);
		if(instance == null) {
			instance = new KitList();
			storage.setData(KITLIST_KEY, instance);
		}
		return instance;
	}
	
	public static void setData(World w) {
		
		MapStorage storage = w.getMapStorage();
		KitList instance = (KitList) storage.getOrLoadData(KitList.class, KITLIST_KEY);
		if(instance.isDirty()) {
			storage.setData(KITLIST_KEY, instance);
		}
	}
	
	
	public static MainKit getInstance() {
		return instance;
	}

	public static void setInstance(MainKit instance) {
		MainKit.instance = instance;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		MainKit.logger = logger;
	}

	public static String getModid() {
		return MODID;
	}

	public static String getKitlistKey() {
		return KITLIST_KEY;
	}

	public KitList getKitlistinstance() {
		return kitlistinstance;
	}

	public void setKitlistinstance(KitList kitlistinstance) {
		this.kitlistinstance = kitlistinstance;
	}

	public static MySQL getMySQL() {
		return mySQL;
	}

	public static void setMySQL(MySQL mySQL) {
		MainKit.mySQL = mySQL;
	}
	
	
}
