package fr.pumpmykins.kit.command;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import fr.pumpmykins.kit.util.ISubCommand;
import fr.pumpmykins.kit.util.PmkStyleTable;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class KitModifyCommand extends ISubCommand {

	private KitList kitlist;

	public KitModifyCommand(KitList kitlistinstance) {
		this.kitlist = kitlistinstance;
	}

	@Override
	public void onCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
		if(sender instanceof EntityPlayer) {
			if(args.length > 0) {
				if(kitlist.getKit(args[0]) != null) {
				
					World w = server.getWorld(0);
					
					Kit k = this.kitlist.getKit(args[0]);
					
					Date date = new Date();
					k.setLast_update(date.toString());
					k.setLast_updator(((EntityPlayer) sender).getUniqueID());
					
					this.kitlist.removeKit(kitlist.getKit(args[0]));
					this.kitlist.addKit(k);
					
					this.kitlist.markDirty();
					
					BlockPos chest_pos = new BlockPos(k.getX(), k.getY(), k.getZ());
					
					w.setBlockToAir(chest_pos.north());
					w.setBlockToAir(chest_pos.east());
					w.setBlockToAir(chest_pos.west());
					w.setBlockToAir(chest_pos.south());
					w.setBlockToAir(chest_pos.down());
					w.setBlockToAir(chest_pos.up());
					
					((EntityPlayer) sender).setPosition(k.getX()-2, k.getY(), k.getZ());
				
					ITextComponent init = new TextComponentString("Protection du kit enlever avec succes, n'oublier pas de faire /kvalid <kitname> apres !");
					init.setStyle(PmkStyleTable.orangeBold());
					sender.sendMessage(init);
					
				}
			}
		}
	}

	@Override
	public List<String> getPermission() {
		
		return Arrays.asList("pumpmykins.staff.responsable");
	}

}
