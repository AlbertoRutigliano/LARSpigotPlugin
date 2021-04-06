package plugin.spigot.defaulpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoordsCommand implements CommandExecutor{

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
						// this.ShowServerCoordinates(l_Player, this.vCoordinatesFilePath);		
						for (CustomLocation cl : readCoordsFromFile()) {
							sender.sendMessage(ChatColor.GOLD + cl.getName() + ChatColor.RED + " " + (int) cl.getX() + " " + (int) cl.getY() + " " + (int) cl.getZ());
						}
						break;
					case 2:	// 2 arguments specified, used only to remove the specified coordinate name. Syntax: remove CoordinateName
						if(args[0].equalsIgnoreCase("remove"))
						{
							//TODO: Must be implemented
							l_Result = false;
						}
						else
						{
							l_Result = false;
						}
						
						break;
					case 5:	// 5 arguments specified, used only to add new coordinate. Syntax: add CoordinateName XPosition YPosition ZPosition
						if(args[0].equalsIgnoreCase("add"))
						{
							//String l_NewCoordinates = String.format("%s %s %s %s", args[1], args[2], args[3], args[4]);
					        //FileManager.AppendStringOnFile(this.vCoordinatesFilePath, l_NewCoordinates);
							
							CustomLocation customLocationToWrite = new CustomLocation(args[1], Double.valueOf(args[2]), Double.valueOf(args[3]), Double.valueOf(args[4]));
							if (writeCoordOnFile(customLocationToWrite)) {
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
	
	private boolean writeCoordOnFile(CustomLocation cl) {
        File f = new File("coordinateTEST.txt");
        ArrayList<CustomLocation> cls = new ArrayList<>();
        cls = readCoordsFromFile();
        cls.add(cl);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cls);
            oos.close();
            fos.close();
            return true;
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
	}
	
	
	private ArrayList<CustomLocation> readCoordsFromFile() {
		ArrayList<CustomLocation> cls = new ArrayList<>();
		 File f = new File("coordinateTEST.txt");
	        try {
	            FileInputStream fis = new FileInputStream(f);
	            ObjectInputStream ois = new ObjectInputStream(fis);
	            
	            cls = (ArrayList<CustomLocation>)ois.readObject();
	            ois.close();
	            fis.close();
	            
	        } catch (FileNotFoundException ex) {
	            ex.printStackTrace();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        } catch (ClassNotFoundException ex) {
	            ex.printStackTrace();
	        }
	        return cls;
	}
	
	
	
}
