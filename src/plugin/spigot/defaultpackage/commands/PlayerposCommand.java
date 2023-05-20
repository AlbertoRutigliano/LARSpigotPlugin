package plugin.spigot.defaultpackage.commands;

import static plugin.spigot.defaultpackage.commands.Commands.PLAYERPOS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import net.md_5.bungee.api.ChatColor;
import plugin.spigot.defaultpackage.Main;
import plugin.spigot.defaultpackage.entities.CustomLocation;
import plugin.spigot.defaultpackage.managers.FileManager;

public class PlayerposCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean l_Result = true;
		
		if(sender instanceof Player)
		{
			String l_Command = label.toLowerCase();
			if(PLAYERPOS.equalsIgnoreCase(l_Command))
			{
				Player l_Player = (Player) sender;
				switch (args.length)
				{
					case 0:	// No arguments specified, show all players coordinates
						ArrayList<Player> l_OnlinePlayers = new ArrayList<>(Main.MyServer.getOnlinePlayers());
						sender.sendMessage(ChatColor.BLUE + "=== Posizioni in tempo reale ===");
						for(Player p:l_OnlinePlayers)
						{
							l_Result = this.ShowPlayerPosition(l_Player, p);
						}
						sender.sendMessage(ChatColor.BLUE + "===========================");
						
						break;
					case 1:	// 1 argument specified, show specified player coordinates
						Player l_SelectedPlayer = Main.MyServer.getPlayer(args[0]);
						sender.sendMessage(ChatColor.BLUE + "=== Posizione in tempo reale ===");
						l_Result = this.ShowPlayerPosition(l_Player, l_SelectedPlayer);
						sender.sendMessage(ChatColor.BLUE + "===========================");
						
						break;
					default:
						l_Result = false;
						
						break;
				}
			}
		}
		
		return l_Result;
	}
	
	// Write the requestedPlayer coordinates in the sender message console
	private boolean ShowPlayerPosition(Player sender, Player requestedPlayer){
		if(sender == null || requestedPlayer == null)
		{
			return false;
		}
		
		int l_X = 0, l_Y = 0, l_Z = 0;
		
		l_X = (int)requestedPlayer.getLocation().getX();
		l_Y = (int)requestedPlayer.getLocation().getY();
		l_Z = (int)requestedPlayer.getLocation().getZ();
		
		String l_Coords = String.format("%d %d %d", l_X, l_Y, l_Z);
		String nearestLocation = nearestLocation(requestedPlayer).getKey().getName();
		int distanceToLocation = (int) (double) nearestLocation(requestedPlayer).getValue();
		
		StringBuilder msg = new StringBuilder()
				.append(ChatColor.DARK_RED + requestedPlayer.getName() + ": " + ChatColor.WHITE + l_Coords + " ~ ");
		
		// IF in his house
		if (nearestLocation.toUpperCase().contains(requestedPlayer.getName().toUpperCase()) && distanceToLocation <= 100)  {
			msg.append("vicino casa sua");
		} else {
			msg.append("a " + distanceToLocation + " blocchi da " + nearestLocation);
		}
		
		sender.sendMessage(msg.toString());
		return true;
	}
	
	
	private Entry<CustomLocation, Double> nearestLocation(Player player) {

		CustomLocation playerCurrentLocation = new CustomLocation(player.getDisplayName(), (int) player.getLocation().getX(),
				(int) player.getLocation().getY(), (int) player.getLocation().getZ());
		
		List<CustomLocation> savedLocations = FileManager.readAllCSVCoords();
		
		double distance = 999999999;
		CustomLocation nearestLocation = null;
		for (CustomLocation cl : savedLocations) {
			if (CustomLocation.WorldName.valueOf(player.getWorld().getName()) == cl.getWorldName() && playerCurrentLocation.calculateDistance(cl) < distance) {
				distance = playerCurrentLocation.calculateDistance(cl);
				nearestLocation = cl;
			}
		}
		
		Entry<CustomLocation, Double> result = Maps.immutableEntry(nearestLocation, distance);
		
		return result;
	}
	

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			return null;
		} else {
			return Collections.emptyList();
		}
	}
}
