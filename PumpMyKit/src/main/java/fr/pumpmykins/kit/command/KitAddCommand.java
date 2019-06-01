package fr.pumpmykins.kit.command;

import java.util.Arrays;
import java.util.List;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import fr.pumpmykins.kit.util.ISubCommand;
import fr.pumpmykins.kit.util.PmkStyleTable;
import net.minecraft.block.state.IBlockState;
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


public class KitAddCommand extends ISubCommand {

	private KitList kitlist;

	public KitAddCommand(KitList kitlistinstance) {
		this.kitlist = kitlistinstance;
	}

	@Override
	public void onCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(sender instanceof EntityPlayer) {
			
			EntityPlayer player = (EntityPlayer) sender;
			
			if(args.length > 0) {
				
				if(player.dimension != 0) {
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
	public List<String> getPermission() {
		
		return Arrays.asList("pumpmykins.staff.responsable");
	}

	
}
