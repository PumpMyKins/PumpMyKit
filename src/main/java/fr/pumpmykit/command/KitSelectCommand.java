package fr.pumpmykit.command;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
import net.minecraftforge.server.permission.PermissionAPI;

public class KitSelectCommand extends ISubCommand {

	private KitList kitlist;

	public KitSelectCommand(KitList kitlistinstance) {

		this.kitlist = kitlistinstance;
	}

	@Override
	public void onCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(sender instanceof EntityPlayer) {

			if(args.length > 0) {
				EntityPlayer player = (EntityPlayer) sender;
				Optional<Integer> kitnum = null;
				try {
					kitnum = KitUtils.getSelectUse(player.getUniqueID());
				} catch (SQLException e1) {
					
					e1.printStackTrace();
				}
	
				int selectRestriction = 0;
				if(PermissionAPI.hasPermission(player, "pumpmykins.vip.tier1"))
					selectRestriction = 1;
				if(PermissionAPI.hasPermission(player, "pumpmykins.vip.tier2"))
					selectRestriction = 3;
				if(PermissionAPI.hasPermission(player, "pumpmykins.vip.tier3"))
					selectRestriction = 5;
				int selectCount = selectRestriction;
	
				if(kitnum.isPresent()) {
	
					selectCount = selectCount - kitnum.get();
					
				} else {
	
					try {
						KitUtils.selectFirstUse(player.getUniqueID(), 0);
					} catch (SQLException e) {
						
						e.printStackTrace();
					}
					kitnum = Optional.of(0);
				}
	
				if(selectCount > 0) {
	
					if(this.kitlist.getKit(args[0]) != null) {
						Kit k = kitlist.getKit(args[0]);
	
						Random rand = new Random();
						int select = rand.nextInt(100000);
						String buyId = k.getName()+player.getName();
						buyId = buyId.concat(Integer.toString(select));
	
						server.getCommandManager().executeCommand(server, "kit buy "+buyId+" "+player.getName()+" "+k.getName());
	
						int newnum = kitnum.get() + 1 ;
	
						try {
							KitUtils.setSelectUse(player.getUniqueID(), newnum);
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
						
						ITextComponent init = new TextComponentString("Vous avez maintenant acces au kit : ");
						init.setStyle(PmkStyleTable.orangeBold());
	
						ITextComponent kitname = new TextComponentString(k.getName());
						kitname.setStyle(PmkStyleTable.itemNumber());
						init.appendSibling(kitname);
						sender.sendMessage(init);
	
					} else {
	
						ITextComponent refuse = new TextComponentString("Le kit n'existe pas !");
						refuse.setStyle(PmkStyleTable.orangeBold());
						sender.sendMessage(refuse);
					}
				}	else {
	
					ITextComponent refuse = new TextComponentString("Vous n'avez aucun kit select en stock !");
					refuse.setStyle(PmkStyleTable.orangeBold());
					sender.sendMessage(refuse);
				}
			} else {
				
				ITextComponent refuse = new TextComponentString("Syntax : /kit select <kitname>");
				refuse.setStyle(PmkStyleTable.orangeBold());
				sender.sendMessage(refuse);
			}
		}
	}

	@Override
	public List<String> getPermission() {
		// TODO Auto-generated method stub
		return Arrays.asList("pumpmykins.vip.tier1", "pumpmykins.vip.tier2", "pumpmykins.vip.tier3");
	}

}