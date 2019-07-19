package fr.pumpmykit.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import fr.pumpmykit.MainKit;
import fr.pumpmykit.exceptions.InsufisentGlobalRandomException;
import fr.pumpmykit.exceptions.InsufisentKitsToRandException;
import fr.pumpmykit.exceptions.InsufisentSelectException;
import fr.pumpmykit.exceptions.UnfoudKitException;
import fr.pumpmykit.exceptions.UnfoundSqlProfileException;
import fr.pumpmykit.utils.Kit;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
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
		}else if(subCommand.equalsIgnoreCase("help")) {			
			this.helpSubCommand(server,sender,args);			
		}else {

			this.synthaxErrorMessage(sender);

		}		

	}

	private void viewSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(args.length != 2) {
			this.synthaxErrorMessage(sender);
			return;			
		}

		String kitName = args[1];
		try {

			Kit kit = MainKit.KITSMANAGER.getKit(kitName);

			ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
			ITextComponent txt2 = new TextComponentString("Contenu du kit ");
			txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
			txt.appendSibling(txt2);

			ITextComponent txt3 = new TextComponentString(kit.getName());
			txt3.setStyle(new Style().setColor(TextFormatting.DARK_BLUE));

			txt.appendSibling(txt3);

			sender.sendMessage(txt);

			for (ItemStack item : kit.getItems()) {

				ITextComponent txt4 = new TextComponentString("+ ");
				txt4.setStyle(new Style().setColor(TextFormatting.AQUA));

				ITextComponent txt5 = new TextComponentString(item.getDisplayName());
				txt5.setStyle(new Style().setColor(TextFormatting.DARK_BLUE));

				txt4.appendSibling(txt5);

				ITextComponent txt6 = new TextComponentString(" (");
				txt6.setStyle(new Style().setColor(TextFormatting.AQUA));

				txt4.appendSibling(txt6);

				ITextComponent txt7= new TextComponentString(" x" + item.getCount());
				txt7.setStyle(new Style().setColor(TextFormatting.DARK_BLUE));

				txt4.appendSibling(txt7);

				ITextComponent txt8 = new TextComponentString(")");
				txt8.setStyle(new Style().setColor(TextFormatting.AQUA));

				txt4.appendSibling(txt8);

				sender.sendMessage(txt4);

			}

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

	private void listSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(args.length != 1) {

			this.synthaxErrorMessage(sender);
			return;

		}

		ITextComponent txt = new TextComponentString("Pour voir le contenu d'un kit, utilisez : ");
		txt.setStyle(new Style().setColor(TextFormatting.AQUA));

		ITextComponent txt2 = new TextComponentString("/kit view  nom_du_kit");
		txt2.setStyle(new Style().setColor(TextFormatting.DARK_BLUE).setBold(true).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit view")));

		sender.sendMessage(txt);
		sender.sendMessage(txt2);

		ITextComponent txt3 = MainKit.CHAT_PREFIX.createCopy();
		ITextComponent txt4 = new TextComponentString("Liste des kits disponibles : ");
		txt4.setStyle(new Style().setColor(TextFormatting.AQUA));
		txt3.appendSibling(txt4);

		sender.sendMessage(txt3);

		Set<Entry<String,Kit>> kits = MainKit.KITSMANAGER.getKitList().getKitlist().entrySet();

		if(kits.isEmpty()) {

			ITextComponent txt5 = new TextComponentString("Aucun kit !");
			txt5.setStyle(new Style().setColor(TextFormatting.RED));

			sender.sendMessage(txt5);

		}else {

			for (Entry<String, Kit> kit : kits) {

				ITextComponent txt6 = new TextComponentString("+ ");
				txt6.setStyle(new Style().setColor(TextFormatting.AQUA));

				ITextComponent txt7 = new TextComponentString(kit.getValue().getDisplayName());
				txt7.setStyle(new Style().setColor(TextFormatting.DARK_BLUE));

				txt6.appendSibling(txt7);

				ITextComponent txt8 = new TextComponentString(" (");
				txt8.setStyle(new Style().setColor(TextFormatting.AQUA));

				txt6.appendSibling(txt8);

				ITextComponent txt9= new TextComponentString(kit.getKey());
				txt9.setStyle(new Style().setColor(TextFormatting.DARK_BLUE));

				txt6.appendSibling(txt9);

				ITextComponent txt10 = new TextComponentString(")");
				txt10.setStyle(new Style().setColor(TextFormatting.AQUA));

				txt6.appendSibling(txt10);

				sender.sendMessage(txt6);

			}

		}

	}

	private void randomSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		
		if(args.length != 1) {

			this.synthaxErrorMessage(sender);
			return;

		}
		
		MainKit.EXEC.execute(new Runnable() {

			@Override
			public void run() {
				
				EntityPlayerMP player = (EntityPlayerMP) sender;

				if(!PermissionAPI.hasPermission(player, "pumpmykit.command.kit")) {

					ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
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
				
				try {					

					MainKit.KITSMANAGER.randomKit(player);

				} catch (UnfoundSqlProfileException e) {
					
					ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
					ITextComponent txt2 = new TextComponentString("SQL Profile unfound error");
					txt2.setStyle(new Style().setColor(TextFormatting.RED));
					txt.appendSibling(txt2);
					sender.sendMessage(txt);
					
					txt = new TextComponentString("Contactez le staff !");
					txt.setStyle(new Style().setColor(TextFormatting.RED));
					
					sender.sendMessage(txt);
					
				} catch (UnfoudKitException e) {

					ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
					ITextComponent txt2 = new TextComponentString("BUG DANS LA MATRICE !");
					txt2.setStyle(new Style().setColor(TextFormatting.RED));
					txt.appendSibling(txt2);
					sender.sendMessage(txt);
					
					txt = new TextComponentString("Cette erreur n'aurait jamais du avoir lieu.");
					txt.setStyle(new Style().setColor(TextFormatting.RED));
					
					sender.sendMessage(txt);
					
					e.printStackTrace();

				} catch (SQLException e) {
					
					ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
					ITextComponent txt2 = new TextComponentString("SQL error");
					txt2.setStyle(new Style().setColor(TextFormatting.RED));
					txt.appendSibling(txt2);
					sender.sendMessage(txt);
					
					txt = new TextComponentString("Contactez le staff !");
					txt.setStyle(new Style().setColor(TextFormatting.RED));
					
					sender.sendMessage(txt);
					
					e.printStackTrace();
					
				} catch (InsufisentGlobalRandomException e) {
					
					ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
					ITextComponent txt2 = new TextComponentString("Vous avez consommé tout vos kits !");
					txt2.setStyle(new Style().setColor(TextFormatting.RED));
					txt.appendSibling(txt2);
					sender.sendMessage(txt);

					txt = new TextComponentString("Voir : ");
					txt.setStyle(new Style().setColor(TextFormatting.RED));

					txt2 = new TextComponentString("http://store.pumpmykins.eu/");
					txt2.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.OPEN_URL, "http://store.pumpmykins.eu/")));

					txt.appendSibling(txt2);

					sender.sendMessage(txt);
					
				} catch (InsufisentKitsToRandException e) {
					
					ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
					ITextComponent txt2 = new TextComponentString("Pas assez de kit dans la liste pour effectuer un tirage !");
					txt2.setStyle(new Style().setColor(TextFormatting.RED));
					txt.appendSibling(txt2);
					sender.sendMessage(txt);
					
					e.printStackTrace();
				}
			}
		});

	}

	private void selectSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		if(args.length != 2) {

			this.synthaxErrorMessage(sender);			
			return;
		}

		MainKit.EXEC.execute(new Runnable() {

			@Override
			public void run() {

				String name = args[1];
				EntityPlayerMP player = (EntityPlayerMP) sender;
				
				if(!PermissionAPI.hasPermission(player, "pumpmykit.command.kit")) {

					ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
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

				try {					

					MainKit.KITSMANAGER.selectKit(player, name);

				} catch (UnfoundSqlProfileException e) {
					
					ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
					ITextComponent txt2 = new TextComponentString("SQL Profile unfound error");
					txt2.setStyle(new Style().setColor(TextFormatting.RED));
					txt.appendSibling(txt2);
					sender.sendMessage(txt);
					
					txt = new TextComponentString("Contactez le staff !");
					txt.setStyle(new Style().setColor(TextFormatting.RED));
					
					sender.sendMessage(txt);
					
				} catch (InsufisentSelectException e) {
					
					ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
					ITextComponent txt2 = new TextComponentString("Vous avez consommé tout vos kits !");
					txt2.setStyle(new Style().setColor(TextFormatting.RED));
					txt.appendSibling(txt2);
					sender.sendMessage(txt);

					txt = new TextComponentString("Voir : ");
					txt.setStyle(new Style().setColor(TextFormatting.RED));

					txt2 = new TextComponentString("http://store.pumpmykins.eu/");
					txt2.setStyle(new Style().setBold(true).setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.OPEN_URL, "http://store.pumpmykins.eu/")));

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
					
				} catch (SQLException e) {
					
					ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
					ITextComponent txt2 = new TextComponentString("SQL error");
					txt2.setStyle(new Style().setColor(TextFormatting.RED));
					txt.appendSibling(txt2);
					sender.sendMessage(txt);
					
					txt = new TextComponentString("Contactez le staff !");
					txt.setStyle(new Style().setColor(TextFormatting.RED));
					
					sender.sendMessage(txt);
					
					e.printStackTrace();
					
				}

			}
		});		

	}

	private void helpSubCommand(MinecraftServer server, ICommandSender sender, String[] args) {

		ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
		ITextComponent txt2 = new TextComponentString("Liste des commandes : ");
		txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
		txt.appendSibling(txt2);
		sender.sendMessage(txt);
		
		txt = new TextComponentString("/kit select nom_du_kit ");
		txt.setStyle(new Style().setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit select")));		
		txt2 = new TextComponentString("Vous pouvez sélectionner, sur chaque serveur, un kit de votre choix !");
		txt2.setStyle(new Style().setColor(TextFormatting.AQUA));		
		txt.appendSibling(txt2);
		sender.sendMessage(txt);
		
		txt = new TextComponentString("/kit random ");
		txt.setStyle(new Style().setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit random")));
		txt2 = new TextComponentString("Vous pouvez effectuer un tirage sur le serveur de votre choix afin d'obtenir un kit aléatoire !");
		txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
		txt.appendSibling(txt2);
		sender.sendMessage(txt);
		
		txt = new TextComponentString("/kit list ");
		txt.setStyle(new Style().setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit list")));
		txt2 = new TextComponentString("Pour obtenir la liste des kits !");
		txt2.setStyle(new Style().setColor(TextFormatting.AQUA));
		txt.appendSibling(txt2);
		sender.sendMessage(txt);
		
		txt = new TextComponentString("/kit view nom_du_kit ");
		txt.setStyle(new Style().setColor(TextFormatting.DARK_BLUE).setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/kit view")));
		txt2 = new TextComponentString("Pour voir le contenue du kit !");
		txt2.setStyle(new Style().setColor(TextFormatting.AQUA));		
		txt.appendSibling(txt2);
		sender.sendMessage(txt);
		
	}

	private void synthaxErrorMessage(ICommandSender sender) {

		ITextComponent txt = MainKit.CHAT_PREFIX.createCopy();
		ITextComponent txt2 = new TextComponentString("Erreur synthaxe !");
		txt2.setStyle(new Style().setColor(TextFormatting.RED));
		txt.appendSibling(txt2);
		sender.sendMessage(txt);

		txt = new TextComponentString("Voir : ");
		txt.setStyle(new Style().setColor(TextFormatting.RED));

		txt2 = new TextComponentString("/kit help");
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
		
		List<String> l = new ArrayList<String>();
		
		l.add("help");
		l.add("list");
		l.add("view");
		l.add("random");
		l.add("select");
		
		if(args.length != 0 & l.contains(args[0])) {
			
			return new ArrayList<>(MainKit.KITSMANAGER.getKitList().getKitlist().keySet());
			
		}
		
		return l;
		
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
