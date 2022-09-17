package plugin.spigot.defaultpackage;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

import static plugin.spigot.defaultpackage.Commands.*;


import org.bukkit.Server;

public class Main extends JavaPlugin implements Listener {
	public static Server MyServer;
	
	private PlayerManager vPlayerManager;

    private TrackRunner trackRunner;
	
	@Override
	public void onEnable() {
		ConfigManager.CreateCustomConfig();
		MyServer = getServer();
		
		try {
			this.vPlayerManager = new PlayerManager(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MyServer.getPluginManager().registerEvents(this, this);
		MyServer.getPluginManager().registerEvents(vPlayerManager, this);

		
        this.trackRunner = new TrackRunner();
        this.trackRunner.runTaskTimer(this, 0, 5);
        
        this.getCommand(TRACK).setExecutor(new TrackCommand(this));
        
		this.getCommand(COORDS).setExecutor(new CoordsCommand());
		this.getCommand(COORDS).setTabCompleter(new CoordsCommand());
		
		this.getCommand(PLAYERPOS).setExecutor(new PlayerposCommand());
		
		this.getCommand(JOKE).setExecutor(new JokeCommand());

		this.getCommand(THANKS).setExecutor(new ThanksCommand());

		ServerManager.setTestServerPort(ConfigManager.GetCustomConfig().getInt(ConfigProperties.SERVER_TEST_PORT.name()));

		ServerManager.InitScoreboard();
		
		ServerManager.InitRandomQuote();
		
		ServerManager.InitSleepingKicker();

	}
	
	@Override
	public void onDisable() {
		ServerManager.RemoveObjectives();
	}
	
    public TrackRunner getTrackRunner() {
        return trackRunner;
    }
	
}

