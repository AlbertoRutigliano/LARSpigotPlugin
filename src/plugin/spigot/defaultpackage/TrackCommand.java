package plugin.spigot.defaultpackage;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import static plugin.spigot.defaultpackage.Commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
							p.sendMessage("Navigatore stoppato");
		                } else {
		                    p.sendMessage("Non stai seguendo nessuna coordinata");
		                }
					}
				break;
				
				case 2: // PLAYER <player>, LOCATION <location>
					if (args[0].equalsIgnoreCase(PLAYER)) {
						stopTracking(p);
						// TODO Implementare
					}
					if (args[0].equalsIgnoreCase(LOCATION)) {
						stopTracking(p);
						String locationName = args[1];
						for (CustomLocation cl : FileManager.readAllCSVCoords()) {
							if (cl.getName().equalsIgnoreCase(locationName)) {
								//Location loc = plugin.getLocationConfig().getLocation(path);
								Location loc = new Location(Main.MyServer.getWorld("world"), cl.getX(), 0, cl.getZ());
								plugin.getTrackRunner().setTracking(p.getUniqueId(), loc);
			                    p.sendMessage("Stai seguendo " + cl.getName());
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
							p.sendMessage("Stai seguendo (x: " + args[1] + ", z: " + args[2] + ")");
		                } catch(NumberFormatException e) {
		                    p.sendMessage("Inserisci dei numeri per le coordinate");
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
		
		for(CustomLocation cl : FileManager.readAllCSVCoords()) {
			hintLocations.add(cl.getName());
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
			StringUtil.copyPartialMatches(args[1], ServerManager.getOnlinePlayersNames(), completions);
			Collections.sort(completions);
			return completions;
		}
				
		
		return Collections.emptyList();
	}
    
    
}
