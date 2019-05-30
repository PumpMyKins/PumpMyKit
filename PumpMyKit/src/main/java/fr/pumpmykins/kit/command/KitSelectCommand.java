package fr.pumpmykins.kit.command;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import fr.pumpmykins.kit.Kit;
import fr.pumpmykins.kit.KitList;
import fr.pumpmykins.kit.util.ISubCommand;
import fr.pumpmykins.kit.util.KitUtils;
import fr.pumpmykins.kit.util.PmkStyleTable;

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


			EntityPlayer player = (EntityPlayer) sender;
			Optional<Integer> kitnum = null;
			try {
				kitnum = KitUtils.getSelectUse(player.getUniqueID());
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}

			int selectRestriction = 0;
			if(PermissionAPI.hasPermission(player, "rank.tier1"))
				selectRestriction = 1;
			if(PermissionAPI.hasPermission(player, "rank.tier2"))
				selectRestriction = 3;
			if(PermissionAPI.hasPermission(player, "rank.tier3"))
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

					server.getCommandManager().executeCommand(server, "kitbuy "+buyId+" "+player.getName()+" "+k.getName());

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
		}
	}

	@Override
	public List<String> getPermission() {
		// TODO Auto-generated method stub
		return Arrays.asList("rank.tier1", "rank.tier2", "rank.tier3");
	}

}
