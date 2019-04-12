package fr.pumpmykins.kit.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import fr.pumpmykins.kit.util.KitUtils;
import fr.pumpmykins.kit.util.PmkStyleTable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
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

public class KitGetCommand implements ICommand {

	private KitList kitlist;
	
	public KitGetCommand(KitList kitlistinstance) {
		
		this.setKitlist(kitlistinstance);
	}

	@Override
	public int compareTo(ICommand o) {
		
		return 0;
	}

	@Override
	public String getName() {
		
		return "kit";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		
		return "kit.syntax.help";
	}

	@Override
	public List<String> getAliases() {
		
		return Lists.newArrayList("k");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(sender instanceof EntityPlayer) {
			
			EntityPlayer player = (EntityPlayer) sender;
			
			if(args.length < 1) {
				
				getUsage(sender);
			} else {
				
				if(kitlist.getKit(args[0]) == null || args[0] == "list") {
					
					ITextComponent init = new TextComponentString("Liste des Kits : ");
					init.setStyle(PmkStyleTable.orangeBold());
					
					sender.sendMessage(init);
					
					for(Kit k : kitlist.getKitlist()) {
						
						ITextComponent item = new TextComponentString(k.getName());
						item.setStyle(PmkStyleTable.itemList());
						sender.sendMessage(item);
						
					}
				} else {
					
					try {
						
						if(KitUtils.getKitUse(player , args[0]) > 0) {
							
							KitUtils.kitUse(player, args[0]);
							
							Kit k = kitlist.getKit(args[0]);
							
							World w = server.getWorld(0);
							
							BlockPos chest_pos = new BlockPos(k.getX(), k.getY(), k.getZ());
							
							TileEntity te = w.getTileEntity(chest_pos);
								
							IItemHandler ih = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
												
							int inventorySpace = 0;
							int kitsize = 0;
							
							List<ItemStack> lis = player.inventory.mainInventory;
							List<Integer> emptySlot = new ArrayList<Integer>();
							
							for(int i = 0; i < 36; i++) {
								
								ItemStack is = lis.get(i);
								if(is.isEmpty()) {
									
									inventorySpace++;
									emptySlot.add(i);
								}
							}
							
							for(int i = 0; i < ih.getSlots(); i++) {
								
								ItemStack is = ih.getStackInSlot(i);
								if(!is.isEmpty()) {
									
									kitsize++;
								}
							}
							
							Iterator<Integer> emptySl = emptySlot.iterator();
							
							if(inventorySpace < kitsize) {
								
								for(int i = 0; i < ih.getSlots(); i++) {
										
									ItemStack is = ih.getStackInSlot(i);
									if(!is.isEmpty()) {
										player.entityDropItem(is, 0F);
									}
								}
							} else {
								for(int i = 0; i < ih.getSlots(); i++) {
									
									if(!ih.getStackInSlot(i).isEmpty())
										player.inventory.mainInventory.set(emptySl.next(), ih.getStackInSlot(i));
									
								}
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
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

	/**
	 * @return the kitlist
	 */
	public KitList getKitlist() {
		return kitlist;
	}

	/**
	 * @param kitlist the kitlist to set
	 */
	public void setKitlist(KitList kitlist) {
		this.kitlist = kitlist;
	}

}
