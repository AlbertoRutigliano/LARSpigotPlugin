package plugin.spigot.defaulpackage;

import org.bukkit.Bukkit;

public class ServerManager {

	// Return true if is day else false
	public static boolean IsDay() {
	    long l_Time = Main.MyServer.getWorld("world").getTime() % 24000;

	    return l_Time < 12300 || l_Time > 23850;
	}
	
	// Send message to all players
	public static void SendMessageToAllPlayers(String message) {
		Bukkit.broadcastMessage(message);
	}
}
