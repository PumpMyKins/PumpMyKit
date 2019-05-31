package fr.pumpmykins.kit.command;

import java.util.ArrayList;
import java.util.List;

import fr.pumpmykins.kit.util.ISubCommand;
import fr.pumpmykins.kit.util.PmkStyleTable;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.permission.PermissionAPI;

public class KitHelpCommand extends ISubCommand {

	@Override
	public void onCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
		List<ITextComponent> message = new ArrayList<ITextComponent>();
		
		ITextComponent init = new TextComponentString("Liste des commandes du mod PmkKit :");
		init.setStyle(PmkStyleTable.orangeBold());
		message.add(init);
		
		ITextComponent getCommand = new TextComponentString("/kit <kitname>");
		ITextComponent getCommandbis = new TextComponentString(" (Commande pour obtenir un Kit)");
		getCommand.setStyle(PmkStyleTable.importantInfo(" "));
		getCommandbis.setStyle(PmkStyleTable.detailsInfo());
		getCommand.appendSibling(getCommandbis);
		message.add(getCommand);
		
		ITextComponent listCommand = new TextComponentString("/kit list");
		ITextComponent listCommandbis = new TextComponentString(" (Affiche la liste des kit)");
		listCommand.setStyle(PmkStyleTable.importantInfo("list"));
		listCommandbis.setStyle(PmkStyleTable.detailsInfo());
		listCommand.appendSibling(listCommandbis);
		message.add(listCommand);
		
		ITextComponent viewCommand = new TextComponentString("/kit view <kitname>");
		ITextComponent viewCommandbis = new TextComponentString(" (Affiche les items du kit)");
		viewCommand.setStyle(PmkStyleTable.importantInfo("view"));
		viewCommandbis.setStyle(PmkStyleTable.detailsInfo());
		viewCommand.appendSibling(viewCommandbis);
		message.add(viewCommand);
		
		// TIER COMMAND
		
		if(PermissionAPI.hasPermission((EntityPlayer) sender, "rank.tier1") || PermissionAPI.hasPermission((EntityPlayer) sender, "rank.tier2") || PermissionAPI.hasPermission((EntityPlayer) sender, "rank.tier3")) {
			
			ITextComponent randomCommand = new TextComponentString("/kit random");
			ITextComponent randomCommandbis = new TextComponentString(" (Pour avoir accès à un kit Random !)");
			randomCommand.setStyle(PmkStyleTable.importantInfo("random"));
			randomCommandbis.setStyle(PmkStyleTable.detailsInfo());
			randomCommand.appendSibling(randomCommandbis);
			message.add(randomCommand);
			
			ITextComponent selectCommand = new TextComponentString("/kit select <kitname>");
			ITextComponent selectCommandbis = new TextComponentString("(Vous donne accès au kit de votre choix !)");
			selectCommand.setStyle(PmkStyleTable.importantInfo("select"));
			selectCommandbis.setStyle(PmkStyleTable.detailsInfo());
			selectCommand.appendSibling(selectCommandbis);
			message.add(selectCommand);
			
		}
		
		// ADMIN COMMAND
		
		if(PermissionAPI.hasPermission((EntityPlayer) sender, "rank.staff.responsable") || PermissionAPI.hasPermission((EntityPlayer) sender, "rank.staff.modo") || PermissionAPI.hasPermission((EntityPlayer) sender, "rank.staff.admin")) {
		
			ITextComponent modifyCommand = new TextComponentString("/kit modify <kitname> ");
			ITextComponent modifyCommandbis = new TextComponentString("(Permet d'aller modifier un kit)");
			modifyCommand.setStyle(PmkStyleTable.importantInfo("modify"));
			modifyCommandbis.setStyle(PmkStyleTable.detailsInfo());
			modifyCommand.appendSibling(modifyCommandbis);
			message.add(modifyCommand);
			
			ITextComponent validCommand = new TextComponentString("/kit valid <kitname> ");
			ITextComponent validCommandbis = new TextComponentString("(Permet de valider la modification d'un kit)");
			validCommand.setStyle(PmkStyleTable.importantInfo("valid"));
			validCommand.setStyle(PmkStyleTable.detailsInfo());
			validCommand.appendSibling(validCommandbis);
			message.add(validCommand);
			
			ITextComponent addCommand = new TextComponentString("/kit add <kitname> ");
			ITextComponent addCommandbis = new TextComponentString("(Génère un coffre et l'enrégistre en temps que kit)");
			addCommand.setStyle(PmkStyleTable.importantInfo("add"));
			addCommandbis.setStyle(PmkStyleTable.detailsInfo());
			addCommand.appendSibling(addCommandbis);
			message.add(addCommand);
			
			ITextComponent deleteCommand = new TextComponentString("/kit delete <kitname> ");
			ITextComponent deleteCommandbis = new TextComponentString("(Supprime le kit)");
			deleteCommand.setStyle(PmkStyleTable.importantInfo("delete"));
			deleteCommandbis.setStyle(PmkStyleTable.detailsInfo());
			deleteCommand.appendSibling(deleteCommandbis);
			message.add(deleteCommand);
		}
		
		for(ITextComponent msg : message) {
			
			sender.sendMessage(msg);
		}
		
	}

	@Override
	public List<String> getPermission() {
		
		return null;
	}
}
