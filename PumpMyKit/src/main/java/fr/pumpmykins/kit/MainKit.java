package fr.pumpmykins.kit;

import java.util.List;

import org.apache.logging.log4j.Logger;

import fr.pumpmykins.kit.command.KitAddCommand;
import fr.pumpmykins.kit.command.KitBuyCommand;
import fr.pumpmykins.kit.command.KitDeleteCommand;
import fr.pumpmykins.kit.command.KitEndBuyCommand;
import fr.pumpmykins.kit.command.KitGetCommand;
import fr.pumpmykins.kit.command.KitModifyCommand;
import fr.pumpmykins.kit.command.KitReloadCommand;
import fr.pumpmykins.kit.command.KitViewCommand;
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
	
	private List<Kit> kitlist;
	
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
		
		event.registerServerCommand(new KitAddCommand());
		event.registerServerCommand(new KitDeleteCommand());
		event.registerServerCommand(new KitViewCommand());
		event.registerServerCommand(new KitGetCommand());
		event.registerServerCommand(new KitBuyCommand());
		event.registerServerCommand(new KitEndBuyCommand());
		event.registerServerCommand(new KitReloadCommand());
		event.registerServerCommand(new KitModifyCommand());
		
		KitList.loadKit();
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
	
	
}
