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
		
		ITextComponent getCommand = new TextComponentString("/kit <kitname> ou /kit list (Commande pour obtenir un Kit)");
		getCommand.setStyle(PmkStyleTable.itemList());
		message.add(getCommand);
		
		ITextComponent viewCommand = new TextComponentString("/kit view <kitname> (Affiche les items du kit)");
		viewCommand.setStyle(PmkStyleTable.itemList());
		message.add(viewCommand);
		
		// ADMIN COMMAND
		
		if(PermissionAPI.hasPermission((EntityPlayer) sender, "kit.admin")) {
		
			ITextComponent modifyCommand = new TextComponentString("/kitmodify <kitname> (Permet d'aller modifier un kit)");
			modifyCommand.setStyle(PmkStyleTable.itemList());
			message.add(modifyCommand);
			
			ITextComponent validCommand = new TextComponentString("/kitvalid <kitname> (Permet de valider la modification d'un kit)");
			validCommand.setStyle(PmkStyleTable.itemList());
			message.add(validCommand);
			
			ITextComponent addCommand = new TextComponentString("/kitadd <kitname> (Génère un coffre et l'enrégistre en temps que kit)");
			addCommand.setStyle(PmkStyleTable.itemList());
			message.add(addCommand);
			
			ITextComponent buyCommand = new TextComponentString("/kitbuy <kitname> <username> <buyId> (Only for funder)");
			buyCommand.setStyle(PmkStyleTable.itemList());
			message.add(buyCommand);
			
			ITextComponent deleteCommand = new TextComponentString("/kitdelete <kitname> (Supprime le kit)");
			deleteCommand.setStyle(PmkStyleTable.itemList());
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
