package plugin.spigot.defaultpackage;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static plugin.spigot.defaultpackage.Commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import org.bukkit.Sound;


public class JokeCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(sender instanceof Player && JOKE.equalsIgnoreCase(label) ) {
			switch (args.length) {
			case 0:	
				ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED);
				sender.sendMessage(ChatColor.GRAY + "Scherzetto in corso...");
				break;
			case 1:					
				switch (args[0]) {
					case CREEPER:
						ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED);
						break;
					case CREEPER_HURT:
						ServerManager.SendSound(Sound.ENTITY_CREEPER_HURT);
						break;
					case ENDERMAN:
						ServerManager.SendSound(Sound.ENTITY_ENDERMAN_STARE);
						break;
					case WITCH:
						ServerManager.SendSound(Sound.ENTITY_WITCH_CELEBRATE);
						break;
					case ARROW:
						ServerManager.SendSound(Sound.ENTITY_ARROW_HIT);
						break;
				default: ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED);	
				}
				sender.sendMessage(ChatColor.GRAY + "Scherzetto in corso...");
				break;
			case 2:	
				Player l_SelectedPlayer = Main.MyServer.getPlayer(args[1]);
				switch (args[0]) {
					case CREEPER:
						ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED, l_SelectedPlayer);
						break;
					case CREEPER_HURT:
						ServerManager.SendSound(Sound.ENTITY_CREEPER_HURT, l_SelectedPlayer);
						break;
					case ENDERMAN:
						ServerManager.SendSound(Sound.ENTITY_ENDERMAN_STARE, l_SelectedPlayer);
						break;
					case WITCH:
						ServerManager.SendSound(Sound.ENTITY_WITCH_CELEBRATE, l_SelectedPlayer);
						break;
					case ARROW:
						ServerManager.SendSound(Sound.ENTITY_ARROW_HIT, l_SelectedPlayer);
						break;
					default: ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED, l_SelectedPlayer);	
				}
				sender.sendMessage(ChatColor.GRAY + "Scherzetto a " + l_SelectedPlayer.getDisplayName() + " in corso...");
				break;
			default: break;
			}

		}

		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		String[] ALLOWED_COMMANDS = {CREEPER, CREEPER_HURT, ENDERMAN, WITCH, ARROW};
		
		List<String> completions = new ArrayList<>();	
		
		Player p = (sender instanceof Player) ? (Player) sender : null;
		
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], Arrays.asList(ALLOWED_COMMANDS), completions);
			Collections.sort(completions);
			return completions;
		} 
		
		if (args.length == 2) {
			ArrayList<String> onlinePlayersNames = ServerManager.getOnlinePlayersNames(p.getWorld().getName());
			onlinePlayersNames.remove(p.getDisplayName());
			StringUtil.copyPartialMatches(args[1], onlinePlayersNames, completions);
			Collections.sort(completions);
			return completions;
		} 
		
		return Collections.emptyList();
	}
	
}
