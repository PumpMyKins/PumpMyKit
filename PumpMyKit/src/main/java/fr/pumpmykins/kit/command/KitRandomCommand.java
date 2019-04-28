package fr.pumpmykins.kit.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.common.collect.Lists;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import fr.pumpmykins.kit.MainKit;
import fr.pumpmykins.kit.util.MySQL;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.permission.PermissionAPI;

public class KitRandomCommand implements ICommand {

	private KitList kitlist;
	
	public KitRandomCommand(KitList kitlistinstance) {
		// TODO Auto-generated constructor stub
		this.kitlist = kitlistinstance;
	}

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "kitrandom";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/kitrandom";
	}

	@Override
	public List<String> getAliases() {
		// TODO Auto-generated method stub
		return Lists.newArrayList("kitrand");
	}

	@SuppressWarnings("unused")
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(sender instanceof EntityPlayer) {
			
			try {
				MySQL mySQL = MainKit.getMySQL();
				mySQL.openConnection();
				if(mySQL.isConnected()) {
					
					EntityPlayer player = (EntityPlayer) sender;
					ResultSet rs = mySQL.getResult("SELECT * FROM RandomKit WHERE `user_uuid` = '"+player.getUniqueID()+"'");
					int randomRestriction = 0;
					if(PermissionAPI.hasPermission(player, "rank.tier1"))
						randomRestriction = 1;
					if(PermissionAPI.hasPermission(player, "rank.tier2"))
						randomRestriction = 3;
					if(PermissionAPI.hasPermission(player, "rank.tier3"))
						randomRestriction = 5;
					int randomCount = randomRestriction;
					
					try {
						if(rs.first()) {
							
							int kitnum = rs.getInt("kitnumber");
							
							randomCount = randomCount - kitnum;
						
						} else {
							
							mySQL.update("INSERT INTO `RandomKit` (`user_uuid`, `kitnumber`) VALUES ('"
							+player.getUniqueID()
							+"',"
							+0
							+")");
							
							int kitnum = 0;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(randomCount > 0) {
						
						List<Kit> kit = this.kitlist.getKitlist();
						int size = kit.size();
						int kitnum =(int) (Math.random() * ((size - 0) +1));
						Kit k = kit.get(kitnum);
						
						String buyId = k.getName()+player.getName();
						int random = (int) (Math.random() * ((100000 - 0) + 1));
						buyId.concat(Integer.toString(random));
						
						server.getCommandManager().executeCommand(server, "kitbuy "+k.getName()+" "+player.getName()+" "+buyId);
						
						int newnum = kitnum +1;
						
						mySQL.update("UPDATE `RandomKit` SET `kitnumber`="+newnum+" WHERE `user_uuid`= '"+player.getUniqueID()+"'");
					}
				}
			} catch(SQLException e1) {
				
				e1.printStackTrace();
			}
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		// TODO Auto-generated method stub
		if(sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if(PermissionAPI.hasPermission(player, "rank.tier1") || PermissionAPI.hasPermission(player, "rank.tier2") || PermissionAPI.hasPermission(player, "rank.tier3"))
				return true;
		}
		return false;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
