package plugin.spigot.defaulpackage;

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


// import static plugin.spigot.defaulpackage.Cmd.*;
import static plugin.spigot.defaulpackage.Commands.*;

public class CoordsCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean l_Result = true;
		
		if(sender instanceof Player)
		{
			String l_Command = label.toLowerCase();
			if(COORDS.equalsIgnoreCase(l_Command))
			{
				Player l_Player = (Player) sender;
				switch(args.length)
				{
					case 0:	// No arguments specified, show saved coordinates
						sender.sendMessage(ChatColor.BLUE + "====== Coordinate Salvate ======");
						for (CustomLocation cl : FileManager.readAllCSVCoords()) {
							if (!cl.isHidden()) {	
								sender.sendMessage(ChatColor.GOLD + cl.getName() + ChatColor.RED + " " + (int) cl.getX() + " " + (int) cl.getY() + " " + (int) cl.getZ());
							}
						}
						sender.sendMessage(ChatColor.BLUE + "=============================");
						break;
					case 1:
						if(ALL.equalsIgnoreCase(args[0]))
						{
							sender.sendMessage(ChatColor.BLUE + "====== Coordinate Salvate ======");
							for (CustomLocation cl : FileManager.readAllCSVCoords()) {
								sender.sendMessage(ChatColor.GOLD + cl.getName() + ChatColor.RED + " " 
										+ cl.getX() + " " + cl.getY() + " " + cl.getZ()
										+ (cl.isHidden() ? ChatColor.GRAY + " hidden" : ""));
							}
							sender.sendMessage(ChatColor.BLUE + "=============================");
						}
						break;
					case 2:	// 2 arguments specified, used only to remove the specified coordinate name. Syntax: remove CoordinateName
						if(REMOVE.equalsIgnoreCase(args[0]))
						{
							if (FileManager.removeCSVCoord(args[1])) {
								l_Player.sendMessage("Coordinate " + args[1] + " rimosse!");
							} else {
								l_Player.sendMessage("Coordinate " + args[1] + " non trovate.");
							}
						}
						
						if(GET.equalsIgnoreCase(args[0]))
						{
							sender.sendMessage(ChatColor.BLUE + "====== Coordinate Salvate ======");
							for (CustomLocation cl : FileManager.readAllCSVCoords()) {
								if (cl.getName().equalsIgnoreCase(args[1])) {	
									sender.sendMessage(ChatColor.GOLD + cl.getName() + ChatColor.RED + " " + cl.getX() + " " + cl.getY() + " " + cl.getZ());
								}
							}
							sender.sendMessage(ChatColor.BLUE + "=============================");
						}
						
						if(ADD.equalsIgnoreCase(args[0]))
						{
							if (FileManager.saveCSVCoord(new CustomLocation(args[1], (int) l_Player.getLocation().getX(), (int) l_Player.getLocation().getY(), (int) l_Player.getLocation().getZ()))) {
								l_Player.sendMessage("Coordinate salvate!");
							}
						}
						break;
					case 3: if(ADD.equalsIgnoreCase(args[0]) && HIDDEN.equalsIgnoreCase(args[2]))
						{
							if (FileManager.saveCSVCoord(new CustomLocation(args[1], (int) l_Player.getLocation().getX(), (int) l_Player.getLocation().getY(), (int) l_Player.getLocation().getZ(), true))) {
								l_Player.sendMessage("Coordinate salvate!");
							}
						}
						break;
					case 5:	// 5 arguments specified, used only to add new coordinate. Syntax: add CoordinateName XPosition YPosition ZPosition
						if(ADD.equalsIgnoreCase(args[0]))
						{
							CustomLocation customLocationToWrite = new CustomLocation(args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]));
							if (FileManager.saveCSVCoord(customLocationToWrite)) {
								l_Player.sendMessage("Coordinate salvate!");
							}
					        
						}
						break;
					default:
						l_Result = false;
						
						break;
				}
			}
		}
		
		return l_Result;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		String[] ALLOWED_COMMANDS = {ADD, REMOVE, ALL, GET};
		
		List<String> completions = new ArrayList<>();
		List<String> hintLocations = new ArrayList<>();
		
		for(CustomLocation cl : FileManager.readAllCSVCoords()) {
			hintLocations.add(cl.getName());
		}
		
		if (args.length == 1) {
			StringUtil.copyPartialMatches(args[0], Arrays.asList(ALLOWED_COMMANDS), completions);
			return completions;
		} 
		
		if (REMOVE.equalsIgnoreCase((args[0])) || GET.equalsIgnoreCase(args[0])) {
			StringUtil.copyPartialMatches(args[1], hintLocations, completions);
			return completions;
		}
		
		if (ADD.equalsIgnoreCase(args[0]) && args[1] != null && args.length == 3) {
			StringUtil.copyPartialMatches(args[2], Arrays.asList(HIDDEN), completions);
			return completions;
		}
		
		Collections.sort(completions);
		return Collections.emptyList();
	}
	

	
}
