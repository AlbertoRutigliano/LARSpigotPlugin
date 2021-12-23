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
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		String[] ALLOWED_COMMANDS = {CREEPER, ENDERMAN, WITCH, ARROW};
		
		List<String> completions = new ArrayList<>();		

		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], Arrays.asList(ALLOWED_COMMANDS), completions);
			return completions;
		} 
		
		Collections.sort(completions);
		return Collections.emptyList();
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(sender instanceof Player && JOKE.equalsIgnoreCase(label) ) {
			switch (args.length) {
			case 0:	
				ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED);
				// TODO Implement random SOUND
				sender.sendMessage(ChatColor.GRAY + "Scherzetto in corso...");
				break;
			case 1:					
				switch (args[0]) {
					case CREEPER:
						ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED);
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
				// TODO Implement specific SOUND to specific PLAYER
				Player l_SelectedPlayer = Main.MyServer.getPlayer(args[1]);
				ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED, l_SelectedPlayer);
				sender.sendMessage(ChatColor.GRAY + "Scherzetto a " + l_SelectedPlayer.getDisplayName() + " in corso...");
				break;
			default: break;
			}

		}

		
		return true;
	}
	
	

}
