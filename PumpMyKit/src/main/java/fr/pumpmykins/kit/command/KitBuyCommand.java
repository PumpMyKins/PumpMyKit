package fr.pumpmykins.kit.command;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fr.pumpmykins.kit.util.ISubCommand;
import fr.pumpmykins.kit.util.KitUtils;
import fr.pumpmykins.kit.util.PmkStyleTable;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class KitBuyCommand extends ISubCommand {

	@Override
	public void onCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
		if(args.length > 0) {
			
			if(args[0].equals("tier")) {
				
				try {
					
					KitUtils.setRandomUse(UUID.fromString(args[1]), 0);
					KitUtils.setSelectUse(UUID.fromString(args[1]), 0);
				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} 
		if(args.length > 2) {
			
			try {
				KitUtils.add(args[0], args[1], args[2]);
				System.out.println(args[0] + "<-- id, "+ args[1]+ "<-- username,"+ args[2] +"<-- kitname");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(sender instanceof EntityPlayer) {
				ITextComponent init = new TextComponentString("Ajout du Kit :"+args[2]+", Pour :"+args[1]);
				init.setStyle(PmkStyleTable.orangeBold());
				sender.sendMessage(init);
			}
		}
	}

	@Override
	public List<String> getPermission() {
		
		return Arrays.asList("pumpmykins.staff.responsable");
	}
	
	
}
