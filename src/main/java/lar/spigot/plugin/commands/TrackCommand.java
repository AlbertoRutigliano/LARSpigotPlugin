package lar.spigot.plugin.commands;

import static lar.spigot.plugin.commands.Commands.LOCATION;
import static lar.spigot.plugin.commands.Commands.NEW;
import static lar.spigot.plugin.commands.Commands.PLAYER;
import static lar.spigot.plugin.commands.Commands.STOP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import lar.spigot.plugin.Main;
import lar.spigot.plugin.entities.CustomLocation;
import lar.spigot.plugin.managers.FileManager;
import lar.spigot.plugin.managers.ServerManager;


/**
 * Handles the track command.
 * Thanks to LADBukkit (Robin Eschbach)
 */
public class TrackCommand implements TabExecutor {

    private final Main plugin;

    public TrackCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
	        Player p = (Player) sender;
	        switch(args.length) {
				case 1:
					if (args[0].equalsIgnoreCase(STOP)) {
						if(plugin.getTrackRunner().isTracking(p.getUniqueId())) {
							stopTracking(p);
							p.sendMessage(ChatColor.GRAY + "Navigatore spento");
		                } else {
		                    p.sendMessage(ChatColor.GRAY + "Non stai seguendo nessuna coordinata");
		                }
					}
				break;
				
				case 2: // PLAYER <player>, LOCATION <location>
					if (args[0].equalsIgnoreCase(PLAYER)) {
						stopTracking(p);
						p.sendMessage(ChatColor.GRAY + "Funzione attualmente in alpha.");
						
						for(Player player : ServerManager.getOnlinePlayers()) {
							if (player.getDisplayName().equalsIgnoreCase(args[1])) {
								plugin.getTrackRunner().setTracking(p.getUniqueId(),  player.getLocation());
								// Location loc = new Location(Main.MyServer.getWorld("world"), player.getLocation().getX(), 0, player.getLocation().getZ());
								/*
								 * TODO Implementare
								plugin.getTrackRunner().setTracking(p.getUniqueId(), player.getLocation());
								Runnable r = new Runnable() {
									public void run() {
										if (plugin.getTrackRunner().isTracking(p.getUniqueId())) {
											plugin.getTrackRunner().unsetTracking(p.getUniqueId());
											plugin.getTrackRunner().setTracking(p.getUniqueId(),  player.getLocation());
										} else {
											plugin.getTrackRunner().unsetTracking(p.getUniqueId());
										}
									}
								};
								r.run();
								*/
								p.sendMessage(ChatColor.GRAY + "Stai seguendo " + ChatColor.GOLD + player.getDisplayName());
							}
						}
					}
					if (args[0].equalsIgnoreCase(LOCATION)) {
						stopTracking(p);
						String locationName = args[1];
						for (CustomLocation cl : FileManager.readAllCSVCoords()) {
							if (cl.getName().equalsIgnoreCase(locationName)) {
								Location loc = new Location(Main.MyServer.getWorld(p.getWorld().getName()), cl.getX(), 0, cl.getZ());
								plugin.getTrackRunner().setTracking(p.getUniqueId(), loc);
			                    p.sendMessage(ChatColor.GRAY + "Stai seguendo " + ChatColor.GOLD + cl.getName());
							}
						}
					}
				break;
				
				case 3: // NEW <x> <y>
					if (args[0].equalsIgnoreCase(NEW)) {
						try {
							stopTracking(p);
		                    Location loc = new Location(Main.MyServer.getWorld("world"), Double.parseDouble(args[1]), 0, Double.parseDouble(args[2]));
							plugin.getTrackRunner().setTracking(p.getUniqueId(), loc);
							p.sendMessage(ChatColor.GRAY + "Stai seguendo (x: " + ChatColor.GOLD + args[1] + ChatColor.GRAY + ", z: " + ChatColor.GOLD +args[2] + ChatColor.GRAY +")");
		                } catch(NumberFormatException e) {
		                    p.sendMessage(ChatColor.GRAY + "Inserisci dei numeri interi per le coordinate");
		                }     
					}	
				break;
			}
        }
        return true;
    }
    
    private void stopTracking(Player p) {
    	if(plugin.getTrackRunner().isTracking(p.getUniqueId())) {
           plugin.getTrackRunner().unsetTracking(p.getUniqueId());
        }
    }

    @Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    	
    	String[] ALLOWED_COMMANDS = {PLAYER, LOCATION, NEW, STOP};
    	
		List<String> completions = new ArrayList<>();
		List<String> hintLocations = new ArrayList<>();
		
		Player p = (sender instanceof Player) ? (Player) sender : null;
		
		for(CustomLocation cl : FileManager.readAllCSVCoords()) {
			if (CustomLocation.WorldName.valueOf(p.getWorld().getName()) == cl.getWorldName()) {
				hintLocations.add(cl.getName());
			}
		}
		
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], Arrays.asList(ALLOWED_COMMANDS), completions);
			return completions;
		} 
		
		if (LOCATION.equalsIgnoreCase((args[0]))) {
			StringUtil.copyPartialMatches(args[1], hintLocations, completions);
			Collections.sort(completions);
			return completions;
		}
		
		if (PLAYER.equalsIgnoreCase((args[0]))) {
			ArrayList<String> onlinePlayersNames = ServerManager.getOnlinePlayersNames(p.getWorld().getName());
			onlinePlayersNames.remove(p.getDisplayName());
			StringUtil.copyPartialMatches(args[1], onlinePlayersNames, completions);
			Collections.sort(completions);
			return completions;
		}
				
		return Collections.emptyList();
	}
    
    
}
