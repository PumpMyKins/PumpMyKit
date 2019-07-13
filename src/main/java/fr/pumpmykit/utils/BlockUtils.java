package fr.pumpmykit.utils;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

public class BlockUtils {

	public static BlockPos getPosBlockYouAreLooking(EntityPlayer p) {
		
		World world = p.getEntityWorld();
		
		RayTraceResult result = p.rayTrace(5, 1.0F);
		
		if(result.typeOfHit == Type.BLOCK) {
			
			return result.getBlockPos();
			
		}
		
		return null;
		
	}
	
}
