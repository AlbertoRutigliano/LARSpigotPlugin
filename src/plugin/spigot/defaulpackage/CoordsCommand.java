package plugin.spigot.defaulpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import static plugin.spigot.defaulpackage.Cmd.*;

public class CoordsCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean l_Result = true;
		
		if(sender instanceof Player)
		{
			String l_Command = label.toLowerCase();
			if(COORDS.isEqual(l_Command))
			{
				Player l_Player = (Player) sender;
				switch(args.length)
				{
					case 0:	// No arguments specified, show saved coordinates
						for (CustomLocation cl : FileManager.readCoordsFromFile()) {
							if (!cl.isHidden()) {
								sender.sendMessage(ChatColor.GOLD + cl.getName() + ChatColor.RED + " " + (int) cl.getX() + " " + (int) cl.getY() + " " + (int) cl.getZ());
							}
						}
						break;
					case 1:
						if(ALL.isEqual(args[0]))
						{
							for (CustomLocation cl : FileManager.readCoordsFromFile()) {
								sender.sendMessage(ChatColor.GOLD + cl.getName() + ChatColor.RED + " " 
										+ (int) cl.getX() + " " + (int) cl.getY() + " " + (int) cl.getZ()
										+ (cl.isHidden() ? ChatColor.GRAY + " hidden" : ""));
							}
						}
						break;
					case 2:
						if(REMOVE.isEqual(args[0]) && l_Player.isOp())
						{
							if (FileManager.removeCoordFromFile(args[1])) {
								l_Player.sendMessage("Coordinate " + args[1] + " rimosse!");
							} else {
								l_Player.sendMessage("Coordinate " + args[1] + " non trovate.");
							}
						}
						
						if(ADD.isEqual(args[0]) && l_Player.isOp())
						{
							if (FileManager.writeCoordOnFile(new CustomLocation(args[1], l_Player.getLocation().getX(), l_Player.getLocation().getY(), l_Player.getLocation().getZ()))) {
								l_Player.sendMessage("Coordinate salvate!");
							}
						}
						break;
					case 3: if(ADD.isEqual(args[0]) && HIDDEN.isEqual(args[2]) && l_Player.isOp())
						{
							if (FileManager.writeCoordOnFile(new CustomLocation(args[1], l_Player.getLocation().getX(), l_Player.getLocation().getY(), l_Player.getLocation().getZ(), true))) {
								l_Player.sendMessage("Coordinate salvate!");
							}
						}
						break;
					case 5:	// 5 arguments specified, used only to add new coordinate. Syntax: add CoordinateName XPosition YPosition ZPosition
						if(ADD.isEqual(args[0]) && l_Player.isOp())
						{
							CustomLocation customLocationToWrite = new CustomLocation(args[1], Double.valueOf(args[2]), Double.valueOf(args[3]), Double.valueOf(args[4]));
							if (FileManager.writeCoordOnFile(customLocationToWrite)) {
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
	
	@Deprecated
	// Write the server saved coordinates in the sender message console
	private boolean ShowServerCoordinates(Player sender, String coordinatesFilePath) {
		if(sender == null)
		{
			return false;
		}
		
		boolean l_CoordinateFileExist = Files.exists(Paths.get(coordinatesFilePath), LinkOption.values());
		
		if(l_CoordinateFileExist == true)
		{
			try (BufferedReader br = Files.newBufferedReader(Paths.get(coordinatesFilePath)))
			{
	        	String line;
	            
	            while ((line = br.readLine()) != null)
	            {
	            	sender.sendMessage(line);
	            }
	            return true;

	        } catch (IOException ex) {
	            System.err.format("IOException: %s%n", ex);
	        }
		}
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		Player l_Player = (Player) sender;
		Cmd[] COMMANDS = {ADD, REMOVE, ALL};
		final List<String> completions = new ArrayList<>();
		
		List<String> hintLocations = new ArrayList<>();
		for(CustomLocation cl : FileManager.readCoordsFromFile()) {
			hintLocations.add(cl.getName());
		}
		
		if (args.length == 1) {
			completions.clear();
			StringUtil.copyPartialMatches(args[0], Arrays.stream(COMMANDS).map(Enum::toString).collect(Collectors.toList()), completions);
			Collections.sort(completions);
			return completions;
		} 
		
		if (REMOVE.isEqual(args[0])) {
			completions.clear();
			StringUtil.copyPartialMatches(args[1], hintLocations, completions);
			Collections.sort(completions);
			return completions;
		}
		
		if (ADD.isEqual(args[0]) && args[1] != null && args.length == 3) {
			completions.clear();
			StringUtil.copyPartialMatches(args[2], Arrays.asList(HIDDEN.toString()), completions);
			Collections.sort(completions);
			return completions;
		}
		
		return Collections.emptyList();
	}
	

	
}
