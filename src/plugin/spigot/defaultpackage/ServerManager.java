package plugin.spigot.defaultpackage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ServerManager {

	private static ScoreboardManager vScoreboardManager;
    private static Scoreboard vScoreboard;
    private static int vTestServerPort;

	public static int getTestServerPort() {
		return vTestServerPort;
	}

	public static void setTestServerPort(int vTestServerPort) {
		ServerManager.vTestServerPort = vTestServerPort;
	}
    
	public static Collection<? extends Player> getOnlinePlayers() {
		return Bukkit.getOnlinePlayers();
	}
	
	public static ArrayList<String> getOnlinePlayersNames() {
		ArrayList<String> onlinePlayersNames = new ArrayList<>(Bukkit.getOnlinePlayers().size());
		for(Player p : Bukkit.getOnlinePlayers()) {
			onlinePlayersNames.add(p.getDisplayName());
		}
		return onlinePlayersNames;
	}
	
	// Send message to all players
	public static void SendMessageToAllPlayers(String message) {
		Bukkit.broadcastMessage(message);
	}
		
	// Send sound to all players
	public static void SendSound(Sound sound) {
		for(Player p : Main.MyServer.getOnlinePlayers()) {
			p.playSound(p.getLocation(), sound, 1.0f, 1.0f);
		}
	}
	
	// Send sound to a single player
	public static void SendSound(Sound sound, Player player) {
		player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
	}
	
	// Initialize Scoreboard
	public static void InitScoreboard() {
		vScoreboardManager = Bukkit.getScoreboardManager();
		vScoreboard = vScoreboardManager.getNewScoreboard();
		
		Objective objective = vScoreboard.registerNewObjective("CustomScoreboard", "cs", ChatColor.BLUE + "Giocatori online");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if (Bukkit.getPort() == getTestServerPort()) {
			objective.getScore("Server di TEST").setScore(ConfigManager.GetCustomConfig().getInt(ConfigProperties.SERVER_TEST_PORT.name()));
		}
		else
		{
			vScoreboard.resetScores("Server di TEST");
		}
		
		Main.MyServer.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class),  new Runnable() {
			public void run() {
				for (Player p : Main.MyServer.getOnlinePlayers()) {
					ResetScoreboard(p);
					if (PlayerManager.vPlayerProperties.get(p) != null) {
						Timestamp now = new Timestamp(new Date().getTime());
						int seconds = (int) ((now.getTime() - PlayerManager.vPlayerProperties.get(p).getLastMoveTimestamp().getTime()) / 1000) % 60 ;
						if (seconds > ConfigManager.GetCustomConfig().getInt(ConfigProperties.SECONDS_TO_AFK.name()) && !p.isSleeping()) {
							objective.getScore(ChatColor.GOLD + p.getName() + ChatColor.GRAY + " afk").setScore((int) p.getHealth()); 										
						} else {
							objective.getScore(ChatColor.GOLD + p.getName() + ChatColor.GREEN + (p.isSleeping() ? " zZz" : ""))
							.setScore((int) p.getHealth()); 		
						}
					} else {
						objective.getScore(ChatColor.GOLD + p.getName() + ChatColor.GRAY + " afk").setScore((int) p.getHealth()); 										
					}
				}
				for (Player p : Main.MyServer.getOnlinePlayers()) {
					p.setScoreboard(vScoreboard);
				}
				
				if (isMidnight()) {
					ServerManager.SendSound(Sound.BLOCK_BELL_USE);
				}
								
			}
		}, 10, 10);
		
	}
	
	private static boolean isMidnight() {
	    long time = Main.MyServer.getWorld("world").getTime();
	    return time > 13000 && time < 13010;
	}
	
	public static boolean IsDay() {
		long l_Time = Main.MyServer.getWorld("world").getTime() % 24000;
		return l_Time < 12300 || l_Time > 23850;
	}
	
	public static void InitRandomQuote() {
	    Main.MyServer.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class),  new Runnable() {
			public void run() {
				if (!ServerManager.getOnlinePlayers().isEmpty()) {
					ServerManager.SendMessageToAllPlayers(MSG.QUOTE.getMessage());
				}
			}
		}, 20L * 60, 20L * 300); // Every 5 minutes
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
