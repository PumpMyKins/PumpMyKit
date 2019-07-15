package fr.pumpmykit.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.pumpmykit.MainKit;
import fr.pumpmykit.exceptions.DuplicateKitException;
import fr.pumpmykit.exceptions.KitIsEmptyException;
import fr.pumpmykit.exceptions.UnfoudKitException;
import fr.pumpmykit.exceptions.UnfoundKitChestException;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
		return "kitadmin";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/kitadmin help";
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

		String subCommand = args[0];
		if(subCommand.trim().isEmpty()) {			
			this.helpSubCommand(server,sender,args);			
		}else if(subCommand.equalsIgnoreCase("add")) {			
			this.addSubCommand(server,sender,args);			
		}else if(subCommand.equalsIgnoreCase("remove")) {			
			this.removeSubCommand(server,sender,args);			
		}else if(subCommand.equalsIgnoreCase("load")) {			
			this.loadSubCommand(server,sender,args);		
		}else if(subCommand.equalsIgnoreCase("update-content")) {			
			this.updateContentSubCommand(server,sender,args);			
		}else if(subCommand.equalsIgnoreCase("help")) {			
			this.helpSubCommand(server,sender,args);			
		}else {

			this.synthaxErrorMessage(sender);

		}		

	}

	private void updateContentSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(args.length != 2) {
			this.synthaxErrorMessage(sender);
			return;			
		}

		String name = args[1];
		try {

			EntityPlayerMP player = (EntityPlayerMP) sender;
			MainKit.KITSMANAGER.updateContentKit(player,name);

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Kit modifié !");
			txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

		} catch (UnfoudKitException e) {

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Kit introuvable !");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

			txt = new TextComponentString("Essayez : ");
			txt.setStyle(new Style().setColor(TextFormatting.RED));

			txt2 = new TextComponentString("/kit list");
			txt2.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit list")));

			txt.appendSibling(txt2);

			sender.sendMessage(txt);

		} catch (UnfoundKitChestException e) {

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Vous devez etre un block au dessus du coffre dans lequel vous avez importer le kit à modifier !");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

		} catch (KitIsEmptyException e) {

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Le kit que vous essayez de créer est vide !");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

		}

	}

	private void loadSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(args.length != 2) {
			this.synthaxErrorMessage(sender);
			return;			
		}

		String name = args[1];
		try {

			EntityPlayerMP player = (EntityPlayerMP) sender;
			MainKit.KITSMANAGER.loadKit(player,name);

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Kit chargé !");
			txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

		} catch (UnfoudKitException e) {

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Kit introuvable !");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

			txt = new TextComponentString("Essayez : ");
			txt.setStyle(new Style().setColor(TextFormatting.RED));

			txt2 = new TextComponentString("/kit list");
			txt2.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit list")));

			txt.appendSibling(txt2);

			sender.sendMessage(txt);

		} catch (UnfoundKitChestException e) {

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Vous devez etre un block au dessus du coffre dans lequel vous voulez importer le kit !");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

		}

	}

	private void removeSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(args.length != 2) {
			this.synthaxErrorMessage(sender);
			return;			
		}

		String name = args[1];
		try {

			MainKit.KITSMANAGER.removeKit(name);

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Kit supprimé !");
			txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

		} catch (UnfoudKitException e) {

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Kit introuvable !");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

			txt = new TextComponentString("Essayez : ");
			txt.setStyle(new Style().setColor(TextFormatting.RED));

			txt2 = new TextComponentString("/kit list");
			txt2.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit list")));

			txt.appendSibling(txt2);

			sender.sendMessage(txt);

		}

	}

	private void addSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(args.length != 3) {
			this.synthaxErrorMessage(sender);
			return;			
		}

		String name = args[1];
		String displayName = args[2];


		EntityPlayerMP player = (EntityPlayerMP) sender;
		try {

			MainKit.KITSMANAGER.addKit(player,name,displayName);

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Kit ajouté !");
			txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

		} catch (UnfoundKitChestException e) {

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Vous devez etre un block au dessus du coffre dans lequel vous avez ajouté le contenu du le kit à ajouter !");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

		} catch (KitIsEmptyException e) {

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Le kit que vous essayez de créer est vide !");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

		} catch (DuplicateKitException e) {

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Kit du même nom déjà existant !");
			txt2.setStyle(new Style().setColor(TextFormatting.RED));
			txt.appendSibling(txt2);
			sender.sendMessage(txt);

			txt = new TextComponentString("Essayez : ");
			txt.setStyle(new Style().setColor(TextFormatting.RED));

			txt2 = new TextComponentString("/kit list");
			txt2.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit list")));

			txt.appendSibling(txt2);

			sender.sendMessage(txt);

		}

	}

	private void helpSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
		ITextComponent txt2 = new TextComponentString("Liste des commandes staff : ");
		txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
		txt.appendSibling(txt2);
		sender.sendMessage(txt);
		
		txt = new TextComponentString("/kit add name display_name");
		txt.setStyle(new Style().setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit add")));
		txt2 = new TextComponentString("Commande pour ajouter un kit !");
		txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
		txt.appendSibling(txt2);
		sender.sendMessage(txt);
		
		txt = new TextComponentString("/kit remove name");
		txt.setStyle(new Style().setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit remove")));
		txt2 = new TextComponentString("Commande pour supprimer un kit !");
		txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
		txt.appendSibling(txt2);
		sender.sendMessage(txt);
		
		txt = new TextComponentString("/kit load name");
		txt.setStyle(new Style().setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit load")));
		txt2 = new TextComponentString("Pour charger le kit dans le coffre en dessou de vous !");
		txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
		txt.appendSibling(txt2);
		sender.sendMessage(txt);
		
		txt = new TextComponentString("/kit update-content name");
		txt.setStyle(new Style().setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit update-content")));
		txt2 = new TextComponentString("Pour modifier le contenu d'un kit par le contenu du coffre en dessou de vous !");
		txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
		txt.appendSibling(txt2);
		sender.sendMessage(txt);
		
		/**
		 * 
		 * l.add("help");
			l.add("add");
			l.add("remove");
			l.add("load");
			l.add("update-content");
		 */
	}

	private void synthaxErrorMessage(ICommandSender sender) {

		ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
		ITextComponent txt2 = new TextComponentString("Erreur synthaxe !");
		txt2.setStyle(new Style().setColor(TextFormatting.RED));
		txt.appendSibling(txt2);
		sender.sendMessage(txt);

		txt = new TextComponentString("Voir : ");
		txt.setStyle(new Style().setColor(TextFormatting.RED));

		txt2 = new TextComponentString("/kitadmin help");
		txt2.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit help")));

		txt.appendSibling(txt2);

		sender.sendMessage(txt);

	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if(args.length == 0) {

			List<String> l = new ArrayList<String>();

			l.add("help");
			l.add("add");
			l.add("remove");
			l.add("load");
			l.add("update-content");

			return l;

		}

		if(args.length == 1) {

			MainKit.KITSMANAGER.getKitList().getKitlist().keySet();

		}

		return Collections.emptyList();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
