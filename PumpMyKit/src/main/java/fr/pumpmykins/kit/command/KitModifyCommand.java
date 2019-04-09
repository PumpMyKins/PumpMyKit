package fr.pumpmykins.kit.command;

import java.util.List;

import com.google.common.collect.Lists;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;

public class KitModifyCommand implements ICommand {

	private KitList kitlist;
	
	public KitModifyCommand(KitList kitlistinstance) {
		
		this.kitlist = kitlistinstance;
	}

	@Override
	public int compareTo(ICommand o) {
		
		return 0;
	}

	@Override
	public String getName() {
		
		return "kitmodify";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		
		return "kit.syntax.modify";
	}

	@Override
	public List<String> getAliases() {
		
		return Lists.newArrayList("km");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(sender instanceof EntityPlayer) {
			if(args.length > 0) {
				if(kitlist.getKit(args[0]) != null) {
				
					World w = server.getWorld(1);
					
					Kit k = kitlist.getKit(args[0]);
					
					BlockPos chest_pos = new BlockPos(k.getX(), k.getY(), k.getZ());
					
					w.setBlockToAir(chest_pos.north());
					w.setBlockToAir(chest_pos.east());
					w.setBlockToAir(chest_pos.west());
					w.setBlockToAir(chest_pos.south());
					w.setBlockToAir(chest_pos.down());
					w.setBlockToAir(chest_pos.up());
				
				}
			}
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if(sender instanceof EntityPlayer) {
			return PermissionAPI.hasPermission((EntityPlayer) sender, "kit.modify");
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
