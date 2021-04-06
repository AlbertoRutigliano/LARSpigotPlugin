package plugin.spigot.defaulpackage;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.Server;

public class Main extends JavaPlugin implements Listener{
	public static Server MyServer;
	
	private String vPlayersListFilePath;
	private String vKickedPlayersFilePath;
	private String vCoordinatesFilePath;
	
	private PlayerManager vPlayerManager;
	

	
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
		this.getCommand("coords").setTabCompleter(new CoordsCommand(this.vCoordinatesFilePath));
		
		this.getCommand("playerpos").setExecutor(new PlayerposCommand());
		
		
		ServerManager.InitScoreboard();
	}
	
	@Override
	public void onDisable() {
		
	}
	
}

