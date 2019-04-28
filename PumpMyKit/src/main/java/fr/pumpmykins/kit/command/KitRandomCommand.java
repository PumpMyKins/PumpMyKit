package fr.pumpmykins.kit.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import fr.pumpmykins.kit.MainKit;
import fr.pumpmykins.kit.util.MySQL;
import fr.pumpmykins.kit.util.PmkStyleTable;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.permission.PermissionAPI;

public class KitRandomCommand implements ICommand {

	private KitList kitlist;

	public KitRandomCommand(KitList kitlistinstance) {

		this.kitlist = kitlistinstance;
	}

	@Override
	public int compareTo(ICommand o) {

		return 0;
	}

	@Override
	public String getName() {

		return "kitrandom";
	}

	@Override
	public String getUsage(ICommandSender sender) {

		return "/kitrandom";
	}

	@Override
	public List<String> getAliases() {

		return Lists.newArrayList("kitrand");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if(sender instanceof EntityPlayer) {

			try {
				MySQL mySQL = MainKit.getMySQL();

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

				int kitnum = 0;

				if(rs.first()) {

					kitnum = rs.getInt("kitnum");

					randomCount = randomCount - kitnum;

				} else {

					mySQL.update("INSERT INTO `RandomKit` (`user_uuid`, `kitnum`) VALUES ('"
							+player.getUniqueID()
							+"',"
							+0
							+")");

				}

				if(randomCount > 0) {

					List<Kit> kit = this.kitlist.getKitlist();
					int size = kit.size();
					Random rand = new Random();
					Kit k = kit.get(rand.nextInt(size));

					String buyId = k.getName()+player.getName();
					int random = rand.nextInt(100000);
					buyId = buyId.concat(Integer.toString(random));

					server.getCommandManager().executeCommand(server, "kitbuy "+buyId+" "+player.getName()+" "+k.getName());

					int newnum = kitnum + 1 ;

					mySQL.update("UPDATE `RandomKit` SET `kitnum`="+newnum+" WHERE `user_uuid`= '"+player.getUniqueID()+"'");

					ITextComponent init = new TextComponentString("Bravo vous avez maintenant acces au kit : ");
					init.setStyle(PmkStyleTable.orangeBold());

					ITextComponent kitname = new TextComponentString(k.getName());
					kitname.setStyle(PmkStyleTable.itemNumber());
					init.appendSibling(kitname);
					sender.sendMessage(init);

				} else {

					ITextComponent refuse = new TextComponentString("Vous n'avez aucun kit random en stock !");
					refuse.setStyle(PmkStyleTable.orangeBold());
					sender.sendMessage(refuse);
				}
			} catch(SQLException e1) {

				e1.printStackTrace();
			}
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {

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

		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {

		return false;
	}


}
