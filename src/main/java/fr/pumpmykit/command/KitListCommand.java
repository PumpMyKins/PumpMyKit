package fr.pumpmykit.command;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.pumpmykit.Kit;
import fr.pumpmykit.KitList;
import fr.pumpmykit.util.ISubCommand;
import fr.pumpmykit.util.KitUtils;
import fr.pumpmykit.util.PmkStyleTable;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
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
		
		Map<String, Integer> allkituse = new HashMap<String, Integer>();
		
		try {
			allkituse = KitUtils.getAllKitUse((EntityPlayer) sender);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		for(Kit k : this.kitlist.getKitlist()) {

			ITextComponent name = new TextComponentString("Nom : ");
			ITextComponent namebis = new TextComponentString(k.getName());
			name.setStyle(PmkStyleTable.detailsInfo());
			namebis.setStyle(PmkStyleTable.importantInfo("view "+k.getName()));
			ITextComponent quantity = new TextComponentString(" | Quantite : ");
			ITextComponent quantitybis = new TextComponentString("");
			if(allkituse.get(k.getName()) != null) {
				quantitybis = new TextComponentString(Integer.toString(allkituse.get(k.getName())));
			}
			else {
				quantitybis = new TextComponentString("0");
			}
			quantity.setStyle(PmkStyleTable.detailsInfo());
			quantitybis.setStyle(PmkStyleTable.importantInfo(k.getName()));
			
			name.appendSibling(namebis);
			name.appendSibling(quantity);
			name.appendSibling(quantitybis);
			
			sender.sendMessage(name);

		}
	}

}
