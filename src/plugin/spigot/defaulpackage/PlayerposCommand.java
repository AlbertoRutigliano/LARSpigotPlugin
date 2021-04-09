package plugin.spigot.defaulpackage;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.google.common.collect.Maps;

import net.md_5.bungee.api.ChatColor;
import static plugin.spigot.defaulpackage.Cmd.*;

public class PlayerposCommand implements CommandExecutor, Listener, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean l_Result = true;
		
		if(sender instanceof Player)
		{
			String l_Command = label.toLowerCase();
			if(PLAYERPOS.isEqual(l_Command))
			{
				Player l_Player = (Player) sender;
				switch (args.length)
				{
					case 0:	// No arguments specified, show all players coordinates
						ArrayList<Player> l_OnlinePlayers = new ArrayList<>(Main.MyServer.getOnlinePlayers());
						for(Player p:l_OnlinePlayers)
						{
							l_Result = this.ShowPlayerPosition(l_Player, p);
						}
						
						break;
					case 1:	// 1 argument specified, show specified player coordinates
						Player l_SelectedPlayer = Main.MyServer.getPlayer(args[0]);
						l_Result = this.ShowPlayerPosition(l_Player, l_SelectedPlayer);
						
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
		
		sender.sendMessage(ChatColor.DARK_RED + requestedPlayer.getName() + ": " + ChatColor.WHITE + l_Coords + 
				" ~ " + nearestLocation(requestedPlayer).getKey().getName() + " (" + (int) (double) nearestLocation(requestedPlayer).getValue()+ " blocchi)");
		return true;
	}
	
	
	private Entry<CustomLocation, Double> nearestLocation(Player player) {

		CustomLocation playerCurrentLocation = new CustomLocation(player.getDisplayName(), player.getLocation().getX(),
				player.getLocation().getY(), player.getLocation().getZ());
		
		ArrayList<CustomLocation> savedLocations = FileManager.readCoordsFromFile();
		
		double distance = 999999999;
		CustomLocation nearestLocation = null;
		for (CustomLocation cl : savedLocations) {			
			if (playerCurrentLocation.calculateDistance(cl) < distance) {
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
