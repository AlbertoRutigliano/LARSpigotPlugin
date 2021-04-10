package plugin.spigot.defaulpackage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ServerManager {

	private static ScoreboardManager vScoreboardManager;
    private static Scoreboard vScoreboard;
    private static int vTestServerPort;
    private static HashMap<Player, Timestamp> playerMovement = new HashMap<>();
    
    
    
	public static HashMap<Player, Timestamp> getPlayerMovement() {
		return playerMovement;
	}

	public static void setPlayerMovement(HashMap<Player, Timestamp> playerMovement) {
		ServerManager.playerMovement = playerMovement;
	}

	public static int getTestServerPort() {
		return vTestServerPort;
	}

	public static void setTestServerPort(int vTestServerPort) {
		ServerManager.vTestServerPort = vTestServerPort;
	}
    
	// Return true if is day else false
	public static boolean IsDay() {
	    long l_Time = Main.MyServer.getWorld("world").getTime() % 24000;

	    return l_Time < 12300 || l_Time > 23850;
	}
	
	// Send message to all players
	public static void SendMessageToAllPlayers(String message) {
		Bukkit.broadcastMessage(message);
	}
	
	// Initialize Scoreboard
	public static void InitScoreboard() {
		vScoreboardManager = Bukkit.getScoreboardManager();
		vScoreboard = vScoreboardManager.getNewScoreboard();
		
		Objective objective = vScoreboard.registerNewObjective("CustomScoreboard", "cs", ChatColor.BLUE + "Giocatori online");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if (Bukkit.getPort() == getTestServerPort()) {
			objective.getScore("Server di TEST").setScore(0);
		}
		else
		{
			vScoreboard.resetScores("Server di TEST");
		}
		
		Main.MyServer.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class),  new Runnable() {
			public void run() {
				for (Player p : Main.MyServer.getOnlinePlayers()) {
					ResetScoreboard(p);
					
					if (playerMovement.get(p) != null) {
						Timestamp now = new Timestamp(new Date().getTime());
						int seconds = (int) ((now.getTime() - playerMovement.get(p).getTime()) / 1000) % 60 ;
						if (seconds > ConfigManager.GetCustomConfig().getInt("secondsToAfk")) {
							objective.getScore(ChatColor.GOLD + p.getName() + ChatColor.GRAY + " afk").setScore((int) p.getHealth()); 										
						} else {
							objective.getScore(ChatColor.GOLD + p.getName() + ChatColor.GREEN + (p.isSleeping() ? " zZz" : ""))
							.setScore((int) p.getHealth()); 		
						}
					} else {
						objective.getScore(ChatColor.GOLD + p.getName() + ChatColor.GRAY + " afk").setScore((int) p.getHealth()); 										
					}
				}
				
				
			}
		}, 10, 10);
		
		for (Player p : Main.MyServer.getOnlinePlayers()) {
			p.setScoreboard(vScoreboard);
		}
	
	}
	
	
	// Reset Scoreboard
	public static void ResetScoreboard(Player player) {
		vScoreboard.resetScores(ChatColor.GOLD + player.getName() + ChatColor.GREEN);
		vScoreboard.resetScores(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " zZz");
		vScoreboard.resetScores(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " afk");
	}
	
	// Removes Scoreboard
	public static void RemoveObjectives()
	{
		vScoreboard.clearSlot(DisplaySlot.SIDEBAR);
	}
}
