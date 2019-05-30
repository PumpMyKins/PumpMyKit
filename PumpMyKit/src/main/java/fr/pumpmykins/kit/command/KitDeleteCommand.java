package fr.pumpmykins.kit.command;

import java.util.Arrays;
import java.util.List;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import fr.pumpmykins.kit.util.ISubCommand;
import fr.pumpmykins.kit.util.PmkStyleTable;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

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
		return Arrays.asList("rank.staff.responsable");
	}

}
