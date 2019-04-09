package fr.pumpmykins.kit;

import org.apache.logging.log4j.Logger;

import fr.pumpmykins.kit.command.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;


@Mod(useMetadata= true, modid = "pmkkit")
public class MainKit {

	@Instance("pmkkit")
	public static MainKit instance;
	
	@SidedProxy(clientSide = "fr.pumpmykins.kit.KitClient", serverSide ="fr.pumpmykins.kit.KitServer")
	public static KitCommon proxy;
	
	public static Logger logger;
	
	private static final String MODID = "pmkkit";
	
	private static final String KITLIST_KEY = MODID+"_kitlist";
	
	private KitList kitlistinstance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		logger = event.getModLog();
		proxy.preInit(event.getSuggestedConfigurationFile());
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		proxy.init();
		PermissionAPI.registerNode("kit.add", DefaultPermissionLevel.OP, "Allow OP to add a Kit");
		PermissionAPI.registerNode("kit.delete", DefaultPermissionLevel.OP, "Allow OP to delete a Kit");
		PermissionAPI.registerNode("kit.buy.end", DefaultPermissionLevel.OP, "Allow OP to end the duration of a Kit");
		PermissionAPI.registerNode("kit.buy.start", DefaultPermissionLevel.OP, "Allow OP to give a Kit to someone");
		PermissionAPI.registerNode("kit.modify", DefaultPermissionLevel.OP, "Allow OP to modify a Kit");
		PermissionAPI.registerNode("kit.reload", DefaultPermissionLevel.OP, "Allow OP to reload the Kit list");
		
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		
		this.setKitlistinstance(new KitList());
		
		event.registerServerCommand(new KitAddCommand(this.kitlistinstance));
		event.registerServerCommand(new KitDeleteCommand(this.kitlistinstance));
		event.registerServerCommand(new KitViewCommand());
		event.registerServerCommand(new KitGetCommand(this.kitlistinstance));
		event.registerServerCommand(new KitBuyCommand());
		event.registerServerCommand(new KitEndBuyCommand());
		event.registerServerCommand(new KitReloadCommand());
		event.registerServerCommand(new KitModifyCommand(this.kitlistinstance));
		event.registerServerCommand(new KitValidCommand(this.kitlistinstance));
			
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
	
	
}
