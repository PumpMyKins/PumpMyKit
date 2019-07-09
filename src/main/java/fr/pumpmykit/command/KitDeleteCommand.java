package old.fr.pumpmykit.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import old.fr.pumpmykit.Kit;
import old.fr.pumpmykit.KitList;
import old.fr.pumpmykit.util.ISubCommand;
import old.fr.pumpmykit.util.PmkStyleTable;

public class KitDeleteCommand extends ISubCommand {

	private KitList kitlist;

	public KitDeleteCommand(KitList kitlistinstance) {
		this.kitlist = kitlistinstance;
	}

	@Override
	public void onCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
		if(args.length > 0) {
			if(this.kitlist.getKit(args[0]) != null) {
				
				Kit k = this.kitlist.getKit(args[0]);
				
				BlockPos chest_pos = new BlockPos(k.getX(), k.getY(), k.getZ());
				
				World w = server.getWorld(0);
				
				w.setBlockToAir(chest_pos);
				w.setBlockToAir(chest_pos.north());
				w.setBlockToAir(chest_pos.east());
				w.setBlockToAir(chest_pos.west());
				w.setBlockToAir(chest_pos.south());
				w.setBlockToAir(chest_pos.down());
				w.setBlockToAir(chest_pos.up());
				
				this.kitlist.removeKit(args[0]);
				
				ITextComponent init = new TextComponentString("Vous venez de supprimer le kit :"+k.getName());
				init.setStyle(PmkStyleTable.orangeBold());
				sender.sendMessage(init);
				
			}
		}
	}

	@Override
	public List<String> getPermission() {
		// TODO Auto-generated method stub
		return Arrays.asList("pumpmykins.staff.responsable");
	}

}
