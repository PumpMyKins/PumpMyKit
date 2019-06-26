package fr.pumpmykit.command;


import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import fr.pumpmykit.Kit;
import fr.pumpmykit.util.KitUtils;
import fr.pumpmykit.util.PmkStyleTable;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.server.permission.PermissionAPI;

public class KitGetCommand {

	public KitGetCommand(ICommandSender sender, Kit k) throws SQLException {
		
		EntityPlayer player = (EntityPlayer) sender;
		
		if(KitUtils.getKitUse(player , k.getName()).size() > 0) {

			KitUtils.useKit(player, k.getName());
	
			World w = sender.getServer().getWorld(0);
			
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

			if(inventorySpace < kitsize) {

				for(int i = 0; i < ih.getSlots(); i++) {

					ItemStack is = ih.getStackInSlot(i).copy();
					if(!is.isEmpty()) {
						player.entityDropItem(is, 0F);
					}
				}
			} else {
				
				Iterator<Integer> emptySl = emptySlot.iterator();
				
				for(int i = 0; i < ih.getSlots(); i++) {

					if(!ih.getStackInSlot(i).isEmpty())
						player.inventory.mainInventory.set(emptySl.next(), ih.getStackInSlot(i).copy());

				}
			}

			ITextComponent init = new TextComponentString("Vous venez de recevoir le kit : ");
			init.setStyle(PmkStyleTable.orangeBold());

			ITextComponent kitname = new TextComponentString(k.getName());
			kitname.setStyle(PmkStyleTable.itemNumber());
			init.appendSibling(kitname);

			player.sendMessage(init);
		} else {

			ITextComponent kitname = new TextComponentString("Vous n'avez pas le kit "+k.getName());
			kitname.setStyle(PmkStyleTable.orangeBold());
			player.sendMessage(kitname);
			
			if(PermissionAPI.hasPermission(player, "pumpmykins.vip.tier1") || PermissionAPI.hasPermission(player, "pumpmykins.vip.tier2") || PermissionAPI.hasPermission(player, "pumpmykins.vip.tier3")) {

				try {

					Optional<Integer> kitnum = KitUtils.getSelectUse(player.getUniqueID());

					if(!kitnum.isPresent()) {

						kitnum = Optional.of(0);
					}


					int selectRestriction = 0;
					if(PermissionAPI.hasPermission(player, "pumpmykins.vip.tier1"))
						selectRestriction = 1;
					if(PermissionAPI.hasPermission(player, "pumpmykins.vip.tier3"))
						selectRestriction = 3;
					if(PermissionAPI.hasPermission(player, "pumpmykins.vip.tier2"))
						selectRestriction = 5;
					int selectCount = selectRestriction - kitnum.get();

					if(selectCount > 0) {

						ITextComponent init = new TextComponentString("Dans le cadre de votre Tier il vous reste "+selectCount+" kit gratuit � selectionner !");
						init.setStyle(PmkStyleTable.orangeBold());

						sender.sendMessage(init);


					}
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
