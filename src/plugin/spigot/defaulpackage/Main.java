package plugin.spigot.defaulpackage;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements Listener{
	public static Server MyServer;
	
	private String vPlayersListFilePath;
	private String vKickedPlayersFilePath;
	private String vCoordinatesFilePath;
	
	private PlayerManager vPlayerManager;
	
	private ScoreboardManager manager;
    private Scoreboard scoreboard;

	
	@Override
	public void onEnable() {		
		MyServer = getServer();
		this.vCoordinatesFilePath = "Coordinate.txt";
		this.vPlayersListFilePath = "onlinePlayers.txt";
		this.vKickedPlayersFilePath = "kickedPlayers.txt";
		this.vPlayerManager = new PlayerManager(this.vPlayersListFilePath, this.vKickedPlayersFilePath);
		
		MyServer.getPluginManager().registerEvents(this, this);
		MyServer.getPluginManager().registerEvents(vPlayerManager, this);

		this.getCommand("coords").setExecutor(new CoordsCommand(this.vCoordinatesFilePath));
		this.getCommand("playerpos").setExecutor(new PlayerposCommand());
		
		initScoreboard();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void initScoreboard() {
		manager = Bukkit.getScoreboardManager();
		scoreboard = manager.getNewScoreboard();
		
		Objective objective = scoreboard.registerNewObjective("CustomScoreboard", "cs", ChatColor.BLUE + "Giocatori online");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this,  new Runnable() {
			public void run() {
				for (Player p : Main.MyServer.getOnlinePlayers()) {
					resetPlayerScores(p);
					objective.getScore(ChatColor.GOLD + p.getName() + ChatColor.GREEN + (p.isSleeping() ? " zZz" : ""))
						.setScore((int) p.getHealth()); 										
					p.setScoreboard(scoreboard);
					}
				}
			}, 10, 10);
	}
	
	private void resetPlayerScores(Player p) {
		scoreboard.resetScores(ChatColor.GOLD + p.getName() + ChatColor.GREEN);
		scoreboard.resetScores(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " zZz");
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player l_Player = e.getPlayer();
		resetPlayerScores(l_Player);
	}
	
}

