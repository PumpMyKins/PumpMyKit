package fr.pumpmykins.kit.command;

import java.util.List;

import com.google.common.collect.Lists;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;

public class KitDeleteCommand implements ICommand {

	private KitList kitlist;
	
	public KitDeleteCommand(KitList kitlistinstance) {
		this.kitlist = kitlistinstance;
	}

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {

		return "kitdelete";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		
		return "kit.syntax.delete";
	}

	@Override
	public List<String> getAliases() {
		
		return Lists.newArrayList("kd");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(args.length > 0) {
			if(kitlist.getKit(args[0]) != null) {
				
				kitlist.removeKit(args[0]);
				
				Kit k = kitlist.getKit(args[0]);
				
				BlockPos chest_pos = new BlockPos(k.getX(), k.getY(), k.getZ());
				
				World w = server.getWorld(1);
				
				w.setBlockToAir(chest_pos);
				w.setBlockToAir(chest_pos.north());
				w.setBlockToAir(chest_pos.east());
				w.setBlockToAir(chest_pos.west());
				w.setBlockToAir(chest_pos.south());
				w.setBlockToAir(chest_pos.down());
				w.setBlockToAir(chest_pos.up());
				
			}
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if(sender instanceof EntityPlayer) {
			return PermissionAPI.hasPermission((EntityPlayer) sender, "kit.delete");
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
