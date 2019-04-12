package fr.pumpmykins.kit.command;

import java.util.List;

import com.google.common.collect.Lists;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import fr.pumpmykins.kit.util.PmkStyleTable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;

public class KitValidCommand implements ICommand {

	private KitList kitlist;
	
	public KitValidCommand(KitList kitlistinstance) {
		this.kitlist = kitlistinstance;
	}

	@Override
	public int compareTo(ICommand o) {
		
		return 0;
	}

	@Override
	public String getName() {
		
		return "kitvalid";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		
		return "/kitvalid <kitname>";
	}

	@Override
	public List<String> getAliases() {
		
		return Lists.newArrayList("kvalid");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(sender instanceof EntityPlayer) {
			
			if(args.length > 0) {
		
				if(kitlist.getKit(args[0]) != null) {
				
					World w = server.getWorld(0);
					
					Kit k = kitlist.getKit(args[0]);
					
					BlockPos chest_pos = new BlockPos(k.getX(), k.getY(), k.getZ());
					IBlockState bedrock = Blocks.BARRIER.getDefaultState();
					
					w.setBlockState(chest_pos.north(), bedrock);
					w.setBlockState(chest_pos.east(), bedrock);
					w.setBlockState(chest_pos.west(), bedrock);
					w.setBlockState(chest_pos.south(), bedrock);
					w.setBlockState(chest_pos.down(), bedrock);
					w.setBlockState(chest_pos.up(), bedrock);
				
					ITextComponent init = new TextComponentString("Kit reprotégé avec succès");
					init.setStyle(PmkStyleTable.orangeBold());
					sender.sendMessage(init);
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
