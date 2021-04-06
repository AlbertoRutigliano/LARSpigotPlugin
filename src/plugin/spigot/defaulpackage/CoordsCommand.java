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

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class CoordsCommand implements CommandExecutor, TabCompleter{
	
	String[] COMMANDS = { "add", "remove" };

	private String vCoordinatesFilePath;
	
	public CoordsCommand(String coordinateFilePath)
	{
		if(coordinateFilePath == null)
		{
			throw new NullArgumentException("coordinateFilePath");
		}
		
		this.vCoordinatesFilePath = coordinateFilePath;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean l_Result = true;
		
		if(sender instanceof Player)
		{
			String l_Command = label.toLowerCase();
			if(l_Command.equalsIgnoreCase("coords"))
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
					case 2:	// 2 arguments specified, used only to remove the specified coordinate name. Syntax: remove CoordinateName
						if(args[0].equalsIgnoreCase("remove"))
						{
							if (FileManager.removeCoordFromFile(args[1])) {
								l_Player.sendMessage("Coordinate " + args[1] + " rimosse!");
							} else {
								l_Player.sendMessage("Coordinate " + args[1] + " non trovate.");
							}
						}
						
						if(args[0].equalsIgnoreCase("add"))
						{
							if (FileManager.writeCoordOnFile(new CustomLocation(args[1], l_Player.getLocation().getX(), l_Player.getLocation().getY(), l_Player.getLocation().getZ()))) {
								l_Player.sendMessage("Coordinate salvate!");
							}
						}
						else
						{
							l_Result = false;
						}
						
						break;
					case 3: if(args[0].equalsIgnoreCase("add") && args[2].equalsIgnoreCase("hidden"))
						{
							if (FileManager.writeCoordOnFile(new CustomLocation(args[1], l_Player.getLocation().getX(), l_Player.getLocation().getY(), l_Player.getLocation().getZ(), true))) {
								l_Player.sendMessage("Coordinate salvate!");
							}
						}
					case 5:	// 5 arguments specified, used only to add new coordinate. Syntax: add CoordinateName XPosition YPosition ZPosition
						if(args[0].equalsIgnoreCase("add"))
						{
							//String l_NewCoordinates = String.format("%s %s %s %s", args[1], args[2], args[3], args[4]);
					        //FileManager.AppendStringOnFile(this.vCoordinatesFilePath, l_NewCoordinates);
							CustomLocation customLocationToWrite = new CustomLocation(args[1], Double.valueOf(args[2]), Double.valueOf(args[3]), Double.valueOf(args[4]));
							if (FileManager.writeCoordOnFile(customLocationToWrite)) {
								l_Player.sendMessage("Coordinate salvate!");
							}
					        
						}
						else
						{
							l_Result = false;
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
		
		final List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0],  Arrays.asList(COMMANDS), completions);
        Collections.sort(completions);

        return (args.length <= 1 ? completions : null);
	}
	

	
}
