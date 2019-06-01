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

public class KitRandomCommand extends ISubCommand {

	private KitList kitlist;

	public KitRandomCommand(KitList kitlistinstance) {

		this.kitlist = kitlistinstance;
	}

	@Override
	public void onCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(sender instanceof EntityPlayer) {

			try {
				
				EntityPlayer player = (EntityPlayer) sender;
				Optional<Integer> kitnum = KitUtils.getRandomUse(player.getUniqueID());
				int randomRestriction = 0;
				if(PermissionAPI.hasPermission(player, "rank.tier1"))
					randomRestriction = 1;
				if(PermissionAPI.hasPermission(player, "rank.tier2"))
					randomRestriction = 3;
				if(PermissionAPI.hasPermission(player, "rank.tier3"))
					randomRestriction = 5;
				int randomCount = randomRestriction;

				if(kitnum.isPresent()) {

					randomCount = randomCount - kitnum.get();

				} else {

					KitUtils.randomFirstUse(player.getUniqueID(), 0);
					kitnum = Optional.of(0);
				}

				if(randomCount > 0) {

					List<Kit> kit = this.kitlist.getKitlist();
					int size = kit.size();
					Random rand = new Random();
					Kit k = kit.get(rand.nextInt(size));

					String buyId = k.getName()+player.getName();
					int random = rand.nextInt(100000);
					buyId = buyId.concat(Integer.toString(random));

					server.getCommandManager().executeCommand(server, "kit buy "+buyId+" "+player.getName()+" "+k.getName());

					int newnum = kitnum.get() + 1 ;

					KitUtils.setRandomUse(player.getUniqueID(), newnum);

					ITextComponent init = new TextComponentString("Bravo vous avez maintenant acces au kit : ");
					init.setStyle(PmkStyleTable.orangeBold());

					ITextComponent kitname = new TextComponentString(k.getName());
					kitname.setStyle(PmkStyleTable.itemNumber());
					init.appendSibling(kitname);
					sender.sendMessage(init);

				} else {

					ITextComponent refuse = new TextComponentString("Vous n'avez aucun kit random en stock !");
					refuse.setStyle(PmkStyleTable.orangeBold());
					sender.sendMessage(refuse);
				}
			} catch(SQLException e1) {

				e1.printStackTrace();
			}
		}
	}

	@Override
	public List<String> getPermission() {
		// TODO Auto-generated method stub
		return Arrays.asList("rank.tier1", "rank.tier2", "rank.tier3");
	}


}
