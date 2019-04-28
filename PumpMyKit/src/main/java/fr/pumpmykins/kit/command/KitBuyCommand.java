package fr.pumpmykins.kit.command;

import java.sql.SQLException;
import java.util.List;

import com.google.common.collect.Lists;

import fr.pumpmykins.kit.util.KitUtils;
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

public class KitBuyCommand implements ICommand {

	@Override
	public int compareTo(ICommand o) {
		
		return 0;
	}

	@Override
	public String getName() {
		
		return "kitbuy";
	}

	@Override
	public String getUsage(ICommandSender sender) {
	
		return "/kitbuy <kitname> <username> <buyId>";
	}

	@Override
	public List<String> getAliases() {
		
		return Lists.newArrayList("kbs");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(args.length > 2) {
			
			try {
				KitUtils.add(args[0], args[1], args[2]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("Kit :"+args[2]+", buyBy :"+args[1]+", ID D'achat :"+args[0]);
			
			if(sender instanceof EntityPlayer) {
				ITextComponent init = new TextComponentString("Ajout du Kit :"+args[2]+", Pour :"+args[1]);
				init.setStyle(PmkStyleTable.orangeBold());
				sender.sendMessage(init);
			}
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if(sender instanceof EntityPlayer) {
			return PermissionAPI.hasPermission((EntityPlayer) sender, "kit.buy.start");
		} else {
			return true;
		}
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
