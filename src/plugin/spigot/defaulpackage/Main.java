package plugin.spigot.defaulpackage;

import org.bukkit.plugin.java.JavaPlugin;
import static plugin.spigot.defaulpackage.Cmd.*;
import org.bukkit.event.Listener;
import org.bukkit.Server;

public class Main extends JavaPlugin implements Listener{
	public static Server MyServer;
	
	private String vPlayersListFilePath;
	private String vKickedPlayersFilePath;
	
	private PlayerManager vPlayerManager;
	

	
	@Override
	public void onEnable() {		
		MyServer = getServer();
		this.vPlayersListFilePath = "onlinePlayers.txt";
		this.vKickedPlayersFilePath = "kickedPlayers.txt";
		this.vPlayerManager = new PlayerManager(this.vPlayersListFilePath, this.vKickedPlayersFilePath);
		
		MyServer.getPluginManager().registerEvents(this, this);
		MyServer.getPluginManager().registerEvents(vPlayerManager, this);

		this.getCommand(COORDS.toString()).setExecutor(new CoordsCommand());
		this.getCommand(COORDS.toString()).setTabCompleter(new CoordsCommand());
		
		this.getCommand("playerpos").setExecutor(new PlayerposCommand());
		
		
		ServerManager.InitScoreboard();
	}
	
	@Override
	public void onDisable() {
		
	}
	
}

