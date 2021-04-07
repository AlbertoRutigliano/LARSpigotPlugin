package plugin.spigot.defaulpackage;

import org.bukkit.plugin.java.JavaPlugin;
import static plugin.spigot.defaulpackage.Cmd.*;
import org.bukkit.event.Listener;
import org.bukkit.Server;

public class Main extends JavaPlugin implements Listener{
	public static Server MyServer;
	
	private String vPropertiesFilePath;
	private String vPlayersListFilePath;
	private String vKickedPlayersFilePath;
	
	private PropertiesManager vPropertiesManager;
	private PlayerManager vPlayerManager;

	@Override
	public void onEnable() {
		MyServer = getServer();
		this.vPropertiesFilePath = "config.properties";
		this.vPlayersListFilePath = "onlinePlayers.txt";
		this.vKickedPlayersFilePath = "kickedPlayers.txt";
		this.vPropertiesManager = new PropertiesManager(this.vPropertiesFilePath);
		this.vPlayerManager = new PlayerManager(this.vPlayersListFilePath, this.vKickedPlayersFilePath);
		
		MyServer.getPluginManager().registerEvents(this, this);
		MyServer.getPluginManager().registerEvents(vPlayerManager, this);

		this.getCommand(COORDS.toString()).setExecutor(new CoordsCommand());
		this.getCommand(COORDS.toString()).setTabCompleter(new CoordsCommand());
		
		this.getCommand("playerpos").setExecutor(new PlayerposCommand());
		
		ServerManager.setTestServerPort(Integer.parseInt(vPropertiesManager.GetValue("serverTestPort")));
		
		ServerManager.InitScoreboard();
	}
	
	@Override
	public void onDisable() {
		ServerManager.RemoveScoreboard();
	}
	
}

