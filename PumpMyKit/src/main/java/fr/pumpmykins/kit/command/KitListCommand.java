package fr.pumpmykins.kit.command;

import java.util.List;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import fr.pumpmykins.kit.util.ISubCommand;
import fr.pumpmykins.kit.util.PmkStyleTable;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class KitListCommand extends ISubCommand {

	private KitList kitlist;
	
	public KitListCommand(KitList kitlistinstance) {
		this.kitlist = kitlistinstance;
	}

	@Override
	public List<String> getPermission() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
		ITextComponent init = new TextComponentString("Liste des Kits : ");
		init.setStyle(PmkStyleTable.orangeBold());

		sender.sendMessage(init);

		for(Kit k : kitlist.getKitlist()) {

			ITextComponent item = new TextComponentString(k.getName());
			item.setStyle(PmkStyleTable.itemList());
			sender.sendMessage(item);

		}
	}

}
