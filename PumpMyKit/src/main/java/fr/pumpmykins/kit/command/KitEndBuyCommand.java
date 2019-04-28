package fr.pumpmykins.kit.command;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.permission.PermissionAPI;

public class KitEndBuyCommand implements ICommand {

	@Override
	public int compareTo(ICommand o) {
		
		return 0;
	}

	@Override
	public String getName() {

		return "kitendbuy";
	}

	@Override
	public String getUsage(ICommandSender sender) {

		return "kit.syntax.endbuy";
	}

	@Override
	public List<String> getAliases() {

		return Lists.newArrayList("kbd");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		/**Will have to be done for the global type kit
		 * Will also have to add a var to the Kit Class in order to setup properly
		 * what kit should and shouldn't be give to everyone
		 * Since we use a mod we could also imagine a Kit Team by using the team from FTBUtilities 
		 */
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if(sender instanceof EntityPlayer) {
			return PermissionAPI.hasPermission((EntityPlayer) sender, "kit.buy.end");
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
