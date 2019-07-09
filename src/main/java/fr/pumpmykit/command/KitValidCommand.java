package old.fr.pumpmykit.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import old.fr.pumpmykit.Kit;
import old.fr.pumpmykit.KitList;
import old.fr.pumpmykit.util.ISubCommand;
import old.fr.pumpmykit.util.PmkStyleTable;

public class KitValidCommand extends ISubCommand {

	private KitList kitlist;

	public KitValidCommand(KitList kitlistinstance) {
		this.kitlist = kitlistinstance;
	}

	@Override
	public void onCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
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
				
					ITextComponent init = new TextComponentString("Kit reprotege avec succes");
					init.setStyle(PmkStyleTable.orangeBold());
					sender.sendMessage(init);
				}
			}
		}
	}

	@Override
	public List<String> getPermission() {
		// TODO Auto-generated method stub
		return Arrays.asList("pumpmykins.staff.responsable");
	}

}
