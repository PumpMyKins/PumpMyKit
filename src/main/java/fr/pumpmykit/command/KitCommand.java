package fr.pumpmykit.command;

import java.util.Collections;
import java.util.List;

import fr.pumpmykit.MainKit;
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

public class KitCommand implements ICommand {
	
	public KitCommand() {
		
		PermissionAPI.registerNode("pumpmykit.command.kit", DefaultPermissionLevel.NONE, "Command Kit Permission");
		
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
		
		if(!PermissionAPI.hasPermission(player, "pumpmykit.command.kit")) {
			
			ITextComponent txt = MainKit.CHAT_PREFIX;
			ITextComponent txt2 = new TextComponentString("Fonctionnalité achetable en boutique");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);
			
			txt = new TextComponentString("Voir : ");
			txt.setStyle(new Style().setColor(TextFormatting.RED));
			
			txt2 = new TextComponentString("http://store.pumpmykins.eu/");
			txt2.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.OPEN_URL, "http://store.pumpmykins.eu/")));
		
			txt.appendSibling(txt2);
			
			sender.sendMessage(txt);
			
			return;
			
		}
		
		if(args.length == 0) {
			
			this.helpSubCommand(server,sender,args);
			return;
			
		}
		
		String subCommand = args[0];
		if(subCommand.trim().isEmpty()) {			
			this.helpSubCommand(server,sender,args);			
		}else if(subCommand.equalsIgnoreCase("select")) {			
			this.selectSubCommand(server,sender,args);			
		}else if(subCommand.equalsIgnoreCase("random")) {			
			this.randomSubCommand(server,sender,args);			
		}else if(subCommand.equalsIgnoreCase("list")) {			
			this.listSubCommand(server,sender,args);		
		}else if(subCommand.equalsIgnoreCase("view")) {			
			this.viewSubCommand(server,sender,args);			
		}else {
			this.helpSubCommand(server,sender,args);
		}		

	}

	private void viewSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void listSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void randomSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
		
		
	}

	private void selectSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
		
		
	}

	private void helpSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
		
		
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
