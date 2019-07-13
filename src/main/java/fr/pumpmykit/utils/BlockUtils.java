package fr.pumpmykit.utils;

import java.util.ArrayList;
import java.util.List;

import fr.pumpmykit.exceptions.UnfoundKitChestException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class BlockUtils {

	public static BlockPos getPosBlockYouAreLooking(EntityPlayer p) {
		
		RayTraceResult result = p.rayTrace(5, 1.0F);
		
		if(result.typeOfHit == Type.BLOCK) {
			
			return result.getBlockPos();
			
		}
		
		return null;
		
	}
	
	public static List<ItemStack> getChestBlockContent(BlockPos pos){
		
		return null;
		
	}
	
	public static void loadContentInChestBlock(BlockPos pos, List<ItemStack> content) {
		
		
		
	}
	
}
