package fr.pumpmykit.utils;

import java.util.ArrayList;
import java.util.List;

import fr.pumpmykit.exceptions.UnfoundKitChestException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class BlockUtils {

	public static BlockPos getPosBlockYouAreLooking(EntityPlayerMP p) {
		
		RayTraceResult result = p.rayTrace(5, 1.0F);
		
		if(result.typeOfHit == Type.BLOCK) {
			
			return result.getBlockPos();
			
		}
		
		return null;
		
	}
	
	public static List<ItemStack> getChestBlockContent(World world, BlockPos pos) throws UnfoundKitChestException{		
		
		if(!world.getBlockState(pos).getBlock().equals(Blocks.CHEST)) {
		    throw new UnfoundKitChestException();
		}
		
		TileEntity te = world.getTileEntity(pos);		
		if(te == null) {
			 throw new UnfoundKitChestException();
		}
		
		TileEntityChest chest = (TileEntityChest) te;
		
		List<ItemStack> content = new ArrayList<>();
		for (int slot = 0; slot < chest.getSizeInventory(); slot++) {

			ItemStack item = chest.getStackInSlot(slot);	
			if(item == null || item.isEmpty()) {
				continue;
			}
			content.add(item);

		}		
		
		chest.clear();
		
		return content;
	}
	
	public static void loadContentInChestBlock(World world, BlockPos pos, List<ItemStack> content) throws UnfoundKitChestException {
		
		if(!world.getBlockState(pos).getBlock().equals(Blocks.CHEST)) {
		    throw new UnfoundKitChestException();
		}
		
		TileEntity te = world.getTileEntity(pos);		
		if(te == null) {
			 throw new UnfoundKitChestException();
		}
		
		TileEntityChest chest = (TileEntityChest) te;
		
		chest.clear();
		
		for (int slot = 0; slot < content.size(); slot++) {

			chest.setInventorySlotContents(slot, content.get(slot));

		}	
		
		chest.markDirty();
	}
	
}
