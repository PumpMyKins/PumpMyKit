package fr.pumpmykit.command;

import java.util.Collections;
import java.util.List;
import fr.pumpmykit.MainKit;
import fr.pumpmykit.exceptions.UnfoudKitException;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

public class KitAdminCommand implements ICommand {

	public KitAdminCommand() {

		PermissionAPI.registerNode("pumpmykit.command.kitadmin", DefaultPermissionLevel.NONE, "Command Kit Permission");

	}

	@Override
	public int compareTo(ICommand arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return "kit";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/kit help";
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		if(!(sender instanceof EntityPlayer)) {
			sender.sendMessage(new TextComponentString("§cPlayer command only !"));
			return;
		}

		EntityPlayer player = (EntityPlayer) sender;

		if(!PermissionAPI.hasPermission(player, "pumpmykit.command.kitadmin")) {

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Fonctionnalité réservée aux membres du staff !");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);
			return;
			
		}
		
		if(args.length == 0) {
			
			this.helpSubCommand(server,sender,args);
			return;
		}
		
		

	}

	private void helpSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
