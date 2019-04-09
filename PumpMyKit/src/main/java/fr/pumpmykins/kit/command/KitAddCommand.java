package fr.pumpmykins.kit.command;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;

public class KitAddCommand implements ICommand {

	private KitList kitlist;
	
	public KitAddCommand(KitList kitlistinstance) {
		this.kitlist = kitlistinstance;
	}

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		
		return "kitadd";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		
		return "kit.syntax.add";
	}

	@Override
	public List<String> getAliases() {
		
		return Lists.newArrayList("ka");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if(sender instanceof EntityPlayer) {
			
			EntityPlayer player = (EntityPlayer) sender;
			
			if(args.length > 0) {
				
				String kitname = args[0];
				System.out.println(kitname);
				BlockPos chest_pos = player.getPosition();
				
				EnumFacing i = player.getHorizontalFacing();
				if(i == EnumFacing.EAST)
					chest_pos = chest_pos.east();
				if(i == EnumFacing.WEST)
					chest_pos = chest_pos.west();
				if(i == EnumFacing.SOUTH)
					chest_pos = chest_pos.south();
				else
					chest_pos = chest_pos.north();
				
				World w = player.getEntityWorld();
				IBlockState chest = Blocks.CHEST.getDefaultState();
				
				w.setBlockState(chest_pos, chest);
				
				Kit k = new Kit();
				k.setName(kitname);
				k.setCreator(player.getUniqueID());
				k.setLast_updator(player.getUniqueID());
				
				Date date= new Date();
				String a = date.toString();
				
				k.setLast_update(a);
				k.setX(chest_pos.getX());
				k.setY(chest_pos.getY());
				k.setZ(chest_pos.getZ());
				
				this.kitlist.addKit(k);
			}
			else {
				getUsage(sender);
			}
		}
		
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		if(sender instanceof EntityPlayer) {
			return PermissionAPI.hasPermission((EntityPlayer) sender, "kit.add");
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
		return false;
	}

}
