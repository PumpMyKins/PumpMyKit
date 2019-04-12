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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
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
		
		return "/kitadd <kitname>";
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
				
				if(player.dimension != 0) { //TODO Temporary v1
					return;
				}
				
				String kitname = args[0];
				
				if(this.kitlist.getKit(kitname) == null) {
					
					BlockPos chest_pos = player.getPosition();
					
					EnumFacing i = player.getHorizontalFacing();
					if(i == EnumFacing.EAST) {
						chest_pos = chest_pos.east();
						chest_pos = chest_pos.east();
					}
					if(i == EnumFacing.WEST) {
						chest_pos = chest_pos.west();
						chest_pos = chest_pos.west();
					}
					if(i == EnumFacing.SOUTH) {
						chest_pos = chest_pos.south();
						chest_pos = chest_pos.south();
					}
					else {
						chest_pos = chest_pos.north();
						chest_pos = chest_pos.north();
					}
					World w = player.getEntityWorld();
				
					IBlockState chest = Blocks.CHEST.getDefaultState();
					IBlockState bedrock = Blocks.BARRIER.getDefaultState();
					
					chest_pos = chest_pos.up();
					
					w.setBlockState(chest_pos, chest);
					w.setBlockState(chest_pos.north(), bedrock);
					w.setBlockState(chest_pos.east(), bedrock);
					w.setBlockState(chest_pos.west(), bedrock);
					w.setBlockState(chest_pos.south(), bedrock);
					w.setBlockState(chest_pos.down(), bedrock);
					w.setBlockState(chest_pos.up(), bedrock);
					
					Kit k = new Kit(player.getUniqueID(), chest_pos, kitname);
					
					this.kitlist.addKit(k);
				} else {
					
					ITextComponent init = new TextComponentString("Le kit : "+kitname+" existe deja !");
					init.setStyle(PmkStyleTable.orangeBold());
					player.sendMessage(init);
				}
			}
			else {
				sender.sendMessage(new TextComponentTranslation("kit.syntax"));
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
		
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
