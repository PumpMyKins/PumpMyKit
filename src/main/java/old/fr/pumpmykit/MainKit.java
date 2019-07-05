package old.fr.pumpmykit;

import java.sql.SQLException;
import java.util.Arrays;

import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
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
import old.fr.pumpmykit.command.KitAddCommand;
import old.fr.pumpmykit.command.KitBuyCommand;
import old.fr.pumpmykit.command.KitDeleteCommand;
import old.fr.pumpmykit.command.KitHelpCommand;
import old.fr.pumpmykit.command.KitListCommand;
import old.fr.pumpmykit.command.KitModifyCommand;
import old.fr.pumpmykit.command.KitRandomCommand;
import old.fr.pumpmykit.command.KitSelectCommand;
import old.fr.pumpmykit.command.KitValidCommand;
import old.fr.pumpmykit.command.KitViewCommand;
import old.fr.pumpmykit.util.MySql;
import old.fr.pumpmykit.util.MySql.MySQLCredentials;


@Mod(useMetadata=true, modid = "pmkkit", acceptableRemoteVersions="*")
public class MainKit {

	@Instance("pmkkit")
	public static MainKit instance;
	
	public static Logger logger;
	
	private static final String MODID = "pumpmykit";
	
	private static final String KITLIST_KEY = MODID+"_kitlist";
	
	private KitList kitlistinstance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		logger = event.getModLog();
		
	}
	
	
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		
		try {
			MySQLCredentials credentials = new MySQLCredentials(ModConfig.sqlConfig.host, ModConfig.sqlConfig.port, ModConfig.sqlConfig.username, ModConfig.sqlConfig.password, ModConfig.sqlConfig.database);
			this.mySql = new MySql(credentials);
			this.mySql.openConnection();
			getLogger().info("MySQL OK");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			getLogger().error("JDBC error, plugin disabled !");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			this.getLogger().severe("MySQL connection error, plugin disabled !");
			return;
		}

		
		
		this.servername = ModConfig.serverconfig.servername;
		
		this.BUYTABLE = ModConfig.serverconfig.buyTable;
		this.KITRANDOMTABLE = ModConfig.serverconfig.randomTable;
		this.KITSELECTTABLE = ModConfig.serverconfig.selectTable;
		
		if(ModConfig.serverconfig.globalbuy)
			this.BUYTABLE = this.BUYTABLE.concat(this.servername);
		if(ModConfig.serverconfig.globalrandom)
			this.KITRANDOMTABLE = this.KITRANDOMTABLE.concat(this.servername);
		if(ModConfig.serverconfig.globalselect)
			this.KITSELECTTABLE = this.KITSELECTTABLE.concat(this.servername);
			
		
		if(mySql.isConnected()) {
			
			System.out.println("MySQL connection success.");
			//BASE DE DONNER
			mySql.update("CREATE TABLE IF NOT EXISTS "+this.BUYTABLE+"( `id` INT NOT NULL AUTO_INCREMENT,`buyId` VARCHAR(50) NOT NULL , `username` VARCHAR(16) NOT NULL , `kitname` VARCHAR(50) NOT NULL ,`used` BOOLEAN, buyAt DATETIME,PRIMARY KEY (`id`))");
			mySql.update("CREATE TABLE IF NOT EXISTS "+this.KITRANDOMTABLE+"(`id` INT NOT NULL AUTO_INCREMENT,`user_uuid` VARCHAR(60) NOT NULL,`kitnum` INT NOT NULL, PRIMARY KEY (`id`))");
			mySql.update("CREATE TABLE IF NOT EXISTS "+this.KITSELECTTABLE+"(`id` INT NOT NULL AUTO_INCREMENT,`user_uuid` VARCHAR(60) NOT NULL,`kitnum` INT NOT NULL, PRIMARY KEY (`id`))");

		} else {
			
			
		}
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		
		this.kitlistinstance = getData(event.getServer().getWorld(0));
		
		event.registerServerCommand(new KitCommand(this.kitlistinstance));
		
		KitCommand.registerSubCommand(Arrays.asList("help", "h"), new KitHelpCommand());
		KitCommand.registerSubCommand(Arrays.asList("view", "v"), new KitViewCommand(this.kitlistinstance));
		KitCommand.registerSubCommand(Arrays.asList("add", "a"), new KitAddCommand(this.kitlistinstance));
		KitCommand.registerSubCommand(Arrays.asList("delete","d"), new KitDeleteCommand(this.kitlistinstance));
		KitCommand.registerSubCommand(Arrays.asList("valid"), new KitValidCommand(this.kitlistinstance));
		KitCommand.registerSubCommand(Arrays.asList("modify"), new KitModifyCommand(this.kitlistinstance));
		KitCommand.registerSubCommand(Arrays.asList("select"), new KitSelectCommand(this.kitlistinstance));
		KitCommand.registerSubCommand(Arrays.asList("random", "rand"), new KitRandomCommand(this.kitlistinstance));
		KitCommand.registerSubCommand(Arrays.asList("list", "l"), new KitListCommand(this.kitlistinstance));
		
		KitCommand.registerSubCommand(Arrays.asList("buy"), new KitBuyCommand());
		
		event.getServer().initiateShutdown();
		
	}
	
	@SubscribeEvent
	public static void configChange(ConfigChangedEvent event) {
		
		if(event.getModID().equals(MODID)) {
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
		}
	}
	
	@Config(modid = MODID)
	public static class ModConfig{
		
		public static SqlConfig sqlConfig = new SqlConfig();
		
		public static ServerConfig serverconfig = new ServerConfig();
		
		public static class SqlConfig {
			
			@Config.Name("Hostname")
			@Config.Comment({"The MySQL hostname to login (IP)"})
			public String host ="";
			
			@Config.Name("Username")
			@Config.Comment({"The MySQL Username to login"})
			public String username ="";
			
			@Config.Name("Password")
			@Config.Comment({"The MySQL Password to login"})
			public String password ="";
			
			@Config.Name("Database")
			@Config.Comment({"The MySQL database to login"})
			public String database ="PumpMyKit";
			
			@Config.Name("Port")
			@Config.Comment({"The MySQL Port of your server"})
			public int port = 3306;
		
		}
		
		public static class ServerConfig {
			
			@Config.Name("ServerName")
			@Config.Comment({"The serveur name in order to make the difference between table !", "Only if Multiple server, if not let it be null ;)"})
			public String servername = "";
			
			@Config.Name("buyTable")
			@Config.Comment({"The name for the Sql table for kit buy"})
			public String buyTable = "buyKit";
			
			@Config.Name("globalbuy")
			@Config.Comment({"All the server should use the same table ?"})
			public boolean globalbuy = false;
			
			@Config.Name("randomTable")
			@Config.Comment({"The name for the Sql table for kit buy"})
			public String randomTable = "randomKit";
			
			@Config.Name("globalrandom")
			@Config.Comment({"All the server should use the same table ?"})
			public boolean globalrandom = true;
			
			@Config.Name("selectTable")
			@Config.Comment({"The name for the Sql table for kit buy"})
			public String selectTable = "selectKit";
			
			@Config.Name("globalselect")
			@Config.Comment({"All the server should use the same table ?"})
			public boolean globalselect = false;
		
		}
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

	public static MySQL getMySQL() throws SQLException {
		if(mySql.isConnected()) {
			return mySql;
		} else {
			mySql.refreshConnection();
			if(mySql.isConnected()) {
				return mySql;
			}
		}
		return null;
	}

	public static void setMySQL(MySQL mySQL) {
		MainKit.mySql = mySQL;
	}

	public String getBUYTABLE() {
		return BUYTABLE;
	}

	public void setBUYTABLE(String bUYTABLE) {
		BUYTABLE = bUYTABLE;
	}

	public String getKITRANDOMTABLE() {
		return KITRANDOMTABLE;
	}

	public void setKITRANDOMTABLE(String kITRANDOMTABLE) {
		KITRANDOMTABLE = kITRANDOMTABLE;
	}

	public static String getKITSELECTTABLE() {
		return KITSELECTTABLE;
	}

	public void setKITSELECTTABLE(String kITSELECTTABLE) {
		KITSELECTTABLE = kITSELECTTABLE;
	}
	
	
}
