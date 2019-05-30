package fr.pumpmykins.kit.util;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.permission.PermissionAPI;
import scala.actors.threadpool.Arrays;

public abstract class ISubCommand {

	public abstract List<String> getPermission();

	public abstract void onCommand(MinecraftServer server, ICommandSender sender, String[] args);

	public boolean runCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(sender instanceof EntityPlayer) {
			if(args.length > 0)
				args = (String[]) Arrays.copyOfRange(args, 1, args.length);


			if(getPermission() != null && !getPermission().isEmpty()) {

				for(String s : getPermission()) {

					if(PermissionAPI.hasPermission((EntityPlayer) sender, s)) {

						onCommand(server, sender, args);
						break;
					}
				}

			} else
				onCommand(server, sender, args);

			return true;
		}
		return false;
	}
}
