package fr.pumpmykit;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.Logger;

import fr.pumpmykit.command.KitCommand;
import fr.pumpmykit.utils.KitList;
import fr.pumpmykit.utils.MySql;
import fr.pumpmykit.utils.MySql.MySqlCredentials;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;


@Mod(modid = MainKit.MODID, name = MainKit.NAME, version = MainKit.VERSION, acceptableRemoteVersions = "*")
public class MainKit
{
	public static final String MODID = "pumpmykit";
	public static final String NAME = "PumpMyKit";
	public static final String VERSION = "1.0";

	public static final ITextComponent CHAT_PREFIX = new TextComponentString("§l§6[§2Pump§eMy§dKit§6]§r§f ");

	public static Logger LOGGER;
	public static ExecutorService EXEC;
	public static KitsManager KITSMANAGER;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LOGGER = event.getModLog();
		EXEC = Executors.newSingleThreadScheduledExecutor();

		try {
			MySqlCredentials credentials = new MySqlCredentials(KitsConfig.host, KitsConfig.port, KitsConfig.username, KitsConfig.password, KitsConfig.database);
			MySql mySql = new MySql(credentials);
			mySql.openConnection();
			KITSMANAGER = new KitsManager(mySql);
			LOGGER.info("KitsManager & MySql OK !");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {

		if(KITSMANAGER != null & KITSMANAGER.isBddInit()) {

			KITSMANAGER.setKitList(KitList.getData(event.getServer().getEntityWorld()));

			event.registerServerCommand(new KitCommand());

		}

	}


}
