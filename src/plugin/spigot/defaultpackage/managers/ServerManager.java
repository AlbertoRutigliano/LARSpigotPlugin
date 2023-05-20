package plugin.spigot.defaultpackage.managers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import plugin.spigot.defaultpackage.ConfigProperties;
import plugin.spigot.defaultpackage.Main;
import plugin.spigot.defaultpackage.entities.CustomLocation;

public class ServerManager {

	private static ScoreboardManager vScoreboardManager;
    private static Scoreboard vScoreboard;
    private static int vTestServerPort;
    private static long vCheckPlayersSleepingStartTime = 0;
    
    /**
     * Get test server port
     * 
     * @return test server port
     */
	public static int getTestServerPort() {
		return vTestServerPort;
	}

    /**
     * Set test server port
     * 
     * @param test server port
     */
	public static void setTestServerPort(int testServerPort) {
		ServerManager.vTestServerPort = testServerPort;
	}

	/**
	 * Initialize the task that manages the scoreboard
	 * <p>
	 * Scoreboard shows for example player names and their life points
	 * on the right of the screen
	 * 
	 */
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
		
		//Scoreboard handler task
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
				
				
								
			}
		}, 10, 10);
		
		//Midnight check taks handler
		Main.MyServer.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class),  new Runnable() {
			@Override
			public void run() {
				if (isMidnight()) {
					ServerManager.getOnlinePlayers(CustomLocation.WorldName.world.toString()).forEach(p -> ServerManager.SendSound(Sound.BLOCK_BELL_USE, p));
				}
			}
		}, 10, 10);
		
	}
	
	/**
	 * Initialize random proverbs task
	 * <p> 
	 * It is used to show to all players random proverbs every n seconds
	 */
	public static void InitRandomQuote() {
	    Main.MyServer.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class),  new Runnable() {
			public void run() {
				if (!ServerManager.getOnlinePlayers().isEmpty()) {
					ServerManager.SendMessageToAllPlayers(ChatColor.GRAY + MSGManager.getMessage(MSGManager.Message.QUOTE));
				}
			}
		}, 100, 20 * 60 * ConfigManager.GetCustomConfig().getLong(ConfigProperties.MINUTES_QUOTE_INTERVAL.name())); // Every MINUTES_QUOTE_INTERVAL minutes
	}
	
	/**
	 * Intialize the task to handle the kick of not sleeping players
	 * <p> 
	 * When the majority (the value could be set into plugin properties file)
	 * of players is sleeping the task kicks all the not sleeping players
	 */
	public static void InitSleepingKicker()
	{
		Main.MyServer.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class),  new Runnable() {
			public void run() {
				//Get total count of players which are in the Overworld
				float onlinePlayers = ServerManager.getOnlinePlayers(CustomLocation.WorldName.world.toString()).size();
				//Get total count of sleeping players
				float sleepingPlayers = PlayerManager.getSleepingPlayers().size();
				//Check if the percentage of sleeping players is greater than a defined percentage
				if ((sleepingPlayers / onlinePlayers) * 100 >= ConfigManager.GetCustomConfig().getInt(ConfigProperties.PERCENTAGE_SLEEPING_TO_NOT_SLEEPING_KICK.name())) {
					if (vCheckPlayersSleepingStartTime == 0)
					{
						vCheckPlayersSleepingStartTime = Main.MyServer.getWorld("world").getTime();
					}
					
					long execTime = Main.MyServer.getWorld("world").getTime();
					long passedSeconds = (execTime-vCheckPlayersSleepingStartTime) / 20;
					ServerManager.getOnlinePlayers(CustomLocation.WorldName.world.toString()).stream().filter(q -> !PlayerManager.vPlayerProperties.get(q).isSleeping()).forEach(q -> q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(MSGManager.getMessage(MSGManager.Message.GO_TO_SLEEP, ConfigManager.GetCustomConfig().getInt(ConfigProperties.SECONDS_TO_NOT_SLEEPING_KICK.name())-passedSeconds, ChatColor.RED))));
					ServerManager.getOnlinePlayers(CustomLocation.WorldName.world.toString()).stream().filter(q -> PlayerManager.vPlayerProperties.get(q).isSleeping()).forEach(q -> q.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(MSGManager.getMessage(MSGManager.Message.OTHER_PLAYERS_NOT_SLEEPING_KICK,ConfigManager.GetCustomConfig().getInt(ConfigProperties.SECONDS_TO_NOT_SLEEPING_KICK.name())-passedSeconds))));
					if (passedSeconds >= ConfigManager.GetCustomConfig().getInt(ConfigProperties.SECONDS_TO_NOT_SLEEPING_KICK.name())) {
						ServerManager.getOnlinePlayers(CustomLocation.WorldName.world.toString()).stream().filter(q -> !PlayerManager.vPlayerProperties.get(q).isSleeping()).forEach(q -> q.kickPlayer(MSGManager.getMessage(MSGManager.Message.NOT_SLEEPING_KICK)));
					}
				} else {
					vCheckPlayersSleepingStartTime = 0;
				}
			}
		}, 20, 20); //1 second = 20 ticks
	}
	
	/**
	 * Returns a Collection containing all the players that are currently online
	 * 
	 * @return collection of online players
	 */
	public static Collection<? extends Player> getOnlinePlayers() {
		return Bukkit.getOnlinePlayers();
	}
	/**
	 * Returns a Collection containing all the players that are currently online
	 * in the given world name
	 * 
	 * @param  world  the world where to check online players
	 * @return		collection of online players
	 */
	public static Collection<? extends Player> getOnlinePlayers(String worldName) {
		return Bukkit.getOnlinePlayers().stream().filter(p -> p.getWorld().getName().equalsIgnoreCase(worldName)).collect(Collectors.toList());
	}

	/**
	 * Returns an ArrayList containing all the online players names
	 * 
	 * @return		ArrayList of online players names
	 */
	public static ArrayList<String> getOnlinePlayersNames() {
		ArrayList<String> onlinePlayersNames = new ArrayList<>(Bukkit.getOnlinePlayers().size());
		for(Player p : Bukkit.getOnlinePlayers()) {
			onlinePlayersNames.add(p.getDisplayName());
		}
		return onlinePlayersNames;
	}
	/**
	 * Returns an ArrayList containing all the online players names
	 * in the given world name
	 * 
	 * @param  world  the world where to get online players names
	 * @return		 ArrayList of online players names
	 */
	public static ArrayList<String> getOnlinePlayersNames(String worldName) {
		ArrayList<String> onlinePlayersNames = new ArrayList<>(Bukkit.getOnlinePlayers().size());
		for(Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld().getName().equalsIgnoreCase(worldName)) {
				onlinePlayersNames.add(p.getDisplayName());
			}
		}
		return onlinePlayersNames;
	}

	/**
	 * Send message to all players
	 * 
	 * @param  message  the message to send
	 */
	public static void SendMessageToAllPlayers(String message) {
		Bukkit.broadcastMessage(message);
	}

	/**
	 * Send sound to all players
	 * 
	 * @param  sound  the sound to reproduce
	 */
	public static void SendSound(Sound sound) {
		for(Player p : Main.MyServer.getOnlinePlayers()) {
			p.playSound(p.getLocation(), sound, 1.0f, 1.0f);
		}
	}
	/**
	 * Send sound to a specific player
	 * 
	 * @param  sound  the sound to reproduce
	 * @param  player the player to send the sound
	 */
	public static void SendSound(Sound sound, Player player) {
		player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
	}
	
	/**
	 * Get World time
	 * 
	 * @param time time to parse
	 * @return String hh : mm
	 */
	private static String parseTime(long time) {
	    long gameTime = time;
	    long hours = gameTime / 1000 + 6;
	    long minutes = (gameTime % 1000) * 60 / 1000;
	    if (hours == 24) hours = 0;
	    String mm = "0" + minutes;
	    mm = mm.substring(mm.length() - 2, mm.length());
	    return hours + ":" + mm;
	}
	
	/**
	 * Check if it is midnight
	 * 
	 * @return true when is midnight
	 */
	private static boolean isMidnight() {
	    long time = Main.MyServer.getWorld("world").getTime();
	    return time > 13000 && time < 13010;
	}

	/**
	 * Check if it is daytime
	 * 
	 * @return true when is daytime
	 */
	public static boolean IsDay() {
		long l_Time = Main.MyServer.getWorld("world").getTime() % 24000;
		return l_Time < 12300 || l_Time > 23850;
	}
	
	/**
	 * Reset scoreboard of a player
	 * 
	 * @param player to reset the scoreboard
	 */
	public static void ResetScoreboard(Player player) {
		vScoreboard.resetScores(ChatColor.GOLD + player.getName() + ChatColor.GREEN);
		vScoreboard.resetScores(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " zZz");
		vScoreboard.resetScores(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " afk");
	}
	
	/**
	 * Remove scoreboard
	 */
	public static void RemoveObjectives()
	{
		vScoreboard.clearSlot(DisplaySlot.SIDEBAR);
	}
}
