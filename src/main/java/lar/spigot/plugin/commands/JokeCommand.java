package lar.spigot.plugin.commands;


import static lar.spigot.plugin.commands.Commands.*;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import lar.spigot.plugin.Main;
import lar.spigot.plugin.managers.ServerManager;

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
					case GHAST:
						ServerManager.SendSound(Sound.ENTITY_GHAST_HURT);
						break;
					case PIGLIN:
						ServerManager.SendSound(getPiglinSound());
						break;
					case SHULKER:
						ServerManager.SendSound(Sound.ENTITY_SHULKER_AMBIENT);
						break;
					case CREEPER:
					default: ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED);
				}
				sender.sendMessage(ChatColor.GRAY + "Scherzetto in corso...");
				break;
			case 2:	
				Player l_SelectedPlayer = Main.MyServer.getPlayer(args[1]);
				switch (args[0]) {
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
					case GHAST:
						ServerManager.SendSound(Sound.ENTITY_GHAST_HURT, l_SelectedPlayer);
						break;
					case PIGLIN:
						ServerManager.SendSound(getPiglinSound(), l_SelectedPlayer);
						break;
					case SHULKER:
						ServerManager.SendSound(Sound.ENTITY_SHULKER_AMBIENT, l_SelectedPlayer);
						break;
					case CREEPER:
					default: ServerManager.SendSound(Sound.ENTITY_CREEPER_PRIMED, l_SelectedPlayer);	
				}
				sender.sendMessage(ChatColor.GRAY + "Scherzetto a " + l_SelectedPlayer.getDisplayName() + " in corso...");
				break;
			default: break;
			}

		}

		return true;
	}

	private Sound getPiglinSound() {
		List<Sound> PIGLIN_SOUNDS = Arrays.asList(Sound.ENTITY_PIGLIN_ADMIRING_ITEM, Sound.ENTITY_PIGLIN_ANGRY, Sound.ENTITY_PIGLIN_CELEBRATE, Sound.ENTITY_PIGLIN_JEALOUS, Sound.ENTITY_PIGLIN_RETREAT);
		return PIGLIN_SOUNDS.get(new Random().nextInt(PIGLIN_SOUNDS.size()));
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		String[] ALLOWED_COMMANDS = {CREEPER, CREEPER_HURT, ENDERMAN, WITCH, ARROW, GHAST, PIGLIN, SHULKER};
		
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
