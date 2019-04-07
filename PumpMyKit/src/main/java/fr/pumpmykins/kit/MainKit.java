package fr.pumpmykins.kit;

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


@Mod(useMetadata= true, modid = "pmkkit")
public class MainKit {

	@Instance("pmkkit")
	public static MainKit instance;
	
	@SidedProxy(clientSide = "fr.pumpmykins.kit.KitClient", serverSide ="fr.pumpmykins.kit.KitServer")
	public static KitCommon proxy;
	
	public static Logger logger;
	
	public static final String MODID = "pmkkit";
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		logger = event.getModLog();
		proxy.preInit(event.getSuggestedConfigurationFile());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		proxy.init();
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
	}
	
	
}
