package fr.pumpmykit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.pumpmykit.exceptions.DuplicateKitException;
import fr.pumpmykit.exceptions.InsufisentGlobalRandomException;
import fr.pumpmykit.exceptions.InsufisentSelectException;
import fr.pumpmykit.exceptions.KitIsEmptyException;
import fr.pumpmykit.exceptions.UnfoudKitException;
import fr.pumpmykit.exceptions.UnfoundKitChestException;
import fr.pumpmykit.exceptions.UnfoundSqlProfileException;
import fr.pumpmykit.utils.BlockUtils;
import fr.pumpmykit.utils.Kit;
import fr.pumpmykit.utils.KitList;
import fr.pumpmykit.utils.MySql;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.items.ItemHandlerHelper;

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

	private void initPlayerKitBdd(EntityPlayerMP player) throws SQLException {
		
		ResultSet rs = this.mySql.sendQuery("SELECT * FROM `playerskit` WHERE `uuid`='" + player.getUniqueID().toString() + "'");

		rs.first();
		this.mySql.sendUpdate("INSERT INTO `playerskit_" + KitsConfig.servername + "`(`uuid`, `select`, `init_date`) VALUES ('" + player.getUniqueID().toString() + "'," + rs.getInt("per_server_select") + "," + rs.getLong("init_date") + ")");

	}

	private void renewPlayerKitBdd(EntityPlayerMP player) throws SQLException {

		ResultSet rs = this.mySql.sendQuery("SELECT * FROM `playerskit` WHERE `uuid`='" + player.getUniqueID().toString() + "'");

		rs.first();
		this.mySql.sendUpdate("UPDATE `playerskit_" + KitsConfig.servername + "` SET `select`=" + rs.getInt("per_server_select") + ",`init_date`=" + rs.getLong("init_date") + " WHERE `uuid`='" + player.getUniqueID().toString() + "'");

	}
	
	private void removePlayerKitBdd(EntityPlayerMP player) throws SQLException{
		
		ResultSet rs = this.mySql.sendQuery("SELECT * FROM `playerskit` WHERE `uuid`='" + player.getUniqueID().toString() + "'");
		
		rs.first();
		this.mySql.sendUpdate("UPDATE `playerskit_" + KitsConfig.servername + "` SET `select`=" + rs.getInt("per_server_select") + ",`init_date`=" + rs.getLong("init_date") + " WHERE `uuid`='" + player.getUniqueID().toString() + "'");
		
	}

	public void randomKit(EntityPlayerMP player) throws SQLException, UnfoundSqlProfileException, InsufisentGlobalRandomException, UnfoudKitException {
		
		ResultSet rs = this.mySql.sendQuery("SELECT * FROM `playerskit` WHERE `uuid`='" + player.getUniqueID().toString() + "'");

		if(!rs.first()) {
			this.removePlayerKitBdd(player);
			throw new UnfoundSqlProfileException(player);
		}

		int global_random = rs.getInt("global_random");
		if(global_random <= 0) {
			throw new InsufisentGlobalRandomException(player);			
		}

		if(this.kitList.getKitlist().isEmpty()) {
			throw new UnfoudKitException("all");
		}
		
		Kit kit = this.kitList.getRandomKit();

		if(kit == null) {
			throw new UnfoudKitException(player);
		}

		this.mySql.sendUpdate("UPDATE `playerskit` SET `global_random`=" + (global_random - 1) + " WHERE `uuid`='" + player.getUniqueID().toString() + "'");

		KitsManager.setKitToPlayer(player, kit);		

	}

	public List<Kit> listKit(){

		return (List<Kit>) this.getKitList().getKitlist().values();

	}

	public Kit getKit(String kitName) throws UnfoudKitException {

		Kit kit = this.kitList.getKit(kitName);

		if(kit == null) {
			throw new UnfoudKitException(kitName);
		}

		return kit;

	}

	public void selectKit(EntityPlayerMP player, String kitName) throws UnfoundSqlProfileException, InsufisentSelectException, UnfoudKitException, SQLException {

		ResultSet rs = this.mySql.sendQuery("SELECT * FROM `playerskit` WHERE `uuid`='" + player.getUniqueID().toString() + "'");

		if(!rs.first()) {
			this.removePlayerKitBdd(player);
			throw new UnfoundSqlProfileException(player);
		}

		long global_init_date = rs.getLong("init_date");

		rs = this.mySql.sendQuery("SELECT * FROM `playerskit_" + KitsConfig.servername + "` WHERE `uuid`='" + player.getUniqueID().toString() + "'");	

		if(!rs.first()) {
			this.initPlayerKitBdd(player);
			rs = this.mySql.sendQuery("SELECT * FROM `playerskit_" + KitsConfig.servername + "` WHERE `uuid`='" + player.getUniqueID().toString() + "'");
			if(!rs.first()) {
				throw new UnfoundSqlProfileException(player);
			}			
		}

		long local_init_date = rs.getLong("init_date");

		if(global_init_date != local_init_date) {
			this.renewPlayerKitBdd(player);	
			rs = this.mySql.sendQuery("SELECT * FROM `playerskit_" + KitsConfig.servername + "` WHERE `uuid`='" + player.getUniqueID().toString() + "'");
			if(!rs.first()) {
				throw new UnfoundSqlProfileException(player);
			}
		}

		int per_server_select = rs.getInt("select");

		if(per_server_select <= 0) {
			throw new InsufisentSelectException(player);
		}

		if(this.kitList.getKitlist().isEmpty()) {
			throw new UnfoudKitException("all");
		}
		
		Kit kit = this.getKit(kitName);
		
		if(kit == null) {
			throw new UnfoudKitException(player);
		}
		
		this.mySql.sendUpdate("UPDATE `playerskit_" + KitsConfig.servername + "` SET `select`=" + (per_server_select - 1) + " WHERE `uuid`='" + player.getUniqueID().toString() + "'");
		KitsManager.setKitToPlayer(player, kit);
	}

	private static void setKitToPlayer(EntityPlayerMP player, Kit kit) {

		WorldServer w = player.getServerWorld();
		
		w.addScheduledTask(new Runnable() {
			
			@Override
			public void run() {
				
				for (ItemStack item : kit.getItems()) {
					ItemHandlerHelper.giveItemToPlayer(player, item);
				}
				
				ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
				ITextComponent txt2 = new TextComponentString("Kit ");
				txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
				txt.appendSibling(txt2);
				
				ITextComponent txt3 = new TextComponentString(kit.getDisplayName());
				txt3.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_BLUE));
				
				txt.appendSibling(txt3);
				
				ITextComponent txt4 = new TextComponentString(" obtenu !");
				txt4.setStyle(new Style().setBold(true).setColor(TextFormatting.AQUA));
				
				txt.appendSibling(txt4);
				
				player.sendMessage(txt);
				
			}
		});
		
	}
	
	public void addKit(EntityPlayerMP player, String name, String displayName) throws UnfoundKitChestException, KitIsEmptyException, DuplicateKitException {
		
		BlockPos blockPos = new BlockPos(player.getPosition().getX(), player.getPosition().getY() - 1, player.getPosition().getZ());
		
		List<ItemStack> content = BlockUtils.getChestBlockContent(player.world,blockPos);
		if(content.isEmpty()) {
			throw new KitIsEmptyException();
		}
		
		Kit k = new Kit(name, displayName, content);		
		this.getKitList().addKit(k);		
		
	}
	
	public void removeKit(String name) throws UnfoudKitException {
		
		this.getKitList().removeKit(name);
		
	}
	
	public void loadKit(EntityPlayerMP player, String name) throws UnfoudKitException, UnfoundKitChestException {
		
		Kit kit = this.getKit(name);
		
		BlockPos blockPos = new BlockPos(player.getPosition().getX(), player.getPosition().getY() - 1, player.getPosition().getZ());
		
		BlockUtils.loadContentInChestBlock(player.world,blockPos, kit.getItems());
		
	}
	
	public void updateContentKit(EntityPlayerMP player, String name) throws UnfoudKitException, UnfoundKitChestException, KitIsEmptyException {
		
		Kit kit = this.getKit(name);
		
		BlockPos blockPos = new BlockPos(player.getPosition().getX(), player.getPosition().getY() - 1, player.getPosition().getZ());
		
		List<ItemStack> content = BlockUtils.getChestBlockContent(player.world,blockPos);
		if(content.isEmpty()) {
			throw new KitIsEmptyException();
		}
		
		kit.setItems(content);
		
	}

}
