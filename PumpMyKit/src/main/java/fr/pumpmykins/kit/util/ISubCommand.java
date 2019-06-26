package fr.pumpmykins.kit.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.permission.PermissionAPI;

public abstract class ISubCommand {

	public abstract List<String> getPermission();

	public abstract void onCommand(MinecraftServer server, ICommandSender sender, String[] args);

	public boolean runCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(sender instanceof EntityPlayer) {
			if(args.length > 0)
				args = (String[]) Arrays.copyOfRange(args, 1, args.length);


			if(getPermission() != null && !getPermission().isEmpty()) {
				
				boolean allow = false;
				
				for(String s : getPermission()) {

					if(PermissionAPI.hasPermission((EntityPlayer) sender, s)) {
						
						allow = true;
						break;
					}
				}
				if(allow)
					onCommand(server, sender, args);
				else {
					ITextComponent permission = new TextComponentString("Vous n'avez pas la permission de faire cela");
					permission.setStyle(PmkStyleTable.orangeBold());
					sender.sendMessage(permission);
				}
					

			} else
				onCommand(server, sender, args);

			return true;
		} else {
			if(args.length > 0)
				args = (String[]) Arrays.copyOfRange(args, 1, args.length);
			onCommand(server, sender, args);
		}
		return false;
	}
}
