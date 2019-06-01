package fr.pumpmykins.kit.command;

import java.util.List;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import fr.pumpmykins.kit.util.ISubCommand;
import fr.pumpmykins.kit.util.PmkStyleTable;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class KitViewCommand extends ISubCommand {
	
	private KitList kitlist;

	public KitViewCommand(KitList kitlistinstance) {
		this.kitlist = kitlistinstance;
	}

	@Override
	public void onCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
		if(args.length > 0) {
			
			if(sender instanceof EntityPlayer) {
				
				if(kitlist.getKit(args[0]) != null) {
					
					EntityPlayer player = (EntityPlayer) sender;
									
					Kit k = kitlist.getKit(args[0]);
					
					World w = server.getWorld(0);
					
					BlockPos chest_pos = new BlockPos(k.getX(), k.getY(), k.getZ());
					
					TileEntity te = w.getTileEntity(chest_pos);
						
					IItemHandler ih = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
										
					ITextComponent init = new TextComponentString("Liste des items du kit : "+k.getName());
					init.setStyle(PmkStyleTable.orangeBold());
					player.sendMessage(init);
					
					
					for(int i = 0; i < ih.getSlots(); i++) {
						
						ItemStack is = ih.getStackInSlot(i);
						if(!is.isEmpty()) {
		
							ITextComponent item = new TextComponentString(is.getDisplayName() +" : ");
							ITextComponent itemNumber = new TextComponentString(Integer.toString(is.getCount()));
							
							item.setStyle(PmkStyleTable.itemList());
							itemNumber.setStyle(PmkStyleTable.itemNumber());
							
							item.appendSibling(itemNumber);
							
							player.sendMessage(item);
						}
					}
					
				} else {
					
					ITextComponent init = new TextComponentString("Le kit n'existe pas !");
					init.setStyle(PmkStyleTable.orangeBold());
					sender.sendMessage(init);
				}
			}
		} else {
			
			
		}
	}

	@Override
	public List<String> getPermission() {
		
		return null;
	}

}
