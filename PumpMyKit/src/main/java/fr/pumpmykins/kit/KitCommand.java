package fr.pumpmykins.kit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import fr.pumpmykins.kit.command.KitGetCommand;
import fr.pumpmykins.kit.util.ISubCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;


public class KitCommand implements ICommand {

    private static HashMap<List<String>, ISubCommand> commands = new HashMap<List<String>, ISubCommand>();
    private KitList kl;
    
	public KitCommand(KitList kitlistinstance) {
		
		this.kl = kitlistinstance;
	}

	public static void registerSubCommand(List<String> s, ISubCommand command) {
		
		commands.put(s, command);
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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		if(args.length >= 1) {
			
			boolean match1 = false;
			boolean match2 = false;
			
			for(List<String> s : commands.keySet()) {
				
				if(s.contains(args[0])) {
					
					commands.get(s).runCommand(server, sender, args);
					match1 = true;
				}
			}
			for(Kit k : this.kl.getKitlist()) {
				
				if(k.getName() == args[0]) {
					
					try {
						new KitGetCommand(sender, k);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					match2 = true;
				}
			}
			if(!match1 && !match2) {
				
				commands.get(Arrays.asList("help", "h")).runCommand(server, sender, args);
			}
		} else {
			
			commands.get(Arrays.asList("help", "h")).runCommand(server, sender, args);
		} 
	}

	@Override
	public int compareTo(ICommand o) {
		
		return 0;
	}

	@Override
	public List<String> getAliases() {
		
		return Lists.newArrayList("k");
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		
		if(args[0] == null) {
			
			List<String> subCommand = new ArrayList<String>();
			while(commands.keySet().iterator().hasNext()) {
				
				List<String> ls = commands.keySet().iterator().next();
				subCommand.add(ls.get(0));
			}
			
			return subCommand;
		}
		
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		
		return false;
	}
	
	
}
