package lar.spigot.plugin.commands;

import static lar.spigot.plugin.commands.Commands.START;
import static lar.spigot.plugin.commands.Commands.ACCEPT;
import static lar.spigot.plugin.commands.Commands.DECLINE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import lar.spigot.plugin.entities.PlayerProperties;
import lar.spigot.plugin.managers.MSGManager;
import lar.spigot.plugin.managers.MSGManager.Message;
import lar.spigot.plugin.managers.PlayerManager;
import lar.spigot.plugin.managers.ServerManager;

public class FightCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
	        Player pSender = (Player) sender;
	        switch(args.length) {
				case 1:
					// ACCEPT
					if (args[0].equalsIgnoreCase(ACCEPT)) {
						PlayerProperties pSenderProp = PlayerManager.vPlayerProperties.get(pSender); 
						// Add player to fighting players for requestor, add requestor player to fighting players for current player and send message for acceptance
						for (Player requestPlayer : pSenderProp.getFightingPlayersRequests()) {
							PlayerManager.vPlayerProperties.get(requestPlayer).getFightingPlayers().add(pSender);
							pSenderProp.getFightingPlayers().add(requestPlayer);
							String message = MSGManager.getMessage(Message.FIGHT_ACCEPTED, pSender.getName(), ChatColor.GOLD, ChatColor.GRAY);
							requestPlayer.sendMessage(message);
							pSender.sendMessage(message);
						}
						pSenderProp.getFightingPlayersRequests().clear();
					}
					// DECLINE
					if (args[0].equalsIgnoreCase(DECLINE)) {
						PlayerProperties pSenderProp = PlayerManager.vPlayerProperties.get(pSender);
						// Remove players from requests players for current player and requestor, send message for decline
						for (Player requestPlayer : pSenderProp.getFightingPlayersRequests()) {
							String message = MSGManager.getMessage(Message.FIGHT_DECLINED, pSender.getName(), ChatColor.GOLD, ChatColor.GRAY);
							requestPlayer.sendMessage(message);
							pSender.sendMessage(message);
						}
						pSenderProp.getFightingPlayersRequests().clear();
					}
				break;

				case 2:
					// START <player>
					if (args[0].equalsIgnoreCase(START)) {
						for(Player player : ServerManager.getOnlinePlayers()) {
							if (player.getDisplayName().equalsIgnoreCase(args[1]) && !pSender.getName().equalsIgnoreCase(args[1])) {
								Player fightingPlayer = ServerManager.getPlayerByName(args[1]);
								// Send invite only if player is not already in fight 
								if (!PlayerManager.vPlayerProperties.get(fightingPlayer).isFighting()) {
									ArrayList<Player> fightingPlayersRequests = PlayerManager.vPlayerProperties.get(fightingPlayer).getFightingPlayersRequests();
									if (!fightingPlayersRequests.contains(pSender)) {
										fightingPlayersRequests.add(pSender);
										fightingPlayer.sendMessage(MSGManager.getMessage(Message.FIGHT_DECLARATION, pSender.getName(), ChatColor.GOLD, ChatColor.GRAY));
										fightingPlayer.sendMessage(MSGManager.getMessage(Message.FIGHT_ACCEPT, ChatColor.GOLD, ChatColor.GRAY));
										fightingPlayer.sendMessage(MSGManager.getMessage(Message.FIGHT_DECLINE, ChatColor.GOLD, ChatColor.GRAY));
										pSender.sendMessage(MSGManager.getMessage(Message.FIGHT_REQUEST_SEND, fightingPlayer.getName(), ChatColor.GOLD, ChatColor.GRAY));
									}
								}else {
									pSender.sendMessage(ChatColor.GOLD + fightingPlayer.getName() + ChatColor.GRAY + " è già impegnato in una battaglia");
								}
							}
						}
					}
				default:
					break;
			}
        }
        return true;
    }
    
    @Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    	
    	String[] ALLOWED_COMMANDS = {START, ACCEPT, DECLINE};
    	
		List<String> completions = new ArrayList<>();
		
		Player p = (sender instanceof Player) ? (Player) sender : null;
		
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], Arrays.asList(ALLOWED_COMMANDS), completions);
			return completions;
		} 
		
		if (START.equalsIgnoreCase((args[0]))) {
			ArrayList<String> onlinePlayersNames = ServerManager.getOnlinePlayersNames(p.getWorld().getName());
			onlinePlayersNames.remove(p.getDisplayName());
			StringUtil.copyPartialMatches(args[1], onlinePlayersNames, completions);
			Collections.sort(completions);
			return completions;
		}
				
		return Collections.emptyList();
	}
    
    
}
