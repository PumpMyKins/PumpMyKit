package fr.pumpmykins.kit.command;

import java.util.List;

import com.google.common.collect.Lists;

import fr.pumpmykins.kit.KitList;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class KitGetCommand implements ICommand {

	private KitList kitlist;
	
	public KitGetCommand(KitList kitlistinstance) {
		
		this.setKitlist(kitlistinstance);
	}

	@Override
	public int compareTo(ICommand o) {
		
		return 0;
	}

	@Override
	public String getName() {
		
		return "kit";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		
		return "kit.syntax.help";
	}

	@Override
	public List<String> getAliases() {
		
		return Lists.newArrayList("k");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
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

	/**
	 * @return the kitlist
	 */
	public KitList getKitlist() {
		return kitlist;
	}

	/**
	 * @param kitlist the kitlist to set
	 */
	public void setKitlist(KitList kitlist) {
		this.kitlist = kitlist;
	}

}
